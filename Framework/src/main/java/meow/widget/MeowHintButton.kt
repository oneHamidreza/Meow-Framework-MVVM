package meow.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.widget.TextViewCompat
import androidx.databinding.DataBindingUtil
import com.etebarian.meowframework.R
import com.etebarian.meowframework.databinding.MeowHintButtonBinding
import com.google.android.material.card.MaterialCardView
import meow.ktx.getColorCompat

class MeowHintButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : MaterialCardView(context, attrs, defStyleAttrs) {

    private val binding = DataBindingUtil.inflate<MeowHintButtonBinding>(
        LayoutInflater.from(context),
        R.layout.meow_hint_button,
        this,
        true
    )

    var hintText: String? = null
        set(value) {
            field = value
            binding.hintText = hintText
        }

    var hintColor = 0
        set(value) {
            field = value
            if (field != 0)
                binding.tvHint.setTextColor(context.getColorCompat(field))
        }

    var hintTextAppearance = 0
        set(value) {
            field = value
            if (field != 0)
                TextViewCompat.setTextAppearance(binding.tvHint, field)
        }

    var titleText: String? = null
        set(value) {
            field = value
            binding.titleText = titleText
        }

    var titleColor = 0
        set(value) {
            field = value
            if (field != 0)
                binding.tvTitle.setTextColor(context.getColorCompat(field))
        }

    var titleTextAppearance = 0
        set(value) {
            field = value
            if (field != 0)
                TextViewCompat.setTextAppearance(binding.tvTitle, field)
        }

    var icon = 0
        set(value) {
            field = value
            if (field != 0)
                binding.ivIcon.setImageResource(field)
        }

    var iconColor = 0
        set(value) {
            field = value
            if (field != 0)
                binding.iconColor = iconColor
        }

    init {
        setAttributesFromXml(attrs, R.styleable.MeowHintButton) {
            hintText = it.getString(R.styleable.MeowHintButton_meow_hint)
            hintColor = it.getColor(R.styleable.MeowHintButton_meow_hintColor, hintColor)
            hintTextAppearance = it.getResourceId(
                R.styleable.MeowHintButton_meow_hintTextAppearance,
                hintTextAppearance
            )
            titleText = it.getString(R.styleable.MeowHintButton_meow_title)
            titleColor = it.getColor(R.styleable.MeowHintButton_meow_titleColor, titleColor)
            titleTextAppearance = it.getResourceId(
                R.styleable.MeowHintButton_meow_titleTextAppearance,
                titleTextAppearance
            )
            icon = it.getResourceId(R.styleable.MeowHintButton_meow_icon, icon)
            iconColor = it.getColor(R.styleable.MeowHintButton_meow_iconColor, iconColor)
        }
        initializeView()
    }

    @SuppressLint("PrivateResource")
    private fun initializeView() {
        val drawable = GradientDrawable()
        drawable.apply {
            setColor(context.getColorCompat(com.google.android.material.R.color.mtrl_filled_background_color))
            cornerRadius = resources.getDimension(R.dimen.pin_box_radius)
        }
        background = drawable
    }

}