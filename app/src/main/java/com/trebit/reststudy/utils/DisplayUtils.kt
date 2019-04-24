package com.trebit.reststudy.utils

import android.content.Context
import android.util.DisplayMetrics
import com.orhanobut.logger.Logger

/**
 * Jstargram
 * Class: DisplayUtils
 * Created by kangjonghyuk on 23/04/2019.
 *
 * Description:
 */

class DisplayUtils {

    companion object {

        // 0 : width
        // 1 : height
        fun getDisplayWH(ctx: Context, value: Int): Float{
            val dm = ctx.resources.displayMetrics
            val dpWidth = dm.widthPixels / dm.density
            val dpHeight = dm.heightPixels / dm.density

            Logger.d("""
                dp Width : $dpWidth
                dp Height : $dpHeight
            """.trimIndent())

            return if ( value == 0 ) dpWidth else dpHeight
        }

        fun dpToPx(ctx: Context, dp: Int): Int = dp.dpToPx(ctx.resources.displayMetrics)

    }
}

fun Int.dpToPx(dm: DisplayMetrics): Int = (this * dm.density).toInt()