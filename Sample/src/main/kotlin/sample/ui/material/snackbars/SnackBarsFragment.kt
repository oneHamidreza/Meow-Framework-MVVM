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

package sample.ui.material.snackbars

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import meow.ktx.instanceViewModel
import meow.ktx.snackI
import meow.ktx.snackL
import meow.ktx.toastL
import meow.widget.decoration.MeowDividerDecoration
import sample.R
import sample.data.Content
import sample.databinding.FragmentSnackbarsBinding
import sample.ui.base.BaseFragment
import sample.ui.content.ContentAdapter

/**
 * Material Snack Bars Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-22
 */

class SnackBarsFragment : BaseFragment<FragmentSnackbarsBinding>() {

    private val viewModel: SnackBarsViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_snackbars

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.fillList()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            addItemDecoration(MeowDividerDecoration(context()))
            adapter = ContentAdapter { _, it, _ ->
                when (it.action) {
                    Content.Action.SNACK_SIMPLE -> {
                        snackL(R.string.snackbars_message)
                    }
                    Content.Action.SNACK_SIMPLE_WITH_ACTION -> {
                        snackL(R.string.snackbars_message, R.string.snackbars_action) {
                            toastL(R.string.snackbars_action_clicked)
                        }
                    }
                    Content.Action.SNACK_CUSTOMIZED_COLOR -> {
                        snackL(
                            R.string.snackbars_message,
                            R.string.snackbars_action,
                            messageTextAppearanceId = R.style.App_TextAppearance_Snack_Message,
                            actionTextAppearanceId = R.style.App_TextAppearance_Snack_Action
                        )
                    }
                    Content.Action.SNACK_INDEFINITE -> {
                        var snackbar: Snackbar? = null
                        snackbar = snackI(R.string.snackbars_message, R.string.snackbars_action) {
                            snackbar?.dismiss()
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

}