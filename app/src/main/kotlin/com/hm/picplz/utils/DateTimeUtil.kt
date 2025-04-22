package com.hm.picplz.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtil {
    private val timeFormat = SimpleDateFormat("a h:mm", Locale.KOREA)
    private val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

    fun getFormattedTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }

    fun getFormattedDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
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

    fun truncateToDate(timestamp: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }
}