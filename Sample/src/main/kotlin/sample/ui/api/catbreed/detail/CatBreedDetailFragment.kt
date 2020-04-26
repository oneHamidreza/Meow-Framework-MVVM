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

package sample.ui.api.catbreed.detail

import meow.controller
import meow.core.arch.MeowFlow
import meow.ktx.instanceViewModel
import sample.R
import sample.data.catbreed.CatBreed
import sample.databinding.FragmentCatBreedDetailBinding
import sample.ui.base.BaseFragment

/**
 * [CatBreed]/Detail Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-11
 */

class CatBreedDetailFragment : BaseFragment<FragmentCatBreedDetailBinding>() {

    private val viewModel: CatBreedDetailViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_cat_breed_detail

    override fun initViewModel() {
        binding.viewModel = viewModel
        binding.controller = controller
        callApiAndObserve()
    }

    private fun callApiAndObserve() {
        MeowFlow.GetDataApi<CatBreed>(this) {
            viewModel.callApi()
        }.apply {
            errorHandlerType = MeowFlow.ErrorHandlerType.EMPTY_STATE
            progressBarInterface = binding.progressbar
            emptyStateInterface = binding.emptyState
        }.observeForDetail(viewModel.eventLiveData)
    }

}