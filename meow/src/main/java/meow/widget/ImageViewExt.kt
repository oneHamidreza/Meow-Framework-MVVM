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

package meow.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Base64
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.NotificationTarget
import com.bumptech.glide.request.transition.ViewPropertyTransition
import meow.util.avoidException
import meow.util.isNotNullOrEmpty
import meow.util.isValidUrl
import meow.util.logD
import java.io.File
import kotlin.math.abs

/**
 * [ImageView] Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-18
 */

val glideAlphaAnimator = ViewPropertyTransition.Animator {
    it.alpha = 0f
    it.animate().alpha(1f).apply {
        duration = 250
        interpolator = LinearInterpolator()
        start()
    }
}

object ImageViewBindingAdapter {
    @BindingAdapter(
        "meow_data",
        "meow_placeHolder",
        "meow_loadAnimator",
        "meow_base64Data",
        requireAll = false
    )
    @JvmStatic
    fun setUrl(
        view: ImageView,
        data: String,
        placeHolderDrawable: Drawable? = null,
        loadAnimator: ViewPropertyTransition.Animator? = null,
        base64Data: String? = null
    ) {
        val config = GlideConfig(placeHolderDrawable, loadAnimator)
        if (base64Data.isNotNullOrEmpty())
            view.loadBase64(data, config)
        else
            view.loadGlide(data, config)
    }
}

class GlideConfig(
    var placeHolderDrawable: Drawable? = null,
    var animator: ViewPropertyTransition.Animator? = null,
    var transformation: Transformation<Bitmap>? = null
)

fun ImageView?.loadGlide(
    dataString: String? = null,
    glideConfig: GlideConfig = GlideConfig(),
    ratio: Double? = 0.0,
    width: Int = 0,
    dataBytes: ByteArray? = null,
    isTransformEnabled: Boolean = true,
    isAnimationEnabled: Boolean = true,
    listener: RequestListener<Drawable?>? = null
) {
    if (this == null || context == null || ratio == null)
        return
    logD(m = "Glide Load : $dataString")

    @Suppress("CanBeVal")
    var useTransform = isTransformEnabled
    val context = context
    var requestBuilder: RequestBuilder<Drawable>
    val glide = Glide.with(context)
    val isString = dataString != null

    requestBuilder = if (isString) {
        if (dataString.isValidUrl() || dataString.isNullOrEmpty()) {
            glide.load(dataString)
        } else {
            glide.load(File(dataString))
        }
    } else {
        glide.load(dataBytes)
    }

    var options = if (glideConfig.transformation == null)
        RequestOptions()
    else
        RequestOptions.bitmapTransform(glideConfig.transformation!!)

    if (Build.VERSION.SDK_INT >= 21)
        options = options.placeholder(glideConfig.placeHolderDrawable)

    if (isAnimationEnabled && glideConfig.animator != null) {
        requestBuilder =
            requestBuilder.transition(GenericTransitionOptions.with(glideConfig.animator!!))
    }

    if (ratio > 0 || width > 0) { //width = ratio
        val p = calculateForBestFit(ratio, width)
        options = options.centerCrop().override(p.x, p.y)
    }
    listener?.let {
        requestBuilder = requestBuilder.listener(it)
    }
    if (!useTransform)
        options = options.dontTransform()

    requestBuilder = requestBuilder.apply(options)

    requestBuilder.into(this)
}

fun ImageView?.calculateForBestFit(ratio: Double?, imageWidth: Int): Point {
    val p = Point()
    if (ratio == null)
        return p

    val maxRatio = 1.78
    val minRatio = 0.8

    if (ratio in minRatio..maxRatio) {
        p.x = imageWidth
        p.y = (p.x / ratio).toInt()
    } else {
        val max = abs(ratio - maxRatio)
        val min = abs(ratio - minRatio)
        val nearest = if (min >= max) max else min
        p.x = imageWidth
        p.y = (p.x / nearest).toInt()
    }

    return p
}

fun NotificationTarget.loadGlide(context: Context, data: String) {
    val glide = Glide.with(context).asBitmap()
    val requestBuilder = glide.apply {
        if (data.isValidUrl())
            load(data)
        else
            avoidException { load(File(data)) }
    }
    requestBuilder.into(this)
}

fun ImageView?.loadBase64(data: String?, glideConfig: GlideConfig = GlideConfig()) {
    if (this == null || data == null)
        return

    loadGlide(dataBytes = Base64.decode(data, Base64.DEFAULT), glideConfig = glideConfig)
}