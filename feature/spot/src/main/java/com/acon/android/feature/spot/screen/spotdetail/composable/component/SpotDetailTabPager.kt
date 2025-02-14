package com.acon.android.feature.spot.screen.spotdetail.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.android.core.designsystem.theme.AconTheme
import com.acon.android.domain.model.spot.SpotDetailMenu
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun SpotDetailTabPager(
    pagerState: PagerState,
    tabs: ImmutableList<String>,
    tabContents: List<SpotDetailMenu>
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray9)
            .padding(top = 200.dp),
    ) {
        TabRow(
            modifier = Modifier
                .background(AconTheme.color.Gray9)
                .align(Alignment.Start),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .width(tabPositions[pagerState.currentPage].width),
                    height = 2.dp,
                    color = AconTheme.color.White
                )
            },
            selectedTabIndex = 0,
            divider = {},
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    modifier = if (tabs.size == 1) {
                        Modifier
                            .background(AconTheme.color.Gray9)
                    } else {
                        Modifier
                            .weight(1f)
                            .background(AconTheme.color.Gray9)
                    },
                    text = {
                        Text(
                            text = title,
                            style = AconTheme.typography.subtitle2_14_med,
                            color = AconTheme.color.White
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    enabled = true
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(AconTheme.color.Gray9),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 17.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(tabContents) { menu ->
                    MenuItem(menu = menu)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SpotDetailTabPagerPreview() {
    val tabs = persistentListOf("메뉴","","","","")
    val tabContents = listOf(
        SpotDetailMenu(1, "Americano", 4000, ""),
        SpotDetailMenu(2, "Latte", 4500, "")
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })

    AconTheme {
        SpotDetailTabPager(
            pagerState = pagerState,
            tabs = tabs,
            tabContents = tabContents
        )
    }
}