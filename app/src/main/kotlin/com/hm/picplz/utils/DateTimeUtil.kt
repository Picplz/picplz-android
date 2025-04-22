package com.hm.picplz.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtil {
    private val timeFormat = SimpleDateFormat("a h:mm", Locale.KOREA)

    fun getFormattedTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }

    fun getTimeAgoText(timestamp: Long): String {
        val minutesAgo = ((System.currentTimeMillis() - timestamp) / (1000 * 60)).toInt()
        return when {
            minutesAgo < 1 -> "방금 전"
            minutesAgo < 60 -> "${minutesAgo}분 전"
            minutesAgo < 24 * 60 -> "${minutesAgo / 60}시간 전"
            else -> "${minutesAgo / (24 * 60)}일 전"
        }
    }
}