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

package meow.ktx

import androidx.lifecycle.ViewModel
import org.kodein.di.Kodein
import org.kodein.di.erased.bind

/**
 * Extensions of [Kodein] class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

inline fun <reified T : ViewModel> Kodein.Builder.bindAutoTag(overrides: Boolean? = null): Kodein.Builder.TypeBinder<T> {
    return bind<T>(javaClass<T>().simpleName, overrides)
}