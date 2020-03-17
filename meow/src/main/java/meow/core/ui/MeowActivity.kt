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

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import meow.core.arch.MeowViewModel
import meow.util.KeyboardUtils
import meow.util.viewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

/**
 * The Base of Activity.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-28
 */

abstract class MeowActivity<B : ViewDataBinding, VM : MeowViewModel> : AppCompatActivity(),
    MVVM<B, VM>,
    KodeinAware {

    open var isEnabledKeyboardUtils = true

    var isShowingKeyboard = false

    override val kodein by closestKodein()

    override fun activity() = this
    override fun context() = this
    override fun contentView() = findViewById<View>(android.R.id.content)

    override lateinit var binding: B

    val viewModel: VM by viewModel(viewModelClass())

    private lateinit var keyboardUtils: KeyboardUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(layoutId())
        initViewModel()
        observeViewModel()

        if (isEnabledKeyboardUtils) {
            keyboardUtils = KeyboardUtils(this) {
                isShowingKeyboard = it
                if (it) onKeyboardStateChanged(
                    isKeyboardUp = true,
                    isFromOnCreate = false
                ) else onKeyboardStateChanged(
                    isKeyboardUp = false,
                    isFromOnCreate = false
                )
            }
            keyboardUtils.enable()
            onKeyboardStateChanged(isKeyboardUp = false, isFromOnCreate = true)
        }
    }

    open fun onKeyboardStateChanged(isKeyboardUp: Boolean, isFromOnCreate: Boolean = false) {}

    private fun bindContentView(layoutId: Int) {
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        if (isEnabledKeyboardUtils)
            keyboardUtils.disable()
        super.onDestroy()
    }
}