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

package sample.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import meow.core.ui.MeowPagerAdapter
import meow.util.bundleOf
import sample.ui.home.contents.ContentsFragment
import sample.ui.markdown.MarkdownFragment

/**
 * Home View Pager Adapter.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-06
 */

class HomePagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : MeowPagerAdapter(fragmentManager, lifecycle) {

    private val fragmentArray: Array<Fragment> =
        arrayOf(
            MarkdownFragment().bundleOf("fileName" to "README.md"),
            ContentsFragment.newInstance(
                ContentsFragment.Type.API
            ),
            ContentsFragment.newInstance(
                ContentsFragment.Type.MATERIAL
            ),
            ContentsFragment.newInstance(
                ContentsFragment.Type.EXTENSIONS
            ),
            ContentsFragment.newInstance(
                ContentsFragment.Type.WIDGET
            )
        )

    override fun getFragments() = fragmentArray
}
