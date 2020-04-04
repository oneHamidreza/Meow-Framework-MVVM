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