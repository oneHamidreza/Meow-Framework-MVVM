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

package sample.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import meow.controller
import meow.ktx.getColorCompat
import meow.ktx.getDimensionToPx
import meow.ktx.instanceViewModel
import meow.ktx.sdkNeed
import meow.widget.setElevationCompat
import sample.R
import sample.databinding.ActivityMainBinding
import sample.ui.base.BaseActivity
import sample.ui.home.HomeFragmentDirections
import sample.ui.material.bottomappbar.BottomAppBarActivity
import sample.ui.material.bottomnavigation.BottomNavigationActivity
import sample.ui.material.collapsing.toolbar.CollapsingToolbarActivity
import sample.ui.material.topappbar.TopAppBarActivity
import sample.widget.navheader.NavHeaderView
import sample.widget.startActivity

/**
 * Main Activity class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-10
 */

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: MainViewModel by instanceViewModel()
    override fun layoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        setupNavigation()

        test()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionDayNight -> {
                controller.swapTheme(this, true)
                //update sp
                true
            }
            R.id.actionLanguage -> {
                controller.updateLanguage(this, if (controller.language == "en") "fa" else "en")
                //update sp
                true
            }
            R.id.actionGithub -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/oneHamidreza/Meow-Framework-MVVM")
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.navHost).apply {
            addOnDestinationChangedListener { _, destination, _ ->

                when (destination.id) {
                    R.id.fragmentHome -> {
                        title = ""
                        binding.toolbar.title = ""
                        binding.toolbar.setElevationCompat(0f)
                        binding.appBar.isLiftOnScroll = true
                        binding.ivLogo.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.toolbar.setElevationCompat(getDimensionToPx(R.dimen.toolbar_elevation).toFloat())
                        binding.toolbar.visibility = View.VISIBLE
                        binding.appBar.isLiftOnScroll = false
                        binding.ivLogo.visibility = View.GONE
                    }
                }
            }
        }
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.apply {
            drawerLayout.setStatusBarBackgroundColor(getColorCompat(R.color.status_bar))
            navigationView.addHeaderView(
                NavHeaderView(
                    context()
                )
            )
            sdkNeed(21) {
                navigationView.setItemBackgroundResource(R.drawable.navigation_view_item_background)
            }

            navigationView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.actionToApiCatBreedIndex -> navController.navigate(HomeFragmentDirections.actionToApiCatBreedIndex())
                    R.id.actionToApiCatBreedDetail -> navController.navigate(HomeFragmentDirections.actionToApiCatBreedDetail())
                    R.id.actionToApiCatBreedForm -> navController.navigate(HomeFragmentDirections.actionToApiCatBreedForm())
                    R.id.actionToApiLogin -> navController.navigate(HomeFragmentDirections.actionToApiLogin())

                    R.id.actionToMaterialAlerts -> navController.navigate(HomeFragmentDirections.actionToMaterialAlerts())
                    R.id.actionToMaterialBottomAppBar -> startActivity<BottomAppBarActivity>()
                    R.id.actionToMaterialBottomNavigation -> startActivity<BottomNavigationActivity>()
                    R.id.actionToMaterialButtons -> navController.navigate(HomeFragmentDirections.actionToMaterialButtons())
                    R.id.actionToMaterialCards -> navController.navigate(HomeFragmentDirections.actionToMaterialCards())
                    R.id.actionToMaterialCheckboxes -> navController.navigate(HomeFragmentDirections.actionToMaterialCheckboxes())
                    R.id.actionToMaterialCollapsingToolbar -> startActivity<CollapsingToolbarActivity>()
                    R.id.actionToMaterialFabSimple -> navController.navigate(HomeFragmentDirections.actionToMaterialFabSimple())
                    R.id.actionToMaterialFabExtended -> navController.navigate(
                        HomeFragmentDirections.actionToMaterialFabExtended()
                    )
                    R.id.actionToMaterialImageviews -> navController.navigate(HomeFragmentDirections.actionToMaterialImageviews())
                    R.id.actionToMaterialRadioButtons -> navController.navigate(
                        HomeFragmentDirections.actionToMaterialRadioButtons()
                    )
                    R.id.actionToMaterialSnackBars -> navController.navigate(HomeFragmentDirections.actionToMaterialSnackBars())
                    R.id.actionToMaterialTabLayout -> navController.navigate(HomeFragmentDirections.actionToMaterialTabLayout())
                    R.id.actionToMaterialTextviews -> navController.navigate(HomeFragmentDirections.actionToMaterialTextviews())
                    R.id.actionToMaterialTopAppBar -> startActivity<TopAppBarActivity>()

                    R.id.actionToExtensionsAndroid -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Android.md",
                                getString(R.string.contents_extensions_android)
                            )
                        )
                    }

                    R.id.actionToExtensionsCurrency -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Currency.md",
                                getString(R.string.contents_extensions_currency)
                            )
                        )
                    }
                    R.id.actionToExtensionsDate -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Date.md",
                                getString(R.string.contents_extensions_date)
                            )
                        )
                    }

                    R.id.actionToExtensionsException -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Exception.md",
                                getString(R.string.contents_extensions_exception)
                            )
                        )
                    }
                    R.id.actionToExtensionsFile -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_File.md",
                                getString(R.string.contents_extensions_file)
                            )
                        )
                    }
                    R.id.actionToExtensionsJson -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Json.md",
                                getString(R.string.contents_extensions_json)
                            )
                        )
                    }
                    R.id.actionToExtensionsKodein -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Kodein.md",
                                getString(R.string.contents_extensions_kodein)
                            )
                        )
                    }
                    R.id.actionToExtensionsKotlin -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Kotlin.md",
                                getString(R.string.contents_extensions_kotlin)
                            )
                        )
                    }
                    R.id.actionToExtensionsLog -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Log.md",
                                getString(R.string.contents_extensions_log)
                            )
                        )
                    }
                    R.id.actionToExtensionsNetwork -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Network.md",
                                getString(R.string.contents_extensions_network)
                            )
                        )
                    }
                    R.id.actionToExtensionsPermission -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Permission.md",
                                getString(R.string.contents_extensions_permission)
                            )
                        )
                    }
                    R.id.actionToExtensionsSharedPreferences -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToSharedPreferences()
                        )
                    }
                    R.id.actionToExtensionsSnackbar -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Snackbar.md",
                                getString(R.string.contents_extensions_snackbar)
                            )
                        )
                    }
                    R.id.actionToExtensionsStatusbar -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Statusbar.md",
                                getString(R.string.contents_extensions_statusbar)
                            )
                        )
                    }
                    R.id.actionToExtensionsString -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_String.md",
                                getString(R.string.contents_extensions_string)
                            )
                        )
                    }
                    R.id.actionToExtensionsSystem -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_System.md",
                                getString(R.string.contents_extensions_system)
                            )
                        )
                    }
                    R.id.actionToExtensionsToast -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Toast.md",
                                getString(R.string.contents_extensions_toast)
                            )
                        )
                    }
                    R.id.actionToExtensionsValidate -> {
                        navController.navigate(
                            HomeFragmentDirections.actionToMarkdown(
                                "Docs/ReadME_Extensions_Validate.md",
                                getString(R.string.contents_extensions_validate)
                            )
                        )
                    }
                }

//                item.isChecked = true
//                for (var it in item.)
                drawerLayout.closeDrawers()
                true
            }
//            navigationView.setupWithNavController(navController)
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun onSupportNavigateUp(): Boolean {
        return (navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp())
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun test() {

    }

}