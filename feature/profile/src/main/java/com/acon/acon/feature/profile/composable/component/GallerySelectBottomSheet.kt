package com.acon.acon.feature.profile.composable.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.AconBottomSheet
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GallerySelectBottomSheet(
    isDefault: Boolean,
    modifier : Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onGallerySelect: () -> Unit = {},
    onDefaultImageSelect: () -> Unit = {}
){
    val padding = if(!isDefault) 75.dp else 115.dp
    AconBottomSheet(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        onGallerySelect()
                        onDismiss()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.upload_photo_from_album),
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 17.dp)
                )
            }
            if(!isDefault) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable {
                            onDefaultImageSelect()
                            onDismiss()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(R.string.set_default_profile_image),
                        color = AconTheme.color.White,
                        style = AconTheme.typography.Title4,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 17.dp)
                    )
                }
            }
        }
    }
}