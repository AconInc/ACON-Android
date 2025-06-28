package com.acon.acon.feature.upload.mock

import com.acon.acon.core.model.model.upload.UploadSpotSuggestion
import com.acon.acon.core.model.model.upload.SearchedSpot
import com.acon.acon.core.model.type.SpotType
import com.acon.acon.feature.upload.screen.UploadSearchUiState

internal val uploadSearchUiStateMock = UploadSearchUiState.Success(
    uploadSpotSuggestions = listOf(
        com.acon.acon.core.model.model.upload.UploadSpotSuggestion(1, "디버그1"),
        com.acon.acon.core.model.model.upload.UploadSpotSuggestion(2, "TheBug2"),
        com.acon.acon.core.model.model.upload.UploadSpotSuggestion(3, "Debug3"),
        com.acon.acon.core.model.model.upload.UploadSpotSuggestion(4, "디벅4"),
        com.acon.acon.core.model.model.upload.UploadSpotSuggestion(5, "디Bug5"),
    ),
    searchedSpots = listOf(
        com.acon.acon.core.model.model.upload.SearchedSpot(
            spotId = 1L,
            name = "BHC 치킨",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = com.acon.acon.core.model.type.SpotType.RESTAURANT,
        ),
        com.acon.acon.core.model.model.upload.SearchedSpot(
            spotId = 2L,
            name = "BBQ",
            address = "서울특별시 강남구 삼성동 123-4567",
            spotType = com.acon.acon.core.model.type.SpotType.RESTAURANT,
        ),
        com.acon.acon.core.model.model.upload.SearchedSpot(
            spotId = 3L,
            name = "아라치",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = com.acon.acon.core.model.type.SpotType.RESTAURANT,
        ),
        com.acon.acon.core.model.model.upload.SearchedSpot(
            spotId = 4L,
            name = "교촌치킨",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = com.acon.acon.core.model.type.SpotType.RESTAURANT,
        ),
        com.acon.acon.core.model.model.upload.SearchedSpot(
            spotId = 5L,
            name = "처갓집",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = com.acon.acon.core.model.type.SpotType.RESTAURANT,
        ),
        com.acon.acon.core.model.model.upload.SearchedSpot(
            spotId = 6L,
            name = "굽네치킨",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = com.acon.acon.core.model.type.SpotType.RESTAURANT,
        ),
        com.acon.acon.core.model.model.upload.SearchedSpot(
            spotId = 7L,
            name = "네네치킨",
            address = "서울특별시 강남구 삼성동 123-45",
            spotType = com.acon.acon.core.model.type.SpotType.RESTAURANT,
        ),
    )
)