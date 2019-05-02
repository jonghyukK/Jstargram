package com.trebit.reststudy.utils

import java.text.SimpleDateFormat

/**
 * Jstargram
 * Class: TimeUtils
 * Created by mac on 2019-05-01.
 *
 * Description:
 */

class TimeUtils {

    companion object {

        fun calculateContentDate(date: String): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val dataTime = formatter.parse(date).time
            val curTime = System.currentTimeMillis()

            var diffTime = Math.abs((dataTime - curTime) / 1000)

            val hour = Math.abs(diffTime / 3600)
            diffTime %= 3600
            val min = Math.abs(diffTime / 60)
            val sec = Math.abs(diffTime % 60)

            return when {
                hour > 24 -> "1일 전"
                hour > 0 -> "${hour}시간 전"
                min > 0 -> "${min}분 전"
                sec > 0 -> "방금 전"
                else -> date
            }
        }
    }
}