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
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.etebarian.meowframework.R
import meow.ktx.avoidException
import kotlin.math.min


/**
 * Meow Circle Image View class.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-04-18
 */

@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
open class MeowCircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttrs) {

    var hasStroke = false
    var strokeColor = 0
    var strokeWidth = 0f

    init {
        setAttributesFromXml(attrs, R.styleable.MeowCircleImageView) {
            strokeColor =
                it.getColor(R.styleable.MeowCircleImageView_meow_strokeColor, strokeColor)
            strokeWidth =
                it.getDimension(R.styleable.MeowCircleImageView_meow_strokeWidth, strokeWidth)
            hasStroke = it.getBoolean(R.styleable.MeowCircleImageView_meow_hasStroke, hasStroke)
        }



        initializeView()
    }

    private fun initializeView() {

    }

    override fun onDraw(canvas: Canvas) {
        avoidException {
            val drawable = drawable ?: return

            if (width == 0 || height == 0)
                return

            val b: Bitmap = (drawable as BitmapDrawable).bitmap

            val bitmap: Bitmap = b.copy(Bitmap.Config.ARGB_8888, true)
            val w = width
            val roundBitmap: Bitmap = getCroppedBitmap(bitmap, w)
            canvas.drawBitmap(roundBitmap, 0f, 0f, null)
        }
    }

    private fun getCroppedBitmap(bmp: Bitmap, radius: Int): Bitmap {
        val bitmap: Bitmap
        bitmap = if (bmp.width != radius || bmp.height != radius) {
            val smallest: Float = min(bmp.width.toFloat(), bmp.height.toFloat())
            val factor = smallest / radius
            Bitmap.createScaledBitmap(
                bmp,
                (bmp.width / factor).toInt(),
                (bmp.height / factor).toInt(),
                false
            )
        } else {
            bmp
        }
        val output: Bitmap = Bitmap.createBitmap(
            radius, radius,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, radius, radius)
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(
            radius / 2 + 0.7f,
            radius / 2 + 0.7f, radius / 2 + 0.1f, paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        paint.color = strokeColor
        canvas.drawCircle(radius / 2 + 0.7f, radius / 2 + 0.7f, radius / 2 + 0.1f, paint)

        return output
    }


}