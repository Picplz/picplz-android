package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

data class DropdownMenuItemData(
    val text: String,
    val textColor: Color,
    val textStyle: TextStyle = MainFontFamily.bodyLarge,
    val itemOnClick: () -> Unit = {}
)

@Composable
fun CommonDropdownMenu(
    initialSelectedText: String, // 초기 노출 텍스트
    triggerButton: @Composable (String) -> Unit, // triggerButton에서 selectedText를 받아서 표시
    menuItems: List<DropdownMenuItemData> // 아이템 리스트
) {
    // 상태 값과 상태 변경 함수
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(initialSelectedText) } // 선택된 텍스트를 저장할 상태

    Box {
        // 트리거 콘텐츠 클릭 시 드롭다운 메뉴 열기
        Box(
            modifier = Modifier
                .clickable(
                    indication = null, // 클릭 효과 제거
                    interactionSource = remember { MutableInteractionSource() } // 새로운 interaction source 사용
                ) {
                    expanded = !expanded // 드롭다운 토글
                }
        ) {
            triggerButton(selectedText) // triggerButton에 selectedText 전달
        }

        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(20.dp))
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(0.dp, 10.dp),
                modifier = Modifier.background(MainThemeColor.White)
            ) {
                menuItems.forEachIndexed { index, item ->
                    DropdownMenuItem(text = {
                        Text(
                            text = item.text,
                            color = item.textColor,
                            style = item.textStyle
                        )
                    }, onClick = {
                        item.itemOnClick() // 클릭 시 지정된 이벤트 실행

                        selectedText = item.text // 선택된 텍스트로 텍스트 변경
                        expanded = false // 드롭다운 닫기
                    })

                    // 마지막 항목이 아니라면 Divider 추가
                    if (index < menuItems.size - 1) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MainThemeColor.Gray2,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonDropdownMenuPreview() {
    PicplzTheme {
        CommonDropdownMenu(
            initialSelectedText = "초기 텍스트", // 초기 텍스트를 전달,
            triggerButton = { selectedText ->
                Text(text = selectedText) // selectedText 값으로 버튼 텍스트 동기화
            },
            menuItems = listOf(
                DropdownMenuItemData("추천순", MainThemeColor.Gray5),
                DropdownMenuItemData("최신순", MainThemeColor.Gray5),
                DropdownMenuItemData("대표이미지 설정", MainThemeColor.Olive)
            )
        )
    }
}
