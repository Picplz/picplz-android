package com.hm.picplz.ui.screen.main.modalBottomSheet

import CommonChip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonModalBottomSheet
import com.hm.picplz.ui.theme.MainThemeFont

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DeviceModalBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
) {
//    TODO: State, ViewModel 연결
    val phoneOptions = listOf("아이폰", "갤럭시")
    val cameraOptions = listOf("DSLR", "미러리스", "필름 카메라", "디지털 카메라", "dfsadfasddfdasfsad")

    var selectedPhones by remember { mutableStateOf(setOf<String>()) }
    var selectedCameras by remember { mutableStateOf(setOf<String>()) }

    CommonModalBottomSheet(
        visible = visible,
        visibleCloseButton = true,
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetMaxHeight = 339.dp + 84.dp,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header()

            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
            ) {
                DeviceContent(
                    phoneOptions = phoneOptions,
                    cameraOptions = cameraOptions,
                    selectedPhones = selectedPhones,
                    selectedCameras = selectedCameras,
                    onTogglePhone = { toggleSelection(it, selectedPhones) { selectedPhones = it } },
                    onToggleCamera = {
                        toggleSelection(it, selectedCameras) {
                            selectedCameras = it
                        }
                    },
                )
            }

            Footer(
                onReset = {
                    selectedPhones = emptySet()
                    selectedCameras = emptySet()
                },
                onSubmit = {
                    // TODO: 선택된 기기로 작가 필터링
                },
            )
        }
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "촬영 기기",
            style = MainThemeFont.TitleSmall,
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DeviceContent(
    phoneOptions: List<String>,
    cameraOptions: List<String>,
    selectedPhones: Set<String>,
    selectedCameras: Set<String>,
    onTogglePhone: (String) -> Unit,
    onToggleCamera: (String) -> Unit,
) {
    Column {
        DeviceChipSection(
            title = "휴대폰",
            options = phoneOptions,
            selected = selectedPhones,
            onToggle = onTogglePhone,
        )

        Spacer(modifier = Modifier.height(30.dp))

        DeviceChipSection(
            title = "카메라",
            options = cameraOptions,
            selected = selectedCameras,
            onToggle = onToggleCamera,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DeviceChipSection(
    title: String,
    options: List<String>,
    selected: Set<String>,
    onToggle: (String) -> Unit,
) {
    Text(
        text = title,
        style = MainThemeFont.ButtonDefault,
    )

    Spacer(modifier = Modifier.height(10.dp))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        options.forEach { option ->
            CommonChip(
                label = option,
                isSelected = option in selected,
                onClickDefaultMode = { onToggle(option) },
            )
        }
    }
}

@Composable
private fun Footer(
    onReset: () -> Unit,
    onSubmit: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .padding(bottom = 45.dp, top = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CommonBottomButton(
                text = "초기화",
                onClick = onReset,
            )
        }
        Box(modifier = Modifier.weight(2f)) {
            CommonBottomButton(
                text = "{}명 작가보기",
                onClick = onSubmit,
            )
        }
    }
}

private fun toggleSelection(
    option: String,
    currentSet: Set<String>,
    onUpdate: (Set<String>) -> Unit,
) {
    onUpdate(
        if (option in currentSet) currentSet - option else currentSet + option,
    )
}
