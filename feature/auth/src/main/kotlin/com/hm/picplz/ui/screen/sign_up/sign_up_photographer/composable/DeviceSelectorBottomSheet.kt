package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.composable

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun DeviceSelectorContent(
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    onDirectInput: (() -> Unit)?,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding(),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
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
                    text = "직접 입력",
                    style = pretendardTypography.bodyMedium,
                )
            }
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
                HorizontalDivider(
                    color = MainThemeColor.Gray2,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceSelectorBottomSheet(
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    onDirectInput: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    visible: Boolean,
) {
    if (visible) {
        val bottomSheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            containerColor = Color.White,
            dragHandle = null,
            modifier =
                Modifier.heightIn(
                    min = 200.dp,
                    max = LocalConfiguration.current.screenHeightDp.dp * 0.867f,
                ),
        ) {
            DeviceSelectorContent(
                options = options,
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

@Preview(showBackground = true, heightDp = 600)
@Composable
fun DeviceSelectorPreview() {
    PicplzTheme {
        DeviceSelectorContent(
            options = listOf("애플", "삼성", "구글"),
            onOptionSelected = {},
            onDirectInput = {},
        )
    }
}

@Preview(showBackground = true, heightDp = 600)
@Composable
fun DeviceSelectorModelPreview() {
    PicplzTheme {
        DeviceSelectorContent(
            options = listOf("아이폰 15 Pro", "아이폰 15", "아이폰 14 Pro", "아이폰 14"),
            onOptionSelected = {},
            onDirectInput = {},
        )
    }
}
