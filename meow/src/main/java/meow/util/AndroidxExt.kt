package meow.util

import android.content.Context
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

fun <T> LiveData<T>.safeObserve(owner: LifecycleOwner? = null, observer: (T) -> Unit) {
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

fun <VM : MeowViewModel, T> T.viewModelInstance(clazz: Class<VM>): Lazy<VM> where T : KodeinAware, T : AppCompatActivity {
    return lazy { MeowViewModelFactory(kodein.direct).create(clazz) }
}

fun <VM : MeowViewModel, T> T.viewModelInstance(clazz: Class<VM>): Lazy<VM> where T : KodeinAware, T : Fragment {
    return lazy { MeowViewModelFactory(kodein.direct).create(clazz) }
}

fun MenuItem.setTypefaceId(context: Context, id: Int) {
    val span = SpannableString(title)
    span.setSpan(
        PopupTypeFaceSpan(context.getFontCompat(id)),
        0,
        span.length,
        Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )
    title = span
}

