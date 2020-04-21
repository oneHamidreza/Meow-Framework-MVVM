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

package sample.data

import androidx.recyclerview.widget.DiffUtil
import kotlinx.serialization.Serializable
import meow.util.isNotNullOrEmpty

/**
 * Content Data class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-22
 */

@Serializable
data class Content(
    var action: Action? = null,
    var title: String,
    var desc: String? = null
) {
    val canShowDesc get() = desc.isNotNullOrEmpty()

    enum class Action {

        API_INDEX,
        API_DETAIL,
        API_FORM,
        API_LOGIN,

        MATERIAL_ALERTS,
        MATERIAL_BOTTOM_APP_BAR,
        MATERIAL_BOTTOM_NAVIGATION,
        MATERIAL_BUTTONS,
        MATERIAL_CARDS,
        MATERIAL_CHECKBOXES,
        MATERIAL_COLLAPSING_TOOLBAR,
        MATERIAL_FAB_SIMPLE,
        MATERIAL_FAB_EXTENDED,
        MATERIAL_IMAGEVIEWS,
        MATERIAL_RADIO_BUTTONS,
        MATERIAL_SNACKBARS,
        MATERIAL_SWITCHES,
        MATERIAL_TABLAYOUT,
        MATERIAL_TEXTVIEWS,
        MATERIAL_TOP_APP_BAR,

        EXTENSIONS_ANDROID,
        EXTENSIONS_CURRENCY,
        EXTENSIONS_DATE,
        EXTENSIONS_EXCEPTION,
        EXTENSIONS_FILE,
        EXTENSIONS_JSON,
        EXTENSIONS_KODEIN,
        EXTENSIONS_KOTLIN,
        EXTENSIONS_NETWORK,
        EXTENSIONS_PERMISSION,
        EXTENSIONS_SNACKBAR,
        EXTENSIONS_STATUS_BAR,
        EXTENSIONS_SHARED_PREFERENCES,
        EXTENSIONS_STRING,
        EXTENSIONS_SYSTEM,
        EXTENSIONS_TOAST,
        EXTENSIONS_VALIDATE,

        WIDGET_ANDROID,
        WIDGET_DASH,
        WIDGET_FORM,
        WIDGET_PROGRESS_BARS,
        WIDGET_RATEBAR,
        WIDGET_TEXT_FIELD,

        ALERT_SIMPLE,
        ALERT_SIMPLE_WITH_LISTENER,
        ALERT_LOADING,

        SNACK_SIMPLE,
        SNACK_SIMPLE_WITH_ACTION,
        SNACK_CUSTOMIZED_COLOR,
        SNACK_INDEFINITE,
    }

    class DiffCallback : DiffUtil.ItemCallback<Content>() {

        override fun areItemsTheSame(oldItem: Content, newItem: Content) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Content, newItem: Content) = oldItem == newItem

    }
}