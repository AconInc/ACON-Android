package com.acon.feature.common.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

private const val KOREA_COUNTRY_CODE = "KR"

/**
 * 한국에 있는지 확인
 * @receiver 검사할 위치
 * @param context Context
 */
suspend fun Location.isInKorea(context: Context): Boolean {
    return withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            val geocoder = Geocoder(context, Locale.KOREAN)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            continuation.resume(addresses.getOrNull(0)?.countryCode == KOREA_COUNTRY_CODE)
                        }

                        override fun onError(errorMessage: String?) {
                            continuation.resume(false)
                        }
                    })
                } else {
                    val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
                    addresses?.let {
                        continuation.resume(it.getOrNull(0)?.countryCode == KOREA_COUNTRY_CODE)
                    } ?: continuation.resume(false)

                }
            } catch (e: Exception) {
                continuation.resume(false)
            }
        }
    }
}