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

package sample.ui.user.get

import android.os.Bundle
import android.widget.Toast
import meow.core.api.MeowResponse
import meow.core.arch.ui.MeowActivity
import meow.utils.*
import sample.R
import sample.data.User
import sample.databinding.ActivityUserGetBinding

/**
 * [User]/Get Activity class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-29
 */

class UserGetActivity : MeowActivity<ActivityUserGetBinding, UserGetViewModel>() {

    override fun layoutId() = R.layout.activity_user_get

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = initViewModel()

        binding.btApiCall.apply {
            setOnClickListener {
                viewModel.apiCall(User.RequestGet("1"))
            }
            performClick()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.modelLiveData.safeObserve {
            println(it)
            when {
                it.isLoading() -> {
                    showLoading()
                }
                it.isSuccess() -> {
                    hideLoading()
                    binding.model = viewModel.model
                    Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
                }
                it.isError() -> {
                    hideLoading()
                    val response = it.response as MeowResponse.Error
                    val message = "response.isError: ${response.exception?.message}"
                    showError(message)//todo create message
                }
                else ->
                    showError("response.isNull")
            }
        }
    }

    private fun showLoading() {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
    }

    private fun showError(message: String) {
        logD("user-get", message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}