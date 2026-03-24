package com.hm.picplz.ui.screen.detail_photographer.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.core.ui.R
import com.hm.picplz.feature.photographer.R as PhotographerR

@Composable
fun FullScreenPhotoView(
    imageUri: String,
    onDismiss: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .systemBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUri),
            contentDescription = stringResource(PhotographerR.string.full_screen_photo),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )

        Icon(
            painter = painterResource(id = R.drawable.triangle_left),
            contentDescription = stringResource(PhotographerR.string.back),
            tint = Color.White,
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .size(24.dp)
                    .clickable { onDismiss() },
        )
    }
}
