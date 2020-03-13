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

package meow.utils

import android.view.View
import android.view.ViewTreeObserver
import androidx.databinding.ViewDataBinding

/**
 * [View] Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-14
 */

inline fun <T : View?> T.afterMeasured(
    crossinline block: T.() -> Unit,
    observeForEver: Boolean = false
) =
    this?.viewTreeObserver?.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            avoidException {
                block()
                if (!observeForEver)
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })

inline fun <T : View?> T.safePost(
    delay: Long = 0L,
    crossinline block: T.() -> Unit
) =
    if (delay == 0L)
        this?.post { avoidException { block() } }
    else
        this?.postDelayed({
            avoidException { block() }
        }, delay)

inline fun <T : ViewDataBinding?> T.afterMeasured(
    delay: Long = 0L,
    crossinline block: View?.() -> Unit
) = this?.root.afterMeasured(block)

inline fun <T : ViewDataBinding?> T.safePost(
    delay: Long = 0L,
    crossinline block: View?.() -> Unit
) = this?.root.safePost(delay, block)