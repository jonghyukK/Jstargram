package com.trebit.reststudy.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.trebit.reststudy.ui.main.fragment.sub.DataGridFragment
import com.trebit.reststudy.ui.main.fragment.sub.DataVerticalFragment

/**
 * Jstargram
 * Class: ItemShowingTypeAdapter
 * Created by kangjonghyuk on 08/04/2019.
 *
 * Description:
 */

class ItemShowingTypeAdapter(fm : FragmentManager,
                             private var tabCount: Int
): FragmentPagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment? {
        return when ( p0 ) {
            0 -> DataGridFragment.newInstance()
            1 -> DataVerticalFragment.newInstance()
            else -> null
        }
    }



    override fun getCount(): Int = tabCount
}