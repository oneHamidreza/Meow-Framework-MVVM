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

package sample.ui.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import meow.controller
import meow.core.ui.MeowActivity
import meow.util.getColorCompat
import meow.util.updateNavigationBarColor
import meow.util.updateStatusBarByTheme
import sample.R

/**
 * Base Activity class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

abstract class BaseActivity<B : ViewDataBinding> : MeowActivity<B>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateStatusBarByTheme(!controller.isNightMode)
        updateNavigationBarColor(getColorCompat(R.color.nav_bar))
    }

}