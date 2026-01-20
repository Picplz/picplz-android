package com.hm.picplz.common.util

import android.content.Context
import androidx.compose.ui.geometry.Offset
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.hypot

@Singleton
class DisplayMetricsUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private fun getScreenWidthDp(): Float {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels / displayMetrics.density
    }

    companion object CircleRatios {
        const val INNER_CIRCLE_RATIO = 0.75f
        const val OUTER_CIRCLE_RATIO = 0.93f
    }

    fun calculateOffsetLimits(padding: Float = 40f): OffsetLimits {
        val screenWidth = getScreenWidthDp()
        val maxOffsetX = (screenWidth - padding * 2) / 2

        return OffsetLimits(
            maxOffset = maxOffsetX,
            innerCircleMaxOffset = maxOffsetX * CircleRatios.INNER_CIRCLE_RATIO,
            outerCircleMinOffset = maxOffsetX * CircleRatios.OUTER_CIRCLE_RATIO,
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
        return hypot(dx, dy)
    }
}
