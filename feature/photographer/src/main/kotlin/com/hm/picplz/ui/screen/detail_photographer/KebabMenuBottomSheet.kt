package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.CommonModalBottomSheet
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.feature.photographer.R as PhotographerR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KebabMenuBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onBlock: () -> Unit,
    onReport: () -> Unit,
) {
    CommonModalBottomSheet(
        visible = visible,
        visibleCloseButton = false,
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetMaxHeight = 160.dp,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onBlock()
                            onDismiss()
                        },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_block),
                    contentDescription = stringResource(PhotographerR.string.menu_block),
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(9.dp))
                Text(
                    text = stringResource(PhotographerR.string.menu_block),
                    style = MainThemeFont.BodyLarge,
                    color = MainThemeColor.Black,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onReport()
                            onDismiss()
                        },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_report),
                    contentDescription = stringResource(PhotographerR.string.menu_report),
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(9.dp))
                Text(
                    text = stringResource(PhotographerR.string.menu_report),
                    style = MainThemeFont.BodyLarge,
                    color = MainThemeColor.Black,
                )
            }
        }
    }
}
