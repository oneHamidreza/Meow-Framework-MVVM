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

package meow.widget.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowframework.R
import meow.controller
import meow.util.dp
import meow.util.getColorCompat
import meow.util.getDimensionToPx

/**
 * The Divider Recycler View Decoration class inherits from [RecyclerView.ItemDecoration].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-09
 */

@Suppress("unused")
class MeowDividerDecoration(
    context: Context,
    marginStart: Int = 0,
    marginEnd: Int = 0,
    private var startPosition: Int = 0
) : RecyclerView.ItemDecoration() {

    private val divider = GradientDrawable()
    private var marginRight: Int
    private var marginLeft: Int

    init {
        divider.shape = GradientDrawable.RECTANGLE
        divider.setSize(1, context.getDimensionToPx(R.dimen.divider_recyclerview_height))
        divider.setColor(context.getColorCompat(R.color.divider_recyclerview))

        marginLeft = marginStart.dp()
        marginRight = marginEnd.dp()

        if (controller.isRtl) {
            val holder = marginLeft
            marginLeft = marginRight
            marginRight = holder
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = 1.dp()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        drawVertical(c, parent)
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft + marginLeft
        val right = parent.width - parent.paddingRight - marginRight

        val childCount = parent.childCount
        for (i in startPosition until childCount - 1) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
    //
    //    public void drawHorizontal(Canvas c, RecyclerView parent) {
    //        final int top = parent.getPaddingTop();
    //        final int bottom = parent.getHeight() - parent.getPaddingBottom();
    //
    //        final int childCount = parent.getChildCount();
    //        for (int i = 0; i < childCount; i++) {
    //            final View child = parent.getChildAt(i);
    //            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
    //                    .getLayoutParams();
    //            final int left = child.getRight() + params.rightMargin;
    //            final int right = left + divider.getIntrinsicHeight();
    //            divider.setBounds(left, top, right, bottom);
    //            divider.draw(c);
    //        }
    //    }
}