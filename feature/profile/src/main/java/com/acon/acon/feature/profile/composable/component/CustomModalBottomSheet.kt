package com.acon.acon.feature.profile.composable.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomModalBottomSheet(
    modifier : Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onGallerySelect: () -> Unit = {},
    onDefaultImageSelect: () -> Unit = {}
){
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AconTheme.color.Gray8,
        contentColor = AconTheme.color.White,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 16.dp).clickable { onGallerySelect() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.and_ic_gallery_w_28),
                    contentDescription = "onGallerySelect Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(end = 28.dp)
                )
                Text(
                    text = "앨범에서 사진 선택",
                    style = AconTheme.typography.subtitle1_16_med,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 16.dp).clickable { onDefaultImageSelect() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.and_ic_basic_profile_w_28),
                    contentDescription = "onGallerySelect Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(end = 28.dp)
                )
                Text(
                    text = "기본 이미지로 변경",
                    style = AconTheme.typography.subtitle1_16_med,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}