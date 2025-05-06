package com.acon.acon.feature.spot.mock

import android.location.Location
import com.acon.acon.domain.model.spot.v2.SpotV2
import com.acon.acon.domain.type.SpotType
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.spot.screen.spotlist.SpotListUiStateV2

internal val spotListUiStateRestaurantMock = SpotListUiStateV2.Success(
    spotList = listOf(
        SpotV2(
            id = 1L,
            name = "하이디라오",
            image = "https://media.triple.guide/triple-cms/c_limit%2Cf_auto%2Ch_2048%2Cw_2048/ff96459a-1ebb-48e7-9baf-b68911d6347e.jpeg",
            dotori = "+9999",
            walkingTime = "도보 10분",
            latitude = 37.477576,
            longitude = 126.889057
        ),
        SpotV2(
            id = 2L,
            name = "샤브올데이",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNTA1MDRfMTA4%2FMDAxNzQ2MzQwNjU0MTYw.aoxFf8q5CLA3DX-h2CA1noUcNy4KdIUue95s0sKjDYYg.k37O5GmawaaHEXR3NpBTOXnfPApZ6yKaDyNLrJ8HjOAg.JPEG%2F20250504_140846.jpg.jpg%3Ftype%3Dw1500_60_sharpen",
            dotori = "320",
            walkingTime = "도보 17분",
            latitude = 37.481291,
            longitude = 126.886514
        ),
        SpotV2(
            id = 3L,
            name = "광주 서울곱창",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20241127_200%2F1732671455823qCmdP_JPEG%2FKakaoTalk_20241122_104136138_02.jpg",
            dotori = "1024",
            walkingTime = "도보 11분",
            latitude = 37.572793,
            longitude = 126.976986
        ),
        SpotV2(
            id = 6L,
            name = "이게 뭘까",
            image = "",
            dotori = "555",
            walkingTime = "도보 1분",
            latitude = 37.572793,
            longitude = 126.976986
        ),
        SpotV2(
            id = 4L,
            name = "더페이머스그릴",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240929_4%2F1727613280951cwtzB_JPEG%2FIMG_8916.jpeg",
            dotori = "+9999",
            walkingTime = "도보 12분",
            latitude = 37.572793,
            longitude = 126.976986
        ),
        SpotV2(
            id = 5L,
            name = "민소푸",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220615_101%2F1655277226221Q2daS_JPEG%2FB4499001-B6CB-4308-9C61-141CE5595A43.jpeg",
            dotori = "+9999",
            walkingTime = "도보 9분",
            latitude = 37.572793,
            longitude = 126.976986
        ),
    ), headTitle = "최고의 선택.",
    selectedSpotType = SpotType.RESTAURANT,
    currentLocation = Location(""),
    userType = UserType.GUEST
)

internal val spotListUiStateCafeMock = SpotListUiStateV2.Success(
    spotList = listOf(
        SpotV2(
            id = 1L,
            name = "카푸치노 아싸씨노",
            image = "https://upload3.inven.co.kr/upload/2025/04/21/bbs/i1370696237.webp?MW=800",
            dotori = "+9999",
            walkingTime = "도보 0분",
            latitude = 37.572793,
            longitude = 126.976986
        ),
        SpotV2(
            id = 2L,
            name = "투썸플레이스",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230313_185%2F1678665285309o3tWN_JPEG%2F20230309_110215.jpg",
            dotori = "+9999",
            walkingTime = "도보 10분",
            latitude = 37.477576,
            longitude = 126.889057
        ),
        SpotV2(
            id = 3L,
            name = "스타벅스",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220510_58%2F1652129129526n5KQ1_JPEG%2F3639_20220509052544_de4cu.jpg",
            dotori = "320",
            walkingTime = "도보 17분",
            latitude = 37.481291,
            longitude = 126.886514
        ),
        SpotV2(
            id = 4L,
            name = "맛맛맛있는 카페",
            image = "",
            dotori = "1024",
            walkingTime = "도보 11분",
            latitude = 37.572793,
            longitude = 126.976986
        ),
        SpotV2(
            id = 5L,
            name = "원두서점",
            image = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNDEyMjhfMjYy%2FMDAxNzM1Mzg3ODQxNzY2.NJGv5aAunk-PSiAnE_ChtiF6yu9lKDq_PBNx2D7RpcAg.yaj2TiG_a3sp2__Oku_PmnW18f3MJt5z1l9zjg8Eotwg.JPEG%2FIMG_0512.jpg",
            dotori = "555",
            walkingTime = "도보 1분",
            latitude = 37.572793,
            longitude = 126.976986
        ),
        SpotV2(
            id = 6L,
            name = "고망고",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220927_189%2F1664259161243IF52z_JPEG%2F20220927_105158.jpg",
            dotori = "+9999",
            walkingTime = "도보 12분",
            latitude = 37.572793,
            longitude = 126.976986
        ),
        SpotV2(
            id = 7L,
            name = "스타벅스2",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220510_58%2F1652129129526n5KQ1_JPEG%2F3639_20220509052544_de4cu.jpg",
            dotori = "320",
            walkingTime = "도보 17분",
            latitude = 37.481291,
            longitude = 126.886514
        ),
        SpotV2(
            id = 8L,
            name = "스타벅스3",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220510_58%2F1652129129526n5KQ1_JPEG%2F3639_20220509052544_de4cu.jpg",
            dotori = "320",
            walkingTime = "도보 17분",
            latitude = 37.481291,
            longitude = 126.886514
        ),
    ), headTitle = "최고의 선택.",
    selectedSpotType = SpotType.CAFE,
    currentLocation = Location(""),
    userType = UserType.GUEST
)