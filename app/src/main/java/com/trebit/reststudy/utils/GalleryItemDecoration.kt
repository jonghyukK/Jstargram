package com.trebit.reststudy.utils

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View

/**
 * Jstargram
 * Class: GalleryItemDecoration
 * Created by kangjonghyuk on 18/04/2019.
 *
 * Description:
 */

class GalleryItemDecoration(ctx: Context): RecyclerView.ItemDecoration() {

    private var size10 : Int
    private var size5  : Int

    init {
        size10 = dpToPx(ctx, 2)
        size5  = dpToPx(ctx, 1)
    }

    private fun dpToPx(ctx: Context, dp: Int): Int {
        return dp.dpToPx(ctx.resources.displayMetrics)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        // 상하 설정
        if ( position == 0 || position == 1 || position == 2 || position == 3) {
            // 첫째 줄
            outRect.top    = size10
            outRect.bottom = size10
        } else {
            outRect.bottom = size10
        }

        val lp = view.layoutParams as GridLayoutManager.LayoutParams

        // 좌우 설정
        when (lp.spanIndex) {
            0 -> outRect.right = size5
            1 -> {
                outRect.left  = size5
                outRect.right = size5
            }
            2 -> {
                outRect.left  = size5
                outRect.right = size5
            }
            3 -> outRect.left = size5
        }
    }

    private fun Int.dpToPx(dm: DisplayMetrics): Int = (this * dm.density).toInt()
}