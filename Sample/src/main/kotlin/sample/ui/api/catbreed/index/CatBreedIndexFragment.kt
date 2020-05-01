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

package sample.ui.api.catbreed.index

import android.os.Bundle
import android.view.View
import meow.core.arch.MeowFlow
import meow.ktx.instanceViewModel
import sample.R
import sample.data.catbreed.CatBreed
import sample.databinding.FragmentCatBreedIndexBinding
import sample.ui.base.BaseFragment

/**
 * [CatBreed]/Index Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-11
 */

class CatBreedIndexFragment : BaseFragment<FragmentCatBreedIndexBinding>() {

    private val viewModel: CatBreedIndexViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_cat_breed_index

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            adapter = CatBreedAdapter()
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        callApiAndObserve()
    }

    private fun callApiAndObserve() {
        MeowFlow.GetDataApi<CatBreed>(this) {
            viewModel.callApi()
        }.apply {
            errorHandlerType = MeowFlow.ErrorHandlerType.EMPTY_STATE
            progressBarInterface = binding.progressbar
            swipeRefreshLayout = binding.srl
            emptyStateInterface = binding.emptyState
        }.observeForIndex(viewModel.eventLiveData, viewModel.listLiveData)
    }

}