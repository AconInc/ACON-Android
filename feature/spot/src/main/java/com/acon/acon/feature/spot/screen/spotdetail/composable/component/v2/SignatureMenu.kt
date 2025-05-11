package com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun SignatureMenu(
    //menuList: List<Menu> // TODO - (메뉴명, 가격) 리스트
) {
    // TODO - List 되면 LazyColumn 으로 변경
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SignatureMenuItem(
            menuName = "마라크림로제파스타",
            menuPrice = "3,000"
        )

        SignatureMenuItem(
            menuName = "조개찜",
            menuPrice = "88,000"
        )

        SignatureMenuItem(
            menuName = "샌드위치",
            menuPrice = "5,000"
        )
    }
}

@Composable
fun SignatureMenuItem(
    menuName: String,
    menuPrice: String
) {
    val displayMenuName = if (menuName.length > 8) { menuName.take(7) + "…" } else { menuName }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(100.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = displayMenuName,
                color = AconTheme.color.White,
                style = AconTheme.typography.Body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(8.dp))
        Text(
            text = menuPrice,
            color = AconTheme.color.White,
            style = AconTheme.typography.Body1,
        )
    }
}

@Composable
@Preview
private fun SignatureMenuItemPreview() {
    AconTheme {
        Box(
            Modifier.background(AconTheme.color.Black)
        ) {
            SignatureMenuItem(
                menuName = "메뉴명",
                menuPrice = "000,000"
            )
        }
    }
}

@Composable
@Preview
private fun SignatureMenuPreview() {
    Box(
        Modifier.background(AconTheme.color.Black)
    ) {
        AconTheme {
            SignatureMenu()
        }
    }
}