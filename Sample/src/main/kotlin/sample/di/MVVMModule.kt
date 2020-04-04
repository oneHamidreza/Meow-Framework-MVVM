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
import sample.data.user.User
import sample.ui.content.ContentViewModel
import sample.ui.dialog.CustomDialogViewModel
import sample.ui.main.MainViewModel
import sample.ui.material.alert.AlertsViewModel
import sample.ui.material.button.ButtonViewModel
import sample.ui.material.card.CardViewModel
import sample.ui.material.collapsing.toolbar.CollapsingToolbarViewModel
import sample.ui.material.fab.extended.FABExtendedViewModel
import sample.ui.material.fab.simple.FABSimpleViewModel
import sample.ui.material.form.FormViewModel
import sample.ui.material.imageview.ImageviewViewModel
import sample.ui.material.radiobutton.RadioButtonViewModel
import sample.ui.material.tablayout.TabLayoutViewModel
import sample.ui.material.tablayout.child.TabLayoutChildViewModel
import sample.ui.material.textview.TextViewViewModel
import sample.ui.menu.MenuViewModel
import sample.ui.sharedpreferences.SharedPreferencesViewModel
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

    bindAutoTag<AlertsViewModel>() with singleton {
        AlertsViewModel(
            kodein.direct.instance()
        )
    }

    bindAutoTag<CardViewModel>() with singleton {
        CardViewModel(
            kodein.direct.instance()
        )
    }

    bindAutoTag<RadioButtonViewModel>() with singleton {
        RadioButtonViewModel(
            kodein.direct.instance()
        )
    }

    bindAutoTag<CollapsingToolbarViewModel>() with singleton {
        CollapsingToolbarViewModel(
            kodein.direct.instance()
        )
    }

    bindAutoTag<FABExtendedViewModel>() with singleton {
        FABExtendedViewModel(
            kodein.direct.instance()
        )
    }

    bindAutoTag<FABSimpleViewModel>() with singleton {
        FABSimpleViewModel(
            kodein.direct.instance()
        )
    }

    bindAutoTag<TabLayoutViewModel>() with singleton {
        TabLayoutViewModel(
            kodein.direct.instance()
        )
    }

    bindAutoTag<TextViewViewModel>() with singleton {
        TextViewViewModel(
            kodein.direct.instance()
        )
    }

    bindAutoTag<TabLayoutChildViewModel>() with singleton {
        TabLayoutChildViewModel(
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
    bindAutoTag<ContentViewModel>() with singleton {
        ContentViewModel(
            kodein.direct.instance()
        )
    }
    bindAutoTag<CustomDialogViewModel>() with singleton {
        CustomDialogViewModel(
            kodein.direct.instance()
        )
    }
    bindAutoTag<FormViewModel>() with singleton {
        FormViewModel(
            kodein.direct.instance()
        )
    }
    bindAutoTag<ButtonViewModel>() with singleton {
        ButtonViewModel(
            kodein.direct.instance()
        )
    }
    bindAutoTag<ImageviewViewModel>() with singleton {
        ImageviewViewModel(
            kodein.direct.instance()
        )
    }
    bindAutoTag<SharedPreferencesViewModel>() with singleton {
        SharedPreferencesViewModel(
            kodein.direct.instance()
        )
    }
    bind() from singleton { User.Repository(instance()) }
}