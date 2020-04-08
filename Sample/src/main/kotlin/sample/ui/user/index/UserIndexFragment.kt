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

package sample.ui.user.index

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import meow.core.arch.MeowFlow
import meow.util.dp
import meow.util.instanceViewModel
import meow.widget.addItemDecoration
import meow.widget.decoration.MeowDividerDecoration
import sample.R
import sample.data.user.User
import sample.databinding.FragmentUserIndexBinding
import sample.ui.base.BaseFragment

/**
 * [User]/Index Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-29
 */

class UserIndexFragment : BaseFragment<FragmentUserIndexBinding>() {

    private val viewModel: UserIndexViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_user_index

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration { position, outRect ->
                if (position == 0)
                    outRect.top = 24.dp()
            }
            addItemDecoration(MeowDividerDecoration(context))
            adapter = UserAdapter(viewModel)
        }

        viewModel.callApi()
    }

    override fun initViewModel() {
        binding.viewModel = viewModel

        MeowFlow.GetDataApi(this).apply {
            onShowLoading = { (requireActivity()).title = "Loading" }//todo getString()
            onHideLoading = { requireActivity().title = "User Index" }
            containerViews = arrayOf()
        }.observe(binding.lifecycleOwner!!, viewModel.listLiveData)
    }

}