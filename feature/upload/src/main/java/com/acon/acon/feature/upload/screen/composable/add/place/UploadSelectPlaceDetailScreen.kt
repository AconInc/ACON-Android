package com.acon.acon.feature.upload.screen.composable.add.place

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.upload.screen.composable.add.UploadPlaceSelectItem
import com.acon.acon.feature.upload.screen.composable.type.UploadPlaceType
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun UploadSelectPlaceDetailScreen(

) {
    // TODO - 임시변수
    // TODO - UploadPlaceType -> 카페 / 식당 선택
    val uploadPlaceType: UploadPlaceType = UploadPlaceType.CAFE

    val placeTitleList = persistentListOf(
        R.string.korean,
        R.string.chinese,
        R.string.japanese,
        R.string.asian,
        R.string.western,
        R.string.fusion,
        R.string.drink_bar,
        R.string.korean_street,
        R.string.buffet,
        R.string.reason_other
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray900)
            .padding(horizontal = 16.dp)
    ) {
        when (uploadPlaceType) {
            UploadPlaceType.RESTAURANT -> {
                Text(
                    text = stringResource(R.string.required_field),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.Danger,
                    modifier = Modifier.padding(top = 40.dp)
                )

                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.upload_place_select_restaurant_title),
                    style = AconTheme.typography.Headline3,
                    color = AconTheme.color.White
                )

                Spacer(Modifier.height(32.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    placeTitleList.chunked(2).fastForEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pair.forEach { stringResId ->
                                UploadPlaceSelectItem(
                                    title = stringResource(id = stringResId),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            UploadPlaceType.CAFE -> {
                Text(
                    text = stringResource(R.string.required_field),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.Danger,
                    modifier = Modifier.padding(top = 40.dp)
                )

                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.upload_place_select_restaurant_cafe),
                    style = AconTheme.typography.Headline3,
                    color = AconTheme.color.White
                )

                Spacer(Modifier.height(66.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    UploadPlaceSelectItem(
                        title = stringResource(R.string.upload_place_select_cafe_option_1),
                        isSelected = true
                    )
                    UploadPlaceSelectItem(
                        title = stringResource(R.string.upload_place_select_cafe_option_2)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun UploadSelectPlaceDetailScreenPreview() {
    AconTheme {
        UploadSelectPlaceDetailScreen()
    }
}