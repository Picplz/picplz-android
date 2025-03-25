package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.utils.CommonUtil

/**
 * ModalBottomSheet를 래핑한 컴포저블 함수로, 모달 형태의 바텀시트.
 * CommonBottomSheet와의 차이점은 활성화된 경우, 모달이기 때문에 다른 백그라운드의 UI와의 상호작용을 차단한다는 점.
 *
 * @param modifier [Modifier] : 이 컴포넌트에 적용할 Modifier
 * @param onDismissRequest [() -> Unit] : 바텀시트를 닫을 때 호출되는 콜백
 * @param sheetState [SheetState] : 바텀시트의 상태를 관리하는 객체
 * @param sheetMaxWidth [Dp] : 바텀시트의 최대 너비. 기본값은 BottomSheetDefaults.SheetMaxWidth
 * @param shape [Shape] : 바텀시트의 모서리 둥글기. 기본값은 RoundedCornerShape(20.dp, 20.dp)
 * @param containerColor [Color] : 바텀시트의 배경색. 기본값은 MainThemeColor.White
 * @param tonalElevation [Dp] : 바텀시트의 음영 깊이. 기본값은 BottomSheetDefaults.Elevation
 * @param scrimColor [Color] : 바텀시트를 뒤에 나타나는 배경색. 기본값은 MainThemeColor.Black.copy(alpha = 0.4f)
 * @param dragHandle [@Composable (() -> Unit)?] : 바텀시트의 드래그 핸들 조작 가능 프로퍼티
 * @param sheetMinHeight [Dp] : 시트 최소 높이
 * @param sheetMaxHeight [Dp] : 시트 최대 높이
 * @param windowInsets [WindowInsets] : 시스템 창 인셋
 * @param properties [ModalBottomSheetProperties] : 바텀시트의 다양한 속성. 기본값은 ModalBottomSheetDefaults.properties()
 * @param content [@Composable ColumnScope.() -> Unit] : 바텀시트 내에 표시할 콘텐츠
 * */

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CommonModalBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    visible: Boolean = true,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = RoundedCornerShape(20.dp, 20.dp),
    containerColor: Color = MainThemeColor.White,
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = MainThemeColor.Black.copy(alpha = 0.4f),
    dragHandle: @Composable (() -> Unit)? = { CustomDragHandle() },
    sheetMinHeight: Dp? = 0.dp,
    sheetMaxHeight: Dp? = Dp.Infinity,
    windowInsets
    : WindowInsets = BottomSheetDefaults.windowInsets,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return available.copy(x = 0f, y = available.y.coerceAtLeast(0f))
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                return available.copy(x = 0f, y = available.y.coerceAtLeast(0f))
            }
        }
    }

    if (visible) {
        ModalBottomSheet(
            modifier = modifier.safeDrawingPadding(),
            containerColor = containerColor,
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            sheetMaxWidth = sheetMaxWidth,
            shape = shape,
            tonalElevation = tonalElevation,
            scrimColor = scrimColor,
            dragHandle = dragHandle,
            windowInsets = windowInsets,
            properties = properties,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .nestedScroll(connection)
            ) {
                Column(
                    modifier = CommonUtil
                        .paddingModifier
                        .heightIn(
                            min = sheetMinHeight ?: 0.dp,
                            max = sheetMaxHeight ?: Dp.Infinity
                        )
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    content()
                }
            }
        }
    }
}

/**
 * 바텀시트의 커스텀 드래그 핸들 컴포넌트(Default)
 */
@Composable
fun CustomDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp),
            shape = RoundedCornerShape(6.dp),
            color = MainThemeColor.Black
        ) {}
    }
}