package com.acon.acon.feature.upload

import com.acon.acon.domain.model.spot.SimpleSpot
import kotlinx.serialization.Serializable

sealed interface UploadRoute {

    @Serializable
    data object Graph : UploadRoute

    @Serializable
    data object Search : UploadRoute

    @Serializable
    data class Review(
        val spot: SimpleSpot
    ) : UploadRoute

    @Serializable
    data class Complete(
        val spot: SimpleSpot
    ) : UploadRoute
}
