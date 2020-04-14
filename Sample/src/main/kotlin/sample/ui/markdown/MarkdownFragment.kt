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

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import meow.util.instanceViewModel
import sample.R
import sample.databinding.FragmentMarkdownBinding
import sample.ui.base.BaseFragment
import sample.widget.TextViewBinding

/**
 * Markdown Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-06
 */

class MarkdownFragment : BaseFragment<FragmentMarkdownBinding>() {

    val navArgs: MarkdownFragmentArgs by navArgs()

    private val viewModel: MarkdownViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_markdown

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        TextViewBinding.setMarkdownAssets(binding.tv, navArgs.fileName)
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

}