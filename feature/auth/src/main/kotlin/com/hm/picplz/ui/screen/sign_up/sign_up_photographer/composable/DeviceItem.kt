package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.domain.model.Device
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography
import java.util.UUID

@Composable
fun DeviceItem(
    device: Device,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(MainThemeColor.Gray1)
                .border(
                    width = 1.dp,
                    color = MainThemeColor.Gray2,
                    shape = RoundedCornerShape(5.dp),
                )
                .padding(horizontal = 15.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = device.companyName,
                style = pretendardTypography.bodyMedium,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text =
                    when (device) {
                        is Device.PhoneDevice -> device.modelName ?: "모델명 없음"
                        is Device.CameraDevice -> "${device.modelName ?: "모델명 없음"} (${device.cameraType})"
                    },
                style = MainFontFamily.bodyBold,
                color = MainThemeColor.Gray5,
            )
        }
        Image(
            painter = painterResource(id = R.drawable.close_circle),
            contentDescription = "삭제",
            modifier =
                Modifier
                    .size(20.dp)
                    .clickable { onRemove() },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceItemPreview() {
    PicplzTheme {
        Column {
            DeviceItem(
                device =
                    Device.PhoneDevice(
                        id = UUID.randomUUID().toString(),
                        companyName = "애플",
                        modelName = "아이폰 16 Pro",
                    ),
                onRemove = {},
            )
            DeviceItem(
                device =
                    Device.CameraDevice(
                        id = UUID.randomUUID().toString(),
                        companyName = "소니",
                        modelName = "a7m3",
                        cameraType = "DSLR 카메라",
                    ),
                onRemove = {},
            )
        }
    }
}
