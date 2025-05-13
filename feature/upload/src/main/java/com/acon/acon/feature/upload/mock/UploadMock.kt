package com.acon.acon.feature.upload.mock

import com.acon.acon.feature.upload.v2.UploadSearchUiState

internal val uploadSearchUiStateMock = UploadSearchUiState.Success(
    recommends = listOf(
        "버거킹",
        "맘스터치",
        "롯데리아",
        "KFC",
        "맥도날드",
    ),
    query = "",
    searchedSpots = listOf()
)