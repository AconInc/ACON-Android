package com.acon.acon.feature.profile.composable.screen

import okhttp3.internal.immutableListOf

data class MockSavedSpot(
    val id: Long,
    val image: String?,
    val name: String
)

internal val mockSpotList = immutableListOf(
    MockSavedSpot(
        id = 1,
        image = "https://acon-bucket.s3.ap-northeast-2.amazonaws.com/members/profile-images/e6547003-b4df-42fe-9275-9d9c5a008e79.jpg",
        name = "카페 브리즈"
    ),
    MockSavedSpot(
        id = 2,
        image = "",
        name = "서울 맛집"
    ),
    MockSavedSpot(
        id = 3,
        image = "https://acon-bucket.s3.ap-northeast-2.amazonaws.com/members/profile-images/e6547003-b4df-42fe-9275-9d9c5a008e79.jpg",
        name = "초밥 천국"
    ),
    MockSavedSpot(
        id = 4,
        image = "https://acon-bucket.s3.ap-northeast-2.amazonaws.com/members/profile-images/e6547003-b4df-42fe-9275-9d9c5a008e79.jpg",
        name = "분식당"
    ),
    MockSavedSpot(
        id = 5,
        image = "",
        name = "핫플 베이커리"
    ),
    MockSavedSpot(
        id = 6,
        image = "https://acon-bucket.s3.ap-northeast-2.amazonaws.com/members/profile-images/e6547003-b4df-42fe-9275-9d9c5a008e79.jpg",
        name = "우식당"
    ),
    MockSavedSpot(
        id = 7,
        image = "",
        name = "얼음식당"
    ),
    MockSavedSpot(
        id = 8,
        image = "https://acon-bucket.s3.ap-northeast-2.amazonaws.com/members/profile-images/e6547003-b4df-42fe-9275-9d9c5a008e79.jpg",
        name = "소리식당"
    ),
    MockSavedSpot(
        id = 9,
        image = "",
        name = "얼음 베이커리"
    ),
)