package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.core.ui.R
import com.hm.picplz.domain.model.ShootingPackage
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import java.text.NumberFormat
import java.util.Locale

private val labelStyle =
    MainThemeFont.Caption.copy(fontWeight = FontWeight.SemiBold)

@Composable
fun ShootingPackageSection(
    modifier: Modifier,
    packages: List<ShootingPackage>,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.shooting_package),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
        )

        Spacer(modifier = Modifier.height(16.dp))

        packages.forEachIndexed { index, pkg ->
            ShootingPackageCard(pkg = pkg)
            if (index != packages.lastIndex) {
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = MainThemeColor.Gray1,
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ShootingPackageCard(pkg: ShootingPackage) {
    val formattedPrice =
        remember(pkg.price) {
            NumberFormat.getNumberInstance(Locale.KOREA).format(pkg.price)
        }

    Column(modifier = Modifier.fillMaxWidth()) {
        // 사진 — 높이 160dp
        Image(
            painter = rememberAsyncImagePainter(model = pkg.imageUri),
            contentDescription = pkg.title,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 패키지 제목 — '제목' (Bold 20sp)
        Text(
            text = pkg.title,
            style = MainThemeFont.Title,
            color = MainThemeColor.Black,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 가격 — '작은 제목' (SemiBold 18sp)
        Text(
            text = stringResource(R.string.price_won_format, formattedPrice),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 촬영 시간
        Row {
            Text(
                text = stringResource(R.string.shooting_time_label),
                style = labelStyle,
                color = MainThemeColor.Gray6,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = pkg.shootingTime,
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 기타 안내
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.package_description_label),
                style = labelStyle,
                color = MainThemeColor.Gray6,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = pkg.description,
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
