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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import meow.core.api.*
import meow.core.arch.MeowFlow.GetDataApi
import meow.core.ui.MVVM
import meow.util.createErrorMessage
import meow.util.safeObserve
import meow.util.toastL
import meow.widget.impl.ErrorImpl
import meow.widget.impl.ProgressBarImpl

/**
 * Meow Flow class containing [GetDataApi].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-10
 */

sealed class MeowFlow(open val mvvm: MVVM<*, *>) {

    class GetDataApi(override val mvvm: MVVM<*, *>) : Api(mvvm) {
        init {
            onBeforeAction = {
                containerViews.forEach { it.visibility = visibilityWhenLoading }
                onShowLoading(null)
            }
            onAfterAction = {
                onHideLoading()
                if (!lastStateHasBeenError)
                    containerViews.forEach { it.visibility = View.VISIBLE }
            }
        }
    }

    open class Api(
        override val mvvm: MVVM<*, *>
    ) : MeowFlow(mvvm) {

        var isErrorShowByToastEnabled: Boolean = true

        var isShowingErrorMassageEnabled: Boolean = true

        var lastStateHasBeenError: Boolean = false

        var containerViews: Array<View> = arrayOf()

        var visibilityWhenLoading: Int = View.GONE

        var progressBarImpl: ProgressBarImpl? = null

        var errorImpl: ErrorImpl? = null//todo impl ???

        var dialog: Dialog? = null

        var onBeforeAction: () -> Unit = { onShowLoading(null) }

        var onAfterAction: () -> Unit = { onHideLoading() }

        var onSuccessAction: (it: MeowResponse<*>) -> Unit = {}

        var onCancellationAction: () -> Unit = {
            if (isShowingErrorMassageEnabled) {
                val message = MeowEvent.Api.Cancellation().message(mvvm.resources())
                onShowErrorMessage(message)
            }
        }

        var onErrorAction: (it: MeowEvent.Api.Error) -> Unit = {
            onShowErrorMessage(it.data.createErrorMessage(mvvm.resources()))
        }

        var onShowErrorMessage: (it: String) -> Unit = {
            if (isErrorShowByToastEnabled)
                mvvm.toastL(it)
        }

        var onShowLoading: (text: String?) -> Unit = {
            progressBarImpl?.show()
            dialog?.show()
            errorImpl?.hide()
        }

        var onHideLoading: () -> Unit = {
            progressBarImpl?.hide()
            dialog?.hide()
        }

        fun observe(owner: LifecycleOwner?, liveData: LiveData<*>) {
            liveData.safeObserve(owner) {
                if (it is MeowEvent<*>) {
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

}