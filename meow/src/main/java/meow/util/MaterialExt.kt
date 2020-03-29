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
import android.view.View
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import meow.core.ui.MVVM
import meow.widget.MeowDraggableLinearLayout
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

fun MVVM<*, *>.alert(overrideThemeResId: Int = 0) =
    MaterialAlertDialogBuilder(context(), overrideThemeResId)

fun MVVM<*, *>.loadingAlert(
    titleResId: Int,
    overrideThemeResId: Int = 0,
    onCanceledBlock: () -> Unit = {}
) =
    loadingAlert(resources().getString(titleResId), overrideThemeResId, onCanceledBlock)

fun MVVM<*, *>.loadingAlert(
    title: String,
    overrideThemeResId: Int = 0,
    onCanceledBlock: () -> Unit = {}
) =
    MaterialAlertDialogBuilder(context(), overrideThemeResId)
        .setView(
            MeowLoadingView(context())
                .setTitle(title)
        )
        .setOnCancelListener { onCanceledBlock() }

fun MeowDraggableLinearLayout.setViewDragListener(vararg cards: MaterialCardView) {
    cards.forEach {
        setViewDragListener(object : MeowDraggableLinearLayout.ViewDragListener {
            override fun onViewCaptured(view: View, i: Int) {
                it.isDragged = true
            }

            override fun onViewReleased(view: View, v: Float, v1: Float) {
                it.isDragged = false
            }
        })

    }
}