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

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import meow.util.createClass
import meow.util.getColorCompat
import meow.util.setStatusBarDarkIcon
import sample.R
import sample.databinding.ActivityMainBinding
import sample.ui.base.BaseActivity

/**
 * [Main] Activity class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-10
 */

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun viewModelClass() = createClass<MainViewModel>()
    override fun layoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        setupNavigation()
        setStatusBarDarkIcon()

        AlertDialog.Builder(this).setTitle("سلام").setView(R.layout.dialog_custom).show()

//        fv.setOnSubmitClickListener {
//            Log.d("testText", "Text is : ${et.text.toString()}")
//            Toast.makeText(this, et.text.toString(), Toast.LENGTH_LONG).show()
//        }
//
//        et.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (et.text!!.length > et.counterMaxLength)
//                    et.error = "Text is longer than allowed limit"
//                else
//                    et.isErrorEnabled = false
//            }
//        })

    }

    private fun setupNavigation() {
        navController = findNavController(R.id.navHost).apply {
            addOnDestinationChangedListener { _, destination, _ ->
            }
        }
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.apply {
            drawerLayout.setStatusBarBackground(ColorDrawable(getColorCompat(R.color.status_bar)))
            navigationView.setupWithNavController(navController)
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun observeViewModel() {
    }

    override fun onSupportNavigateUp(): Boolean {
        return (navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp())
    }
}