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
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.etebarian.meowframework.R
import com.etebarian.meowframework.databinding.MeowDialogLoadingBinding
import meow.ktx.dp

/**
 * Meow Progress Bar class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-23
 */

@Suppress("unused")
class MeowLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : LinearLayout(context, attrs, defStyleAttrs) {

    init {
        setPaddingRelativeAll(16.dp())
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    private val binding = DataBindingUtil.inflate<MeowDialogLoadingBinding>(LayoutInflater.from(context), R.layout.meow_dialog_loading, this, true)

    fun setTitle(title: String): MeowLoadingView {
        binding.title = title
        return this
    }
}