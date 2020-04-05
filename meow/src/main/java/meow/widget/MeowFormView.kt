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
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.etebarian.meowframework.R
import com.google.i18n.phonenumbers.PhoneNumberUtil
import meow.util.avoidException
import meow.util.fetchAllDigit
import meow.util.isValidEmail


/**
 * Meow Form View class.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-08
 */

open class MeowFormView(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var etList = listOf<MeowTextField>()
    private var isResetForm: Boolean = false
    private var checkList = ArrayList<MeowTextField>()

    init {
        setAttributesFromXml(attrs, R.styleable.MeowFormView) {
            isResetForm = it.getBoolean(R.styleable.MeowFormView_resetForm, isResetForm)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        etList = getAllEditTexts()
    }

    fun validate(listener: () -> Unit) {
        etList.forEach { childE ->
            checkWithTextFieldType(childE)
        }
        if (checkList.distinct().size == etList.size) {
            listener()
            checkList.clear()
            if (isResetForm)
                resetForm()
        }
    }

    private fun resetForm() {
        etList.forEach {
            it.text?.clear()
            it.isErrorEnabled = false
        }
    }

    private fun checkWithTextFieldType(textField: MeowTextField) {
        when (textField.validateType) {
            MeowTextField.VALIDATE_TYPE_DEFAULT -> checkList.add(textField)
            MeowTextField.VALIDATE_TYPE_EMPTY -> emptyValidator(textField)
            MeowTextField.VALIDATE_TYPE_MOBILE -> mobileValidator(textField)
            MeowTextField.VALIDATE_TYPE_EMAIL -> emailValidator(textField)
        }
    }

    private fun emptyValidator(textField: MeowTextField) {
        val text = textField.text?.trim()
        if (text.isNullOrEmpty()) {
            textField.error = textField.errorEmpty
        } else {
            textField.isErrorEnabled = false
            checkList.add(textField)
        }
    }

    private fun mobileValidator(textField: MeowTextField) {
        val text = textField.text?.trim()
        if (text.isNullOrEmpty()) {
            textField.error = textField.errorEmpty
        } else {
            avoidException {
                if (!isValidPhoneCustom(text.toString())) {
                    textField.error = textField.errorMobile
                } else {
                    textField.isErrorEnabled = false
                    checkList.add(textField)
                }
            }
        }
    }

    private fun emailValidator(textField: MeowTextField) {
        val text = textField.text?.trim()
        if (text.isNullOrEmpty()) {
            textField.error = textField.errorEmpty
        } else {
            avoidException {
                if (!text.toString().isValidEmail()) {
                    textField.error = textField.errorEmail
                } else {
                    textField.isErrorEnabled = false
                    checkList.add(textField)
                }
            }
        }
    }

    private fun isValidPhoneCustom(s: String): Boolean {//todo @Ali use two methods

        fun getCountryISOFromPhone(s: String?): String {
            val mPhoneNumberUtil = PhoneNumberUtil.getInstance()
            var region = ""
            if (s != null) {
                avoidException {
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
        return avoidException {
            if (s.startsWith("+98") || s.startsWith("98")) {
                val m = s.fetchAllDigit()
                return !m.isNullOrEmpty() && m.startsWith("98") && m.length == 12
            }
            util.isValidNumber(util.parse(s, countryIso))
        } ?: false
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
}