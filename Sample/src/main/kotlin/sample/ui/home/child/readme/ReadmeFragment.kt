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

package sample.ui.home.child.readme

import android.os.Bundle
import android.view.View
import io.noties.markwon.Markwon
import io.noties.markwon.image.ImagesPlugin
import meow.util.instanceViewModel
import sample.R
import sample.databinding.FragmentReadmeBinding
import sample.ui.base.BaseFragment

/**
 * Readme Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-06
 */

class ReadmeFragment : BaseFragment<FragmentReadmeBinding>() {

    private val viewModel: ReadmeViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_readme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvReadme.apply {
            val str = resources.assets.open("README.md").bufferedReader().use { it.readText() }
            Markwon.builder(context()).apply {
                usePlugin(ImagesPlugin.create())
            }.build().setMarkdown(this, str)
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

}