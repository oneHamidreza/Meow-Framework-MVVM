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

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import meow.MeowApp
import meow.ktx.KeyboardUtils
import meow.ktx.PermissionUtils

/**
 * Fragment Activity interface.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-11
 */

interface FragmentActivityInterface<B : ViewDataBinding> : LifecycleOwner {

    var binding: B

    var permissionUtils: PermissionUtils?

    var isEnabledKeyboardUtils: Boolean
    var isShowingKeyboard: Boolean
    var keyboardUtils: KeyboardUtils?

    var isFromNavigateUp: Boolean

    @LayoutRes
    fun layoutId(): Int

    fun contentView(): View

    fun context(): Context

    fun app() = activity().application as MeowApp

    fun activity(): MeowActivity<*>

    fun resources(): Resources = context().resources

    fun initViewModel()

    fun needPermissions(vararg permissions: String, onResult: (isSuccess: Boolean) -> Unit) {
        permissionUtils = PermissionUtils(this)
        permissionUtils?.check(*permissions) {
            onResult(it)
        }
    }

    fun onRequestPermission(requestCode: Int, grantResults: IntArray) {
        permissionUtils?.onRequest(requestCode, grantResults)
    }

    fun setupKeyboardUtils() {
        if (isEnabledKeyboardUtils) {
            keyboardUtils = KeyboardUtils(activity()) {
                isShowingKeyboard = it
                if (it) onKeyboardStateChanged(
                    isKeyboardUp = true,
                    isFromOnCreate = false
                ) else onKeyboardStateChanged(
                    isKeyboardUp = false,
                    isFromOnCreate = false
                )
            }
            keyboardUtils?.enable()
            onKeyboardStateChanged(isKeyboardUp = false, isFromOnCreate = true)
        }
    }

    fun onKeyboardStateChanged(isKeyboardUp: Boolean, isFromOnCreate: Boolean = false) {

    }

    fun onKeyboardDestroy() {
        if (isEnabledKeyboardUtils)
            keyboardUtils?.disable()
    }

    fun getPersistentView(inflater: LayoutInflater, container: ViewGroup?, layout: Int): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        return binding.root
    }
}

fun FragmentActivityInterface<*>.isActivity() = this is AppCompatActivity
fun FragmentActivityInterface<*>.isFragment() = this is Fragment
fun FragmentActivityInterface<*>.isDialogFragment() = this is DialogFragment
fun FragmentActivityInterface<*>.isBottomSheet() = this is BottomSheetDialogFragment
fun FragmentActivityInterface<*>.findNavControllerIfExists(): NavController? =
    if (isFragment())
        NavHostFragment.findNavController(this as Fragment)
    else
        null