package com.acon.acon.domain.repository

import com.acon.acon.domain.model.area.LegalArea
import com.acon.acon.domain.model.spot.Condition
import com.acon.acon.domain.model.spot.MenuBoardList
import com.acon.acon.domain.model.spot.SpotDetail
import com.acon.acon.domain.model.spot.v2.SpotList

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
    ): Result<SpotDetail>

    suspend fun getLegalDong(
        latitude: Double,
        longitude: Double,
    ): Result<LegalArea>

    suspend fun fetchMenuBoards(
        spotId: Long
    ): Result<MenuBoardList>
}