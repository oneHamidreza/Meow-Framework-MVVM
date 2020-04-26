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

package sample.ui.api.catbreed.form

import android.os.Bundle
import android.view.View
import meow.controller
import meow.core.arch.MeowFlow
import meow.ktx.instanceViewModel
import meow.ktx.toastL
import sample.R
import sample.data.catbreed.CatBreed
import sample.databinding.FragmentCatBreedFormBinding
import sample.ui.base.BaseFragment

/**
 * [CatBreed]/Form Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-14
 */

class CatBreedFormFragment : BaseFragment<FragmentCatBreedFormBinding>() {

    private val viewModel: CatBreedFormViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_cat_breed_form

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btSend.setOnClickListener {
            callApiAndObserve()
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        binding.apply {
            progressbar.hide()
            etName.apiField = "name"//todo @Ali create field from attrs
        }
    }

    private fun callApiAndObserve() {
        MeowFlow.PutDataApi<CatBreed.Api.ResponseCreate>(this) {
            binding.formView.validate {
                val request = CatBreed.Api.RequestCreate(binding.etName.textString)
                viewModel.callApi(request)
            }
        }.apply {
            containerViews = arrayOf(binding.formView)
            errorHandlerType = MeowFlow.ErrorHandlerType.SNACK_BAR
            progressBarInterface = binding.progressbar
//            dialog = loadingAlert(R.string.dialog_cat_breed_form).show()
            onRequestNotValidFromResponse = {
                binding.formView.showErrorFromApi(it)
            }
            onSuccessAction = {
                binding.formView.resetForm()
                toastL(if (controller.isPersian) it.persianMessage else it.message)
            }
        }.observeForForm(viewModel.eventLiveData)
    }

}