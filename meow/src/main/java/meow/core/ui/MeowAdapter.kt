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

package meow.core.ui

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import meow.core.arch.MeowViewModel

/**
 * Meow Adapter class inherits from [RecyclerView.Adapter].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-08
 */

abstract class MeowAdapter<T, VH : MeowViewHolder<T, VM>, VM : MeowViewModel>(
    open var viewModel: VM,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback) {
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(position, getItem(position), viewModel)
    }
}