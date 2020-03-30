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

package sample.ui.material.collapsing.toolbar

import android.os.Bundle
import meow.util.javaClass
import sample.R
import sample.databinding.ActivityCollapsingToolbarBinding
import sample.ui.base.BaseActivity

/**
 * Collapsing Toolbar Activity class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-30
 */

class CollapsingToolbarActivity :
    BaseActivity<ActivityCollapsingToolbarBinding, CollapsingToolbarViewModel>() {

    override fun viewModelClass() = javaClass<CollapsingToolbarViewModel>()
    override fun layoutId() = R.layout.activity_collapsing_toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        title = getString(R.string.activity_collapsing_toolbar)
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun observeViewModel() {
    }

}