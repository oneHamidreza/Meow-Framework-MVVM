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

package meow.ktx

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import meow.core.ui.FragmentActivityInterface
import meow.core.ui.isActivity
import meow.core.ui.isFragment

/**
 * Permission Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-17
 */

private const val REQUEST_CODE = 1122

fun Context?.shouldShowPermissionDialog(permissions: List<String>): Boolean {
    if (this == null)
        return false

    permissions.forEach {
        if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
            return true
        }
    }

    return false
}

fun Fragment?.shouldShowPermissionDialog(permissions: List<String>) =
    this?.requireContext().shouldShowPermissionDialog(permissions)

class PermissionUtils(
    private var mvvm: FragmentActivityInterface<*>
) {
    private var onResult: (isSuccess: Boolean) -> Unit = {}

    fun check(vararg requestedPermissions: String, onResult: (isSuccess: Boolean) -> Unit) {
        this.onResult = onResult

        val notGrantedPermissions = arrayListOf<String>()
        requestedPermissions.forEach {
            notGrantedPermissions.checkAndAddPermission(it)
        }
        if (notGrantedPermissions.isEmpty()) {
            onResult(true)
            return
        }

        if (mvvm.isActivity())
            ActivityCompat.requestPermissions(
                mvvm.activity(), notGrantedPermissions.toTypedArray(),
                REQUEST_CODE
            )
        if (mvvm.isFragment())
            (mvvm as Fragment).requestPermissions(
                notGrantedPermissions.toTypedArray(),
                REQUEST_CODE
            )
    }

    private fun ArrayList<String>.checkAndAddPermission(permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                mvvm.context(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            add(permission)
            // Check for Rationale Option
            return ActivityCompat.shouldShowRequestPermissionRationale(mvvm.activity(), permission)
        }
        return true
    }

    fun onRequest(requestCode: Int, grantResults: IntArray) {
        if (requestCode != REQUEST_CODE)
            return
        if (grantResults.isEmpty())
            return

        var allGranted = true
        for (i in grantResults.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                allGranted = false
                break
            }
        }
        onResult(allGranted)
    }
}