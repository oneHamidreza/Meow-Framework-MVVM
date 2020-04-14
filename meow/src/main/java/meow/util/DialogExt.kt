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

package meow.util

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.forEach
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import meow.controller
import meow.core.ui.MeowFragment
import meow.widget.MeowLoadingView

/**
 * Material Components Extesions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-22
 */

fun Context.alert(overrideThemeResId: Int = 0) =
    MaterialAlertDialogBuilder(this, overrideThemeResId)

fun MeowFragment<*>.alert(overrideThemeResId: Int = 0) =
    MaterialAlertDialogBuilder(context(), overrideThemeResId)

fun MeowFragment<*>.loadingAlert(
    titleResId: Int,
    overrideThemeResId: Int = 0,
    onCanceledBlock: () -> Unit = {}
) =
    context().loadingAlert(resources().getString(titleResId), overrideThemeResId, onCanceledBlock)

fun MeowFragment<*>.loadingAlert(
    title: String,
    overrideThemeResId: Int = 0,
    onCanceledBlock: () -> Unit = {}
) =
    context().loadingAlert(title, overrideThemeResId, onCanceledBlock)

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


fun MeowFragment<*>.popup(
    view: View,
    menuResId: Int,
    onClickedItem: (item: MenuItem) -> Unit
) = context().popup(view, menuResId, onClickedItem)

fun Context.popup(
    view: View,
    menuResId: Int,
    onClickedItem: (item: MenuItem) -> Unit
) = PopupMenu(this, view).apply {
    menuInflater.inflate(menuResId, menu)
    setOnMenuItemClickListener { m ->
        onClickedItem(m)
        true
    }
    menu.forEach {
        it.setTypefaceResId(this@popup, controller.defaultTypefaceResId)
    }
}