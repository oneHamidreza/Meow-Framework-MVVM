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

package sample.ui.content

import android.view.LayoutInflater
import android.view.ViewGroup
import meow.core.ui.MeowAdapter
import meow.core.ui.MeowViewHolder
import sample.data.Content
import sample.databinding.ItemContentBinding

/**
 * [Title] Adapter class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-21
 */

typealias Model = Content
typealias ViewHolder = MeowViewHolder<Model, ViewModel>
typealias ViewModel = ContentViewModel
typealias DiffCallback = Content.DiffCallback

class ContentAdapter(
    override var viewModel: ViewModel,
    var onClickedItem: (item: Model) -> Unit
) : MeowAdapter<Model, ViewHolder, ViewModel>(
    viewModel,
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeowViewHolder(binding.root) { _, model, viewModel ->
            binding.let {
                it.viewModel = viewModel
                it.model = model
                it.executePendingBindings()

                it.root.setOnClickListener {
                    onClickedItem(model)
                }
            }
        }
    }

}