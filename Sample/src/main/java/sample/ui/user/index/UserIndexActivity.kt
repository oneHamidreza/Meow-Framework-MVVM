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

package sample.ui.user.index

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import meow.utils.*
import sample.R
import sample.data.User
import sample.databinding.ActivityUserIndexBinding
import sample.ui.BaseActivity

/**
 * [User]/Index Activity class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-29
 */

class UserIndexActivity : BaseActivity<ActivityUserIndexBinding>() {

    private lateinit var listAdapter: UserAdapter

    private val viewModel: UserIndexViewModel by viewModel()
    override fun layoutId() = R.layout.activity_user_index

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listAdapter = UserAdapter(application, viewModel)
        binding.recyclerView.adapter = listAdapter

        viewModel.apiCall()

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.eventLiveData.safeObserve {
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
//                    binding.recyclerView.adapter = listAdapter
                    hideLoading()
                    Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.listLiveData.safeObserve {
            listAdapter.submitList(it)
        }
    }

    private fun showLoading() {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}