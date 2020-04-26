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

package sample.ui.material.alerts

import android.os.Bundle
import android.view.View
import meow.ktx.alert
import meow.ktx.instanceViewModel
import meow.ktx.loadingAlert
import meow.ktx.toastL
import meow.widget.decoration.MeowDividerDecoration
import sample.R
import sample.data.Content
import sample.databinding.FragmentAlertsBinding
import sample.ui.base.BaseFragment
import sample.ui.content.ContentAdapter

/**
 * Material Alerts Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-22
 */

class AlertsFragment : BaseFragment<FragmentAlertsBinding>() {

    private val viewModel: AlertsViewModel by instanceViewModel()

    override fun layoutId() = R.layout.fragment_alerts

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.fillList()
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            addItemDecoration(MeowDividerDecoration(context()))
            adapter = ContentAdapter { _, it, _ ->
                when (it.action) {
                    Content.Action.ALERT_SIMPLE -> {
                        alert()
                            .setTitle(R.string.alerts_simple_title)
                            .setMessage(R.string.alerts_simple_message)
                            .show()
                    }
                    Content.Action.ALERT_SIMPLE_WITH_LISTENER -> {
                        alert()
                            .setTitle(R.string.alerts_simple_with_listener_title)
                            .setMessage(R.string.alerts_simple_with_listener_message)
                            .setPositiveButton(R.string.ok) { d, _ ->
                                toastL(R.string.alerts_warn_ok_clicked)
                                d.dismiss()
                            }
                            .setNegativeButton(R.string.cancel) { d, _ ->
                                toastL(R.string.alerts_warn_cancel_clicked)
                                d.dismiss()
                            }
                            .show()
                    }
                    Content.Action.ALERT_LOADING -> {
                        loadingAlert(getString(R.string.alerts_loading_title)) {
                            toastL(R.string.alerts_warn_on_canceled)
                        }.show()
                    }
                    else -> {
                    }
                }
            }
        }
    }

}