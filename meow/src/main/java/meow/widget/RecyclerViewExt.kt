package meow.widget

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.*
import meow.widget.decoration.ItemOffsetBlock
import meow.widget.decoration.SimpleDecoration

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

/**
 * [RecyclerView] class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-30
 */

object RecyclerViewBindingAdapter {

    @BindingAdapter("meow_items")
    @JvmStatic
    fun <T : Any, VH : RecyclerView.ViewHolder> setItems(view: RecyclerView, items: List<T>?) {
        @Suppress("UNCHECKED_CAST")
        (view.adapter as? ListAdapter<T, VH>)?.submitList(items?.toMutableList())
    }

}

class ReverseLinearLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        reverseLayout = true
    }
}

open class HorizontalLinearLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        orientation = HORIZONTAL
    }
}

class ReverseHorizontalLinearLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : HorizontalLinearLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        reverseLayout = true
    }
}

open class DefaultGridLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : GridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        spanCount = 2
    }
}

class ReverseGridLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : DefaultGridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        reverseLayout = true
    }
}

open class HorizontalGridLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : DefaultGridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        orientation = LinearLayoutManager.HORIZONTAL
    }
}

class ReverseHorizontalGridLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : HorizontalGridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        reverseLayout = true
    }
}

open class DefaultStaggeredGridLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : StaggeredGridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        spanCount = 2
    }
}

class ReverseStaggeredGridLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : DefaultStaggeredGridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        reverseLayout = true
    }
}

open class HorizontalStaggeredGridLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : DefaultStaggeredGridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        orientation = LinearLayoutManager.HORIZONTAL
    }
}

class ReverseHorizontalStaggeredGridLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : HorizontalStaggeredGridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        reverseLayout = true
    }
}

fun RecyclerView.addItemDecoration(block: ItemOffsetBlock) =
    addItemDecoration(SimpleDecoration(block))