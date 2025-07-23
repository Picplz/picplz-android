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
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MoodKeywordModalBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
) {
//    TODO: State, ViewModel 연결
    val moodKeywords = listOf(
        "캐주얼", "고급미", "심플", "단아", "몽환적",
        "빈티지", "청량", "화려", "퇴폐적", "키치", "힙스터"
    )
    var selectedMoodKeywords by remember { mutableStateOf(emptySet<String>()) }

    CommonModalBottomSheet(
        visible = visible,
        visibleCloseButton = true,
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetMaxHeight = 289.dp + 84.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header()

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                MoodKeywordContent(
                    keywords = moodKeywords,
                    selectedKeywords = selectedMoodKeywords,
                    onToggleKeyword = {
                        toggleSelection(
                            it,
                            selectedMoodKeywords
                        ) { selectedMoodKeywords = it }
                    }
                )
            }

            Footer(
                onReset = {
                    selectedMoodKeywords = emptySet()
                },
                onSubmit = {
                    // TODO: 선택된 기기로 작가 필터링
                }
            )
        }
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "분위기 키워드",
            style = MainThemeFont.TitleSmall,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MoodKeywordContent(
    keywords: List<String>,
    selectedKeywords: Set<String>,
    onToggleKeyword: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        keywords.forEach { keyword ->
            CommonChip(
                label = keyword,
                isSelected = keyword in selectedKeywords,
                onClickDefaultMode = {
                    onToggleKeyword(keyword)
                }
            )
        }
    }
}

@Composable
private fun Footer(
    onReset: () -> Unit,
    onSubmit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(bottom = 45.dp, top = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CommonBottomButton(
            text = "초기화",
            onClick = onReset,
            modifier = Modifier.weight(1f),
            contentColor = MainThemeColor.Black,
            borderColor = MainThemeColor.Gray3,
            containerColor = MainThemeColor.White
        )
        CommonBottomButton(
            text = "{}명 작가보기",
            onClick = onSubmit,
            modifier = Modifier.weight(2f)
        )
    }
}

private fun toggleSelection(
    option: String,
    currentSet: Set<String>,
    onUpdate: (Set<String>) -> Unit
) {
    onUpdate(
        if (option in currentSet) currentSet - option else currentSet + option
    )
}