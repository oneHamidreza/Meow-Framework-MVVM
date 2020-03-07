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
import meow.utils.*
import sample.R
import sample.data.User
import sample.databinding.ActivityUserGetBinding
import sample.ui.BaseActivity

/**
 * [User]/Get Activity class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-29
 */

class UserGetActivity : BaseActivity<ActivityUserGetBinding>() {

    private val viewModel: UserGetViewModel by viewModel()
    override fun layoutId() = R.layout.activity_user_get

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btApiCall.apply {
            setOnClickListener {
                viewModel.apiCall(User.RequestGet("1"))
            }
            performClick()
        }

        binding.btCancel.setOnClickListener {
            viewModel.cancelAllJobs()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.modelLiveData.safeObserve {
            binding.model = viewModel.model
            logD(m = "New Status Received : $it")
            when {
                it.isCancellation() -> {
                    hideLoading()
                    showError("Canceling is Working")
                }
                it.isError() -> {
                    hideLoading()
                    showError(it.response.createErrorMessage(resources))
                }
                it.isLoading() -> {
                    showLoading()
                }
                it.isSuccess() -> {
                    hideLoading()
                    binding.model = viewModel.model
                    Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showLoading() {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
    }

    private fun showError(message: String) {
        binding.tvStatus.text = message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}