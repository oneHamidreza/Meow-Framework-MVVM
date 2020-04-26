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

package sample.ui.menu

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import meow.ktx.dp
import meow.ktx.instanceViewModel
import meow.ktx.popup
import meow.ktx.safeObserve
import meow.widget.addItemDecoration
import meow.widget.decoration.MeowDividerDecoration
import sample.R
import sample.data.Content
import sample.databinding.FragmentMenuBinding
import sample.ui.base.BaseFragment
import sample.ui.content.ContentAdapter

/**
 * Menu Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-11
 */

class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    private val viewModel: MenuViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_menu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()
        viewModel.fillList()

        binding.completeTextView.addTextChangedListener {
            viewModel.search(it.toString())
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        viewModel.listLiveData.safeObserve(this) {
            if (binding.completeTextView.adapter == null) {
                val suggesteds =
                    it.filterIndexed { i, _ -> i % 10 == 0 }.map { content -> content.title }
                binding.completeTextView.setAdapter(
                    ArrayAdapter(
                        context(),
                        R.layout.dropdown_menu_popup_item,
                        suggesteds
                    )
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_sample, menu)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            addItemDecoration(MeowDividerDecoration(context()))
            addItemDecoration { position, outRect ->
                if (position == 0)
                    outRect.top = binding.textInput.measuredHeight + 24.dp()
            }
            adapter = ContentAdapter() { _, it, view ->
                showPopup(it, view)
            }
        }
    }

    private fun showPopup(content: Content, view: View) {
        popup(view, R.menu.menu_content) {
            viewModel.delete(content)
        }.show()
    }

}