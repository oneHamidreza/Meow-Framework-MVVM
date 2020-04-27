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
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.customview.widget.ViewDragHelper
import java.util.*

/**
 * Meow Draggable Linear Layout.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-30
 */

class MeowDraggableLinearLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) : LinearLayout(context!!, attrs) {

    /** A listener to use when a child view is being dragged  */
    interface ViewDragListener {
        fun onViewCaptured(view: View, i: Int)
        fun onViewReleased(
            view: View,
            v: Float,
            v1: Float
        )
    }

    private val viewDragHelper: ViewDragHelper
    private val draggableChildren: MutableList<View> =
        ArrayList()
    private var viewDragListener: ViewDragListener? = null
    fun addDraggableChild(child: View) {
        require(!(child.parent !== this))
        draggableChildren.add(child)
    }

    fun removeDraggableChild(child: View) {
        require(!(child.parent !== this))
        draggableChildren.remove(child)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(ev)
        return super.onTouchEvent(ev)
    }

    private val dragCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(view: View, i: Int): Boolean {
            return view.visibility == View.VISIBLE && viewIsDraggableChild(view)
        }

        override fun onViewCaptured(view: View, i: Int) {
            if (viewDragListener != null) {
                viewDragListener!!.onViewCaptured(view, i)
            }
        }

        override fun onViewReleased(
            view: View,
            v: Float,
            v1: Float
        ) {
            if (viewDragListener != null) {
                viewDragListener!!.onViewReleased(view, v, v1)
            }
        }

        override fun getViewHorizontalDragRange(view: View): Int {
            return view.width
        }

        override fun getViewVerticalDragRange(view: View): Int {
            return view.height
        }

        override fun clampViewPositionHorizontal(
            view: View,
            left: Int,
            dx: Int
        ): Int {
            return left
        }

        override fun clampViewPositionVertical(
            view: View,
            top: Int,
            dy: Int
        ): Int {
            return top
        }
    }

    private fun viewIsDraggableChild(view: View): Boolean {
        return draggableChildren.isEmpty() || draggableChildren.contains(view)
    }

    fun setViewDragListener(viewDragListener: ViewDragListener?) {
        this.viewDragListener = viewDragListener
    }

    init {
        viewDragHelper = ViewDragHelper.create(this, dragCallback)
    }
}