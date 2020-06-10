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

package meow.ktx

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

/**
 * Keyboard Utils class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

class KeyboardUtils(
    private val activity: Activity,
    private val contentView: View? = null,
    private val changePadding: Boolean = true,
    listener: (showingKeyboard: Boolean) -> Unit = {}
) {

    private val decorView: View? get() = activity.window?.decorView
    var isLastKeyboardState = false

    //a small helper to allow showing the editText focus
    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val r = Rect()
        //r will be populated with the coordinates of your view that area still visible.
        decorView?.getWindowVisibleDisplayFrame(r)

        //get screen height and calculate the difference with the useable area from the r
        val height = decorView?.context?.resources?.displayMetrics?.heightPixels ?: 0
        var diff = height - r.bottom

        if (diff < 0)
            diff = 0

        //if it could be a keyboard add the padding to the view
        if (diff != 0) {
            if (!isLastKeyboardState)
                listener(true)
            isLastKeyboardState = true
            // if the use-able screen height differs from the total screen height we assume that it shows a keyboard now
            //check if the padding is 0 (if yes set the padding for the keyboard)
            if (contentView?.paddingBottom != diff && changePadding) {
                //set the padding of the contentView for the keyboard
                contentView?.setPadding(0, 0, 0, diff)
            }
        } else {
            if (isLastKeyboardState)
                listener(false)
            isLastKeyboardState = false
            //check if the padding is != 0 (if yes reset the padding)
            if (contentView?.paddingBottom != 0 && changePadding) {
                //reset the padding of the contentView
                contentView?.setPadding(0, 0, 0, 0)
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun enable() {
        sdkNeed(19) {
            decorView?.viewTreeObserver?.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun disable() {
        sdkNeed(19) {
            decorView?.viewTreeObserver?.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }
}