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

package sample.ui.home.child.contents

import meow.core.arch.MeowViewModel
import meow.core.arch.SingleLiveData
import sample.App
import sample.data.Content

/**
 * Exclusive View Model.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-06
 */

class ContentsViewModel(app: App) : MeowViewModel(app) {

    val listLiveData = SingleLiveData<List<Content>>()

    fun fillList(
        titles: List<String>,
        descs: List<String>?,
        actions: List<Content.Action>?
    ) {
        val list = arrayListOf<Content>()

        titles.forEachIndexed { i, it ->
            list.add(
                Content(
                    title = it,
                    desc = descs?.get(i),
                    action = actions?.get(i)
                )
            )
        }

        listLiveData.postValue(list)
    }

}
