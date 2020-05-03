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

package sample.ui.markdown

import android.text.Spanned
import androidx.navigation.fragment.navArgs
import meow.core.arch.MeowFlow
import meow.ktx.instanceViewModel
import meow.ktx.safeObserve
import sample.R
import sample.databinding.FragmentMarkdownBinding
import sample.ui.base.BaseFragment
import sample.widget.setMarkdownData

/**
 * Markdown Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-06
 */

class MarkdownFragment : BaseFragment<FragmentMarkdownBinding>() {

    private val navArgs: MarkdownFragmentArgs by navArgs()

    private val viewModel: MarkdownViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_markdown

    override fun initViewModel() {
        if (binding.viewModel == null) {
            binding.viewModel = viewModel
            callApiAndObserve()
        }

        viewModel.modelLiveData.safeObserve(this) {
            binding.tv.setMarkdownData(it)
        }

        if (navArgs.title != null)
            activity().supportActionBar?.title = navArgs.title

    }

    private fun callApiAndObserve() {
        MeowFlow.GetDataApi<Pair<String, Spanned>>(this) {
            viewModel.callApi(navArgs.fileName)
        }.apply {
            errorHandlerType = MeowFlow.ErrorHandlerType.EMPTY_STATE
            swipeRefreshLayout = binding.srl
            emptyStateInterface = binding.emptyState
        }.observeForDetail(viewModel.eventLiveData)
    }

}