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

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Typeface
import android.text.*
import android.text.style.MetricAffectingSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import com.etebarian.meowframework.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import meow.controller
import meow.util.getField
import meow.util.getFontCompat
import meow.util.toPersianNumber


/**
 * Meow TextField class.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-07
 */

class MeowTextField : TextInputLayout {

    var fontFamily: Int = 0
    var isPersianNumber = controller.isPersian

    val editText = TextInputEditText(context)

    var validateType = VALIDATE_TYPE_DEFAULT
    private var inputType = InputType.TYPE_CLASS_TEXT

    var text: Editable?
        set(value) {
            editText.text = value
        }
        get() = editText.text

    var errorEmpty: String? = ""
    var errorMobile: String? = ""
    var errorEmail: String? = ""

    var textSize = resources.getDimension(R.dimen.text_field_text_size)

    companion object {
        const val VALIDATE_TYPE_DEFAULT = 0
        const val VALIDATE_TYPE_EMPTY = 1
        const val VALIDATE_TYPE_MOBILE = 2
        const val VALIDATE_TYPE_EMAIL = 3
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    ) {
        setAttributesFromXml(attrs, R.styleable.MeowTextField) {
            initializeAttributes(it)
        }
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttributesFromXml(attrs, R.styleable.MeowTextField) {
            initializeAttributes(it)
        }
        initializeView()
    }

    private fun initializeAttributes(it: TypedArray) {
        fontFamily = it.getResourceId(
                R.styleable.MeowTextField_meow_fontFamily,
                controller.defaultTypefaceResId
        )

        inputType = it.getInt(R.styleable.MeowTextField_inputType, inputType)

        validateType = it.getInt(R.styleable.MeowTextField_validateType, validateType)

        textSize = it.getDimension(R.styleable.MeowTextField_textSize, textSize)

        errorEmpty = it.getString(R.styleable.MeowTextField_errorEmpty)
        if (errorEmpty.isNullOrEmpty())
            errorEmpty = context.getString(R.string.error_required_value)

        errorMobile = it.getString(R.styleable.MeowTextField_errorEmpty)
        if (errorMobile.isNullOrEmpty())
            errorMobile = context.getString(R.string.error_mobile_not_valid)

        errorEmail = it.getString(R.styleable.MeowTextField_errorEmail)
        if (errorEmail.isNullOrEmpty())
            errorEmail = context.getString(R.string.error_email_not_valid)

        isPersianNumber = it.getBoolean(R.styleable.MeowTextField_isPersianNumber, isPersianNumber)
    }

    private fun initializeView() {
        addView(editText)
        editText.typeface = context.getFontCompat(fontFamily)
        typeface = context.getFontCompat(fontFamily)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSize)
        editText.inputType = this.inputType
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setCounterTypeface()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        if (isPersianNumber)
            text = text.toPersianNumber()
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        editText.addTextChangedListener(textWatcher)
    }

    override fun setError(errorText: CharSequence?) {
        if (!errorText.isNullOrEmpty())
            super.setError(setErrorTypeface(errorText.toString()))
    }

    private fun setCounterTypeface() {
        val counterView = getField<TextInputLayout, TextView>("counterView")
        counterView?.typeface = context.getFontCompat(fontFamily)
    }

    private fun setErrorTypeface(string: String): SpannableString {

        class TypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {
            override fun updateMeasureState(p: TextPaint) {
                p.typeface = typeface
                p.flags = p.flags or Paint.SUBPIXEL_TEXT_FLAG
            }

            override fun updateDrawState(tp: TextPaint) {
                tp.typeface = typeface
                tp.flags = tp.flags or Paint.SUBPIXEL_TEXT_FLAG
            }
        }

        val s = SpannableString(string)
        s.setSpan(
                TypefaceSpan(context.getFontCompat(fontFamily)),
            0,
            s.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return s
    }
}