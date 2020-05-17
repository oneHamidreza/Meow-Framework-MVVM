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

package meow.core.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import meow.controller
import meow.ktx.KeyboardUtils
import meow.ktx.PermissionUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

/**
 * Meow Activity class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-28
 */

abstract class MeowActivity<B : ViewDataBinding> : LocalizationActivity(),
    FragmentActivityInterface<B>,
    KodeinAware {

    var isEnabledContextWrapper = true
    var isEnabledAutoUpdateStatusBarTheme = false

    override var isEnabledKeyboardUtils = true
    override var isShowingKeyboard = false
    override var keyboardUtils: KeyboardUtils? = null

    override var rootView: View? = null
    override var isFromNavigateUp = false

    override val kodein by closestKodein()

    override var permissionUtils: PermissionUtils? = null
    override fun activity() = this
    override fun context() = this
    override fun contentView(): View = findViewById(android.R.id.content)

    override lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller.updateLanguage(this, controller.language)
        controller.updateTheme(this, updateConfig = false)
        bindContentView(layoutId())
        initViewModel()
        setupKeyboardUtils()
    }

    private fun bindContentView(layoutId: Int) {
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.let {
            val uiMode = it.uiMode
            it.setTo(baseContext.resources.configuration)
            it.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermission(requestCode, grantResults)
    }

    override fun onDestroy() {
        onKeyboardDestroy()
        super.onDestroy()
    }

}