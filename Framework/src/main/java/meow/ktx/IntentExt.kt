package meow.ktx

//import androidx.browser.customtabs.CustomTabsIntent
import android.content.Intent
import android.net.Uri
import meow.core.ui.FragmentActivityInterface

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

/**
 * Intent Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-05-02
 */

fun FragmentActivityInterface<*>.openChrome(url: String?) {
    if (url == null)
        return

    avoidException(
        tryBlock = {
//            val intentBuilder = CustomTabsIntent.Builder()
//            intentBuilder.setToolbarColor(
//                MaterialColors.getColor(
//                    context(),
//                    R.attr.colorPrimary,
//                    ""
//                )
//            )
//            intentBuilder.setSecondaryToolbarColor(
//                MaterialColors.getColor(
//                    context(),
//                    R.attr.colorSecondary,
//                    ""
//                )
//            )
//            intentBuilder.build().launchUrl(context(), Uri.parse(url.addHttpIfNeed()))
        },
        exceptionBlock = {
            openBrowser(url)
        })
}

fun FragmentActivityInterface<*>.openBrowser(url: String?) {
    if (url == null)
        return

    avoidException {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.addHttpIfNeed()))
        context().startActivity(intent)
    }
}
