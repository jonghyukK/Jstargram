package com.trebit.reststudy.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.trebit.reststudy.KEY_EMAIL
import com.trebit.reststudy.KEY_VIEW_TYPE
import com.trebit.reststudy.ui.main.fragment.ContentsFragments
import com.trebit.reststudy.ui.main.fragment.ViewType

/**
 * Jstargram
 * Class: ItemShowingTypeAdapter
 * Created by kangjonghyuk on 08/04/2019.
 *
 * Description:
 */

class ItemShowingTypeAdapter(fm : FragmentManager,
                             private var tabCnt: Int,
                             private val myEmail : String
): FragmentPagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment? {

        return when ( p0 ) {
            0 -> ContentsFragments.newInstance(ViewType.GRID    , myEmail)
            1 -> ContentsFragments.newInstance(ViewType.VERTICAL, myEmail)
            else -> null
        }
    }

    override fun getCount(): Int = tabCnt
}