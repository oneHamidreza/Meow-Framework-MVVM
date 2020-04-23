package meow.util

import android.content.Context
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import meow.core.arch.MeowViewModel
import meow.core.arch.MeowViewModelFactory
import meow.core.ui.MeowFragment
import org.kodein.di.KodeinAware
import org.kodein.di.direct


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
 * Androidx Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-30
 */

//todo convert meow.util to meow.ktx

fun <T> LiveData<T>?.safeObserve(owner: LifecycleOwner? = null, observer: (T) -> Unit) {
    if (this == null)
        return

    val archObserver = Observer<T> { value ->
        if (value is T) avoidException { observer(value) }
    }
    if (owner != null)
        observe(owner, archObserver)
    else
        observeForever(archObserver)
}

/*
    Android ViewModel dependency injection with Kodein by Kirill Rozhenkov
    https://proandroiddev.com/android-viewmodel-dependency-injection-with-kodein-249f80f083c9
*/

inline fun <reified VM : MeowViewModel, T> T.instanceViewModel(): Lazy<VM> where T : KodeinAware, T : AppCompatActivity {
    val clazz = javaClass<VM>()
    return lazy { MeowViewModelFactory(kodein.direct).create(clazz) }
}

inline fun <reified VM : MeowViewModel, T> T.instanceViewModel(): Lazy<VM> where T : KodeinAware, T : Fragment {
    val clazz = javaClass<VM>()
    return lazy { MeowViewModelFactory(kodein.direct).create(clazz) }
}

fun sdkNeed(buildSdk: Int, block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= buildSdk)
        avoidException {
            block()
        }
}

inline fun <reified T> sdkNeedRType(buildSdk: Int, block: () -> T) {
    if (Build.VERSION.SDK_INT >= buildSdk)
        avoidException {
            block()
        }
}

fun sdkNeedReturn(buildSdk: Int, block: () -> Unit): Boolean {
    return if (Build.VERSION.SDK_INT >= buildSdk) {
        avoidException {
            block()
        }
        true
    } else {
        false
    }
}

fun MenuItem.setTypefaceResId(context: Context, id: Int) {
    val span = SpannableString(title)
    span.setSpan(
        PopupTypeFaceSpan(context.getFontCompat(id)),
        0,
        span.length,
        Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )
    title = span
}

fun MeowFragment<*>.bundleOf(vararg pairs: Pair<String, Any?>) = apply {
    arguments = androidx.core.os.bundleOf(*pairs)
}