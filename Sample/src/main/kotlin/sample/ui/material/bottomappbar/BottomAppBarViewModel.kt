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

package sample.ui.material.bottomappbar

import androidx.lifecycle.MutableLiveData
import meow.core.arch.MeowViewModel
import sample.App
import sample.R
import sample.data.Content

/**
 * Bottom App Bar View Model.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-05
 */

class BottomAppBarViewModel(app: App) : MeowViewModel(app) {
    val listLiveData = MutableLiveData<List<Content>>()

    fun fillList() {
        val list = arrayListOf<Content>()
        (1..200).forEach {
            list.add(
                Content(
                    title = app.getString(R.string.content_item_title, it),
                    desc = app.getString(R.string.content_item_desc, it)
                )
            )
        }
        listLiveData.postValue(list)
    }
}
