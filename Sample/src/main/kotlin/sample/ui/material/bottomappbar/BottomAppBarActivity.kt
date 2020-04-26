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

package sample.ui.material.bottomappbar

import android.os.Bundle
import meow.ktx.instanceViewModel
import meow.widget.decoration.MeowDividerDecoration
import sample.R
import sample.databinding.ActivityBottomAppBarBinding
import sample.ui.base.BaseActivity
import sample.ui.content.ContentAdapter

/**
 * Material Bottom App Bar Activity.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-05
 */

class BottomAppBarActivity : BaseActivity<ActivityBottomAppBarBinding>() {

    private val viewModel: BottomAppBarViewModel by instanceViewModel()

    override fun layoutId() = R.layout.activity_bottom_app_bar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.bar)
        title = getString(R.string.activity_bottom_app_bar)
        setupRecyclerView()
        viewModel.fillList()
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            addItemDecoration(MeowDividerDecoration(context()))
            adapter = ContentAdapter()
        }
    }

}