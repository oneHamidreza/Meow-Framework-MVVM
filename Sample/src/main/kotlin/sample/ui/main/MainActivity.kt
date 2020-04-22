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
import androidx.navigation.ui.setupWithNavController
import meow.controller
import meow.util.getColorCompat
import meow.util.getDimensionToPx
import meow.util.instanceViewModel
import meow.widget.setElevationCompat
import sample.R
import sample.databinding.ActivityMainBinding
import sample.ui.base.BaseActivity
import sample.ui.material.bottomappbar.BottomAppBarActivity
import sample.ui.material.bottomnavigation.BottomNavigationActivity
import sample.ui.material.collapsing.toolbar.CollapsingToolbarActivity
import sample.ui.material.topappbar.TopAppBarActivity
import sample.widget.navheader.NavHeaderView

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
            R.id.activityBottomAppBar -> {
                startActivity(Intent(this, BottomAppBarActivity::class.java))
                true
            }
            R.id.activityBottomNavigation -> {
                startActivity(Intent(this, BottomNavigationActivity::class.java))
                true
            }
            R.id.activityCollapsingToolbar -> {
                startActivity(Intent(this, CollapsingToolbarActivity::class.java))
                true
            }
            R.id.activityTopAppBar -> {
                startActivity(Intent(this, TopAppBarActivity::class.java))
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
            navigationView.setNavigationItemSelectedListener { menuItem ->
//                menuItem.isChecked = true
                drawerLayout.closeDrawers()
                true
            }
            navigationView.setupWithNavController(navController)
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

}