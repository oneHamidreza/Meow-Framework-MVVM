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

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.etebarian.meowframework.R
import meow.controller
import meow.core.api.HttpCodes
import meow.core.api.MeowEvent
import meow.core.api.MeowResponse
import meow.core.api.SimpleModel
import retrofit2.HttpException

/**
 * Extensions of [MeowResponse].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

fun <T> MeowResponse<T>.isSuccess() =
    ((this as? MeowResponse.Success)?.code) in controller.apiSuccessRange

fun MeowResponse<*>?.isBadRequest() = this?.code == HttpCodes.BAD_REQUEST.code
fun MeowResponse<*>?.isUnAuthorized() = this?.code == HttpCodes.UNAUTHORIZED.code
fun MeowResponse<*>?.isForbidden() = this?.code == HttpCodes.FORBIDDEN.code
fun MeowResponse<*>?.isNotFound() = this?.code == HttpCodes.NOT_FOUND.code
fun MeowResponse<*>?.isUnprocessableEntity() = this?.code == HttpCodes.UNPROCESSABLE_ENTITY.code
fun MeowResponse<*>?.isError() = this is MeowResponse.Error
fun MeowResponse<*>?.isHttpError() = this is MeowResponse.HttpError
fun MeowResponse<*>?.isParseError() = this is MeowResponse.ParseError
fun MeowResponse<*>?.isConnectionError() = this is MeowResponse.ConnectionError
fun MeowResponse<*>?.isNetworkError() = this is MeowResponse.NetworkError
fun MeowResponse<*>?.isGeneralError() = this is MeowResponse.GeneralError
fun MeowResponse<*>?.isUnexpectedError() = this is MeowResponse.UnExpectedError
fun MeowResponse<*>?.isRequestNotValidError() = this is MeowResponse.RequestNotValid

fun createResponseFromHttpError(throwable: HttpException): MeowResponse.Error<*> {
    return avoidException(
        tryBlock = {
            throwable.response()?.errorBody()?.source()?.let {
                MeowResponse.HttpError(
                    code = throwable.code(),
                    data = it.toClass(),
                    exception = throwable
                )
            } ?: MeowResponse.UnExpectedError()
        },
        exceptionBlock = {
            MeowResponse.HttpError(
                code = throwable.code(),
                exception = throwable
            )
        }) ?: MeowResponse.UnExpectedError()
}

fun MeowResponse<*>.processAndPush(liveData: MutableLiveData<MeowEvent<*>>) {
    avoidException {
        val eventWithRepository = when {
            isError() -> MeowEvent.Api.Error(this)
            isSuccess() -> MeowEvent.Api.Success(this)
            else -> MeowEvent.Api.Error(this)
        }
        liveData.postValue(eventWithRepository)
    }
}

fun <T> ofApiSuccessEvent(data: T) = MeowEvent.Api.Success(MeowResponse.Success(data))

fun MeowResponse<*>?.createErrorMessage(resources: Resources): String {
    if (this == null)
        return resources.getStringCompat(R.string.error_response_unexpected).format(-100)
    if (!this.isError())
        return resources.getStringCompat(R.string.error_response_unexpected).format(-400)
    if ((this as MeowResponse.Error<*>).exception == null)
        return resources.getStringCompat(R.string.error_response_unexpected).format(-500)

    return when {
        isHttpError() -> {
            val suggest = when (code) {
                HttpCodes.UNAUTHORIZED.code -> resources.getStringCompat(R.string.error_response_http_suggest_unauthorized)
                HttpCodes.BAD_REQUEST.code -> resources.getStringCompat(R.string.error_response_http_suggest_bad_request)
                HttpCodes.UNPROCESSABLE_ENTITY.code -> resources.getStringCompat(R.string.error_response_http_suggest_unprocessable_entity)
                HttpCodes.TOO_MANY_REQUESTS.code -> resources.getStringCompat(R.string.error_response_http_suggest_too_request)
                else -> ""
            }
            val modelIfExists = this.data as? SimpleModel?
            modelIfExists?.messageIfExist
                ?: resources.getStringCompat(R.string.error_response_http_with_suggest)
                    .format(suggest, code)
        }
        isParseError() -> resources.getStringCompat(R.string.error_response_parse)
        isNetworkError() -> resources.getStringCompat(R.string.error_network)
        isConnectionError() -> resources.getStringCompat(R.string.error_connection)
        isGeneralError() -> resources.getStringCompat(R.string.error_response_general)
        isUnexpectedError() -> resources.getStringCompat(R.string.error_response_unexpected)
            .format(-400)
        isRequestNotValidError() -> resources.getStringCompat(R.string.error_response_request_not_valid)
        else -> ""
    }
}