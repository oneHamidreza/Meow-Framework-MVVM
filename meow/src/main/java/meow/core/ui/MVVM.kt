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
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import meow.core.arch.MeowViewModel
import meow.util.PermissionUtils

/**
 * MVVM interface.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-11
 */

interface MVVM<B : ViewDataBinding, VM : MeowViewModel> {

    var binding: B

    var permissionUtils: PermissionUtils?

    @LayoutRes
    fun layoutId(): Int

    fun viewModelClass(): Class<VM>

    fun contentView(): View

    fun context(): Context

    fun activity(): FragmentActivity

    fun resources() = context().resources
    fun getStringCompat(id: Int) = resources().getString(id)//todo all

    fun initViewModel()

    fun observeViewModel()

    fun needPermission(permissions: ArrayList<String>, onResult: (isSuccess: Boolean) -> Unit) {
        permissionUtils = PermissionUtils(this)
        permissionUtils?.check(permissions, onResult)
    }

    fun onRequestPermission(requestCode: Int, grantResults: IntArray) {
        permissionUtils?.onRequest(requestCode, grantResults)
    }
}

fun MVVM<*, *>.isActivity() = this is AppCompatActivity
fun MVVM<*, *>.isFragment() = this is Fragment
fun MVVM<*, *>.isDialogFragment() = this is DialogFragment
fun MVVM<*, *>.isBottomSheet() = this is BottomSheetDialogFragment
