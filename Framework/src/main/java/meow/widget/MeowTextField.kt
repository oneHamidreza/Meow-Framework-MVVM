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
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.etebarian.meowframework.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import meow.controller
import meow.core.api.FormErrorModel
import meow.widget.impl.FormInterface


/**
 * Meow TextField class.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-07
 */


//todo @ali all of class in meow.widget must be test in Android 4.4.
//todo @ali add username validation
class MeowTextField : TextInputLayout, FormInterface {

    private var fontFamily: Int = 0
    val editText = TextInputEditText(context)
    var validateType = VALIDATE_TYPE_DEFAULT
    private var inputType = InputType.TYPE_CLASS_TEXT
    var text: Editable?
        set(value) {
            editText.text = value
        }
        get() = editText.text

    val textString get() = text.toString()

    var errorEmpty: String? = ""
    var errorMobile: String? = ""
    var errorEmail: String? = ""

    var textSize = resources.getDimension(R.dimen.field_size)

    override var apiField: String? = null

    companion object {
        const val VALIDATE_TYPE_DEFAULT = 0
        const val VALIDATE_TYPE_EMPTY = 1
        const val VALIDATE_TYPE_MOBILE = 2
        const val VALIDATE_TYPE_MOBILE_LEGACY = 3
        const val VALIDATE_TYPE_EMAIL = 4
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

        inputType = it.getInt(R.styleable.MeowTextField_meow_inputType, inputType)

        validateType = it.getInt(R.styleable.MeowTextField_meow_validateType, validateType)

        textSize = it.getDimension(R.styleable.MeowTextField_meow_textSize, textSize)

        apiField = it.getString(R.styleable.MeowTextField_meow_textSize)

        errorEmpty = it.getString(R.styleable.MeowTextField_meow_errorEmpty)
        if (errorEmpty.isNullOrEmpty())
            errorEmpty = context.getString(R.string.error_required_value)

        errorMobile = it.getString(R.styleable.MeowTextField_meow_errorMobile)
        if (errorMobile.isNullOrEmpty())
            errorMobile = context.getString(R.string.error_mobile_not_valid)

        errorMobile = it.getString(R.styleable.MeowTextField_meow_errorMobileLegacy)
        if (errorMobile.isNullOrEmpty())
            errorMobile = context.getString(R.string.error_mobile_not_valid)

        errorEmail = it.getString(R.styleable.MeowTextField_meow_errorEmail)
        if (errorEmail.isNullOrEmpty())
            errorEmail = context.getString(R.string.error_email_not_valid)
    }

    private fun initializeView() {
        addView(editText)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSize)
        editText.inputType = this.inputType
        if (controller.isPersian)
            textDirection = View.TEXT_DIRECTION_LTR
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        editText.addTextChangedListener(textWatcher)
    }

    override fun showErrorFromApi(items: List<FormErrorModel>) {
        items.apply { print(size) }
        if (apiField == null)
            return
        val messageSB = StringBuilder()
        val filtered = items.filter { it.field == apiField }
        filtered.forEachIndexed { i, it ->
            messageSB.append(it.message)
            if (i != filtered.lastIndex)
                messageSB.append("\n")
        }
        if (messageSB.isNotEmpty())
            error = messageSB.toString()
    }
}