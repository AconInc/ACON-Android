package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.profile.SavedSpot
import com.acon.acon.core.model.model.spot.Condition
import com.acon.acon.core.model.model.spot.MenuBoardList
import com.acon.acon.core.model.model.spot.SpotDetail
import com.acon.acon.core.model.model.spot.SpotList

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