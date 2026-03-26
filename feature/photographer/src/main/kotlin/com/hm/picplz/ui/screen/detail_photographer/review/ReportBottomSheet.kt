package com.hm.picplz.ui.screen.detail_photographer.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.screen.common.CommonModalBottomSheet
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.feature.photographer.R as PhotographerR

private const val ITEM_HEIGHT = 57
private const val SHEET_PADDING = 120

private val reportOptions: List<Int> =
    listOf(
        PhotographerR.string.report_harmful_content,
        PhotographerR.string.report_plagiarism,
        PhotographerR.string.report_privacy_risk,
        PhotographerR.string.report_not_customer,
        PhotographerR.string.report_other,
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
) {
    CommonModalBottomSheet(
        visible = visible,
        visibleCloseButton = false,
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetMaxHeight = (ITEM_HEIGHT * reportOptions.size + SHEET_PADDING).dp,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            reportOptions.forEachIndexed { index, resId ->
                val label = stringResource(resId)
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(57.dp)
                            .clickable {
                                onSelect(label)
                                onDismiss()
                            },
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = label,
                        style = MainThemeFont.Body,
                        color = MainThemeColor.Black,
                    )
                }
                if (index != reportOptions.lastIndex) {
                    HorizontalDivider(
                        color = MainThemeColor.Gray2,
                        thickness = 0.96.dp,
                    )
                }
            }
        }
    }
}
