package com.acon.acon.feature.spot.screen.spotdetail.composable

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.SignatureMenu
import com.acon.acon.feature.spot.toPriceString

@Composable
internal fun SignatureMenu(
    signatureMenuList: List<SignatureMenu>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        signatureMenuList.fastForEach { menu ->
            SignatureMenuItem(
                menuName = menu.name,
                menuPrice = menu.price.toPriceString()
            )
        }
    }
}

@Composable
internal fun SignatureMenuItem(
    menuName: String,
    menuPrice: String
) {
    val displayMenuName = if (menuName.length > 8) { menuName.take(7) + stringResource(R.string.ellipsis) } else { menuName }

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
            fontWeight = FontWeight.SemiBold
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
            SignatureMenu(
                signatureMenuList = emptyList()
            )
        }
    }
}