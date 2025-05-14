package com.acon.acon.feature.upload.mock

import com.acon.acon.domain.model.upload.v2.SearchedSpot
import com.acon.acon.domain.type.SpotType
import com.acon.acon.feature.upload.v2.UploadSearchUiState

internal val uploadSearchUiStateMock = UploadSearchUiState.Success(
    suggestions = listOf(
        "버거킹",
        "맘스터치",
        "롯데리아",
        "KFC",
        "맥도날드",
    ),
    query = "",
    searchedSpots = listOf(
        SearchedSpot(
            spotId = 1L,
            name = "BHC 치킨",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = SpotType.RESTAURANT,
        ),
        SearchedSpot(
            spotId = 2L,
            name = "BBQ",
            address = "서울특별시 강남구 삼성동 123-4567",
            spotType = SpotType.RESTAURANT,
        ),
        SearchedSpot(
            spotId = 3L,
            name = "아라치",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = SpotType.RESTAURANT,
        ),
        SearchedSpot(
            spotId = 4L,
            name = "교촌치킨",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = SpotType.RESTAURANT,
        ),
        SearchedSpot(
            spotId = 5L,
            name = "처갓집",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = SpotType.RESTAURANT,
        ),
        SearchedSpot(
            spotId = 6L,
            name = "굽네치킨",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = SpotType.RESTAURANT,
        ),
        SearchedSpot(
            spotId = 7L,
            name = "네네치킨",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = SpotType.RESTAURANT,
        ),
    )
)