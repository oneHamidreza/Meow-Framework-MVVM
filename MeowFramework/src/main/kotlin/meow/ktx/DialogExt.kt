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
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import meow.controller
import meow.widget.MeowLoadingView

/**
 * Material Components Extesions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-22
 */

private fun Context.createAlertDialog(
    title: String? = null,
    message: String? = null,
    overrideThemeResId: Int = 0
) = MaterialAlertDialogBuilder(this, overrideThemeResId)
    .setTitle(title)
    .setMessage(message)

private fun Context.createAlertDialog(
    titleResId: Int = 0,
    messageResId: Int = 0,
    overrideThemeResId: Int = 0
) = MaterialAlertDialogBuilder(this, overrideThemeResId)
    .apply {
        if (titleResId != 0)
            setTitle(getString(titleResId))
        if (messageResId != 0)
            setMessage(getString(messageResId))
    }

fun Context.alert(
    title: String? = null,
    message: String? = null,
    overrideThemeResId: Int = 0
) =
    createAlertDialog(title, message, overrideThemeResId)

fun Fragment.alert(
    title: String? = null,
    message: String? = null,
    overrideThemeResId: Int = 0
) =
    requireContext().createAlertDialog(title, message, overrideThemeResId)

fun Context.alert(
    titleResId: Int = 0,
    messageResId: Int = 0,
    overrideThemeResId: Int = 0
) =
    createAlertDialog(titleResId, messageResId, overrideThemeResId)

fun Fragment.alert(
    titleResId: Int = 0,
    messageResId: Int = 0,
    overrideThemeResId: Int = 0
) =
    requireContext().createAlertDialog(titleResId, messageResId, overrideThemeResId)

fun Fragment.loadingAlert(
    titleResId: Int,
    overrideThemeResId: Int = 0,
    onCanceledBlock: () -> Unit = {}
) =
    requireContext().loadingAlert(
        resources.getString(titleResId),
        overrideThemeResId,
        onCanceledBlock
    )

fun Fragment.loadingAlert(
    title: String,
    overrideThemeResId: Int = 0,
    onCanceledBlock: () -> Unit = {}
) =
    requireContext().loadingAlert(title, overrideThemeResId, onCanceledBlock)

fun Context.loadingAlert(
    title: String,
    overrideThemeResId: Int = 0,
    onCanceledBlock: () -> Unit = {}
) =
    MaterialAlertDialogBuilder(this, overrideThemeResId)
        .setView(
            MeowLoadingView(this)
                .setTitle(title)
        )
        .setOnCancelListener { onCanceledBlock() }

fun Context.popup(
        view: View,
        menuResId: Int,
        onClickedItem: (item: MenuItem) -> Unit,
        gravity: Int = Gravity.NO_GRAVITY,
) = PopupMenu(this, view, gravity).apply {
    menuInflater.inflate(menuResId, menu)
    setOnMenuItemClickListener { m ->
        onClickedItem(m)
        true
    }
    menu.forEach {
        it.setTypefaceResId(this@popup, controller.defaultTypefaceResId)
    }
}

fun Fragment.popup(
    view: View,
    menuResId: Int,
    onClickedItem: (item: MenuItem) -> Unit
) = requireContext().popup(view, menuResId, onClickedItem)
