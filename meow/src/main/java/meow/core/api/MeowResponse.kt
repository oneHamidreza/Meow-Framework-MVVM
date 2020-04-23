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
import meow.util.*
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
     * Unprocessable Entity Error response model in restful api with http code 422.
     *
     */
    class UnprocessableEntityError(
        override var code: Int = 0,
        override var data: List<FormErrorModel>? = null,
        override var exception: Throwable?
    ) : Error<List<FormErrorModel>>()

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

data class UIErrorModel(
    var icon: Int = R.drawable.ic_error_outline,
    var title: String,
    var message: String? = null,
    var actionText: String? = null
) {
    val titlePlusMessage get() = "${if (title.isNotEmpty()) "$title " else ""}$message"
}

@Serializable
data class FormErrorModel(
    @Json(name = "field") var field: String? = null,
    @Json(name = "message") var message: String? = null
)

fun <T> MeowResponse<T>.isSuccess() =
    ((this as? MeowResponse.Success)?.code) in controller.apiSuccessRange

fun MeowResponse<*>?.isCancellation() = this is MeowResponse.Cancellation
fun MeowResponse<*>?.isError() = this is MeowResponse.Error
fun MeowResponse<*>?.isHttpError() = this is MeowResponse.HttpError
fun MeowResponse<*>?.isParseError() = this is MeowResponse.ParseError
fun MeowResponse<*>?.isConnectionError() = this is MeowResponse.ConnectionError
fun MeowResponse<*>?.isNetworkError() = this is MeowResponse.NetworkError
fun MeowResponse<*>?.isUnexpectedError() = this is MeowResponse.UnExpectedError
fun MeowResponse<*>?.isRequestNotValidError() = this is MeowResponse.RequestNotValid
fun MeowResponse<*>?.isUnprocessableEntityError() = this is MeowResponse.UnprocessableEntityError

fun createResponseFromHttpError(throwable: HttpException): MeowResponse.Error<*> {
    return avoidException(
        tryBlock = {
            throwable.response()?.errorBody()?.source()?.let {
                if (throwable.code() == HttpCodes.UNPROCESSABLE_ENTITY.code) MeowResponse.UnprocessableEntityError(
                    code = throwable.code(),
                    data = it.fromJsonList<FormErrorModel>().apply { logD(m = this?.size) },
                    exception = throwable
                ) else
                    MeowResponse.HttpError(
                        code = throwable.code(),
                        data = it.fromJson(),
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
        logD(m = eventWithRepository.javaClass.name)
        liveData.postValue(eventWithRepository)
    }
}

fun <T> ofSuccessApiEvent(data: T) = MeowEvent.Api.Success(MeowResponse.Success(data))
fun <T> ofErrorApiEvent(data: T, exception: Throwable? = null, code: Int = HttpCodes.UNKNOWN.code) =
    MeowEvent.Api.Error(MeowResponse.Error(data, exception, code))

fun MeowResponse<*>?.createErrorModel(
    resources: Resources,
    forceFromApi: Boolean = false
): UIErrorModel {

    fun unexpected(code: Int) =
        UIErrorModel(
            icon = R.drawable.ic_error,
            title = resources.getString(R.string.error_response_unexpected_title),
            message = resources.getString(R.string.error_response_unexpected_message).format(code)
        )

    if (this == null)
        return unexpected(-100)
    if (!this.isError())
        return unexpected(-200)
    if ((this as MeowResponse.Error<*>).exception == null)
        return unexpected(-300)

    return when {
        isHttpError() -> {
            val httpErrorUI = UIErrorModel(
                title = resources.getString(R.string.error_http_title).format(code),
                actionText = resources.getString(R.string.error_actionText_try_again)
            )
            when (code) {
                HttpCodes.UNAUTHORIZED.code -> httpErrorUI.apply {
                    icon = R.drawable.ic_error_unauthorized
                    title = resources.getString(R.string.error_response_http_unauthorized_title)
                    message = resources.getString(R.string.error_response_http_unauthorized_message)
                    actionText = null
                }
                HttpCodes.BAD_REQUEST.code -> httpErrorUI.apply {
                    title = resources.getString(R.string.error_response_http_bad_request_title)
                    message = resources.getString(R.string.error_response_http_bad_request_message)
                }
                HttpCodes.UNPROCESSABLE_ENTITY.code -> httpErrorUI.apply {
                    title =
                        resources.getString(R.string.error_response_http_unprocessable_entity_title)
                    message =
                        resources.getString(R.string.error_response_http_unprocessable_entity_message)
                }
                HttpCodes.TOO_MANY_REQUESTS.code -> httpErrorUI.apply {
                    title = resources.getString(R.string.error_response_http_too_many_request_title)
                    message =
                        resources.getString(R.string.error_response_http_too_many_request_message)
                }
                HttpCodes.INTERNAL_SERVER_ERROR.code -> httpErrorUI.apply {
                    icon = R.drawable.ic_error_internal_server
                    title = resources.getString(R.string.error_http_internal_server_title)
                    message = resources.getString(R.string.error_http_internal_server_message)
                }
            }

            val modelIfExists = this.data as? SimpleResponse?
            val messageFromApi = modelIfExists?.messageIfExist
            if (forceFromApi || messageFromApi.isNotNullOrEmpty())
                httpErrorUI.apply { message = modelIfExists?.messageIfExist }
            else
                httpErrorUI
        }
        isNetworkError() ->
            UIErrorModel(
                icon = R.drawable.ic_error_network,
                title = resources.getString(R.string.error_network_title),
                message = resources.getString(R.string.error_network_message),
                actionText = resources.getString(R.string.error_actionText_try_again)
            )
        isConnectionError() ->
            UIErrorModel(
                icon = R.drawable.ic_error_connection,
                title = resources.getString(R.string.error_connection_title),
                message = resources.getString(R.string.error_connection_message),
                actionText = resources.getString(R.string.error_actionText_try_again)
            )
        isParseError() ->
            UIErrorModel(
                title = resources.getString(R.string.error_response_parse_title),
                message = resources.getString(R.string.error_response_parse_message),
                actionText = resources.getString(R.string.error_actionText_try_again)
            )
        isUnexpectedError() ->
            UIErrorModel(
                title = resources.getString(R.string.error_response_unexpected_title),
                message = resources.getString(R.string.error_response_unexpected_message),
                actionText = resources.getString(R.string.error_actionText_try_again)
            )
        isRequestNotValidError() ->
            UIErrorModel(
                title = resources.getString(R.string.error_response_request_not_valid_title),
                message = resources.getString(R.string.error_response_request_not_valid_message),
                actionText = resources.getString(R.string.error_actionText_try_again)
            )
        else -> unexpected(-500)
    }
}