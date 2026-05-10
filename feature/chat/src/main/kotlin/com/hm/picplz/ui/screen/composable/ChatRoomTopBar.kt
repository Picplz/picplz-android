package com.hm.picplz.ui.screen.composable

import androidx.compose.runtime.Composable
import com.hm.picplz.ui.screen.common.CommonTopBarWithMenu
import com.hm.picplz.ui.screen.common.CommonTopBarWithSubtitle
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun ChatRoomTopBar(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    if (subtitle.isEmpty()) {
        CommonTopBarWithMenu(
            text = title,
            onClickBack = onBackClick,
            onClickMenu = onMenuClick,
        )
    } else {
        CommonTopBarWithSubtitle(
            text = title,
            subText = subtitle,
            subTextStyle = caption.copy(color = MainThemeColor.Green120),
            onClickBack = onBackClick,
            onClickMenu = onMenuClick,
        )
    }
}
