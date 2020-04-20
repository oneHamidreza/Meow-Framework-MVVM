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

package sample.ui.material.fab.extended

import android.os.Bundle
import android.view.View
import meow.util.instanceViewModel
import meow.util.safeObserve
import meow.widget.decoration.MeowDividerDecoration
import meow.widget.safePost
import sample.R
import sample.databinding.FragmentFabExtendedBinding
import sample.ui.base.BaseFragment
import sample.ui.content.ContentAdapter
import sample.ui.content.ContentViewModel

/**
 * Material Floating Action Button Extended Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-22
 */

class FABExtendedFragment : BaseFragment<FragmentFabExtendedBinding>() {

    private val viewModel: FABExtendedViewModel by instanceViewModel()
    private val contentViewModel: ContentViewModel by instanceViewModel()

    override fun layoutId() = R.layout.fragment_fab_extended

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.fillList()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            addItemDecoration(MeowDividerDecoration(context()))
            adapter = ContentAdapter()
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        viewModel.scrollToFirstLiveData.safeObserve(viewLifecycleOwner) {
            binding.recyclerView.safePost(50) {
                scrollToPosition(0)
            }
        }
    }

}