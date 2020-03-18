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

package sample.ui.menu

import android.app.Application
import android.view.View
import meow.core.arch.MeowViewModel
import meow.core.arch.SingleLiveData
import sample.R

/**
 * Menu View Model class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-11
 */

class MenuViewModel(app: Application) : MeowViewModel(app) {

    val navigationLiveData = SingleLiveData<Int>()
    val languageLiveData = SingleLiveData<String>()

    fun onClickedUserDetailApi(@Suppress("UNUSED_PARAMETER") view: View) {
        navigationLiveData.postValue(R.id.actionToUserDetail)
    }

    fun onClickedUserIndexApi(@Suppress("UNUSED_PARAMETER") view: View) {
        navigationLiveData.postValue(R.id.actionToUserIndex)
    }

    fun onClickedToPersianLanguage(@Suppress("UNUSED_PARAMETER") view: View) {
        languageLiveData.postValue("fa")
    }

}