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

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import meow.MeowApp
import meow.controller
import meow.core.api.*
import meow.util.avoidException
import meow.util.hasNotNetwork
import meow.util.launchSilent
import meow.util.resources
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * The Base of View Model in MVVM architecture.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-24
 */

open class MeowViewModel(open val app: MeowApp) : AndroidViewModel(app), KodeinAware {

    override val kodein: Kodein by closestKodein()

    var jobWithIds = mutableListOf<Pair<Int, Job>>()

    var exceptionHandler = CoroutineExceptionHandler { _, t ->
        if (controller.isDebugMode) {
            t.printStackTrace()
        }
    }

    protected fun <T> safeCallApi(
        liveData: MutableLiveData<MeowEvent<*>>,
        request: MeowRequest? = null,
        isNetworkRequired: Boolean = true,
        job: Job = Job(),
        apiAction: suspend () -> T,
        resultBlock: (response: MeowResponse<*>, responseModel: T?) -> Unit
    ) = launchSilent(
        exceptionHandler = exceptionHandler,
        job = job
    ) {

        if (request != null && !request.validate()) {
            resultBlock(MeowResponse.RequestNotValid(request), null)
            return@launchSilent
        }

        if (isNetworkRequired && app.hasNotNetwork()) {
            liveData.postValue(MeowEvent.Api.Error(MeowResponse.NetworkError()))
            return@launchSilent
        } else
            liveData.postValue(MeowEvent.Api.Loading())

        var lastId = jobWithIds.lastOrNull()?.first ?: 0
        lastId++
        jobWithIds.add(Pair(lastId, job))

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
                is SocketTimeoutException -> MeowResponse.ConnectionError()
                is IOException -> MeowResponse.NetworkError()
                is HttpException -> createResponseFromHttpError(e)
                is CancellationException -> MeowResponse.Cancellation()
                else -> MeowResponse.ParseError(exception = e)
            }
        }

        response.processAndPush(liveData)
        @Suppress("UNCHECKED_CAST") val data = avoidException { response.data as? T? }
        resultBlock(response, data)
    }

    protected fun cancelAndRemoveJob(job: Job) {
        avoidException {
            job.cancel()
        }
        val found = jobWithIds.find { it.second == job }
        if (found != null)
            jobWithIds.remove(found)
    }

    protected fun cancelAllJobs() {
        jobWithIds.forEach { avoidException { it.second.cancel() } }
        jobWithIds.clear()
    }

    fun getString(id: Int, vararg formatArgs: Any) = app.resources().getString(id, formatArgs)

}