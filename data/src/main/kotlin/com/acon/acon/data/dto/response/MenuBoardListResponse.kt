package com.acon.acon.data.dto.response

import com.acon.core.model.MenuBoardList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuBoardListResponse(
    @SerialName("menuboardImageList") val menuBoardImageList: List<String>
) {
    fun toMenuBoardList() = com.acon.core.model.MenuBoardList(
        menuBoardImageList = menuBoardImageList
    )
}