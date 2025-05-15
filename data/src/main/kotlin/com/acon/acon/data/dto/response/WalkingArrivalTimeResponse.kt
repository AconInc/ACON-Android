package com.acon.acon.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DirectionsResponse(
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
    @SerialName("currentDateTime") val currentDateTime: String,
    @SerialName("route") val route: Route
)

@Serializable
data class Route(
    @SerialName("traoptimal") val traoptimal: List<RouteOption> = emptyList(),
    @SerialName("trafast") val trafast: List<RouteOption> = emptyList(),
    @SerialName("tracomfort") val tracomfort: List<RouteOption> = emptyList(),
    @SerialName("traavoidtoll") val traavoidtoll: List<RouteOption> = emptyList(),
    @SerialName("traavoidcaronly") val traavoidcaronly: List<RouteOption> = emptyList()
)

@Serializable
data class RouteOption(
    @SerialName("summary") val summary: Summary,
    @SerialName("path") val path: List<List<Double>>,
    @SerialName("section") val section: List<Section> = emptyList(),
    @SerialName("guide") val guide: List<Guide> = emptyList()
)

@Serializable
data class Summary(
    @SerialName("start") val start: LocationInfo,
    @SerialName("goal") val goal: GoalInfo,
    @SerialName("distance") val distance: Int,
    @SerialName("duration") val duration: Int,
    @SerialName("departureTime") val departureTime: String,
    @SerialName("bbox") val bbox: List<List<Double>>,
    @SerialName("tollFare") val tollFare: Int? = null,
    @SerialName("taxiFare") val taxiFare: Int? = null,
    @SerialName("fuelPrice") val fuelPrice: Int? = null
)

@Serializable
data class LocationInfo(
    @SerialName("location") val location: List<Double>
)

@Serializable
data class GoalInfo(
    @SerialName("location") val location: List<Double>,
    @SerialName("dir") val dir: Int
)

@Serializable
data class Section(
    @SerialName("pointIndex") val pointIndex: Int,
    @SerialName("pointCount") val pointCount: Int,
    @SerialName("distance") val distance: Int,
    @SerialName("name") val name: String,
    @SerialName("congestion") val congestion: Int,
    @SerialName("speed") val speed: Int
)

@Serializable
data class Guide(
    @SerialName("pointIndex") val pointIndex: Int,
    @SerialName("type") val type: Int,
    @SerialName("instructions") val instructions: String,
    @SerialName("distance") val distance: Int,
    @SerialName("duration") val duration: Int
)