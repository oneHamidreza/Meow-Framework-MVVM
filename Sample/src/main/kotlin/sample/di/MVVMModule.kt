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

import meow.ktx.bindAutoTag
import org.kodein.di.Kodein.Module
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton
import sample.data.GithubRepository
import sample.data.catbreed.CatBreed
import sample.data.user.User
import sample.ui.api.catbreed.detail.CatBreedDetailViewModel
import sample.ui.api.catbreed.form.CatBreedFormViewModel
import sample.ui.api.catbreed.index.CatBreedIndexViewModel
import sample.ui.api.login.LoginViewModel
import sample.ui.content.ContentViewModel
import sample.ui.home.HomeViewModel
import sample.ui.home.contents.ContentsViewModel
import sample.ui.main.MainViewModel
import sample.ui.markdown.MarkdownViewModel
import sample.ui.material.alerts.AlertsViewModel
import sample.ui.material.bottomappbar.BottomAppBarViewModel
import sample.ui.material.bottomnavigation.BottomNavigationViewModel
import sample.ui.material.buttons.ButtonsViewModel
import sample.ui.material.cards.CardsViewModel
import sample.ui.material.checkboxes.CheckboxesViewModel
import sample.ui.material.collapsing.toolbar.CollapsingToolbarViewModel
import sample.ui.material.fab.extended.FABExtendedViewModel
import sample.ui.material.fab.simple.FABSimpleViewModel
import sample.ui.material.imageviews.ImageviewsViewModel
import sample.ui.material.radiobuttons.RadioButtonsViewModel
import sample.ui.material.snackbars.SnackBarsViewModel
import sample.ui.material.switches.SwitchesViewModel
import sample.ui.material.tablayout.TabLayoutViewModel
import sample.ui.material.tablayout.child.TabLayoutChildViewModel
import sample.ui.material.textviews.TextViewsViewModel
import sample.ui.material.topappbar.TopAppBarViewModel
import sample.ui.menu.MenuViewModel
import sample.ui.meowwidget.dash.DashViewModel
import sample.ui.meowwidget.dividers.DividersViewModel
import sample.ui.meowwidget.form.FormViewModel
import sample.ui.meowwidget.progressbars.ProgressBarsViewModel
import sample.ui.meowwidget.ratingbars.RatingBarsViewModel
import sample.ui.sharedpreferences.SharedPreferencesViewModel

/**
 * The Module of MVVM (ViewModels, Repositories).
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

val mvvmModule = Module("MVVM Module", false) {

    bindAutoTag<ContentsViewModel>() with provider {
        ContentsViewModel(kodein.direct.instance())
    }

    bindAutoTag<HomeViewModel>() with provider {
        HomeViewModel(kodein.direct.instance())
    }

    bindAutoTag<MainViewModel>() with provider {
        MainViewModel(kodein.direct.instance())
    }

    // Markdown
    bindAutoTag<MarkdownViewModel>() with provider {
        MarkdownViewModel(
            kodein.direct.instance(),
            instance()
        )
    }

    // Api Samples
    bindAutoTag<CatBreedIndexViewModel>() with provider {
        CatBreedIndexViewModel(
            kodein.direct.instance(),
            instance()
        )
    }
    bindAutoTag<CatBreedDetailViewModel>() with provider {
        CatBreedDetailViewModel(
            kodein.direct.instance(),
            instance()
        )
    }
    bindAutoTag<CatBreedFormViewModel>() with provider {
        CatBreedFormViewModel(
            kodein.direct.instance(),
            instance()
        )
    }
    bindAutoTag<LoginViewModel>() with provider {
        LoginViewModel(
            kodein.direct.instance(),
            instance()
        )
    }

    // KTX
    bindAutoTag<SharedPreferencesViewModel>() with provider {
        SharedPreferencesViewModel(kodein.direct.instance(), instance())
    }

    bindAutoTag<AlertsViewModel>() with provider {
        AlertsViewModel(kodein.direct.instance())
    }

    bindAutoTag<BottomAppBarViewModel>() with provider {
        BottomAppBarViewModel(kodein.direct.instance())
    }

    bindAutoTag<BottomNavigationViewModel>() with provider {
        BottomNavigationViewModel(kodein.direct.instance())
    }

    bindAutoTag<CardsViewModel>() with provider {
        CardsViewModel(kodein.direct.instance())
    }

    bindAutoTag<CheckboxesViewModel>() with provider {
        CheckboxesViewModel(kodein.direct.instance())
    }

    bindAutoTag<CollapsingToolbarViewModel>() with provider {
        CollapsingToolbarViewModel(kodein.direct.instance())
    }

    bindAutoTag<DashViewModel>() with provider {
        DashViewModel(kodein.direct.instance())
    }

    bindAutoTag<FABExtendedViewModel>() with provider {
        FABExtendedViewModel(kodein.direct.instance())
    }

    bindAutoTag<FABSimpleViewModel>() with provider {
        FABSimpleViewModel(kodein.direct.instance())
    }

    bindAutoTag<MenuViewModel>() with provider {
        MenuViewModel(kodein.direct.instance())
    }

    bindAutoTag<ProgressBarsViewModel>() with provider {
        ProgressBarsViewModel(kodein.direct.instance())
    }

    bindAutoTag<RadioButtonsViewModel>() with provider {
        RadioButtonsViewModel(kodein.direct.instance())
    }

    bindAutoTag<SnackBarsViewModel>() with provider {
        SnackBarsViewModel(kodein.direct.instance())
    }

    bindAutoTag<SwitchesViewModel>() with provider {
        SwitchesViewModel(kodein.direct.instance())
    }

    bindAutoTag<TabLayoutViewModel>() with provider {
        TabLayoutViewModel(kodein.direct.instance())
    }

    bindAutoTag<TabLayoutChildViewModel>() with provider {
        TabLayoutChildViewModel(kodein.direct.instance())
    }

    bindAutoTag<TextViewsViewModel>() with provider {
        TextViewsViewModel(kodein.direct.instance())
    }

    bindAutoTag<TopAppBarViewModel>() with provider {
        TopAppBarViewModel(kodein.direct.instance())
    }

    bindAutoTag<ContentViewModel>() with provider {
        ContentViewModel(kodein.direct.instance())
    }

    bindAutoTag<FormViewModel>() with provider {
        FormViewModel(kodein.direct.instance())
    }

    bindAutoTag<ButtonsViewModel>() with provider {
        ButtonsViewModel(kodein.direct.instance())
    }

    bindAutoTag<ImageviewsViewModel>() with provider {
        ImageviewsViewModel(kodein.direct.instance())
    }

    bindAutoTag<RatingBarsViewModel>() with provider {
        RatingBarsViewModel(kodein.direct.instance())
    }

    // Meow Widget
    bindAutoTag<DividersViewModel>() with provider {
        DividersViewModel(kodein.direct.instance())
    }

    bind() from singleton { CatBreed.Repository(instance()) }

    bind() from singleton { User.Repository(instance()) }

    bind() from singleton { GithubRepository(instance()) }


}