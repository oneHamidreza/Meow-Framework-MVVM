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

package meow.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.*
import android.text.style.MetricAffectingSpan
import android.util.AttributeSet
import android.widget.TextView
import com.etebarian.meowframework.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import meow.controller
import meow.util.avoidException
import meow.util.getField
import meow.util.getFont
import meow.util.logD
import meow.widget.impl.TextViewImpl


/**
 * The TextField Widget.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-07
 */

class MeowTextField : TextInputLayout, TextViewImpl {

    override var fontPath: String? = ""
        set(value) {
            field = value
            editText.typeface = context.getFont(fontPath)
            typeface = context.getFont(fontPath)
        }
    override var isPersian = controller.isPersian
    override var beforeText: String? = ""
    override var afterText: String? = ""
    override var hasSpan = false
    override var forcePaddingFont = controller.isForceFontPadding
    override var forceGravity = false

    val editText = TextInputEditText(context)

    var validateType = VALIDATE_TYPE_DEFAULT
    private var inputType = INPUT_TYPE_DEFAULT

    var text: Editable?
        set(value) {
            editText.text = value
        }
        get() = editText.text

    var errorEmpty: String? = ""
    var errorMobile: String? = ""
    var errorEmail: String? = ""

    companion object {
        const val INPUT_TYPE_DEFAULT = 0
        const val INPUT_TYPE_EMAIL = 1
        const val INPUT_TYPE_PASSWORD = 2
        const val INPUT_TYPE_PHONE = 3
        const val INPUT_TYPE_NUMBER_DECIMAL = 4
        const val INPUT_TYPE_MULTI_LINE = 5
        const val INPUT_TYPE_NUMBER_FLOAT = 6
        const val INPUT_TYPE_WEBSITE = 7
        const val INPUT_TYPE_PERSON_NAME = 8

        const val VALIDATE_TYPE_DEFAULT = 0
        const val VALIDATE_TYPE_EMPTY = 1
        const val VALIDATE_TYPE_MOBILE = 2
        const val VALIDATE_TYPE_EMAIL = 3

        private fun createInputType(inputType: Int): Int {
            return when (inputType) {
                INPUT_TYPE_DEFAULT -> InputType.TYPE_CLASS_TEXT
                INPUT_TYPE_EMAIL -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                INPUT_TYPE_PASSWORD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                INPUT_TYPE_PHONE -> InputType.TYPE_CLASS_PHONE
                INPUT_TYPE_NUMBER_DECIMAL -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                INPUT_TYPE_MULTI_LINE -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                INPUT_TYPE_NUMBER_FLOAT -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                INPUT_TYPE_WEBSITE -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
                INPUT_TYPE_PERSON_NAME -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                else -> InputType.TYPE_CLASS_TEXT
            }
        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttributeFromXml(context, attrs)
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setAttributeFromXml(context, attrs)
        initializeView()
    }

    @SuppressLint("ResourceType")
    override fun setAttributeFromXml(context: Context, attrs: AttributeSet) {
        super.setAttributeFromXml(context, attrs)

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MeowTextField, 0, 0)
        avoidException(
            tryBlock = {
                a.apply {
                    inputType = a.getInt(R.styleable.MeowTextField_meow_inputType, inputType)
                    validateType =
                        a.getInt(R.styleable.MeowTextField_meow_validateType, validateType)

                    errorEmpty = getString(R.styleable.MeowTextField_errorEmpty)
                    if (errorEmpty.isNullOrEmpty())
                        errorEmpty = context.getString(R.string.error_required_value)

                    errorMobile = getString(R.styleable.MeowTextField_errorEmpty)
                    if (errorMobile.isNullOrEmpty())
                        errorMobile = context.getString(R.string.error_mobile_not_valid)

                    errorEmail = getString(R.styleable.MeowTextField_errorEmail)
                    if (errorEmail.isNullOrEmpty())
                        errorEmail = context.getString(R.string.error_email_not_valid)
                }
            },
            finallyBlock = {
                a.recycle()
            }
        )
    }

    private fun initializeView() {
        fontPath = fontPath
        addView(editText)
        editText.textSize = 14f
        editText.inputType = createInputType(this.inputType)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setCounterTypeface()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        editText.addTextChangedListener(textWatcher)
    }

    override fun setError(errorText: CharSequence?) {
        super.setError(setFontToError(errorText.toString()))
    }

    private fun setCounterTypeface() {
        val counterView = getField<TextView>("counterView") ?: return
        logD(m = "counterView: ${counterView != null}")//todo test
        counterView.typeface = context.getFont(fontPath)
    }

    private fun setFontToError(string: String): SpannableString {

        class TypefaceSpan(private val mTypeface: Typeface) : MetricAffectingSpan() {

            override fun updateMeasureState(p: TextPaint) {
                p.typeface = mTypeface
                p.flags = p.flags or Paint.SUBPIXEL_TEXT_FLAG
            }

            override fun updateDrawState(tp: TextPaint) {
                tp.typeface = mTypeface
                tp.flags = tp.flags or Paint.SUBPIXEL_TEXT_FLAG
            }
        }

        val s = SpannableString(string)
        s.setSpan(
            TypefaceSpan(context.getFont(fontPath)!!),
            0,
            s.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return s
    }
}