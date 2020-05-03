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

package sample.ui.markdown

import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import meow.core.api.MeowEvent
import meow.core.arch.MeowViewModel
import sample.App
import sample.data.GithubRepository

/**
 * Markdown View Model.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-14
 */

class MarkdownViewModel(
    app: App,
    val repository: GithubRepository
) : MeowViewModel(app) {

    var eventLiveData = MutableLiveData<MeowEvent<*>>()
    var modelLiveData = MutableLiveData<Spanned>()

    fun callApi(path: String) {
        safeCallApi(
            liveData = eventLiveData,
            isNetworkRequired = true,
            apiAction = { repository.getMarkdownFromApi(path) }
        ) { _, it ->
            modelLiveData.postValue(it)
        }
    }

}
