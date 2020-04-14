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
import androidx.lifecycle.LiveData
import com.etebarian.meowframework.R
import meow.core.api.*
import meow.core.arch.MeowFlow.GetDataApi
import meow.core.arch.MeowFlow.PutDataApi
import meow.core.ui.FragmentActivityInterface
import meow.util.safeObserve
import meow.util.snackL
import meow.util.toastL
import meow.widget.impl.MeowEmptyStateInterface
import meow.widget.impl.ProgressBarInterface

/**
 * Meow Flow class containing [GetDataApi], [PutDataApi].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-10
 */

sealed class MeowFlow(open val fragmentActivity: FragmentActivityInterface<*>) {

    class PutDataApi<T>(fragmentActivity: FragmentActivityInterface<*>, action: () -> Unit) :
        Api<T>(fragmentActivity, action) {

        var onRequestNotValidFromResponse: (errorItems: List<FormErrorModel>) -> Unit = {}

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
            onErrorAction = {
                containerViews.forEach { v -> v.visibility = View.VISIBLE }
                val response = it.data
                val errorItems =
                    (if (response.code == HttpCodes.UNPROCESSABLE_ENTITY.code) response.data as? List<FormErrorModel> else null)
                if (response.code == HttpCodes.UNPROCESSABLE_ENTITY.code && !errorItems.isNullOrEmpty())
                    onRequestNotValidFromResponse(errorItems)
                else
                    onShowErrorMessage(it.data.createErrorModel(fragmentActivity.resources()))
            }
        }
    }

    class GetDataApi<T>(fragmentActivity: FragmentActivityInterface<*>, action: () -> Unit) :
        Api<T>(fragmentActivity, action) {
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

    open class Api<T>(
        fragmentActivity: FragmentActivityInterface<*>,
        private var action: () -> Unit
    ) : MeowFlow(fragmentActivity) {

        var isShowingErrorMassageEnabled: Boolean = true

        var errorHandlerType: ErrorHandlerType = ErrorHandlerType.TOAST

        var lastStateHasBeenError: Boolean = false

        var containerViews: Array<View> = arrayOf()

        var visibilityWhenLoading: Int = View.GONE

        var progressBarInterface: ProgressBarInterface? = null

        var emptyStateInterface: MeowEmptyStateInterface? = null

        var emptyErrorModel: UIErrorModel = UIErrorModel(
            icon = R.drawable.ic_sentiment_dissatisfied,
            title = fragmentActivity.resources().getString(R.string.error_empty_title),
            actionText = null
        )

        var dialog: Dialog? = null

        var onBeforeAction: () -> Unit = { onShowLoading(null) }

        var onAfterAction: () -> Unit = { onHideLoading() }

        var onSuccessAction: (data: T) -> Unit = {}

        var onCancellationAction: () -> Unit = {
            val title = MeowEvent.Api.Cancellation().title(fragmentActivity.resources())
            val message = MeowEvent.Api.Cancellation().message(fragmentActivity.resources())
            onShowErrorMessage(UIErrorModel(title = title, message = message))
        }

        var onErrorAction: (it: MeowEvent.Api.Error) -> Unit = {
            onShowErrorMessage(it.data.createErrorModel(fragmentActivity.resources()))
        }

        var onShowErrorMessage: (error: UIErrorModel) -> Unit = {
            if (isShowingErrorMassageEnabled) {
                if (errorHandlerType == ErrorHandlerType.TOAST)
                    fragmentActivity.toastL(it.titlePlusMessage)
                if (errorHandlerType == ErrorHandlerType.SNACK_BAR) {
                    if (it.actionText.isNullOrEmpty())
                        fragmentActivity.snackL(it.titlePlusMessage)
                    else {
                        fragmentActivity.snackL(it.titlePlusMessage, it.actionText) {
                            onClickedActionSnack()
                        }
                    }
                }
                if (errorHandlerType == ErrorHandlerType.EMPTY_STATE)
                    emptyStateInterface?.show(it)
            }
        }

        var onShowLoading: (text: String?) -> Unit = {
            progressBarInterface?.show()
            dialog?.show()
            emptyStateInterface?.hide()
        }

        var onHideLoading: () -> Unit = {
            progressBarInterface?.hide()
            dialog?.hide()
        }

        var onClickedActionEmptyState: () -> Unit = {
            action()
        }

        var onClickedActionSnack: () -> Unit = {
            action()
        }

        fun observeForIndex(
            eventLiveData: LiveData<*>,
            listLiveData: LiveData<List<T>>
        ) = observe(eventLiveData, listLiveData)

        fun observeForDetail(
            eventLiveData: LiveData<*>
        ) = observe(eventLiveData)

        fun observeForForm(
            eventLiveData: LiveData<*>
        ) =
            observe(eventLiveData)

        private fun observe(
            eventLiveData: LiveData<*>,
            listLiveData: LiveData<List<T>>? = null
        ) {
            emptyStateInterface?.setOnActionClickListener {
                onClickedActionEmptyState()
            }

            action()

            eventLiveData.safeObserve(fragmentActivity.viewLifecycleOwner()) {
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
                            lastStateHasBeenError = false
                            onAfterAction()
                            val response = (it as MeowEvent.Api.Success).data
                            @Suppress("UNCHECKED_CAST") val data = response.data as? T
                            if (data != null)
                                onSuccessAction(data!!)
                        }
                        it.isApiError() -> {
                            lastStateHasBeenError = true
                            onAfterAction()
                            onErrorAction(it as MeowEvent.Api.Error)
                        }
                    }
                }
            }
            listLiveData.safeObserve(fragmentActivity.viewLifecycleOwner()) {
                if (it.isEmpty())
                    emptyStateInterface?.show(emptyErrorModel)
                else
                    emptyStateInterface?.hide()
            }
        }
    }

    enum class ErrorHandlerType {
        TOAST,
        SNACK_BAR,
        EMPTY_STATE,
    }

}