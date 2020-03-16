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

import android.app.Dialog
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import meow.core.api.MeowEvent
import meow.core.api.MeowResponse
import meow.core.arch.MeowFlow.GetDataFromApi
import meow.core.ui.FragmentActivityFlow
import meow.utils.*
import meow.widget.impl.ErrorImpl
import meow.widget.impl.ProgressBarImpl

/**
 * Meow Flow class containing [GetDataFromApi].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-10
 */

sealed class MeowFlow(open val flowInterface: FragmentActivityFlow) {

    class GetDataFromApi(override val flowInterface: FragmentActivityFlow) : Api(flowInterface) {
        init {
            onBeforeAction = {
                containerViews.forEach { it.visibility = visibilityWhenLoading }
                showLoading()
            }
            onAfterAction = {
                hideLoading()
                if (!lastStateHasBeenError)
                    containerViews.forEach { it.visibility = View.VISIBLE }
            }
        }
    }

    open class Api(
        override val flowInterface: FragmentActivityFlow
    ) : MeowFlow(flowInterface) {

        var isErrorShowByToastEnabled: Boolean = true

        var isShowingErrorMassageEnabled: Boolean = true

        var lastStateHasBeenError: Boolean = false

        var containerViews: Array<View> = arrayOf()

        var visibilityWhenLoading: Int = View.GONE

        var progressBarImpl: ProgressBarImpl? = null

        var errorImpl: ErrorImpl? = null//todo impl ???

        var dialog: Dialog? = null

        var onBeforeAction: () -> Unit = { showLoading() }

        var onAfterAction: () -> Unit = { hideLoading() }

        var onSuccessAction: (it: MeowResponse<*>) -> Unit = {}

        var onCancellationAction: () -> Unit = {
            if (isShowingErrorMassageEnabled) {
                val message = MeowEvent.Api.Cancellation().message(flowInterface.resources())
                onShowErrorMessage(message)
            }
        }

        var onErrorAction: (it: MeowEvent.Api.Error) -> Unit = {
            onShowErrorMessage(it.data.createErrorMessage(flowInterface.resources()))
        }

        var onShowErrorMessage: (it: String) -> Unit = {
            if (isErrorShowByToastEnabled)
                Toast.makeText(flowInterface.context(), it, Toast.LENGTH_LONG).show()
        }

        var showLoading: () -> Unit = {
            progressBarImpl?.show()
            dialog?.show()
            errorImpl?.hide()
        }

        var hideLoading: () -> Unit = {
            progressBarImpl?.hide()
            dialog?.hide()
        }

        fun observe(owner: LifecycleOwner?, liveData: LiveData<MeowEvent<*>>) {
            liveData.safeObserve(owner) {
                when {
                    it.isApiCancellation() -> {
                        lastStateHasBeenError = true
                        onAfterAction()
                        onCancellationAction()
                    }
                    it.isApiLoading() -> {
                        lastStateHasBeenError = false
                        onBeforeAction()
                    }
                    it.isApiSuccess() -> {
                        onAfterAction()
                        onSuccessAction((it as MeowEvent.Api.Success).data)
                    }
                    it.isApiError() -> {
                        lastStateHasBeenError = true
                        onAfterAction()
                        onErrorAction(it as MeowEvent.Api.Error)
                    }
                }
            }
        }
    }

}