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

import meow.core.api.MeowEvent

/**
 * Extensions of [MeowEvent].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

fun MeowEvent<*>?.isApiLoading() = this is MeowEvent.Api.Loading
fun MeowEvent<*>?.isApiCancellation() = this is MeowEvent.Api.Cancellation
fun MeowEvent<*>?.isApiSuccess() = this is MeowEvent.Api.Success
fun MeowEvent<*>?.isApiError() = this is MeowEvent.Api.Error