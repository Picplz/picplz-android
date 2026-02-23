package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
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
        sheetContent = {
            Column(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .padding(bottom = if (navigationBarPadding) totalBottomPadding else 0.dp),
            ) {
                sheetContent()
            }
        },
        sheetPeekHeight =
            sheetPeekHeight
                ?: (30.dp + if (navigationBarPadding) totalBottomPadding else 0.dp),
        sheetShape = sheetShape,
        content = content,
    )
}
