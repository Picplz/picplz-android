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
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.screen.common.CommonModalBottomSheet
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

private const val ITEM_HEIGHT = 57
private const val SHEET_PADDING = 80

enum class ReviewSortType(val label: String) {
    LATEST("최신순"),
    LIKES("좋아요순"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewSortBottomSheet(
    visible: Boolean,
    selectedSortType: ReviewSortType,
    onDismiss: () -> Unit,
    onSelect: (ReviewSortType) -> Unit,
) {
    CommonModalBottomSheet(
        visible = visible,
        visibleCloseButton = false,
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetMaxHeight = (ITEM_HEIGHT * ReviewSortType.entries.size + SHEET_PADDING).dp,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            ReviewSortType.entries.forEachIndexed { index, type ->
                val isSelected = type == selectedSortType
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(57.dp)
                            .clickable {
                                onSelect(type)
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = type.label,
                        style = MainThemeFont.TitleSmall,
                        color = if (isSelected) MainThemeColor.Black else MainThemeColor.Gray3,
                    )
                }
                if (index != ReviewSortType.entries.toTypedArray().lastIndex) {
                    HorizontalDivider(color = MainThemeColor.Gray2, thickness = 0.96.dp)
                }
            }
        }
    }
}
