package meow.widget

import android.content.Context
import android.util.AttributeSet
import meow.widget.ratingbar.ScaleRatingBar

open class MeowRatingBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : ScaleRatingBar(
    context,
    attrs,
    defStyleAttrs
) {

    init {
        initializeView()
    }

    private fun initializeView() {
    }
}