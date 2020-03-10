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
import meow.core.api.MeowEvent
import meow.utils.*
import meow.widget.ErrorImpl
import meow.widget.ProgressBarImpl

/**
 * The Meow Component class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-10
 */

sealed class MeowFlow {

    class DetailApi(
        val containerViews: Array<View>,
        val visibilityWhenLoading: Int = View.GONE
    ) : Api() {
        init {
            onBeforeAction = {
                containerViews.forEach { it.visibility = visibilityWhenLoading }
                showLoading()
            }
            onAfterAction = {
                hideLoading()
                containerViews.forEach { it.visibility = View.VISIBLE }
            }
        }
    }

    open class Api(
        var progressBarImpl: ProgressBarImpl? = null,
        var errorImpl: ErrorImpl? = null,
        var dialog: Dialog? = null,
        var onBeforeAction: () -> Unit = { showLoading() },
        var onAfterAction: () -> Unit = { hideLoading() },
        var onSuccessAction: (it: MeowEvent) -> Unit = {},
        var onCancellationAction: () -> Unit = {},
        var onErrorAction: (it: MeowEvent.Error) -> Unit = {},
        var showLoading: () -> Unit = {
            progressBarImpl?.show()
            dialog?.show()
            errorImpl?.hide()
        },
        var hideLoading: () -> Unit = {
            progressBarImpl?.hide()
            dialog?.hide()
        }
    ) : MeowFlow() {
        fun observe(liveData: LiveData<MeowEvent>?) {
            liveData?.safeObserve {
                when {
                    it.isLoading() -> {
                        onBeforeAction()
                    }
                    it.isSuccess() -> {
                        onAfterAction()
                        onSuccessAction(it)
                    }
                    it.isCancellation() -> {
                        onAfterAction()
                        onCancellationAction()
                    }
                    it.isError() -> {
                        onAfterAction()
                        errorImpl?.show()
                        onErrorAction(it as MeowEvent.Error)
                    }
                }
            }
        }
    }

}