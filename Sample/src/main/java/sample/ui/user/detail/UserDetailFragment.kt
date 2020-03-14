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

package sample.ui.user.detail

import android.os.Bundle
import android.widget.Toast
import meow.core.arch.MeowFlow
import meow.utils.createErrorMessage
import sample.R
import sample.data.User
import sample.databinding.FragmentUserDetailBinding
import sample.ui.base.BaseFragment

/**
 * [User]/Detail Fragment class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-29
 */

class UserDetailFragment : BaseFragment<FragmentUserDetailBinding, UserDetailViewModel>() {

    override fun viewModelClass() = UserDetailViewModel::class.java
    override fun layoutId() = R.layout.fragment_user_detail

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        observeViewModel()

        viewModel.requestApi(User.RequestGet("1"))
    }

    private fun initViewModel() {
        binding.viewModel = viewModel
    }

    private fun observeViewModel() {
        MeowFlow.DetailApi(arrayOf(binding.tvModel, binding.tvStatus)).apply {
            onCancellationAction = {
                showError("Canceling is Working")
            }
            onSuccessAction = {
                binding.model = viewModel.model
            }
            onErrorAction = {
                showError(it.data.createErrorMessage(resources))
            }
        }.observe(binding.lifecycleOwner!!, viewModel.apiLiveData)
    }

    private fun showError(message: String) {
        binding.tvStatus.text = message
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}