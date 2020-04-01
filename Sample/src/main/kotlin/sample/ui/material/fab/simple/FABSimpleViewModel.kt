package sample.ui.material.fab.simple

import android.view.View
import androidx.lifecycle.MutableLiveData
import meow.core.arch.MeowViewModel
import meow.core.arch.SingleLiveData
import sample.App
import sample.R
import sample.data.Content

class FABSimpleViewModel(app: App) : MeowViewModel(app) {

    val listLiveData = MutableLiveData<List<Content>>()
    val addItemLiveData = SingleLiveData<Boolean>()

    fun fillList() {
        val list = arrayListOf<Content>()
        (1..200).forEach {
            list.add(
                Content(
                    title = app.getString(R.string.content_item_title, it),
                    desc = app.getString(R.string.content_item_desc, it)
                )
            )
        }
        listLiveData.postValue(list)
    }

    fun onClickedAddItem(@Suppress("UNUSED_PARAMETER") view: View) {
        listLiveData.apply {
            postValue(ArrayList(value!!).apply {
                add(
                    0, Content(
                        title = app.getString(R.string.content_item_custom_title),
                        desc = app.getString(R.string.content_item_custom_desc)
                    )
                )
            })
        }
        addItemLiveData.postValue(true)
    }

}