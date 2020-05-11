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

package sample.ui.api.login

import meow.core.api.MeowEvent
import meow.core.api.MeowOauthToken
import meow.core.arch.MeowViewModel
import meow.core.arch.SingleLiveData
import sample.App
import sample.data.user.User

/**
 * Login View Model class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-14
 */

class LoginViewModel(
    override val app: App,
    private val repository: User.Repository
) : MeowViewModel(app) {

    var eventLiveData = SingleLiveData<MeowEvent<*>>()
    var modelLiveData = SingleLiveData<MeowOauthToken>()

    fun callApi(request: User.Api.RequestLogin) {
        safeCallApi(
            liveData = eventLiveData,
            isNetworkRequired = true,
            apiAction = { repository.postLoginToApi(request) }
        ) { _, it ->
            modelLiveData.postValue(it)
        }
    }
}