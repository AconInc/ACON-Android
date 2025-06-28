package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.area.LegalArea
import com.acon.acon.core.model.model.profile.SavedSpot
import com.acon.acon.core.model.model.spot.Condition
import com.acon.acon.core.model.model.spot.MenuBoardList
import com.acon.acon.core.model.model.spot.SpotDetail
import com.acon.acon.core.model.model.spot.SpotList

interface SpotRepository {

    suspend fun fetchSpotList(
        latitude: Double,
        longitude: Double,
        condition: com.acon.acon.core.model.model.spot.Condition,
    ): Result<com.acon.acon.core.model.model.spot.SpotList>

    suspend fun fetchRecentNavigationLocation(
        spotId: Long,
    ): Result<Unit>

    suspend fun fetchSpotDetail(
        spotId: Long,
        isDeepLink: Boolean,
    ): Result<com.acon.acon.core.model.model.spot.SpotDetail>

    suspend fun getLegalDong(
        latitude: Double,
        longitude: Double,
    ): Result<com.acon.acon.core.model.model.area.LegalArea>

    suspend fun fetchMenuBoards(
        spotId: Long
    ): Result<com.acon.acon.core.model.model.spot.MenuBoardList>

    suspend fun fetchSpotDetailFromUser(
        spotId: Long
    ): Result<com.acon.acon.core.model.model.spot.SpotDetail>

    suspend fun fetchSavedSpotList(): Result<List<com.acon.acon.core.model.model.profile.SavedSpot>>

    suspend fun addBookmark(
        spotId: Long
    ): Result<Unit>

    suspend fun deleteBookmark(
        spotId: Long
    ): Result<Unit>
}