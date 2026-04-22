package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

private object MyPagePhotoPermissionScreenDefaults {
    val HorizontalPadding = 24.dp
    val TitleDescriptionSpacing = 20.dp
    val DescriptionButtonSpacing = 32.dp
    val ButtonWidth = 256.dp
    val ButtonHeight = 50.dp
    val ButtonCornerRadius = 5.dp
    val ButtonVerticalPadding = 14.dp
    val ButtonHorizontalPadding = 20.dp
}

@Composable
fun MyPagePhotoPermissionScreen(
    modifier: Modifier = Modifier,
    actionLabel: String,
    onActionClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = MyPagePhotoPermissionScreenDefaults.HorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.modify_profile_photo_permission_title),
                style = MainThemeFont.Title,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(MyPagePhotoPermissionScreenDefaults.TitleDescriptionSpacing))
            Text(
                text = stringResource(R.string.modify_profile_photo_permission_description),
                style = MainThemeFont.BodyLarge,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(MyPagePhotoPermissionScreenDefaults.DescriptionButtonSpacing))
            PhotoPermissionActionButton(
                text = actionLabel,
                onClick = onActionClick,
                modifier =
                    Modifier
                        .width(MyPagePhotoPermissionScreenDefaults.ButtonWidth)
                        .height(MyPagePhotoPermissionScreenDefaults.ButtonHeight),
            )
        }
    }
}

@Composable
private fun PhotoPermissionActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MainThemeColor.Black,
                contentColor = MainThemeColor.White,
            ),
        shape = RoundedCornerShape(MyPagePhotoPermissionScreenDefaults.ButtonCornerRadius),
        contentPadding =
            PaddingValues(
                vertical = MyPagePhotoPermissionScreenDefaults.ButtonVerticalPadding,
                horizontal = MyPagePhotoPermissionScreenDefaults.ButtonHorizontalPadding,
            ),
    ) {
        Text(text = text)
    }
}
