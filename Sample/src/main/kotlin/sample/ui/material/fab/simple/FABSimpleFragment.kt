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

package sample.ui.material.fab.simple

import android.os.Bundle
import android.view.View
import meow.ktx.dp
import meow.ktx.instanceViewModel
import meow.ktx.safeObserve
import meow.ktx.snackL
import meow.widget.addItemDecoration
import meow.widget.decoration.MeowDividerDecoration
import meow.widget.safePost
import sample.R
import sample.databinding.FragmentFabSimpleBinding
import sample.ui.base.BaseFragment
import sample.ui.content.ContentAdapter

/**
 * Material Floating Action Button Fragment class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-22
 */

class FABSimpleFragment : BaseFragment<FragmentFabSimpleBinding>() {

    private val viewModel: FABSimpleViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_fab_simple

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.fillList()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            addItemDecoration(MeowDividerDecoration(context()))
            addItemDecoration { position, outRect ->
                if (position == binding.recyclerView.adapter!!.itemCount - 1)
                    outRect.bottom = 72.dp()
            }
            adapter = ContentAdapter()
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        viewModel.addItemLiveData.safeObserve(this) {
            binding.recyclerView.safePost(50) {
                scrollToPosition(0)
            }
            snackL(R.string.fab_simple_snack_success)
        }
    }

}