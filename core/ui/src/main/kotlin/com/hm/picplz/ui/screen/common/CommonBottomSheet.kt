package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.LocalNavigationHeight
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CommonBottomSheetScaffold(
    modifier: Modifier = Modifier,
    sheetContent: @Composable ColumnScope.() -> Unit,
    sheetPeekHeight: Dp? = 30.dp,
    sheetShape: Shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    navigationBarPadding: Boolean = false,
    scaffoldState: BottomSheetScaffoldState,
    content: @Composable (PaddingValues) -> Unit,
) {
    val systemNavBarHeight =
        WindowInsets.navigationBars
            .asPaddingValues()
            .calculateBottomPadding()

    val appBottomNavHeight = LocalNavigationHeight.current

    val totalBottomPadding =
        systemNavBarHeight +
            (if (navigationBarPadding) appBottomNavHeight else 0.dp)

    BottomSheetScaffold(
        modifier =
            modifier
                .safeDrawingPadding(),
        scaffoldState = scaffoldState,
        sheetContainerColor = MainThemeColor.White,
        sheetDragHandle = {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 19.dp),
                contentAlignment = Alignment.Center,
            ) {
                Spacer(
                    modifier =
                        Modifier
                            .width(32.dp)
                            .height(4.dp)
                            .background(
                                color = MainThemeColor.Gray2,
                                shape = RoundedCornerShape(2.dp),
                            ),
                )
            }
        },
        sheetContent = {
            Column(
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = if (navigationBarPadding) totalBottomPadding else 0.dp),
            ) {
                sheetContent()
            }
        },
        sheetPeekHeight =
            (sheetPeekHeight ?: 30.dp) +
                (if (navigationBarPadding) totalBottomPadding else 0.dp),
        sheetShape = sheetShape,
        content = content,
    )
}
