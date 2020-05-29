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

package sample.ui.material.tablayout

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import meow.ktx.instanceViewModel
import sample.R
import sample.databinding.FragmentTablayoutBinding
import sample.ui.base.BaseFragment

/**
 * Material Tab Layout Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-22
 */

class TabLayoutFragment : BaseFragment<FragmentTablayoutBinding>() {

    private val viewModel: TabLayoutViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_tablayout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.apply {
            viewpager.isSaveEnabled = false
            viewpager.adapter = TabLayoutPagerAdapter(childFragmentManager, lifecycle)
            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                tab.text = getString(R.string.tab_title).format(position + 1)
            }.attach()

            tabLayout.getTabAt(0)?.orCreateBadge?.apply {
                isVisible = true
                number = viewModel!!.getBadgeNumber()
            }
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

}