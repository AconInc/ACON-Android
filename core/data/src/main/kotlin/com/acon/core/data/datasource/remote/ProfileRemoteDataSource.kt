package com.acon.core.data.datasource.remote

import com.acon.core.data.dto.request.UpdateProfileRequest
import com.acon.core.data.dto.response.profile.PreSignedUrlResponse
import com.acon.core.data.dto.response.profile.ProfileResponse
import com.acon.core.data.api.remote.auth.ProfileAuthApi
import com.acon.core.data.dto.request.ReplaceVerifiedAreaRequest
import com.acon.core.data.dto.request.SaveSpotRequest
import com.acon.core.data.dto.response.area.VerifiedAreaListResponse
import retrofit2.Response
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(
    private val profileAuthApi: ProfileAuthApi
) {
    suspend fun fetchProfile(): ProfileResponse {
        return profileAuthApi.fetchProfile()
    }

    suspend fun getPreSignedUrl(): PreSignedUrlResponse {
        return profileAuthApi.getPreSignedUrl()
    }

    suspend fun validateNickname(nickname: String): Response<Unit> {
        return profileAuthApi.validateNickname(nickname)
    }

    suspend fun updateProfile(fileName: String, nickname: String, birthday: String?): Response<Unit> {
        return profileAuthApi.updateProfile(
            request = UpdateProfileRequest(profileImage = fileName, nickname = nickname, birthDate = formatBirthday(birthday))
        )
    }

    suspend fun fetchSavedSpots() = profileAuthApi.fetchSavedSpots()
    suspend fun saveSpot(saveSpotRequest: SaveSpotRequest) = profileAuthApi.saveSpot(saveSpotRequest)

    suspend fun fetchVerifiedAreaList() : VerifiedAreaListResponse {
        return profileAuthApi.fetchVerifiedAreaList()
    }

    suspend fun replaceVerifiedArea(
        previousVerifiedAreaId: Long,
        latitude: Double,
        longitude: Double
    ) {
        return profileAuthApi.replaceVerifiedArea(
            request = ReplaceVerifiedAreaRequest(
                previousVerifiedAreaId = previousVerifiedAreaId,
                latitude = latitude,
                longitude = longitude
            )
        )
    }

    suspend fun deleteVerifiedArea(verifiedAreaId: Long) {
        return profileAuthApi.deleteVerifiedArea(verifiedAreaId)
    }
}

private fun formatBirthday(birthday: String?): String? {
    return birthday?.let {
        "${it.substring(0, 4)}.${it.substring(4, 6)}.${it.substring(6, 8)}"
    }
}
