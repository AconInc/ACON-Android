package com.acon.acon.feature.spot.mock

import com.acon.acon.domain.model.spot.Spot
import com.acon.acon.domain.type.SpotType

internal val spotListMock = listOf(
    Spot(
        1,
        "https://t1.daumcdn.net/cfile/tistory/2124AD42583654321E",
        96,
        SpotType.RESTAURANT,
        "바다회사랑",
        walkingTime = 6
    ),
    Spot(
        2,
        "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNDA3MTFfODcg%2FMDAxNzIwNjc5ODk2NTY5.oR9Nun931wkBqQaDlaz1Ofoa4EXDzbNWc9VU_RRu0TUg.r-20oN0pvltLbmZ8SBXPYNgB-lk-zAzBoGpu3YiX2xgg.JPEG%2F1711243960592.jpg&type=sc960_832",
        88,
        SpotType.RESTAURANT,
        "BHC치킨",
        walkingTime = 16
    ),

    Spot(
        3,
        "https://cdn.aitimes.com/news/photo/202012/135037_133168_497.jpg",
        85,
        SpotType.CAFE,
        "스타벅스",
        walkingTime = 6
    ),
    Spot(
        4,
        "https://stock.mk.co.kr/photos/20230731/PEP20220920094901009_P4.jpg",
        82,
        SpotType.RESTAURANT,
        "하이디라오",
        walkingTime = 12
    ),
    Spot(
        5,
        "https://lh4.googleusercontent.com/proxy/JYJtsy49SAoUKD92gASXvI0pf-lIsl6GR8UmfQxvR-jaGsQs92mnYeGqxg34gcQwnVA2fizSpalRd7Q9KuhYXucfeWnfm09T6GzDt4y1IUGdwFBQ",
        77,
        SpotType.CAFE,
        "할리스",
        walkingTime = 8
    ),
    Spot(
        6,
        "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/17/68/1e/17/photo0jpg.jpg?w=900&h=500&s=1",
        65,
        SpotType.CAFE,
        "설빙",
        walkingTime = 20
    )
)
