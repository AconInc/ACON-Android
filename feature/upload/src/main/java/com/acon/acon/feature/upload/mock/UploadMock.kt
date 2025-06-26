package com.acon.acon.feature.upload.mock

import com.acon.core.model.upload.UploadSpotSuggestion
import com.acon.core.model.upload.SearchedSpot
import com.acon.core.type.SpotType
import com.acon.acon.feature.upload.screen.UploadSearchUiState

internal val uploadSearchUiStateMock = UploadSearchUiState.Success(
    uploadSpotSuggestions = listOf(
        UploadSpotSuggestion(1, "디버그1"),
        UploadSpotSuggestion(2, "TheBug2"),
        UploadSpotSuggestion(3, "Debug3"),
        UploadSpotSuggestion(4, "디벅4"),
        UploadSpotSuggestion(5, "디Bug5"),
    ),
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