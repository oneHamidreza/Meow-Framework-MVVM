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
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.etebarian.meowframework.R
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialAutoCompleteTextView
import meow.util.dp


/**
 * Meow Spinner class.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-04-09
 */

open class MeowSpinner : TextInputLayout {

    val autoCompleteTextView = MaterialAutoCompleteTextView(context)
    private var items: ArrayList<AdapterItem> = ArrayList()

    val adapter = CustomArrayAdapter(
        context!!,
        R.layout.exposed_dropdown_menu_item,
        items
    )

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeView()
    }

    private fun initializeView() {
        addView(autoCompleteTextView)
        autoCompleteTextView.inputType = InputType.TYPE_NULL
        autoCompleteTextView.setTextIsSelectable(false)
        build()
    }

    fun addItem(title: String, description: String? = null, imageViewResId: Int = 0): MeowSpinner {
        items.add(AdapterItem(title, description, imageViewResId))
        return this
    }

    fun addItem(titleRes: Int, descriptionRes: Int = 0, imageViewResId: Int = 0): MeowSpinner {
        items.add(
            AdapterItem(
                context.getString(titleRes),
                if (descriptionRes == 0) "" else context.getString(descriptionRes),
                imageViewResId
            )
        )
        return this
    }

    fun build() {
        adapter.notifyDataSetChanged()
        setAdapter(adapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val title = adapter.getItem(position)?.title
            autoCompleteTextView.setText(title, false)
        }
    }

    fun setOnItemCLickListener(listener: (parent: AdapterView<*>, view: View, position: Int, id: Long) -> Unit) {
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val title = adapter.getItem(position)?.title
            autoCompleteTextView.setText(title, false)
            listener(parent, view, position, id)
        }
    }

    fun <T> setAdapter(adapter: ArrayAdapter<T>) {
        autoCompleteTextView.setAdapter(adapter)
    }

    class CustomArrayAdapter(
        context: Context,
        var resourceId: Int,
        var objects: List<AdapterItem>
    ) : ArrayAdapter<AdapterItem?>(context, resourceId, objects) {

        override fun getDropDownView(
            position: Int, convertView: View?,
            parent: ViewGroup?
        ): View {
            return getCustomView(position, convertView, parent)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomView(position, convertView, parent)
        }

        private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val row: View = inflater.inflate(resourceId, parent, false)

            val title = row.findViewById(R.id.tv_title) as TextView
            val description = row.findViewById(R.id.tv_description) as TextView
            val imageView = row.findViewById(R.id.iv) as ImageView

            if (objects[position].description.isNullOrEmpty()) {
                description.visibility = View.GONE
                title.setPadding(16.dp(), 16.dp(), 16.dp(), 16.dp())
            }
            if (objects[position].imageViewResId == 0) {
                imageView.visibility = View.GONE
            }

            title.text = objects[position].title
            description.text = objects[position].description
            imageView.setImageResource(objects[position].imageViewResId)

            return row
        }
    }

    class AdapterItem(
        var title: String? = null,
        var description: String? = null,
        var imageViewResId: Int = 0
    )
}