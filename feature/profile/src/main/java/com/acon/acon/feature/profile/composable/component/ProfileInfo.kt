package com.acon.acon.feature.profile.composable.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.composable.type.ProfileInfoType
import com.acon.acon.feature.profile.R

@Composable
fun ProfileInfo(
    profileInfoType: ProfileInfoType,
    modifier: Modifier = Modifier,
    aconCount: String = "",
    area: String = "",
) {
    val title = if(profileInfoType == ProfileInfoType.ACON) {
        stringResource(R.string.profile_info_your_acon_count)
    } else {
        stringResource(R.string.profile_info_my_verified_neighborhood)
    }

    val contentDescription = if(profileInfoType == ProfileInfoType.ACON) {
        stringResource(R.string.content_description_your_acon_count)
    } else {
        stringResource(R.string.content_description_my_verified_neighborhood)
    }

    val image = if(profileInfoType == ProfileInfoType.ACON) {
        com.acon.acon.core.designsystem.R.drawable.ic_profile_acon_g_20
    } else {
        com.acon.acon.core.designsystem.R.drawable.ic_hometown_acon_g_20
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color = AconTheme.color.Gray8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = ImageVector.vectorResource(image),
                contentDescription = contentDescription
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = title,
                style = AconTheme.typography.subtitle2_14_med,
                color = AconTheme.color.Gray2
            )
        }
        Spacer(Modifier.height(10.dp))
        if(profileInfoType == ProfileInfoType.ACON) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = aconCount,
                    style = AconTheme.typography.title2_20_b,
                    color = AconTheme.color.Main_org1
                )
                Text(
                    text = stringResource(R.string.profile_info_max_acon_count),
                    style = AconTheme.typography.subtitle2_14_med,
                    color = AconTheme.color.Gray5
                )
            }
        } else {
            Text(
                text = area,
                style = AconTheme.typography.title2_20_b,
                color = if(area == stringResource(R.string.profile_info_not_verified)) AconTheme.color.Gray5 else AconTheme.color.Main_org1 ,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AreaProfileInfoPreview() {
    AconTheme {
        ProfileInfo(
            profileInfoType = ProfileInfoType.AREA,
            aconCount = "15",
            area = "망원동",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun AconProfileInfoPreview() {
    AconTheme {
        ProfileInfo(
            profileInfoType = ProfileInfoType.ACON,
            aconCount = "15",
            area = "망원동",
            modifier = Modifier.fillMaxWidth()
        )
    }
}