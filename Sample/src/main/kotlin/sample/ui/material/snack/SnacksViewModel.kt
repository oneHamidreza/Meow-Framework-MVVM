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

package sample.ui.material.snack

import meow.core.arch.MeowViewModel
import meow.core.arch.SingleLiveData
import meow.util.getStringArray
import sample.App
import sample.R
import sample.data.Content

/**
 * Material Snack View Model.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-04
 */

class SnacksViewModel(app: App) : MeowViewModel(app) {

    val listLiveData = SingleLiveData<List<Content>>()

    fun fillList() {
        val actions = arrayOf(
            Content.Action.SNACK_SIMPLE,
            Content.Action.SNACK_SIMPLE_WITH_ACTION,
            Content.Action.SNACK_CUSTOMIZED_COLOR,
            Content.Action.SNACK_INDEFINITE
        )
        val titles = app.getStringArray(R.array.snacks_contents_list_title)
        val descs = app.getStringArray(R.array.snacks_contents_list_desc)
        val list = arrayListOf<Content>()

        titles.forEachIndexed { i, it ->
            list.add(
                Content(
                    title = it,
                    action = actions[i],
                    desc = descs[i]
                )
            )
        }

        listLiveData.postValue(list)
    }

}