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

package sample.ui.material.alert

import android.os.Bundle
import android.view.View
import meow.util.*
import meow.widget.decoration.MeowDividerDecoration
import sample.R
import sample.data.Content
import sample.databinding.FragmentAlertsBinding
import sample.ui.base.BaseFragment
import sample.ui.content.ContentAdapter
import sample.ui.content.ContentViewModel

/**
 * Material Alerts Fragment class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-22
 */

class AlertsFragment : BaseFragment<FragmentAlertsBinding, AlertsViewModel>() {

    override fun layoutId() = R.layout.fragment_alerts
    override fun viewModelClass() = javaClass<AlertsViewModel>()

    private val contentViewModel by viewModel(javaClass<ContentViewModel>())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.fillList()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            addItemDecoration(MeowDividerDecoration(context()))
            adapter = ContentAdapter(contentViewModel) {
                when (it.action) {
                    Content.Action.ALERT_SIMPLE -> {
                        alert()
                            .setTitle(R.string.alert_simple_title)
                            .setMessage(R.string.alert_simple_message)
                            .show()
                    }
                    Content.Action.ALERT_SIMPLE_WITH_LISTENER -> {
                        alert()
                            .setTitle(R.string.alert_simple_with_listener_title)
                            .setMessage(R.string.alert_simple_with_listener_message)
                            .setPositiveButton(R.string.ok) { d, _ ->
                                toastL(R.string.toast_alert_ok_clicked)
                                d.dismiss()
                            }
                            .setNegativeButton(R.string.cancel) { d, _ ->
                                toastL(R.string.toast_alert_cancel_clicked)
                                d.dismiss()
                            }
                            .show()
                    }
                    Content.Action.ALERT_LOADING -> {
                        loadingAlert(getString(R.string.alert_loading_title)) {
                            toastL(R.string.toast_alert_on_canceled)
                        }.show()
                    }
                }
            }
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun observeViewModel() {
    }

}