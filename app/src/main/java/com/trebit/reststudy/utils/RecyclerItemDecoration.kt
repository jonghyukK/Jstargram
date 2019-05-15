package com.trebit.reststudy.utils

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * Jstargram
 * Class: RecyclerItemDecoration
 * Created by kangjonghyuk on 18/04/2019.
 *
 * Description:
 */

class RecyclerItemDecoration(ctx: Context): RecyclerView.ItemDecoration() {

    private var size10 : Int = DisplayUtils.dpToPx(ctx, 3)
    private var size5  : Int = DisplayUtils.dpToPx(ctx, 2)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view) // item position
        val spanCount = 3
        val spacing = size5 //spacing between views in grid

        if (position >= 0) {
            val column = position % spanCount // item column

            outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = 0
            outRect.right = 0
            outRect.top = 0
            outRect.bottom = 0
        }
    }
}