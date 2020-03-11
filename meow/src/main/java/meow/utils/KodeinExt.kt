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

package meow.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import meow.core.arch.MeowViewModel
import meow.core.arch.MeowViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.erased.bind

/**
 * Extensions of [Kodein] class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */


/*
    Android ViewModel dependency injection with Kodein by Kirill Rozhenkov
    https://proandroiddev.com/android-viewmodel-dependency-injection-with-kodein-249f80f083c9
*/

fun <VM : MeowViewModel, T> T.viewModel(clazz: Class<VM>): Lazy<VM> where T : KodeinAware, T : AppCompatActivity {
    return lazy { MeowViewModelFactory(kodein.direct).create(clazz) }
}

fun <VM : MeowViewModel, T> T.viewModel(clazz: Class<VM>): Lazy<VM> where T : KodeinAware, T : Fragment {
    return lazy { MeowViewModelFactory(kodein.direct).create(clazz) }
}

inline fun <reified T : ViewModel> Kodein.Builder.bindAutoTag(overrides: Boolean? = null): Kodein.Builder.TypeBinder<T> {
    return bind<T>(T::class.java.simpleName, overrides)
}