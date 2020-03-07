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

package meow.core.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.io.IOException
import meow.controller
import meow.core.api.MeowRequest
import meow.core.api.MeowResponse
import meow.core.api.MeowStatus
import meow.core.api.SimpleModel
import meow.core.di.Injector
import meow.utils.*
import retrofit2.HttpException

/**
 * The Base of View Model in MVVM architecture.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-24
 */

open class MeowViewModel : ViewModel() {

    var job = lazy {
        Job()
    }
    var exceptionHandler = CoroutineExceptionHandler { _, t ->
        if (controller.isDebugMode) {
            logD(m = t.message)
            t.printStackTrace()
        }
    }

    fun <T> MutableLiveData<MeowStatus>.safeApiCall(
        request: MeowRequest? = null,
        job: Job = Job(),
        isNetworkRequired: Boolean = true,
        apiAction: suspend () -> T,
        resultBlock: (response: MeowResponse<*>, responseModel: T?) -> Unit
    ) {
        launchSilent(
            exceptionHandler = exceptionHandler,
            job = job
        ) {
            if (request?.validate() == false)
                resultBlock(MeowResponse.RequestNotValid(request), null)

            if (isNetworkRequired && Injector.context().hasNotNetwork())
                postValue(MeowStatus.Error(MeowResponse.NetworkError()))
            else
                postValue(MeowStatus.Loading())

            val response = try {
                val dataIfExists = try {
                    apiAction()
                } catch (e: Exception) {
                    if (controller.isDebugMode)
                        e.printStackTrace()
                    throw(e)
                }
                MeowResponse.Success(dataIfExists)
            } catch (e: Exception) {
                if (controller.isDebugMode)
                    e.printStackTrace()
                when (e) {
                    is IOException -> MeowResponse.NetworkError()
                    is HttpException -> createResponseFromHttpError<SimpleModel>(e)
                    else -> MeowResponse.ParseError(exception = e)
                }
            }
            response.processAndPush(this@safeApiCall)
            resultBlock(response, response.data as? T?)
        }
    }
}