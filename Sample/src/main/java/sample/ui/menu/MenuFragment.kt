package sample.ui.menu

import android.graphics.Color
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import meow.utils.safeObserve
import meow.utils.setTintCompat
import sample.R
import sample.core.actionToUserDetail
import sample.databinding.FragmentMenuBinding
import sample.ui.base.BaseFragment

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

/**
 * Menu Fragment class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-11
 */

class MenuFragment : BaseFragment<FragmentMenuBinding, MenuViewModel>() {

    override fun layoutId() = R.layout.fragment_menu
    override fun viewModelClass() = MenuViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()

        binding.ivTest.drawable.setTintCompat(Color.RED)
    }

    private fun observeViewModel() {
        binding.viewModel = viewModel
        binding.viewModel!!.navigationLiveData.safeObserve(binding.lifecycleOwner) {
            when (it) {
                R.id.actionToUserDetail -> findNavController().actionToUserDetail()
            }
        }
    }

}