package com.hm.picplz.ui.screen.common.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.CommonStatusText
import com.hm.picplz.ui.screen.common.PhotographerStatus
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun PhotographerCard(
    modifier: Modifier = Modifier,
    profileImage: Int = R.drawable.default_profile,
    username: String = "",
    status: PhotographerStatus = PhotographerStatus.ENABLED,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(74.dp)
            .clickable { onClick() }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = profileImage),
                contentDescription = "profile-image",
                modifier = Modifier
                    .size(74.dp)
                    .clip(CircleShape)
                    .border(1.dp, MainThemeColor.Black, CircleShape),
            )
            Spacer(modifier = Modifier.height(9.58.dp))
            Text(text = username)
            CommonStatusText(type = status)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotographerCardPreview() {
    PicplzTheme {
        PhotographerCard(
            profileImage = R.drawable.user_undefined,
            username = "유가영",
            status = PhotographerStatus.ENABLED,
            onClick = {}
        )
    }
}
