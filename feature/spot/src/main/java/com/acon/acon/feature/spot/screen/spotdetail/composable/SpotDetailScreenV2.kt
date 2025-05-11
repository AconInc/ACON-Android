package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.SignatureMenu
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.StoreChip
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.StoreDetailButtonSet
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.StoreImageIndicator
import com.acon.feature.common.compose.getTextSizeDp

@Composable
fun SpotDetailScreenV2(

) {
    val pageCount = 7
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 7 }
    )
    val indicatorScrollState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // TODO - 임시 코드 (서버에서 이미지 받아야 함)
        if (false) {
            HorizontalPager(
                state = pagerState,
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    //contentScale = ContentScale.FillBounds
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = AconTheme.color.GlassWhiteDefault
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.acon_line),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "장소 이미지를\n준비하고 있어요",
                        color = AconTheme.color.Gray200,
                        style = AconTheme.typography.body1_15_reg,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(20.dp))
            AconTopBar(
                modifier = Modifier.padding(vertical = 16.dp),
                paddingValues = PaddingValues(0.dp),
                leadingIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_28),
                            contentDescription = "뒤로가기",
                            tint = AconTheme.color.White
                        )
                    }
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "장소명",
                    style = AconTheme.typography.Title4,
                    color = AconTheme.color.White,
                )

                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.acon_line),
                    contentDescription = stringResource(R.string.dotori_count_content_description),
                    tint = AconTheme.color.Gray50
                )
                Text(
                    text = "+8888", //spot.dotori
                    style = AconTheme.typography.Body1,
                    fontWeight = FontWeight.W400,
                    color = AconTheme.color.White,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .width(
                            getTextSizeDp("+9999", AconTheme.typography.Body1).width
                        ),
                    textAlign = TextAlign.End
                )
            }

            Spacer(Modifier.height(8.dp))
            StoreChip(
                modifier = Modifier.padding(start = 20.dp)
            )

            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "대표메뉴",
                        color = AconTheme.color.White,
                        style = AconTheme.typography.Title4,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(Modifier.height(12.dp))
                    SignatureMenu()
                }

                Spacer(modifier = Modifier.weight(1f))
                StoreDetailButtonSet(
                    onClickMenuBoard = {},
                    onClickShare = {},
                    onClickMoreOptions = {},
                    isMenuBoarEnabled = false
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                StoreImageIndicator(
                    pageCount = pageCount,
                    pagerState = pagerState,
                    indicatorScrollState = indicatorScrollState,
                    modifier = Modifier
                        .width(66.dp)
                        .padding(top = 16.dp, bottom = 12.dp)
                )
            }

            AconFilledButton(
                onClick = {}, // TODO - 클릭 시, 지도로 가게 주소로 길찾기
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "도보 00분 길찾기", // TODO - 보행자 도보로 걸리는 시간 표시 해야 함
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Title4
                )
            }
        }
    }
}

@Preview
@Composable
private fun SpotDetailScreenV2Preview() {
    AconTheme {
        Box(
            modifier = Modifier.background(AconTheme.color.Black)
        ) {
            SpotDetailScreenV2()
        }
    }
}