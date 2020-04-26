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

package sample.ui.home

import com.google.android.material.tabs.TabLayoutMediator
import meow.ktx.getStringArrayCompat
import meow.ktx.instanceViewModel
import sample.R
import sample.databinding.FragmentHomeBinding
import sample.ui.base.BaseFragment

/**
 * Home Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-03
 */

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_home

    override fun initViewModel() {
        binding.viewModel = viewModel
        binding.apply {
            viewpager.offscreenPageLimit = 5
            viewpager.adapter = HomePagerAdapter(childFragmentManager, lifecycle)
            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                tab.text = getStringArrayCompat(R.array.home_tab_titles)[position]
            }.attach()
        }
    }

}