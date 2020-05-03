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

package sample.ui.sharedpreferences

import android.os.Bundle
import android.text.Spanned
import android.view.View
import meow.core.arch.MeowFlow
import meow.ktx.instanceViewModel
import meow.ktx.safeObserve
import meow.ktx.snackL
import sample.R
import sample.databinding.FragmentSharedPreferencesBinding
import sample.ui.base.BaseFragment
import sample.widget.setMarkdownData

/**
 * Shared Preferences Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-03
 */

class SharedPreferencesFragment : BaseFragment<FragmentSharedPreferencesBinding>() {

    private val viewModel: SharedPreferencesViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_shared_preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApiAndObserve()
    }

    private fun callApiAndObserve() {
        MeowFlow.GetDataApi<Pair<String, Spanned>>(this) {
            viewModel.callApi("Docs/ReadME_Extensions_Shared_Preferences.md")
        }.apply {
            errorHandlerType = MeowFlow.ErrorHandlerType.EMPTY_STATE
            progressBarInterface = binding.progressbar
            emptyStateInterface = binding.emptyState
        }.observeForDetail(viewModel.eventLiveData)

        viewModel.modelLiveData.safeObserve(this) {
            binding.tv.setMarkdownData(it)
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        viewModel.userStateLiveData.safeObserve(this) {
            val text = it.second.toString()
            when (it.first) {
                State.PUT -> {
                    snackL(getString(R.string.shared_preferences_warn_put) + " " + text)
                }
                State.GET -> {
                    snackL(getString(R.string.shared_preferences_warn_get) + " " + text)
                }
            }
        }

        viewModel.testStateLiveData.safeObserve(this) {
            snackL(it)
        }
    }

}