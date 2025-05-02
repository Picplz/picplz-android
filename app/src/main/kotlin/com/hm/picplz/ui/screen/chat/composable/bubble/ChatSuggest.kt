package com.hm.picplz.ui.screen.chat.composable.bubble

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.model.ChatMessage
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChatSuggest(
    modifier: Modifier = Modifier,
    suggestedChats: List<String>,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "추천 질문",
            style = MainFontFamily.bodyBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            suggestedChats.forEach { chat ->
                ChatSuggestButton(chatText = chat)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatSuggestPreview() {
    PicplzTheme {
        ChatSuggest(
            suggestedChats = listOf(
                "촬영 소요 시간은 얼마나 걸리나요",
                "촬영 장소를 추천해주실 수 있나요",
                "현장에서 옵션 변경이 가능한가요?"
            )
        )
    }
}