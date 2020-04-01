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
import meow.core.arch.MeowFlow
import meow.util.javaClass
import meow.util.safeObserve
import meow.util.snackL
import meow.util.toastL
import sample.R
import sample.data.user.User
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

    override fun viewModelClass() = javaClass<UserDetailViewModel>()
    override fun layoutId() = R.layout.fragment_user_detail

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callApi()
    }

    private fun callApi() {
        viewModel.callApi(User.RequestGet("1"))
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun observeViewModel() {
        MeowFlow.GetDataApi(this).apply {
            containerViews = arrayOf(binding.tvModel, binding.tvStatus)
        }.observe(binding.lifecycleOwner, viewModel.apiLiveData)

        viewModel.snackMessageLiveData.safeObserve(this) {
            snackL(it, "OK") {
                toastL(R.string.app_name)
            }
        }

        viewModel.permissionLiveData.safeObserve(this) {
            needPermission(it) { isSuccess ->
                snackL("permission is Granted : $isSuccess")
            }
        }
    }

}