package com.acon.acon.feature.spot.mock

import android.location.Location
import com.acon.acon.core.common.utils.toLocalTime
import com.acon.acon.domain.model.spot.v2.Spot
import com.acon.acon.domain.type.SpotType
import com.acon.acon.domain.type.TagType
import com.acon.acon.domain.type.TransportMode
import com.acon.acon.feature.spot.screen.spotlist.SpotListUiStateV2

internal val spotListUiStateRestaurantMock = SpotListUiStateV2.Success(
    transportMode = TransportMode.WALKING,
    spotList = listOf(
        Spot(
            id = 9L,
            name = "부대찌개대사관",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20250213_28%2F1739414323555woVbl_JPEG%2F%25C7%25C3%25B7%25B9%25C0%25CC%25BD%25BA_5.jpg",
            acorn = 9999,
            eta = 9,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = listOf(TagType.TOP_1, TagType.NEW, TagType.LOCAL),
            closingTime = "23:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 1L,
            name = "하이디라오",
            image = "https://media.triple.guide/triple-cms/c_limit%2Cf_auto%2Ch_2048%2Cw_2048/ff96459a-1ebb-48e7-9baf-b68911d6347e.jpeg",
            acorn = 9999,
            eta = 10,
            latitude = 37.477576,
            longitude = 126.889057,
            tags = listOf(TagType.TOP_2, TagType.LOCAL),
            closingTime = "23:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 2L,
            name = "샤브올데이",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNTA1MDRfMTA4%2FMDAxNzQ2MzQwNjU0MTYw.aoxFf8q5CLA3DX-h2CA1noUcNy4KdIUue95s0sKjDYYg.k37O5GmawaaHEXR3NpBTOXnfPApZ6yKaDyNLrJ8HjOAg.JPEG%2F20250504_140846.jpg.jpg%3Ftype%3Dw1500_60_sharpen",
            acorn = 320,
            eta = 17,
            latitude = 37.481291,
            longitude = 126.886514,
            tags = listOf(TagType.TOP_3),

            closingTime = "01:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 3L,
            name = "광주 서울곱창",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20241127_200%2F1732671455823qCmdP_JPEG%2FKakaoTalk_20241122_104136138_02.jpg",
            acorn = 1024,
            eta = 11,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = listOf(TagType.TOP_4),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 6L,
            name = "이게 뭘까",
            image = "",
            acorn = 555,
            eta = 1,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = listOf(TagType.TOP_5),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 4L,
            name = "더페이머스그릴",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240929_4%2F1727613280951cwtzB_JPEG%2FIMG_8916.jpeg",
            acorn = 9999,
            eta = 12,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = listOf(TagType.NEW),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 5L,
            name = "민소푸",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220615_101%2F1655277226221Q2daS_JPEG%2FB4499001-B6CB-4308-9C61-141CE5595A43.jpeg",
            acorn = 9999,
            eta = 9,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = emptyList(),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = false,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 6L,
            name = "천우돈",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20250417_9%2F1744862236690pDhhG_JPEG%2F20250412_171412.jpg",
            acorn = 999,
            eta = 9,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = emptyList(),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = false,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 7L,
            name = "88갈비",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221207_162%2F16704202241364Ar0Q_JPEG%2F1670336405574.jpg",
            acorn = 99,
            eta = 9,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = emptyList(),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = false,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 8L,
            name = "애슐리퀸즈",
            image = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNTAyMTlfNjEg%2FMDAxNzM5OTMxMzA0NDY2.v_0Ddkqr9MSmAs99KC5vnIrRNAPBSrTO_1ue3hILjSIg.RSa92XZdL4SPsCRQcJ6gz9C8-vjBeOyAeT8EJexQPsQg.JPEG%2FKakaoTalk_20250219_085453546_20.jpg",
            acorn = 9999,
            eta = 9,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = emptyList(),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = false,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 9L,
            name = "애슐리퀸즈",
            image = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNTAyMTlfNjEg%2FMDAxNzM5OTMxMzA0NDY2.v_0Ddkqr9MSmAs99KC5vnIrRNAPBSrTO_1ue3hILjSIg.RSa92XZdL4SPsCRQcJ6gz9C8-vjBeOyAeT8EJexQPsQg.JPEG%2FKakaoTalk_20250219_085453546_20.jpg",
            acorn = 9999,
            eta = 9,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = emptyList(),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 10L,
            name = "애슐리퀸즈",
            image = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNTAyMTlfNjEg%2FMDAxNzM5OTMxMzA0NDY2.v_0Ddkqr9MSmAs99KC5vnIrRNAPBSrTO_1ue3hILjSIg.RSa92XZdL4SPsCRQcJ6gz9C8-vjBeOyAeT8EJexQPsQg.JPEG%2FKakaoTalk_20250219_085453546_20.jpg",
            acorn = 9999,
            eta = 9,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = emptyList(),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 11L,
            name = "애슐리퀸즈",
            image = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNTAyMTlfNjEg%2FMDAxNzM5OTMxMzA0NDY2.v_0Ddkqr9MSmAs99KC5vnIrRNAPBSrTO_1ue3hILjSIg.RSa92XZdL4SPsCRQcJ6gz9C8-vjBeOyAeT8EJexQPsQg.JPEG%2FKakaoTalk_20250219_085453546_20.jpg",
            acorn = 9999,
            eta = 9,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = emptyList(),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
    ), headTitle = "최고의 선택.",
    selectedSpotType = SpotType.RESTAURANT,
    currentLocation = Location(""),
)

internal val spotListUiStateCafeMock = SpotListUiStateV2.Success(
    transportMode = TransportMode.BIKING,
    spotList = listOf(
        Spot(
            id = 1L,
            name = "카푸치노 아싸씨노",
            image = "https://upload3.inven.co.kr/upload/2025/04/21/bbs/i1370696237.webp?MW=800",
            acorn = 9999,
            eta = 0,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = listOf(TagType.TOP_1, TagType.NEW),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 2L,
            name = "투썸플레이스",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230313_185%2F1678665285309o3tWN_JPEG%2F20230309_110215.jpg",
            acorn = 9999,
            eta = 10,
            latitude = 37.477576,
            longitude = 126.889057,
            tags = listOf(TagType.TOP_2, TagType.LOCAL),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 3L,
            name = "스타벅스",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220510_58%2F1652129129526n5KQ1_JPEG%2F3639_20220509052544_de4cu.jpg",
            acorn = 320,
            eta = 17,
            latitude = 37.481291,
            longitude = 126.886514,
            tags = listOf(TagType.TOP_3, TagType.NEW, TagType.LOCAL),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 4L,
            name = "맛맛맛있는 카페",
            image = "",
            acorn = 1024,
            eta = 11,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = listOf(TagType.TOP_4),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 5L,
            name = "원두서점",
            image = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNDEyMjhfMjYy%2FMDAxNzM1Mzg3ODQxNzY2.NJGv5aAunk-PSiAnE_ChtiF6yu9lKDq_PBNx2D7RpcAg.yaj2TiG_a3sp2__Oku_PmnW18f3MJt5z1l9zjg8Eotwg.JPEG%2FIMG_0512.jpg",
            acorn = 555,
            eta = 1,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = listOf(TagType.TOP_5),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 6L,
            name = "고망고",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220927_189%2F1664259161243IF52z_JPEG%2F20220927_105158.jpg",
            acorn = 9999,
            eta = 12,
            latitude = 37.572793,
            longitude = 126.976986,
            tags = listOf(TagType.NEW),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 7L,
            name = "스타벅스2",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220510_58%2F1652129129526n5KQ1_JPEG%2F3639_20220509052544_de4cu.jpg",
            acorn = 320,
            eta = 18,
            latitude = 37.481291,
            longitude = 126.886514,
            tags = emptyList(),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
        Spot(
            id = 8L,
            name = "스타벅스3",
            image = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220510_58%2F1652129129526n5KQ1_JPEG%2F3639_20220509052544_de4cu.jpg",
            acorn = 320,
            eta = 17,
            latitude = 37.481291,
            longitude = 126.886514,
            tags = emptyList(),
            closingTime = "22:00".toLocalTime()!!,
            isOpen = true,
            nextOpening = "10:00".toLocalTime()!!
        ),
    ), headTitle = "최고의 선택.",
    selectedSpotType = SpotType.CAFE,
    currentLocation = Location(""),
)