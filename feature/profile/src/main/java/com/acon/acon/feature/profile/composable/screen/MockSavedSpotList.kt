package com.acon.acon.feature.profile.composable.screen

import com.acon.acon.domain.model.spot.v2.SavedSpot
import okhttp3.internal.immutableListOf

internal val mockSpotList = immutableListOf(
    SavedSpot(
        id = 1,
        image = "https://acon-bucket.s3.ap-northeast-2.amazonaws.com/members/profile-images/e6547003-b4df-42fe-9275-9d9c5a008e79.jpg",
        name = "카페 브리즈"
    ),
    SavedSpot(
        id = 2,
        image = "",
        name = "서울 맛집"
    ),
    SavedSpot(
        id = 3,
        image = "https://acon-bucket.s3.ap-northeast-2.amazonaws.com/members/profile-images/e6547003-b4df-42fe-9275-9d9c5a008e79.jpg",
        name = "초밥 천국"
    ),
    SavedSpot(
        id = 4,
        image = "https://acon-bucket.s3.ap-northeast-2.amazonaws.com/members/profile-images/e6547003-b4df-42fe-9275-9d9c5a008e79.jpg",
        name = "분식당"
    ),
    SavedSpot(
        id = 5,
        image = "",
        name = "핫플 베이커리"
    ),
    SavedSpot(
        id = 6,
        image = "https://acon-bucket.s3.ap-northeast-2.amazonaws.com/members/profile-images/e6547003-b4df-42fe-9275-9d9c5a008e79.jpg",
        name = "우식당"
    ),
    SavedSpot(
        id = 7,
        image = "",
        name = "얼음식당"
    ),
    SavedSpot(
        id = 8,
        image = "https://acon-bucket.s3.ap-northeast-2.amazonaws.com/members/profile-images/e6547003-b4df-42fe-9275-9d9c5a008e79.jpg",
        name = "소리식당"
    ),
    SavedSpot(
        id = 9,
        image = "",
        name = "얼음 베이커리"
    )
)