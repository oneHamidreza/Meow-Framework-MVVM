package meow.widget.impl

import android.content.Context
import android.util.AttributeSet
import meow.R
import meow.utils.MeowShapeDrawable
import meow.utils.avoidExceptionFinal

/**
 * Created by 1HE on 9/30/2018.
 */

interface MeowShapeDrawableImpl {

    var shapeDrawable: MeowShapeDrawable?

    fun setAttributeFromXmlShapeDrawable(context: Context, attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MeowShapeDrawable, 0, 0)
        avoidExceptionFinal({
            shapeDrawable = MeowShapeDrawable.createFromTypedArray(context, a)
        }) { a.recycle() }
    }
}