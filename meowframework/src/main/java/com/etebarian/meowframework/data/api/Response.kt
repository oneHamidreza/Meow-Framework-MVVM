package com.etebarian.meowframework.data.api

/**
 * The model of Response in restful api by success and fail statuses.
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-24
 */

/**
 * The parent of restful api responses.
 * @property T containing the type of embedded class in Response
 */
sealed class Response<out T> {

    /**
     * Success response model in restful api.
     * @property T containing the type of embedded class in Response
     * @property data containing the received data from a restful api at success status
     */
    class Success<out T>(val data: T) : Response<T>()

    /**
     * Success response in restful api.
     * @property exception containing the exception at fail status
     * @property code containing the http response code
     * @property error containing the error has been exist or not
     * @property errors containing the errorX items if has been exists
     * @property message containing the http message
     * @property method containing the http method
     * @property path containing the http path
     */
    data class Error(val exception: Throwable,
                     val code: Int? = null,
                     val error: Boolean? = null,
                     val errors: List<ErrorX>? = null,
                     val message: String? = null,
                     val method: String? = null,
                     val path: String? = null) : Response<Nothing>()
}

/**
 * Error response in restful api.
 * @property message containing the error message
 * @property path containing the http path
 */
data class ErrorX(
        val message: String,
        val path: String
)