package com.hm.picplz.ui.screen.common.device

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
private fun DragHandle() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MainThemeColor.Gray3),
        )
    }
}

@Composable
private fun DeviceSelectorContent(
    options: List<String>,
    directInputText: String,
    onOptionSelected: (String) -> Unit,
    onDirectInput: (() -> Unit)?,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(max = 540.dp)
                .padding(horizontal = 20.dp),
    ) {
        if (onDirectInput != null) {
            OutlinedButton(
                onClick = onDirectInput,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = MainThemeColor.Black,
                    ),
                border =
                    BorderStroke(
                        width = 1.dp,
                        color = MainThemeColor.Gray3,
                    ),
                contentPadding =
                    PaddingValues(
                        horizontal = 10.dp,
                        vertical = 10.dp,
                    ),
                shape = RoundedCornerShape(5.dp),
            ) {
                Text(
                    text = directInputText,
                    style = pretendardTypography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(options) { option ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                            }
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = option,
                        style = pretendardTypography.titleSmall,
                        color = MainThemeColor.Black,
                    )
                }
                HorizontalDivider(color = MainThemeColor.Gray2)
            }
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceSelectorBottomSheet(
    options: List<String>,
    directInputText: String,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    visible: Boolean,
    onDirectInput: (() -> Unit)? = null,
) {
    if (visible) {
        val bottomSheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            dragHandle = { DragHandle() },
            containerColor = MainThemeColor.White,
        ) {
            DeviceSelectorContent(
                options = options,
                directInputText = directInputText,
                onOptionSelected = { option ->
                    onOptionSelected(option)
                    onDismiss()
                },
                onDirectInput =
                    if (onDirectInput != null) {
                        {
                            onDirectInput()
                            onDismiss()
                        }
                    } else {
                        null
                    },
            )
        }
    }
}
