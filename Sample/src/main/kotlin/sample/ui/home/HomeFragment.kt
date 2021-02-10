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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

    private var rootView: View? = null

    private val viewModel: HomeViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewpager.offscreenPageLimit = 5
            viewpager.isSaveEnabled =
                false // for this bug : java.lang.IllegalStateException: Fragment no longer exists for key
            viewpager.adapter = HomePagerAdapter(childFragmentManager, lifecycle)
            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                tab.text = getStringArrayCompat(R.array.home_tab_titles)[position]
            }.attach()
        }
    }

    override fun getPersistentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        layout: Int
    ): View {
        if (rootView == null) {
            // Inflate the layout for this fragment
            binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
            rootView = binding.root
        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove rootView from the existing parent view group
            // (it will be added back).
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
            isFromNavigateUp = true
        }
        return rootView!!
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

}