/*
 * Copyright (C) 2020 Hamidreza Etebarian & Ali Modares.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.widget

import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.syntax.Prism4jThemeDefault
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.GrammarLocator
import io.noties.prism4j.GrammarUtils
import io.noties.prism4j.Prism4j
import io.noties.prism4j.Prism4j.*
import sample.prism4j.languages.Prism_clike
import sample.prism4j.languages.Prism_groovy
import sample.prism4j.languages.Prism_json
import sample.prism4j.languages.Prism_markup
import java.util.regex.Pattern.compile


/**
 * TextView Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-14
 */

object TextViewBinding {

    object PrismKotlin {
        fun create(prism4j: Prism4j?): Grammar? {
            val kotlin = GrammarUtils.extend(
                GrammarUtils.require(prism4j!!, "clike"),
                "kotlin",
                GrammarUtils.TokenFilter { token -> "class-name" != token.name() },
                token(
                    "keyword",
                    pattern(
                        compile("(^|[^.])\\b(?:abstract|actual|annotation|as|break|by|catch|class|companion|const|constructor|continue|crossinline|data|do|dynamic|else|enum|expect|external|final|finally|for|fun|get|if|import|in|infix|init|inline|inner|interface|internal|is|lateinit|noinline|null|object|open|operator|out|override|package|private|protected|public|reified|return|sealed|set|super|suspend|tailrec|this|throw|to|try|typealias|val|var|vararg|when|where|while)\\b"),
                        true
                    )
                ),
                token(
                    "function",
                    pattern(compile("\\w+(?=\\s*\\()")),
                    pattern(compile("(\\.)\\w+(?=\\s*\\{)"), true)
                ),
                token(
                    "number",
                    pattern(compile("\\b(?:0[xX][\\da-fA-F]+(?:_[\\da-fA-F]+)*|0[bB][01]+(?:_[01]+)*|\\d+(?:_\\d+)*(?:\\.\\d+(?:_\\d+)*)?(?:[eE][+-]?\\d+(?:_\\d+)*)?[fFL]?)\\b"))
                ),
                token(
                    "operator",
                    pattern(compile("\\+[+=]?|-[-=>]?|==?=?|!(?:!|==?)?|[\\/*%<>]=?|[?:]:?|\\.\\.|&&|\\|\\||\\b(?:and|inv|or|shl|shr|ushr|xor)\\b"))
                )
            )
            GrammarUtils.insertBeforeToken(
                kotlin, "string",
                token(
                    "raw-string",
                    pattern(compile("(\"\"\"|''')[\\s\\S]*?\\1"), false, false, "string")
                )
            )
            GrammarUtils.insertBeforeToken(
                kotlin, "keyword",
                token(
                    "annotation",
                    pattern(
                        compile("\\B@(?:\\w+:)?(?:[A-Z]\\w*|\\[[^\\]]+\\])"),
                        false,
                        false,
                        "builtin"
                    )
                )
            )
            GrammarUtils.insertBeforeToken(
                kotlin, "function",
                token("label", pattern(compile("\\w+@|@\\w+"), false, false, "symbol"))
            )

            // this grammar has 1 token: interpolation, which has 2 patterns
            var interpolationInside: Grammar
            run {


                // okay, I was cloning the tokens of kotlin grammar (so there is no recursive chain of calls),
                // but it looks like it wants to have recursive calls
                // I did this because interpolation test was failing due to the fact that `string`
                // `raw-string` tokens didn't have `inside`, so there were not tokenized
                // I still find that it has potential to fall with stackoverflow (in some cases)
                val tokens: MutableList<Token> =
                    ArrayList(kotlin.tokens().size + 1)
                tokens.add(
                    token(
                        "delimiter",
                        pattern(compile("^\\$\\{|\\}$"), false, false, "variable")
                    )
                )
                tokens.addAll(kotlin.tokens())
                interpolationInside = grammar(
                    "inside",
                    token(
                        "interpolation",
                        pattern(
                            compile("\\$\\{[^}]+\\}"),
                            false,
                            false,
                            null,
                            grammar("inside", tokens)
                        ),
                        pattern(compile("\\$\\w+"), false, false, "variable")
                    )
                )
            }
            val string = GrammarUtils.findToken(kotlin, "string")
            val rawString = GrammarUtils.findToken(kotlin, "raw-string")
            if (string != null
                && rawString != null
            ) {
                val stringPattern = string.patterns()[0]
                val rawStringPattern = rawString.patterns()[0]
                string.patterns().add(
                    pattern(
                        stringPattern.regex(),
                        stringPattern.lookbehind(),
                        stringPattern.greedy(),
                        stringPattern.alias(),
                        interpolationInside
                    )
                )
                rawString.patterns().add(
                    pattern(
                        rawStringPattern.regex(),
                        rawStringPattern.lookbehind(),
                        rawStringPattern.greedy(),
                        rawStringPattern.alias(),
                        interpolationInside
                    )
                )
                string.patterns().removeAt(0)
                rawString.patterns().removeAt(0)
            } else {
                throw RuntimeException(
                    "Unexpected state, cannot find `string` and/or `raw-string` tokens " +
                            "inside kotlin grammar"
                )
            }
            return kotlin
        }
    }

    class MyGrammarLocator : GrammarLocator {
        override fun grammar(
            prism4j: Prism4j,
            language: String
        ): Grammar? {
            return when (language) {
                "clike" -> Prism_clike.create(prism4j)
                "groovy" -> Prism_groovy.create(prism4j)
                "json" -> Prism_json.create(prism4j)
                "kotlin" -> PrismKotlin.create(prism4j)
                "markup" -> Prism_markup.create(prism4j)
                else -> null
            }
        }

        override fun languages() = setOf("clike", "groovy", "json", "kotlin", "markup")
    }

    @BindingAdapter("markdown_assets")
    fun setMarkdownAssets(textView: TextView, markdownAssets: String) =
        textView.apply {
            val str = resources.assets.open(markdownAssets).bufferedReader().use { it.readText() }
            Markwon.builder(context).apply {
                val prism4j = Prism4j(MyGrammarLocator())
                val highlight = SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDefault.create());
                usePlugin(highlight)

                usePlugin(ImagesPlugin.create())
                usePlugin(TablePlugin.create(context))
            }.build().setMarkdown(this, str)
        }

}