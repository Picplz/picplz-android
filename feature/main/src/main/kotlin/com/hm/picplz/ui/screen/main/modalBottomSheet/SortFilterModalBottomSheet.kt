package com.hm.picplz.ui.screen.main.modalBottomSheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

enum class SortType(val label: String) {
    POPULAR("인기순"),
    RATING("별점순"),
    FOLLOWER("팔로워순")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SortFilterModalBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSelect: (SortType) -> Unit
) {
    CommonModalBottomSheet(
        visible = visible,
        visibleCloseButton = false,
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetMaxHeight = 222.dp + 84.dp
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {
            SortType.entries.forEachIndexed { index, type ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp)
                        .clickable {
                            onSelect(type)
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = type.label,
                        style = MainThemeFont.TitleSmall
                    )
                }
                if (index != SortType.entries.toTypedArray().lastIndex) {
                    HorizontalDivider(color = MainThemeColor.Gray2, thickness = 0.96.dp)
                }
            }
        }
    }
}
