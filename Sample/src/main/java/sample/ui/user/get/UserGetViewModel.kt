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

package sample.ui.user.get

import androidx.lifecycle.MutableLiveData
import meow.core.api.MeowStatus
import meow.core.arch.MeowViewModel
import meow.utils.ofSuccessState
import sample.data.User

/**
 * User/Get View Model class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-29
 */

class UserGetViewModel(val repository: User.Repository) : MeowViewModel() {

    var modelLiveData = MutableLiveData<MeowStatus>()
    var model: User.Model? = null

    fun apiCall(request: User.RequestGet) {
        modelLiveData.safeApiCall(
            request = request,
            isNetworkRequired = false,
            apiAction = { repository.getUserByIdApi(request) }
        ) { _, it ->
            model = it
        }
    }

    fun fetchUserOffline() {
        model = repository.getSavedUser()
        modelLiveData.postValue(ofSuccessState(model))
    }

}