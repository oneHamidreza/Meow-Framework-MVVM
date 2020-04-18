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

package sample.ui.api.login

import android.os.Bundle
import android.view.View
import meow.core.api.MeowOauthToken
import meow.core.arch.MeowFlow
import meow.util.instanceViewModel
import meow.util.loadingAlert
import meow.util.toastL
import org.kodein.di.erased.instance
import sample.R
import sample.data.DataSource
import sample.data.user.User
import sample.databinding.FragmentLoginBinding
import sample.ui.base.BaseFragment

/**
 * Login Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-14
 */

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val dataSource: DataSource by instance()

    private val viewModel: LoginViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btSend.setOnClickListener {
            binding.formView.validate {
                callApiAndObserve()
            }
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        binding.apply {
            etUsername.apiField = "username"
            etPassword.apiField = "password"
        }
    }

    private fun callApiAndObserve() {
        MeowFlow.PutDataApi<MeowOauthToken>(this) {
            val request = User.Api.RequestLogin(
                username = binding.etUsername.textString,
                password = binding.etPassword.textString
            )
            viewModel.callApi(request)
        }.apply {
            errorHandlerType = MeowFlow.ErrorHandlerType.SNACK_BAR
            dialog = loadingAlert(R.string.dialog_login).show()
            onRequestNotValidFromResponse = {
                binding.formView.showErrorFromApi(it)
            }
            onSuccessAction = {
                if (it.isLoginSuccess) {
                    binding.formView.resetForm()
                    dataSource.saveApiToken(it.accessToken ?: "")
                    toastL(R.string.warn_login_success)
                } else
                    toastL(R.string.warn_login_failed)
            }
        }.observeForForm(viewModel.eventLiveData)
    }

}