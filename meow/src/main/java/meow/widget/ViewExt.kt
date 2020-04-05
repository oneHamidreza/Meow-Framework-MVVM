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

import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.card.MaterialCardView
import meow.util.avoidException
import meow.util.dp
import meow.util.isNotNullOrEmpty
import meow.util.logD

/**
 * [View] Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-14
 */

object ViewBindingAdapter {

    @BindingAdapter("meow_gap")
    @JvmStatic
    fun setGap(view: LinearLayout, gap: Int = 0) {
        (0 until view.childCount).forEach { pos ->
            val child = view.getChildAt(pos)
            child.updateLayoutParams<LinearLayout.LayoutParams> {
                if (view.orientation == LinearLayout.HORIZONTAL)
                    it.marginEnd = if (pos != view.childCount - 1) gap.dp() else 0
                if (view.orientation == LinearLayout.VERTICAL)
                    it.bottomMargin = if (pos != view.childCount - 1) gap.dp() else 0
            }
        }
    }

    @BindingAdapter("aspectRatio")
    @JvmStatic
    fun setAspectRatio(view: View, aspectRatio: String? = null) {
        if (aspectRatio.isNotNullOrEmpty()) {
            val second = aspectRatio!!.substring(aspectRatio.lastIndexOf(":") + 1)
            val first = aspectRatio.replace(":$second", "")
            view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    view.viewTreeObserver.removeOnPreDrawListener(this)

                    val widthSize = View.MeasureSpec.getSize(view.measuredWidth)
                    val heightSize = (second.toFloat() / first.toFloat() * widthSize).toInt()
                    val newHeightSpec =
                        View.MeasureSpec.makeMeasureSpec(heightSize, View.MeasureSpec.EXACTLY)
                    logD(m = "width: $widthSize, height : $newHeightSpec, $first:$second")
                    if (view.layoutParams is LinearLayout.LayoutParams)
                        view.layoutParams =
                            LinearLayout.LayoutParams(view.measuredWidth, newHeightSpec)
                    if (view.layoutParams is FrameLayout.LayoutParams)
                        view.layoutParams =
                            FrameLayout.LayoutParams(view.measuredWidth, newHeightSpec)
                    if (view.layoutParams is RelativeLayout.LayoutParams)
                        view.layoutParams =
                            RelativeLayout.LayoutParams(view.measuredWidth, newHeightSpec)
                    if (view.layoutParams is CoordinatorLayout.LayoutParams)
                        view.layoutParams =
                            CoordinatorLayout.LayoutParams(view.measuredWidth, newHeightSpec)
                    if (view.layoutParams is AppBarLayout.LayoutParams)
                        view.layoutParams =
                            AppBarLayout.LayoutParams(view.measuredWidth, newHeightSpec)
                    return true
                }
            })
        }
    }
}

inline fun <T : View?> T.afterMeasured(
    observeForEver: Boolean = false,
    crossinline block: T.() -> Unit
) =
    this?.viewTreeObserver?.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            avoidException {
                block()
                if (!observeForEver)
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })

inline fun <T : View?> T.safePost(
    delay: Long = 0L,
    crossinline block: T.() -> Unit
) =
    if (delay == 0L)
        this?.post { avoidException { block() } }
    else
        this?.postDelayed({
            avoidException { block() }
        }, delay)

inline fun <T : ViewDataBinding?> T.afterMeasured(
    crossinline block: View?.() -> Unit
) = this?.root.afterMeasured(block = block)

inline fun <T : ViewDataBinding?> T.safePost(
    delay: Long = 0L,
    crossinline block: View?.() -> Unit
) = this?.root.safePost(delay, block)

fun View?.setElevationCompat(elevation: Float) {
    if (this == null)
        return
    ViewCompat.setElevation(this, elevation)
}

fun View?.screenshot(onReadyBitmap: (Bitmap?) -> Unit) {
    if (this == null) {
        onReadyBitmap(null)
        return
    }
    val bitmap = avoidException {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        draw(canvas)
        bitmap
    }
    onReadyBitmap(bitmap)
}

fun <T> View?.updateLayoutParams(onLayoutChange: (params: T) -> Unit) {
    if (this == null)
        return
    avoidException {
        @Suppress("UNCHECKED_CAST")
        onLayoutChange(layoutParams as T)
        layoutParams = layoutParams
    }
}

fun View?.setAttributesFromXml(
    set: AttributeSet?,
    attrs: IntArray,
    block: (it: TypedArray) -> Unit
) {
    if (set == null)
        return

    val a = this?.context?.theme?.obtainStyledAttributes(set, attrs, 0, 0) ?: return
    avoidException(
        tryBlock = {
            block(a)
        },
        finallyBlock = {
            a.recycle()
        }
    )
}

fun View.setPaddingRelativeAll(padding: Int) =
    setPaddingRelative(padding, padding, padding, padding)

fun MeowDraggableLinearLayout.setViewDragListener(vararg cards: MaterialCardView) {
    cards.forEach {
        setViewDragListener(object : MeowDraggableLinearLayout.ViewDragListener {
            override fun onViewCaptured(view: View, i: Int) {
                it.isDragged = true
            }

            override fun onViewReleased(view: View, v: Float, v1: Float) {
                it.isDragged = false
            }
        })

    }
}

fun MaterialCardView.setCheckableWithLongClick(isCheckable: Boolean) {
    setOnLongClickListener(if (isCheckable) View.OnLongClickListener {
        isChecked = !isChecked
        true
    } else null)
}