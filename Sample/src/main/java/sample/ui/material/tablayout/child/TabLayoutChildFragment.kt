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

package sample.ui.material.tablayout.child

import android.os.Bundle
import meow.util.createClass
import sample.R
import sample.databinding.FragmentTablayoutChildBinding
import sample.ui.base.BaseFragment

/**
 * Material Tab Layout Child Fragment class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-22
 */

class TabLayoutChildFragment :
    BaseFragment<FragmentTablayoutChildBinding, TabLayoutChildViewModel>() {

    companion object {
        private const val KEY_POS = "pos"

        fun newInstance(pos: Int) = TabLayoutChildFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_POS, pos)
            }
        }

    }

    private var pos = 0

    override fun layoutId() = R.layout.fragment_tablayout_child
    override fun viewModelClass() = createClass<TabLayoutChildViewModel>()

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_POS, pos)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = savedInstanceState ?: arguments
        bundle?.apply {
            pos = getInt(KEY_POS)
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        binding.pos = pos
    }

    override fun observeViewModel() {

    }

}