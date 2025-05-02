package com.hm.picplz.ui.screen.chat.composable.bubble

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ImageChat(
    modifier: Modifier = Modifier,
    imageUris: List<String>
) {

    when {
        imageUris.isEmpty() -> {}
        imageUris.size == 1 -> {
            SingleImageChat(
                modifier = modifier,
                imageUri = imageUris.first(),
            )
        }
        imageUris.size == 2 -> {
            DoubleImageChat(
                modifier = modifier,
                imageUris = imageUris,
            )
        }
        else -> {
            GridImageChat(
                modifier = modifier,
                imageUris = imageUris,
            )
        }
    }
}

@Composable
private fun SingleImageChat(
    imageUri: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(200.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build()
        )

        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center),
                    color = MainThemeColor.Gray5,
                    strokeWidth = 2.dp
                )
            }
            is AsyncImagePainter.State.Error -> {}
            else -> {}
        }
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "채팅 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
        )
    }
}


@Composable
fun DoubleImageChat(
    modifier: Modifier,
    imageUris: List<String>
) {
    Row(
        modifier = modifier
            .width(200.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        imageUris.forEach { imageUri ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build()
                )

                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(10.dp)
                                .align(Alignment.Center),
                            color = MainThemeColor.Gray5,
                            strokeWidth = 2.dp
                        )
                    }
                    is AsyncImagePainter.State.Error -> {}
                    else -> {}
                }

                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "채팅 이미지",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Composable
fun GridImageChat(
    modifier: Modifier,
    imageUris: List<String>
) {
    val imageRows = (imageUris.size + 2) / 3
    Column(
        modifier = modifier
            .width(200.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        for (imageRow in 0 until imageRows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                for (col in 0 until 3) {
                    val index = imageRow * 3 + col
                    if (index < imageUris.size) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUris[index])
                                    .crossfade(true)
                                    .build()
                            )

                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .align(Alignment.Center),
                                        color = MainThemeColor.Gray5,
                                        strokeWidth = 2.dp
                                    )
                                }
                                is AsyncImagePainter.State.Error -> {}
                                else -> {}
                            }

                            Image(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp)),
                                painter = rememberAsyncImagePainter(imageUris[index]),
                                contentDescription = "전송된 이미지",
                                contentScale = ContentScale.Crop,
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleImageChatPreview() {
    PicplzTheme {
        ImageChat(
            imageUris = listOf(
                "https://picsum.photos/id/243/300/300",
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DoubleImageChatPreview() {
    PicplzTheme {
        ImageChat(
            imageUris = listOf(
                "https://picsum.photos/id/243/300/300",
                "https://picsum.photos/id/243/300/300",
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GridImageChatPreview() {
    PicplzTheme {
        ImageChat(
            imageUris = listOf(
                "https://picsum.photos/id/243/300/300",
                "https://picsum.photos/id/243/300/300",
                "https://picsum.photos/id/243/300/300",
                "https://picsum.photos/id/243/300/300",
                "https://picsum.photos/id/243/300/300",
                "https://picsum.photos/id/243/300/300",
                "https://picsum.photos/id/243/300/300",
            )
        )
    }
}