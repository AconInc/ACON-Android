package com.acon.acon.domain.repository

import com.acon.core.model.area.LegalArea
import com.acon.core.model.profile.SavedSpot
import com.acon.core.model.spot.Condition
import com.acon.core.model.spot.MenuBoardList
import com.acon.core.model.spot.SpotDetail
import com.acon.core.model.spot.SpotList

interface SpotRepository {

    suspend fun fetchSpotList(
        latitude: Double,
        longitude: Double,
        condition: Condition,
    ): Result<SpotList>

    suspend fun fetchRecentNavigationLocation(
        spotId: Long,
    ): Result<Unit>

    suspend fun fetchSpotDetail(
        spotId: Long,
        isDeepLink: Boolean,
    ): Result<SpotDetail>

    suspend fun getLegalDong(
        latitude: Double,
        longitude: Double,
    ): Result<LegalArea>

    suspend fun fetchMenuBoards(
        spotId: Long
    ): Result<MenuBoardList>

    suspend fun fetchSpotDetailFromUser(
        spotId: Long
    ): Result<SpotDetail>

    suspend fun fetchSavedSpotList(): Result<List<SavedSpot>>

    suspend fun addBookmark(
        spotId: Long
    ): Result<Unit>

    suspend fun deleteBookmark(
        spotId: Long
    ): Result<Unit>
}