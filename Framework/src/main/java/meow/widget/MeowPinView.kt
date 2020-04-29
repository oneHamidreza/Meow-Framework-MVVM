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
import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnKeyListener
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.DataBindingUtil
import com.etebarian.meowframework.R
import com.etebarian.meowframework.databinding.MeowPinViewBinding
import meow.controller
import meow.ktx.*

/**
 * Meow Pin View class.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-04-30
 */


@Suppress("unused", "LeakingThis", "MemberVisibilityCanBePrivate")
open class MeowPinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttrs) {

    private val binding = DataBindingUtil.inflate<MeowPinViewBinding>(
        LayoutInflater.from(context),
        R.layout.meow_pin_view,
        this,
        true
    )

    companion object {
        const val TYPE_OUTLINED = 0
        const val TYPE_FILLED = 1
    }

    protected var count = 4
    var textColor = 0

    var textAppearance = 0
    var hintTextAppearance = 0
    var errorTextAppearance = 0

    var boxStyle: Int = TYPE_OUTLINED

    var icon = 0
        set(value) {
            field = value
            binding.icon = icon
        }

    var iconColor = 0
        set(value) {
            field = value
            binding.iconColor = iconColor
        }

    var hintColor = 0
        set(value) {
            field = value
            binding.hintColor = field
        }

    var hint = ""
        set(value) {
            field = value
            binding.hintText = field
        }

    var showBack = true
        set(value) {
            field = value
            binding.showBack = showBack
        }

    var error = ""
        set(value) {
            field = value
            if (field.isEmpty())
                hideError()
            else
                showError(field)
        }

    var onCompletedListener: () -> Unit = {}

    private lateinit var allowCallWatcher: BooleanArray
    private lateinit var etList: Array<AppCompatEditText?>

    init {
        setAttributesFromXml(attrs, R.styleable.MeowPinView) {
            count = it.getInt(R.styleable.MeowPinView_meow_count, count)
            hint = it.getString(R.styleable.MeowPinView_meow_hint) ?: hint
            icon = it.getResourceId(R.styleable.MeowPinView_meow_icon, icon)
            iconColor = it.getColor(R.styleable.MeowPinView_meow_iconColor, iconColor)
            textColor = it.getColor(R.styleable.MeowPinView_meow_textColor, textColor)
            hintColor = it.getColor(R.styleable.MeowPinView_meow_hintColor, hintColor)
            showBack = it.getBoolean(R.styleable.MeowPinView_meow_showBack, showBack)
            textAppearance =
                it.getResourceId(R.styleable.MeowPinView_meow_textAppearance, textAppearance)
            hintTextAppearance = it.getResourceId(
                R.styleable.MeowPinView_meow_hintTextAppearance,
                hintTextAppearance
            )
            errorTextAppearance = it.getResourceId(
                R.styleable.MeowPinView_meow_errorTextAppearance,
                errorTextAppearance
            )
            boxStyle = it.getInt(R.styleable.MeowPinView_meow_boxStyle, boxStyle)
        }
        initializeView()
    }

    private fun initializeView() {
        orientation = VERTICAL
        draw()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val margin = 8.dp()
        var etSizeWidth = (widthSize - 72.dp() - (count - 1) * margin) / count
        if (etSizeWidth > 48.dp())
            etSizeWidth = 48.dp()
        if (etSizeWidth < 24.dp())
            etSizeWidth = 24.dp()

        for (i in etList.indices) {
            val params = etList[i]?.layoutParams as LinearLayout.LayoutParams
            params.width = etSizeWidth
            if (i != count - 1)
                params.rightMargin = margin
            etList[i]?.layoutParams = params
        }
    }

    @SuppressLint("ResourceType", "PrivateResource")
    private fun draw() {
        etList = arrayOfNulls(count)
        allowCallWatcher = BooleanArray(count)
        allowCallWatcher.fill(true)

        for (i in count - 1 downTo 0) {
            val et = AppCompatEditText(context)

            val params = LinearLayout.LayoutParams(48.dp(), 48.dp())

            et.apply {
                layoutParams = params

                val drawable = GradientDrawable()
                drawable.apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = resources.getDimension(R.dimen.pin_box_radius)
                    when (boxStyle) {
                        TYPE_OUTLINED -> setStroke(
                            1.dp(),
                            context.getColorCompat(R.color.stroke_color)
                        )

                        TYPE_FILLED -> setColor(context.getColorCompat(com.google.android.material.R.color.mtrl_filled_background_color))
                    }
                }
                background = drawable

                val padding = 8.dp()
                setPadding(padding, padding, padding, padding)

                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
                setTextColor(textColor)
                TextViewCompat.setTextAppearance(et, textAppearance)

                gravity = Gravity.CENTER
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))

                setOnKeyListener(OnKeyListener { _, keyCode, event ->
                    if (event.action != KeyEvent.ACTION_DOWN)
                        return@OnKeyListener true
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        avoidException {
                            (context as? Activity)?.onBackPressed()
                        }
                        return@OnKeyListener true
                    }

                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (text.toString().isEmpty() && i != 0) {
                            etList[i - 1]?.requestFocus()
                            val lastText = etList[i - 1]?.text.toString()
                            if (lastText.length == 1)
                                etList[i - 1]?.setSelection(1)
                        }
                    } else {
                        if (i != this@MeowPinView.count - 1 && et.text.toString().isNotEmpty()) {
                            val ch = Character.toChars(event.unicodeChar)
                            etList[i + 1]?.setText(String(ch))
                        }
                    }
                    false
                })

                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        charSequence: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (!allowCallWatcher[i])
                            return

                        val s = charSequence?.toString() ?: ""
                        val formatted = if (!controller.isPersian) s else s.toPersianNumber()
                        allowCallWatcher[i] = false
                        setText(formatted)
                        setSelection(et.text?.length ?: 0)
                        allowCallWatcher[i] = true

                        val code = getCode()
                        val completed = code.isNotEmpty() && code.length == this@MeowPinView.count
                        if (et.text?.length == 1 && completed) {
                            onCompletedListener()
                        } else if (i != this@MeowPinView.count - 1 && et.text.toString()
                                .isNotEmpty()
                        ) {
                            etList[i + 1]?.requestFocus()
                        }

                        updateBackspaceImage()
                    }

                    override fun afterTextChanged(s: Editable) {

                    }
                })
            }

            etList[i] = et
            binding.ll.addView(et, 0)
        }

        binding.ivBackspace.setOnClickListener {
            clear()
        }

        hideError()
        updateBackspaceImage()

        binding.iv.setImageResource(icon)

        TextViewCompat.setTextAppearance(binding.tvHint, hintTextAppearance)
        TextViewCompat.setTextAppearance(binding.tvError, errorTextAppearance)
    }

    private fun setTextNoWatcher(s: String, pos: Int) {
        allowCallWatcher[pos] = false
        etList[pos]?.setText(s)
        allowCallWatcher[pos] = true
    }

    private fun updateBackspaceImage() {
        val code = getCode()
        if (code.isEmpty()) {
            binding.ivBackspace.isEnabled = false
            binding.ivBackspace.alpha = 0.1f
        } else {
            binding.ivBackspace.isEnabled = true
            binding.ivBackspace.alpha = 1f
        }
    }

    fun getCode(): String {
        val code = StringBuilder()
        for (et in etList) {
            code.append(et?.text.toString())
        }
        return code.toString().toEnglishNumber()
    }

    fun setCode(code: String) {
        if (code.isEmpty())
            return

        val chars = code.toCharArray()
        for (i in etList.indices) {
            if (i <= chars.size - 1) {
                var s = chars[i].toString()
                if (controller.isPersian)
                    s = s.toPersianNumber()
                setTextNoWatcher(s, i)
            }
        }

        if (code.length == count)
            onCompletedListener()
    }

    fun clear() {
        for (i in etList.indices) {
            setTextNoWatcher("", i)
        }
        updateBackspaceImage()
        etList[0]?.requestFocus()
    }

    override fun setEnabled(enabled: Boolean) {
        setEnabledCustom(enabled, false)
    }

    fun setEnabledCustom(isEnabled: Boolean, changeAlpha: Boolean) {
        super.setEnabled(isEnabled)
        alpha = calculateDisableAlpha(isEnabled, changeAlpha)
    }

    fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
        avoidException {
            parent.requestChildFocus(this, this)
        }
    }

    fun hideError() {
        binding.tvError.text = ""
        binding.tvError.visibility = View.GONE
    }

    fun calculateDisableAlpha(isEnabled: Boolean, changeAlpha: Boolean): Float {
        return if (changeAlpha) {
            if (!isEnabled)
                0.38f
            else
                1f
        } else 1f
    }
}