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
import meow.core.api.MeowEvent
import meow.core.arch.MeowViewModel
import meow.core.arch.SingleLiveData
import meow.ktx.hasNetwork
import meow.ktx.ofPair
import sample.App
import sample.data.GithubRepository
import sample.widget.createMarkwon

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

    var eventLiveData = SingleLiveData<MeowEvent<*>>()
    var modelLiveData = SingleLiveData<Spanned>()

    fun callApi(path: String) {
        safeCallApi(
            liveData = eventLiveData,
            isNetworkRequired = app.hasNetwork(),
            apiAction = {
                val saved = repository.fetchMarkdownData(path)
                val model = if (saved.isNotEmpty() && !app.hasNetwork())
                    ofPair(saved, app.createMarkwon().toMarkdown(saved))
                else
                    repository.getMarkdownFromApi(path)

                model
            }
        ) { _, it ->
            modelLiveData.postValue(it?.second)
            if (it != null)
                repository.saveMarkdownData(path, it.first)
        }
    }

}
