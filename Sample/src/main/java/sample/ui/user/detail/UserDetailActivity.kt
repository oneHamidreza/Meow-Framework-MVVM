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
import meow.core.arch.MeowComponent
import meow.utils.createErrorMessage
import meow.utils.viewModel
import sample.R
import sample.data.User
import sample.databinding.ActivityUserDetailBinding
import sample.ui.BaseActivity

/**
 * [User]/Get Activity class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-29
 */

class UserDetailActivity : BaseActivity<ActivityUserDetailBinding>() {

    private val viewModel: UserDetailViewModel by viewModel()
    override fun layoutId() = R.layout.activity_user_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeViewModel()

        viewModel.requestApi(User.RequestGet("1"))
    }

    private fun observeViewModel() {
        binding.viewModel = viewModel
        MeowComponent.DetailApi(arrayOf(binding.tvModel, binding.tvStatus)).apply {
            onCancellationAction = {
                showError("Canceling is Working")
            }
            onSuccessAction = {
                binding.model = viewModel.model
            }
            onErrorAction = {
                showError(it.response.createErrorMessage(resources))
            }
        }.observe(viewModel.eventLiveData)
    }

    private fun showError(message: String) {
        binding.tvStatus.text = message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}