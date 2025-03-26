package com.hm.picplz.ui.screen.common.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.screen.common.CommonScaffold
import com.hm.picplz.ui.theme.MainThemeColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CommonSnackBarHost(
    modifier: Modifier = Modifier, // modifier를 통해 스낵바 위치 조절 가능
    snackBarHostState: SnackbarHostState,
    durationInMillis: Int = 2000, // 지속 시간 (밀리초 단위)
    actionOnNewLine: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(15.dp),
    containerColor: Color = Color(0xFF0E0E0F).copy(alpha = 0.8f),
    contentColor: Color = MainThemeColor.White,
    actionColor: Color = MainThemeColor.Black,
    actionContentColor: Color = MainThemeColor.Black,
    dismissActionContentColor: Color = MainThemeColor.Black,
) {
    val currentStackBarData = snackBarHostState.currentSnackbarData

    LaunchedEffect(currentStackBarData) {
        if (currentStackBarData != null) {
            delay(durationInMillis.toLong())
            currentStackBarData.dismiss()
        }
    }

    SnackbarHost(
        hostState = snackBarHostState,
    ) {
        CommonSnackbar(
            snackbarData = it,
            modifier = modifier,
            actionOnNewLine = actionOnNewLine,
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            actionColor = actionColor,
            actionContentColor = actionContentColor,
            dismissActionContentColor = dismissActionContentColor
        )
    }
}


// ✅ CommonSnackBarHost 사용 예제
@Suppress("unused")
@Composable
fun CommonSnackBarHostExample() {
    // 코루틴 스코프 생성 (SnackbarHostState 사용 시 필요)
    val scope = rememberCoroutineScope()

    // 🔹 SnackbarHostState를 remember로 관리하여 상태 유지
    val snackBarHostState = remember { SnackbarHostState() }

    // CommonScaffold 내부에 CommonSnackBarHost 적용
    CommonScaffold(
        snackbarHost = {
            CommonSnackBarHost(
                modifier = Modifier.padding(bottom = 80.dp), // 하단 패딩 추가 (위치 조절)
                snackBarHostState = snackBarHostState, // 생성한 SnackbarHostState 전달
                durationInMillis = 3000, // 스낵바 지속 시간 (3초)
            )
        }
    ) {
        // 버튼 클릭 시 스낵바 표시
        Button(onClick = {
            scope.launch {
                // 여러 번 클릭 시 기존 스낵바를 닫고 새로운 메시지를 표시하도록 처리
                snackBarHostState.currentSnackbarData?.dismiss()

                // 스낵바 메시지 표시
                snackBarHostState.showSnackbar(
                    message = "원하는 메시지 작성"
                )
            }
        }) {
            Text(text = "스낵바 테스트")
        }
    }
}