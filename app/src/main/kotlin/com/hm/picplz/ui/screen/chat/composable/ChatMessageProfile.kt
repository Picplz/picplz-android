package com.hm.picplz.ui.screen.chat.composable

import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hm.picplz.R
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChatMessageProfile(
    modifier: Modifier = Modifier,
    profileImageUri: Uri?,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(profileImageUri ?: R.drawable.active_dot)
            .crossfade(true)
            .placeholder(R.drawable.active_dot)
            .error(R.drawable.active_dot)
            .build(),
        contentDescription = "메세지 프로필",
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true)
@Composable
fun ChatMessageProfilePreview() {
    PicplzTheme {
        ChatMessageProfile(
            profileImageUri = Uri.parse(
                "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large"
            )
        )
    }
}