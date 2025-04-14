package com.hm.picplz.utils

object DateTimeUtil {
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