package com.acon.acon.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReverseGeocodingResponse(
    @SerialName("status")
    val status: StatusResponse,
    @SerialName("results")
    val results: List<ResultResponse>,
)

@Serializable
data class StatusResponse(
    @SerialName("code")
    val code: Int,
    @SerialName("name")
    val name: String,
    @SerialName("message")
    val message: String,
)

@Serializable
data class ResultResponse(
    @SerialName("name")
    val name: String,
    @SerialName("code")
    val code: CodeResponse,
    @SerialName("region")
    val region: RegionResponse,
)

@Serializable
data class CodeResponse(
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val type: String,
    @SerialName("mappingId")
    val mappingId: String,
)

@Serializable
data class RegionResponse(
    @SerialName("area3")
    val area3: AreaResponse,
)

@Serializable
data class AreaResponse(
    @SerialName("name")
    val name: String,
)