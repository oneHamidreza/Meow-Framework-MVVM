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

    private var cachedModel: String? = null

    private val viewModel: MarkdownViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_markdown

    override fun initViewModel() {
        binding.viewModel = viewModel
        if (navArgs.title != null)
            activity().supportActionBar?.title = navArgs.title
        if (cachedModel == null)
            callApiAndObserve(true) //todo improve avoid call twice
        else {
            callApiAndObserve(false)
            viewModel.modelLiveData.postValue(cachedModel)
        }
    }

    private fun callApiAndObserve(allowCallApi: Boolean) {
        MeowFlow.GetDataApi<String>(this) {
            viewModel.callApi(navArgs.fileName)
        }.apply {
            allowCallAction = allowCallApi
            errorHandlerType = MeowFlow.ErrorHandlerType.EMPTY_STATE
            progressBarInterface = binding.progressbar
            swipeRefreshLayout = binding.srl
            emptyStateInterface = binding.emptyState
        }.observeForDetail(viewModel.eventLiveData)

        viewModel.modelLiveData.safeObserve(this) {//todo call twice after pop-up
            binding.tv.setMarkdownData(it)
            cachedModel = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cachedModel = null
    }

    override fun onDestroy() {
        super.onDestroy()
        cachedModel = null
    }

}