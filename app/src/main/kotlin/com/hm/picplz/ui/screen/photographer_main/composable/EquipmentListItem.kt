package com.hm.picplz.ui.screen.photographer_main.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.common.CommonToggleSwitch
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun EquipmentListItem(
    modifier: Modifier = Modifier,
    equipmentType: String,
    deviceName: String,
    isEnabled: Boolean = true,
    onEnabledChanged: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box (
            modifier = Modifier
                .width(60.dp)
        ){
            Text(
                text = equipmentType,
                modifier = Modifier.padding(end = 8.dp),
                style = typography.bodyLarge
            )
        }
        Box (
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = deviceName,
                style = typography.bodyMedium,
                color = MainThemeColor.Gray5
            )
        }

        CommonToggleSwitch(
            checked = isEnabled,
            onCheckedChange = onEnabledChanged
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EquipmentListItemPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        EquipmentListItem(
            equipmentType = "내 폰",
            deviceName = "아이폰 16",        )
    }
}
