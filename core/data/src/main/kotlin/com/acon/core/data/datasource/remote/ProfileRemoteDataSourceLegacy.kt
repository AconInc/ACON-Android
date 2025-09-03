package com.acon.core.data.datasource.remote

import com.acon.core.data.dto.request.UpdateProfileRequestLegacy
import com.acon.core.data.dto.response.profile.PreSignedUrlResponse
import com.acon.core.data.dto.response.profile.ProfileResponseLegacy
import com.acon.core.data.api.remote.auth.ProfileAuthApiLegacy
import com.acon.core.data.dto.request.ReplaceVerifiedAreaRequest
import com.acon.core.data.dto.request.SaveSpotRequest
import com.acon.core.data.dto.response.area.VerifiedAreaListResponse
import retrofit2.Response
import javax.inject.Inject

class ProfileRemoteDataSourceLegacy @Inject constructor(
    private val profileAuthApiLegacy: ProfileAuthApiLegacy
) {
    suspend fun fetchProfile(): ProfileResponseLegacy {
        return profileAuthApiLegacy.fetchProfile()
    }

    suspend fun getPreSignedUrl(): PreSignedUrlResponse {
        return profileAuthApiLegacy.getPreSignedUrl()
    }

    suspend fun validateNickname(nickname: String): Response<Unit> {
        return profileAuthApiLegacy.validateNickname(nickname)
    }

    suspend fun updateProfile(fileName: String, nickname: String, birthday: String?): Response<Unit> {
        return profileAuthApiLegacy.updateProfile(
            request = UpdateProfileRequestLegacy(profileImage = fileName, nickname = nickname, birthDate = formatBirthday(birthday))
        )
    }

    suspend fun fetchSavedSpots() = profileAuthApiLegacy.fetchSavedSpots()
    suspend fun saveSpot(saveSpotRequest: SaveSpotRequest) = profileAuthApiLegacy.saveSpot(saveSpotRequest)

    suspend fun fetchVerifiedAreaList() : VerifiedAreaListResponse {
        return profileAuthApiLegacy.fetchVerifiedAreaList()
    }

    suspend fun replaceVerifiedArea(
        previousVerifiedAreaId: Long,
        latitude: Double,
        longitude: Double
    ) {
        return profileAuthApiLegacy.replaceVerifiedArea(
            request = ReplaceVerifiedAreaRequest(
                previousVerifiedAreaId = previousVerifiedAreaId,
                latitude = latitude,
                longitude = longitude
            )
        )
    }

    suspend fun deleteVerifiedArea(verifiedAreaId: Long) {
        return profileAuthApiLegacy.deleteVerifiedArea(verifiedAreaId)
    }
}

private fun formatBirthday(birthday: String?): String? {
    return birthday?.let {
        "${it.substring(0, 4)}.${it.substring(4, 6)}.${it.substring(6, 8)}"
    }
}
