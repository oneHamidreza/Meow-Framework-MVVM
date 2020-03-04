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

import meow.core.api.HttpCodes
import meow.core.api.MeowResponse
import meow.core.controller

/**
 * The Extensions of [MeowResponse].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

fun <T> MeowResponse<T>.isSuccess() =
    ((this as? MeowResponse.Success)?.code) in controller.apiSuccessRange

fun <T> MeowResponse<T>.isBadRequest() = this.code == HttpCodes.BAD_REQUEST.code
fun <T> MeowResponse<T>.isUnAuthorized() = this.code == HttpCodes.UNAUTHORIZED.code
fun <T> MeowResponse<T>.isForbidden() = this.code == HttpCodes.FORBIDDEN.code
fun <T> MeowResponse<T>.isNotFound() = this.code == HttpCodes.NOT_FOUND.code
fun <T> MeowResponse<T>.isUnprocessableEntity() = this.code == HttpCodes.UNPROCESSABLE_ENTITY.code
fun <T> MeowResponse<T>.isError() = this is MeowResponse.Error