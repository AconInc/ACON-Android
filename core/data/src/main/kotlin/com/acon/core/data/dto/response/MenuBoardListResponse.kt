package com.acon.core.data.dto.response

import com.acon.acon.core.model.model.spot.MenuBoardList
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