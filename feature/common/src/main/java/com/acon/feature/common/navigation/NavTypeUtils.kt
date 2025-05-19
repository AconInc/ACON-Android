package com.acon.feature.common.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.acon.acon.domain.model.upload.v2.SearchedSpot
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

val searchedSpotNavType by lazy {
    typeOf<SearchedSpot>() to createNavType<SearchedSpot>()
}

inline fun <reified T : Any> createNavType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }

    override fun serializeAsValue(value: T): String = json.encodeToString(value)
}