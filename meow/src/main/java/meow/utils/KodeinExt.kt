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

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance

/**
 * The Extensions of [Kodein] class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */


/*
    Android ViewModel dependency injection with Kodein by Kirill Rozhenkov
    https://proandroiddev.com/android-viewmodel-dependency-injection-with-kodein-249f80f083c9
*/

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : KodeinAware, T : FragmentActivity {
    return lazy { ViewModelProviders.of(this, direct.instance()).get(VM::class.java) }
}

inline fun <reified T : ViewModel> Kodein.Builder.bindAutoTag(overrides: Boolean? = null): Kodein.Builder.TypeBinder<T> {
    return bind<T>(T::class.java.simpleName, overrides)
}

//inline fun <reified VM : ViewModel> VM.overrideInjectionRuleForTesting() =
//    KodeinViewModelInjector.overrideInjectionRuleForTesting(
//        VM::class.java, this)
//
//@Suppress("unused")
//inline fun <reified VM : ViewModel> VM.clearInjectionRuleForTesting() =
//    KodeinViewModelInjector.clearInjectionRuleForTesting(VM::class.java)

//inline fun <reified VM : ViewModel> viewModelBinder(
//    baseContainer: Kodein = KodeinViewModelInjector.container,
//    crossinline binder: (Kodein.Builder.() -> Unit)
//) = lazy {
//    getMeowKodeinFactory<VM>(baseContainer, binder).create(createClass())
//}
//
//inline fun <reified VM : ViewModel> getMeowKodeinFactory(
//    baseContainer: Kodein,
//    crossinline binder: (Kodein.Builder.() -> Unit)
//) = object : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
////                val testViewModel = KodeinViewModelInjector
////                    .getTestViewModel(VM::class.java)
//        val testViewModel = null
//        @Suppress("UNCHECKED_CAST")
//        return when (testViewModel) {
//            null ->
//                Kodein {
//                    extend(baseContainer)
//                    binder.invoke(this)
//                }.run {
//                    val viewModel by instance<VM>()
//                    viewModel
//                }
//            else -> testViewModel
//        } as T
//    }
//}