package meow.widget

import android.content.Context
import android.util.AttributeSet
import com.willy.ratingbar.ScaleRatingBar

open class MeowRateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : ScaleRatingBar(
    context,
    attrs,
    defStyleAttrs
) {//todo @Ali copy from repository in this project because jitPack

    init {
        initializeView()
    }

    private fun initializeView() {

    }
}