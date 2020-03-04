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

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable
import meow.core.api.exceptions.NetworkConnectionException
import meow.core.api.exceptions.UnexpectedException

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
     * Success response model in restful api with http code 200.
     * some of web services give the client 200..399 range to success response.
     * so you can use [MeowController().apiSuccessRange].
     *
     * @property T containing the type of embedded class in Response
     * @property data containing the received data from a restful api at success status
     */
    class Success<T>(override var data: T?) : MeowResponse<T>(HttpCodes.OK.code, data)

    /**
     * Http Error response model in restful api.
     *
     */
    class HttpError(
        override var data: SimpleModel? = null,
        override var code: Int = 0,
        override var exception: Throwable? = null
    ) : Error<SimpleModel>()

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
    class NetworkError : Error<SimpleModel>(exception = NetworkConnectionException())

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
data class SimpleModel(
    @Json(name = "status") var status: Int = 0,
    @Json(name = "message") var message: String? = null,
    @Json(name = "body") var body: String? = null,
    @Json(name = "name") var name: String? = null
)

@Serializable
data class FormErrorModel(
    @Json(name = "field") var field: String? = null,
    @Json(name = "message") var message: String? = null
)