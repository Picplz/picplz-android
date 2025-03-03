package com.hm.picplz.utils

import android.content.Context
import androidx.compose.ui.geometry.Offset
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class DisplayMetricsUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private fun getScreenWidthDp(): Float {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels / displayMetrics.density
    }

    fun calculateOffsetLimits(padding: Float = 40f): OffsetLimits {
        val screenWidth = getScreenWidthDp()
        val maxOffsetX = (screenWidth - padding * 2) / 2

        return OffsetLimits(
            maxOffset = maxOffsetX,
            innerCircleMaxOffset = maxOffsetX * 0.75f,
            outerCircleMinOffset = maxOffsetX * 0.93f
        )
    }

    data class OffsetLimits(
        val maxOffset: Float,
        val innerCircleMaxOffset: Float,
        val outerCircleMinOffset: Float
    )

    fun calculateScreenDistance(point1: Offset, point2: Offset): Float {
        val dx = point1.x - point2.x
        val dy = point1.y - point2.y
        return sqrt(dx * dx + dy * dy)
    }
}