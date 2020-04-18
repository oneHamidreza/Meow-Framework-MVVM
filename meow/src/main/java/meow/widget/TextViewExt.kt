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
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter

/**
 * [android.widget.TextView] Extensions.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-15
 */

object TextViewBindingAdapter {

    @SuppressLint("SetTextI18n")
    @BindingAdapter("prefixText")
    @JvmStatic
    fun setPrefix(view: TextView, prefixText: String) {
        view.text = "$prefixText${view.text}"
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("suffixText")
    @JvmStatic
    fun setSuffix(view: TextView, suffixText: String) {
        view.text = "${view.text}$suffixText"
    }
}

fun TextView.setTextAppearanceCompat(@StyleRes resId: Int) =
    TextViewCompat.setTextAppearance(this, resId)
