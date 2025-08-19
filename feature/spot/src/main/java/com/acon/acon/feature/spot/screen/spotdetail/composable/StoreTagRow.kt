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
import com.acon.acon.core.model.type.TagType

@Composable
internal fun StoreTagRow(
    tags: List<com.acon.acon.core.model.type.TagType>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (com.acon.acon.core.model.type.TagType.NEW in tags) {
            AconTag(
                text = stringResource(R.string.store_tag_new),
                backgroundColor = AconTheme.color.TagNew
            )
        }

        if (com.acon.acon.core.model.type.TagType.LOCAL in tags) {
            AconTag(
                text = stringResource(R.string.store_tag_local),
                backgroundColor = AconTheme.color.TagLocal
            )
        }

        val rankingTag = tags.firstOrNull { it.name.startsWith("TOP_") }
        rankingTag?.let {
            val rank = it.name.removePrefix("TOP_").toIntOrNull() ?: return@let
            AconTag(
                text = stringResource(R.string.store_tag_ranking, rank),
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
            tags = emptyList()
        )
    }
}