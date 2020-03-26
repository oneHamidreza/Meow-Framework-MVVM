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

package sample.ui.user.detail

import android.Manifest
import android.view.View
import androidx.lifecycle.MutableLiveData
import meow.core.api.MeowEvent
import meow.core.arch.MeowViewModel
import meow.core.arch.SingleLiveData
import sample.App
import sample.data.User

/**
 * User/Get View Model class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-29
 */

class UserDetailViewModel(app: App, val repository: User.Repository) : MeowViewModel(app) {

    var apiLiveData = SingleLiveData<MeowEvent<*>>()
    var modelLiveData = MutableLiveData<User>()
    var snackMessageLiveData = SingleLiveData<String>()
    var permissionLiveData = SingleLiveData<ArrayList<String>>()

    fun callApi(request: User.RequestGet) {
        safeCallApi(
            liveData = apiLiveData,
            request = request,
            isNetworkRequired = false,
            apiAction = { repository.getUserByIdApi(request) }
        ) { _, it ->
            modelLiveData.postValue(it)
        }
    }

    fun onClickedApiCall(@Suppress("UNUSED_PARAMETER") view: View) =
        callApi(User.RequestGet("2"))

    fun onClickedSnack(@Suppress("UNUSED_PARAMETER") view: View) =
        snackMessageLiveData.postValue("salam test")

    fun onClickedPermission(@Suppress("UNUSED_PARAMETER") view: View) =
        permissionLiveData.postValue(
            arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

    fun onClickedCancelJobs(@Suppress("UNUSED_PARAMETER") view: View) = cancelAllJobs()

//    fun fetchUserOffline() {
//        model = repository.getSavedUser()
//        eventLiveData.postValue(ofSuccessEvent(model))
//    }

}