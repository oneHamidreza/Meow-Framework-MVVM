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

package meow.core.api

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.etebarian.meowframework.R
import com.squareup.moshi.Json
import kotlinx.serialization.Serializable
import meow.controller
import meow.core.api.exceptions.NetworkConnectionException
import meow.core.api.exceptions.UnexpectedException
import meow.util.avoidException
import meow.util.isNotNullOrEmpty
import meow.util.toClass
import retrofit2.HttpException

/**
 * The model of Response in restful api by success and fail statuses.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-24
 */

/**
 * The parent of restful api responses is sealed.
 *
 * @property T containing the type of embedded class in Response
 */
sealed class MeowResponse<T>(
    open val code: Int = 0,
    open var data: T? = null
) {

    /**
     * Cancellation response model in restful api with http code UNKNOWN when request is canceled.
     *
     */
    class Cancellation : MeowResponse<Nothing>()

    /**
     * Success response model in restful api with http code 200.
     * some of web services give the client 200..399 range to success response.
     * so you can use [MeowController().apiSuccessRange].
     *
     * @property T is the type of embedded class in Response
     * @property data is the received data from a restful api at success status
     */
    class Success<T>(override var data: T?) : MeowResponse<T>(HttpCodes.OK.code, data)

    /**
     * Http Error response model in restful api.
     *
     */
    class HttpError(
        override var code: Int = 0,
        override var data: SimpleResponse? = null,
        override var exception: Throwable? = null
    ) : Error<SimpleResponse>()

    /**
     * Unexpected Error response model in restful api with http code UNKNOWN.
     *
     */
    class UnExpectedError : Error<Nothing>(exception = UnexpectedException())

    /**
     * Unexpected Error response model in restful api with http code UNKNOWN.
     *
     */
    class ParseError(override var exception: Throwable?) : Error<Nothing>()

    /**
     * Network Error response model in restful api with http code UNKNOWN.
     *
     */
    class NetworkError : Error<Nothing>(exception = NetworkConnectionException())

    /**
     * Connection Error response model in restful api with http code UNKNOWN.
     *
     */
    class ConnectionError : Error<Nothing>(exception = NetworkConnectionException())

    /**
     * Network Error response model in restful api with http code UNKNOWN.
     *
     */
    class GeneralError(override var exception: Throwable?) : Error<Nothing>()

    /**
     * Request Not Valid Error response model in restful api with http code UNKNOWN.
     *
     */
    class RequestNotValid(override var data: MeowRequest? = null) : Error<MeowRequest>()

    /**
     * Error response in restful api.
     *
     * @property exception containing the exception at fail status
     */
    open class Error<T>(
        override var data: T? = null,
        open var exception: Throwable? = null,
        override val code: Int = HttpCodes.UNKNOWN.code
    ) : MeowResponse<T>(code, data)
}

@Serializable
data class SimpleResponse(
    @Json(name = "status") var status: Int = 0,
    @Json(name = "message") var message: String? = null,
    @Json(name = "body") var body: String? = null,
    @Json(name = "name") var name: String? = null
) {
    val messageIfExist: String?
        get() = when {
            message.isNotNullOrEmpty() -> message
            name.isNotNullOrEmpty() -> name
            body.isNotNullOrEmpty() -> body
            else -> null
        }
}

//todo use
@Serializable
data class FormErrorModel(
    @Json(name = "field") var field: String? = null,
    @Json(name = "message") var message: String? = null
)


fun <T> MeowResponse<T>.isSuccess() =
    ((this as? MeowResponse.Success)?.code) in controller.apiSuccessRange

fun MeowResponse<*>?.isBadRequest() = this?.code == HttpCodes.BAD_REQUEST.code
fun MeowResponse<*>?.isUnAuthorized() = this?.code == HttpCodes.UNAUTHORIZED.code
fun MeowResponse<*>?.isForbidden() = this?.code == HttpCodes.FORBIDDEN.code
fun MeowResponse<*>?.isNotFound() = this?.code == HttpCodes.NOT_FOUND.code
fun MeowResponse<*>?.isUnprocessableEntity() = this?.code == HttpCodes.UNPROCESSABLE_ENTITY.code
fun MeowResponse<*>?.isCancellation() = this is MeowResponse.Cancellation
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
            isCancellation() -> MeowEvent.Api.Cancellation()
            else -> MeowEvent.Api.Error(this)
        }
        liveData.postValue(eventWithRepository)
    }
}

fun <T> ofSuccessApiEvent(data: T) = MeowEvent.Api.Success(MeowResponse.Success(data))
fun <T> ofErrorApiEvent(data: T, exception: Throwable? = null, code: Int = HttpCodes.UNKNOWN.code) =
    MeowEvent.Api.Error(MeowResponse.Error(data, exception, code))

fun MeowResponse<*>?.createErrorMessage(resources: Resources): String {
    if (this == null)
        return resources.getString(R.string.error_response_unexpected).format(-100)
    if (!this.isError())
        return resources.getString(R.string.error_response_unexpected).format(-200)
    if ((this as MeowResponse.Error<*>).exception == null)
        return resources.getString(R.string.error_response_unexpected).format(-300)

    return when {
        isHttpError() -> {
            val suggest = when (code) {
                HttpCodes.UNAUTHORIZED.code -> resources.getString(R.string.error_response_http_suggest_unauthorized)
                HttpCodes.BAD_REQUEST.code -> resources.getString(R.string.error_response_http_suggest_bad_request)
                HttpCodes.UNPROCESSABLE_ENTITY.code -> resources.getString(R.string.error_response_http_suggest_unprocessable_entity)
                HttpCodes.TOO_MANY_REQUESTS.code -> resources.getString(R.string.error_response_http_suggest_too_request)
                else -> ""
            }
            val modelIfExists = this.data as? SimpleResponse?
            modelIfExists?.messageIfExist
                ?: resources.getString(R.string.error_response_http_with_suggest)
                    .format(suggest, code)
        }
        isParseError() -> resources.getString(R.string.error_response_parse)
        isNetworkError() -> resources.getString(R.string.error_network)
        isConnectionError() -> resources.getString(R.string.error_connection)
        isGeneralError() -> resources.getString(R.string.error_response_general)
        isUnexpectedError() -> resources.getString(R.string.error_response_unexpected)
            .format(-400)
        isRequestNotValidError() -> resources.getString(R.string.error_response_request_not_valid)
        else -> ""
    }
}