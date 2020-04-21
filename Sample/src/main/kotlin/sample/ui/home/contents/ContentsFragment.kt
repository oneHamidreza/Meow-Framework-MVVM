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

package sample.ui.home.contents

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import meow.util.avoidException
import meow.util.instanceViewModel
import meow.widget.decoration.MeowDividerDecoration
import sample.R
import sample.data.Content.Action.*
import sample.databinding.FragmentContentsBinding
import sample.ui.base.BaseFragment
import sample.ui.content.ContentAdapter
import sample.ui.content.ContentViewModel
import sample.ui.home.HomeFragmentDirections
import sample.ui.material.collapsing.toolbar.CollapsingToolbarActivity
import sample.ui.material.topappbar.TopAppBarActivity

/**
 * Contents Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-07
 */

class ContentsFragment : BaseFragment<FragmentContentsBinding>() {

    enum class Type {
        API,
        MATERIAL,
        EXTENSIONS,
        WIDGET,
    }

    companion object {
        const val KEY_TYPE = "type"

        fun newInstance(
            type: Type
        ): ContentsFragment {
            return ContentsFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_TYPE, type.ordinal)
                }
            }
        }
    }

    var type: Type? = null

    private val viewModel: ContentsViewModel by instanceViewModel()
    private val contentViewModel: ContentViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_contents

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_TYPE, type?.ordinal ?: 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (arguments ?: savedInstanceState)?.apply {
            type = avoidException {
                Type.values()[getInt(
                    KEY_TYPE
                )]
            }
        }
        val titles = (when (type!!) {
            Type.API -> resources().getStringArray(R.array.contents_api_titles)
            Type.MATERIAL -> resources().getStringArray(R.array.contents_material_titles)
            Type.EXTENSIONS -> resources().getStringArray(R.array.contents_extensions_titles)
            Type.WIDGET -> resources().getStringArray(R.array.contents_widget_titles)
        }).toList()
        val actions = (when (type!!) {
            Type.API -> arrayOf(
                API_INDEX,
                API_DETAIL,
                API_FORM,
                API_LOGIN
            )
            Type.MATERIAL -> arrayOf(
                MATERIAL_ALERTS,
                MATERIAL_BOTTOM_APP_BAR,
                MATERIAL_BOTTOM_NAVIGATION,
                MATERIAL_BUTTONS,
                MATERIAL_CARDS,
                MATERIAL_CHECKBOXES,
                MATERIAL_COLLAPSING_TOOLBAR,
                MATERIAL_FAB_SIMPLE,
                MATERIAL_FAB_EXTENDED,
                MATERIAL_IMAGEVIEWS,
                MATERIAL_RADIO_BUTTONS,
                MATERIAL_SNACKBARS,
                MATERIAL_SWITCHES,
                MATERIAL_TABLAYOUT,
                MATERIAL_TEXTVIEWS,
                MATERIAL_TOP_APP_BAR
            )
            Type.EXTENSIONS -> arrayOf(
                EXTENSIONS_ANDROID,
                EXTENSIONS_CURRENCY,
                EXTENSIONS_DATE,
                EXTENSIONS_EXCEPTION,
                EXTENSIONS_FILE,
                EXTENSIONS_JSON,
                EXTENSIONS_KODEIN,
                EXTENSIONS_KOTLIN,
                EXTENSIONS_NETWORK,
                EXTENSIONS_PERMISSION,
                EXTENSIONS_SNACKBAR,
                EXTENSIONS_STATUS_BAR,
                EXTENSIONS_SHARED_PREFERENCES,
                EXTENSIONS_STRING,
                EXTENSIONS_SYSTEM,
                EXTENSIONS_TOAST,
                EXTENSIONS_VALIDATE
            )
            Type.WIDGET -> arrayOf(
                WIDGET_ANDROID,
                WIDGET_DASH,
                WIDGET_FORM,
                WIDGET_PROGRESS_BARS,
                WIDGET_RATEBAR,
                WIDGET_TEXT_FIELD
            )
        }).toList()
        val descs = titles.map { "" }
        setupRecyclerView()

        viewModel.fillList(titles, descs, actions)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            addItemDecoration(MeowDividerDecoration(context()))
            adapter = ContentAdapter { _, it, _ ->
                when (it.action) {
                    API_INDEX -> findNavController().navigate(HomeFragmentDirections.actionToCatBreedIndex())
                    API_DETAIL -> findNavController().navigate(HomeFragmentDirections.actionToCatBreedDetail())
                    API_FORM -> findNavController().navigate(HomeFragmentDirections.actionToCatBreedForm())
                    API_LOGIN -> findNavController().navigate(HomeFragmentDirections.actionToLogin())

                    MATERIAL_ALERTS -> findNavController().navigate(HomeFragmentDirections.actionToAlerts())
                    MATERIAL_BOTTOM_APP_BAR -> findNavController().navigate(HomeFragmentDirections.actionToBottomAppBar())
                    MATERIAL_BOTTOM_NAVIGATION -> findNavController().navigate(
                        HomeFragmentDirections.actionToBottomNavigation()
                    )
                    MATERIAL_BUTTONS -> findNavController().navigate(HomeFragmentDirections.actionToButtons())
                    MATERIAL_CARDS -> findNavController().navigate(HomeFragmentDirections.actionToCards())
                    MATERIAL_CHECKBOXES -> findNavController().navigate(HomeFragmentDirections.actionToCheckboxes())
                    MATERIAL_COLLAPSING_TOOLBAR -> startActivity(
                        Intent(
                            context(),
                            CollapsingToolbarActivity::class.java
                        )
                    )//todo add IntentExt
                    MATERIAL_FAB_EXTENDED -> findNavController().navigate(HomeFragmentDirections.actionToFabExtended())
                    MATERIAL_FAB_SIMPLE -> findNavController().navigate(HomeFragmentDirections.actionToFabSimple())
                    MATERIAL_IMAGEVIEWS -> findNavController().navigate(HomeFragmentDirections.actionToImageviews())
                    MATERIAL_RADIO_BUTTONS -> findNavController().navigate(HomeFragmentDirections.actionToRadioButtons())
                    MATERIAL_SNACKBARS -> findNavController().navigate(HomeFragmentDirections.actionToSnackBars())
                    MATERIAL_SWITCHES -> findNavController().navigate(HomeFragmentDirections.actionToSwitches())
                    MATERIAL_TABLAYOUT -> findNavController().navigate(HomeFragmentDirections.actionToTabLayout())
                    MATERIAL_TEXTVIEWS -> findNavController().navigate(HomeFragmentDirections.actionToTextviews())
                    MATERIAL_TOP_APP_BAR -> startActivity(
                        Intent(
                            context(),
                            TopAppBarActivity::class.java
                        )
                    )

                    WIDGET_DASH -> findNavController().navigate(HomeFragmentDirections.actionToDash())
                    WIDGET_FORM -> findNavController().navigate(HomeFragmentDirections.actionToForm())
                    WIDGET_PROGRESS_BARS -> findNavController().navigate(HomeFragmentDirections.actionToProgressBars())
                    WIDGET_RATEBAR -> findNavController().navigate(HomeFragmentDirections.actionToRateBar())

                    EXTENSIONS_SHARED_PREFERENCES -> findNavController().navigate(
                        HomeFragmentDirections.actionToSharedPreferences()
                    )

                    else -> {
                    }
                }
            }
        }
    }

}