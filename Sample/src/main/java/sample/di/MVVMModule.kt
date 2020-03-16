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

package sample.di

import meow.util.bindAutoTag
import org.kodein.di.Kodein.Module
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import sample.data.User
import sample.ui.main.MainViewModel
import sample.ui.menu.MenuViewModel
import sample.ui.user.detail.UserDetailViewModel
import sample.ui.user.index.UserIndexViewModel

/**
 * The Module of MVVM (ViewModels, Repositories).
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

val mvvmModule = Module("MVVM Module", false) {
    bindAutoTag<MainViewModel>() with singleton {
        MainViewModel(
            kodein.direct.instance()
        )
    }
    bindAutoTag<MenuViewModel>() with singleton {
        MenuViewModel(
            kodein.direct.instance()
        )
    }
    bindAutoTag<UserDetailViewModel>() with singleton {
        UserDetailViewModel(
            kodein.direct.instance(),
            instance()
        )
    }
    bindAutoTag<UserIndexViewModel>() with singleton {
        UserIndexViewModel(
            kodein.direct.instance(),
            instance()
        )
    }
    bind() from singleton { User.Repository(instance()) }
}