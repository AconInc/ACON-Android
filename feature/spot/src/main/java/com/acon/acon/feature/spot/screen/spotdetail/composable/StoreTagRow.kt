package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.tag.AconTag
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
internal fun StoreTagRow(
    isNew: Boolean,
    isLocal: Boolean,
    isRanking: Boolean,
    rankingNumber: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isNew) {
            AconTag(
                text = stringResource(R.string.store_tag_new),
                backgroundColor = AconTheme.color.TagNew
            )
        }
        if (isLocal) {
            AconTag(
                text = stringResource(R.string.store_tag_local),
                backgroundColor = AconTheme.color.TagLocal
            )
        }
        if (isRanking) {
            AconTag(
                text = stringResource(R.string.store_tag_ranking, rankingNumber),
                backgroundColor = AconTheme.color.Gray900
            )
        }
    }
}

@Preview
@Composable
private fun StoreChipPreview() {
    AconTheme {
        StoreTagRow(
            isNew = true,
            isLocal = true,
            isRanking = true,
            rankingNumber = 1
        )
    }
}