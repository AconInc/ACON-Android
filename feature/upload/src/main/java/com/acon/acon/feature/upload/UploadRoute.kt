package com.acon.acon.feature.upload

import com.acon.acon.domain.model.upload.v2.SearchedSpot
import kotlinx.serialization.Serializable

sealed interface UploadRoute {

    @Serializable
    data object Graph : UploadRoute

    @Serializable
    data object Search : UploadRoute

    @Serializable
    data class Review(
        val searchedSpot: SearchedSpot
    ) : UploadRoute

    @Serializable
    data class Complete(
        val spotName: String
    ) : UploadRoute
}
