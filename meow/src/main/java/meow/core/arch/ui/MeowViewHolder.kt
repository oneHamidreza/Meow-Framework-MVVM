package meow.core.arch.ui

import android.view.View

import androidx.recyclerview.widget.RecyclerView
import meow.core.arch.MeowViewModel

/**
 * Created by 1HE on 10/1/2018.
 */
class MeowViewHolder<T, VM : MeowViewModel>(
    itemView: View?,
    var onBindBlock: (position: Int, model: T, viewModel: VM) -> Unit
) : RecyclerView.ViewHolder(itemView!!) {

    fun onBind(position: Int, model: T, viewModel: VM) {
        onBindBlock(position, model, viewModel)
    }
}
