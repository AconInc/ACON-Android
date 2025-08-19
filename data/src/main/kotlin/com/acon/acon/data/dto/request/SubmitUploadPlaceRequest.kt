package com.acon.acon.data.dto.request

import com.acon.acon.core.model.type.SpotType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitUploadPlaceRequest(
    @SerialName("spotName") val spotName: String,
    @SerialName("address") val address: String,
    @SerialName("spotType") val spotType: SpotType,
    @SerialName("featureList") val featureList: List<FeatureRequest>,
    @SerialName("recommendedMenu") val recommendedMenu: String,
    @SerialName("imageList") val imageList: List<String>? = emptyList()
)

@Serializable
data class FeatureRequest(
    @SerialName("category") val category: String,
    @SerialName("optionList") val optionList: List<String>
)