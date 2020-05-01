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

package sample.ui.sharedpreferences

import android.view.View
import androidx.lifecycle.MutableLiveData
import meow.core.api.MeowEvent
import meow.core.arch.MeowViewModel
import meow.core.arch.SingleLiveData
import meow.ktx.fromJson
import meow.ktx.ofPair
import meow.ktx.toJson
import sample.App
import sample.data.GithubRepository
import sample.data.user.User

/** todo all docs like this
 * Shared Preferences View Model.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-03
 */

class SharedPreferencesViewModel(
    override var app: App,
    val repository: GithubRepository
) : MeowViewModel(app) {

    var eventLiveData = MutableLiveData<MeowEvent<*>>()
    var modelLiveData = MutableLiveData<String>()

    val userStateLiveData = SingleLiveData<Pair<State, User>>()
    val testStateLiveData = SingleLiveData<String>()

    fun callApi(path: String) {
        safeCallApi(
            liveData = eventLiveData,
            isNetworkRequired = true,
            apiAction = { repository.getMarkdownFromApi(path) }
        ) { _, it ->
            modelLiveData.postValue(it)
        }
    }

    fun onClickedPut(@Suppress("UNUSED_PARAMETER") view: View) {
        val user = User(
            id = "245",
            firstName = "Hamidreza",
            lastName = "Etebarian"
        )
        app.dataSource.saveUser(user)
        userStateLiveData.postValue(ofPair(State.PUT, user))
    }


    fun onClickedGet(@Suppress("UNUSED_PARAMETER") view: View) {
        userStateLiveData.postValue(ofPair(State.GET, app.dataSource.fetchUser()))
    }

    fun onClickedTest(@Suppress("UNUSED_PARAMETER") view: View) {
        val json = User(
            id = "245",
            firstName = "Hamidreza",
            lastName = "Etebarian"
        ).toJson()
        val reversed = json.fromJson<User>().toJson()
        testStateLiveData.postValue("$json = $reversed")
    }

}

enum class State {
    GET, PUT
}