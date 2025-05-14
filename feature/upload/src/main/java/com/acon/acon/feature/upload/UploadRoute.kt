package com.acon.acon.feature.upload

import kotlinx.serialization.Serializable

sealed interface UploadRoute {

    @Serializable
    data object Graph : UploadRoute

    @Serializable
    data object Search : UploadRoute

    @Serializable
    data object Review : UploadRoute
}
