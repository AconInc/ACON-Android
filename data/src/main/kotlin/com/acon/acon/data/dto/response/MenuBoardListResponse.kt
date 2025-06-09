package com.acon.acon.data.dto.response

import com.acon.acon.domain.model.spot.MenuBoardList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuBoardListResponse(
    @SerialName("menuboardImageList") val menuBoardImageList: List<String>
) {
    fun toMenuBoardList() = MenuBoardList(
        menuBoardImageList = menuBoardImageList
    )
}