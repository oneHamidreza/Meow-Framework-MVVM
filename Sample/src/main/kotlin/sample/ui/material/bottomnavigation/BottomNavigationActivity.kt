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

package sample.ui.material.bottomnavigation

import android.os.Bundle
import android.view.Gravity
import meow.util.instanceViewModel
import meow.util.toastL
import sample.R
import sample.databinding.ActivityBottomNavigationBinding
import sample.ui.base.BaseActivity

/**
 * Material Bottom Navigation Activity.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-05
 */

class BottomNavigationActivity : BaseActivity<ActivityBottomNavigationBinding>() {

    private val viewModel: BottomNavigationViewModel by instanceViewModel()
    override fun layoutId() = R.layout.activity_bottom_navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.activity_bottom_navigation)
        setupBottomNavigation()
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.apply {
//            setupWithNavController(findNavController(R.id.))//todo handle navigation
            setOnNavigationItemSelectedListener {
                toastL(it.title.toString(), gravity = Gravity.CENTER)
                true
            }
        }
    }
}