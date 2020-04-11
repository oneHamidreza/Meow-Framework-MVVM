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

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import meow.controller
import meow.util.dp

/**
 * todo improve & write test
 * The Material Card Recycler View Decoration class inherits from [RecyclerView.ItemDecoration].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-09
 */

@Suppress("unused")
class MaterialCardDecoration(
    private var hasShadow: Boolean = true,
    private var spanCount: Int = 1,
    private var gapSize: Int = 8,
    private var gapStartSize: Int = 8,
    private var gapEndSize: Int = 0,
    private var orientation: Int = ORIENTATION_VERTICAL,
    private var shadowSize: Int = 4,
    private var layoutManager: RecyclerView.LayoutManager? = null
) : RecyclerView.ItemDecoration() {

    companion object {
        const val ORIENTATION_VERTICAL = 1
        const val ORIENTATION_HORIZONTAL = 2
    }

    init {
        gapSize = if (hasShadow) gapSize - shadowSize else gapSize
        gapSize = gapSize.dp()

        gapStartSize = if (hasShadow) gapStartSize - shadowSize else gapStartSize
        gapStartSize = gapStartSize.dp()

        if (gapSize < 0)
            gapSize = 0
        if (gapStartSize < 0)
            gapStartSize = 0
    }

    fun getLastIndex(parent: RecyclerView) =
        (parent.adapter as? ListAdapter<*, *>)?.itemCount ?: 1 - 1


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        val m = if (position % spanCount == 0) position else position - 1

        if (orientation == ORIENTATION_VERTICAL) {
            if (m == 0)
                outRect.top = gapStartSize

            if (spanCount == 1) {
                outRect.left = gapSize
                outRect.right = gapSize
            } else {
                if (position % spanCount == 0) {//left
                    outRect.left = gapSize
                    outRect.right = calculateGapSize() / 2
                } else {//right
                    outRect.left = calculateGapSize() / 2
                    outRect.right = gapSize
                }
            }

            if (m == getLastIndex(parent)) {
                outRect.bottom = gapEndSize.dp()
            } else {
                outRect.bottom = calculateGapSize()
            }

            calculateForRtl(outRect)
        }

        if (orientation == ORIENTATION_HORIZONTAL) {
            if (m == 0)
                outRect.left = gapStartSize

            outRect.right = calculateGapSize()
            calculateForRtl(outRect)
        }

    }

    private fun calculateForRtl(outRect: Rect) {
        if (controller.isRtl) {
            val holder = outRect.left
            outRect.left = outRect.right
            outRect.right = holder
        }
    }

    private fun calculateGapSize() = if (hasShadow) gapSize - shadowSize.dp() else gapSize

}