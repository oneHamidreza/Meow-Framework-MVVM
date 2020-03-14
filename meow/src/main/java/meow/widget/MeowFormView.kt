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
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.i18n.phonenumbers.PhoneNumberUtil
import meow.R
import meow.utils.*


/**
 * The TextField Widget.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-08
 */

open class MeowFormView : LinearLayout {

    private val TAG = "FormViewLogs"
    private var etList = listOf<MeowTextField>()
    private var btList = listOf<MeowButton>()
    private var isResetForm: Boolean = false
    private var isError = false


    constructor(context: Context) : super(context) {
        initializeView()
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


    private fun initializeView() {
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        etList = getAllEditTexts()
        btList = getAllButtons()
    }

    private fun setAttributeFromXml(context: Context, attrs: AttributeSet) {

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MeowFormView, 0, 0)
        avoidExceptionFinal({
            a.apply {
                isResetForm = getBoolean(R.styleable.MeowFormView_resetForm, isResetForm)
            }
        }) { a.recycle() }
    }


    fun setOnSubmitClickListener(listener: () -> Unit) {
        btList.forEach {
            if (it.isAttachToForm) {
                it.setOnClickListener {
                    etList.forEach { childE ->
                        checkWithTextFieldType(childE)
                    }
                    if (!isError) {
                        listener()
                        if (isResetForm) resetForm()
                    }
                }
            }
        }
    }

    fun resetForm() {
        etList.forEach {
            it.text?.clear()
            it.isErrorEnabled = false
        }
    }

    private fun checkWithTextFieldType(textField: MeowTextField) {
        when (textField.validateType) {
            MeowTextField.VALIDATE_TYPE_DEFAULT -> {
            }
            MeowTextField.VALIDATE_TYPE_EMPTY -> emptyValidator(textField)
            MeowTextField.VALIDATE_TYPE_MOBILE -> mobileValidator(textField)
            MeowTextField.VALIDATE_TYPE_EMAIL -> emailValidator(textField)
        }
    }

    private fun emptyValidator(textField: MeowTextField) {
        val text = textField.text?.trim()
        if (text.isNullOrEmpty()) {
            textField.error = textField.errorEmpty
            isError = true
        } else {
            isError = false
            textField.isErrorEnabled = false
        }
    }

    private fun mobileValidator(textField: MeowTextField) {
        val text = textField.text?.trim()
        if (text.isNullOrEmpty()) {
            textField.error = textField.errorEmpty
            isError = true
        } else {
            avoidException {
                if (!isValidPhoneCustom(text.toString())) {
                    textField.error = textField.errorMobile
                    isError = true
                } else {
                    isError = false
                    textField.isErrorEnabled = false
                }
            }
        }
    }

    private fun emailValidator(textField: MeowTextField) {
        val text = textField.text?.trim()
        if (text.isNullOrEmpty()) {
            textField.error = textField.errorEmpty
            isError = true
        } else {
            avoidException {
                if (!text.toString().isValidEmail()) {
                    textField.error = textField.errorEmail
                    isError = true
                } else {
                    isError = false
                    textField.isErrorEnabled = false
                }
            }
        }
    }

    private fun isValidPhoneCustom(s: String): Boolean {

        fun getCountryISOFromPhone(s: String?): String {
            val mPhoneNumberUtil = PhoneNumberUtil.getInstance()
            var region = ""
            if (s != null) {
                avoidException {
                    Log.d(TAG, "phone : $s")
                    var phone = s
                    if (!phone.contains("+"))
                        phone = "+$phone"
                    val p = mPhoneNumberUtil.parse(phone, null)
                    val sb = StringBuilder(16)
                    sb.append('+').append(p.countryCode).append(p.nationalNumber)
                    region = mPhoneNumberUtil.getRegionCodeForNumber(p)
                }
            }
            return region
        }

        val countryIso = getCountryISOFromPhone(s)
        val util = PhoneNumberUtil.getInstance()
        return avoidExceptionReturn({
            if (s.startsWith("+98") || s.startsWith("98")) {
                val m = s.fetchAllDigit()
                return !m.isEmptyCheckNull() && m.startsWith("98") && m.length == 12
            }
            util.isValidNumber(util.parse(s, countryIso))
        }) { false }
    }

    private fun getAllEditTexts(): List<MeowTextField> {
        val list = arrayListOf<MeowTextField>()

        fun find(v: View, list: ArrayList<MeowTextField>) {
            if (v is MeowTextField)
                list.add(v)
            else if (v is ViewGroup) {
                for (i in 0 until v.childCount) {
                    find(v.getChildAt(i), list)
                }
            }
        }

        find(this, list)

        return list
    }

    private fun getAllButtons(): List<MeowButton> {
        val list = arrayListOf<MeowButton>()

        fun find(v: View, list: ArrayList<MeowButton>) {
            if (v is MeowButton)
                list.add(v)
            else if (v is ViewGroup) {
                for (i in 0 until v.childCount) {
                    find(v.getChildAt(i), list)
                }
            }
        }

        find(this, list)

        return list
    }
}