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
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.etebarian.meowframework.R
import com.etebarian.meowframework.databinding.MeowEmptyStateBinding
import meow.core.api.UIErrorModel
import meow.ktx.*
import meow.widget.impl.MeowEmptyStateInterface

/**
 * Meow Empty State class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-12
 */

@Suppress("unused")
class MeowEmptyState @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : LinearLayout(context, attrs, defStyleAttrs), MeowEmptyStateInterface {

    private val binding = DataBindingUtil.inflate<MeowEmptyStateBinding>(
        LayoutInflater.from(context),
        R.layout.meow_empty_state,
        this,
        true
    )

    var icon: Drawable? = null
        set(value) {
            field = value
            if (isAttachedToWindow) {
                binding.iv.apply {
                    setImageDrawable(icon)
                    visibility = if (value != null) View.VISIBLE else View.GONE
                }
            }
        }

    @SuppressLint("RestrictedApi")
    var iconTint: ColorStateList =
        ColorStateList.valueOf(getMaterialColor(R.attr.colorPrimary))
        set(value) {
            field = value
            binding.iconTint = value
        }
    var iconSize: Int = 0.dp()
        set(value) {
            field = value
            if (isAttachedToWindow) {
                binding.iv.updateLayoutParams<LayoutParams> {
                    it.width = if (iconSize == 0) LayoutParams.WRAP_CONTENT else iconSize
                    it.height = if (iconSize == 0) LayoutParams.WRAP_CONTENT else iconSize
                }
            }
        }
    var title: String? = ""
        set(value) {
            field = value
            binding.title = value
        }
    var titleTextColor: ColorStateList = context.ofColorStateList(R.color.on_background_high)
        set(value) {
            field = value
            binding.titleTextColor = value
        }
    var desc: String? = ""
        set(value) {
            field = value
            binding.desc = value
            binding.showDesc = value.isNotNullOrEmpty()
        }
    var descTextColor: ColorStateList = context.ofColorStateList(R.color.on_background_medium)
        set(value) {
            field = value
            binding.descTextColor = value
        }
    var primaryActionText: String? = ""
        set(value) {
            field = value
            binding.primaryActionText = value
            binding.hasAction = value.isNotNullOrEmpty()
        }

    init {
        setPaddingRelativeAll(16.dp())
        orientation = VERTICAL
        gravity = Gravity.CENTER
        setAttributesFromXml(attrs, R.styleable.MeowEmptyState) {
            icon = it.getDrawable(R.styleable.MeowEmptyState_meow_icon) ?: icon
            iconTint =
                it.getColorStateList(R.styleable.MeowEmptyState_meow_iconTint) ?: iconTint
            iconSize =
                it.getDimensionPixelSize(R.styleable.MeowEmptyState_meow_iconSize, iconSize)
            title = it.getString(R.styleable.MeowEmptyState_meow_title)
            titleTextColor =
                it.getColorStateList(R.styleable.MeowEmptyState_meow_titleTextColor)
                ?: titleTextColor
            desc = it.getString(R.styleable.MeowEmptyState_meow_desc)
            descTextColor =
                it.getColorStateList(R.styleable.MeowEmptyState_meow_descTextColor)
                ?: descTextColor
            primaryActionText =
                it.getString(R.styleable.MeowEmptyState_meow_primaryActionText)
        }

        afterMeasured {
            icon = icon
            iconSize = iconSize
        }
        hide()
    }

    override fun getActionButton() = binding.bt

    override fun show(uiErrorModel: UIErrorModel) {
        visibility = View.VISIBLE

        icon = context.getDrawableCompat(uiErrorModel.icon) ?: ColorDrawable(Color.TRANSPARENT)
        title = uiErrorModel.title
        desc = uiErrorModel.message
        primaryActionText = uiErrorModel.actionText
    }

    override fun hide() {
        visibility = View.GONE
    }

}