package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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

private val dragHandleHeight = 33.dp // top 10 + handle 4 + bottom 19

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CommonBottomSheetScaffold(
    modifier: Modifier = Modifier,
    sheetContent: @Composable ColumnScope.() -> Unit,
    sheetPeekHeight: Dp? = 30.dp,
    sheetMaxHeight: Dp? = null,
    sheetShape: Shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    navigationBarPadding: Boolean = false,
    scaffoldState: BottomSheetScaffoldState,
    content: @Composable (PaddingValues) -> Unit,
) {
    val appBottomNavHeight = LocalNavigationHeight.current
    val bottomNavOffset = if (navigationBarPadding) appBottomNavHeight else 0.dp

    val contentMaxHeightModifier =
        sheetMaxHeight?.let {
            Modifier.heightIn(max = it - dragHandleHeight)
        } ?: Modifier

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
                    contentMaxHeightModifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = bottomNavOffset),
            ) {
                sheetContent()
            }
        },
        sheetPeekHeight = (sheetPeekHeight ?: 30.dp) + bottomNavOffset,
        sheetShape = sheetShape,
        content = content,
    )
}
