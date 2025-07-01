package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.UpdateProfileRequest
import com.acon.acon.data.dto.response.profile.PreSignedUrlResponse
import com.acon.acon.data.dto.response.profile.ProfileResponse
import com.acon.acon.data.api.remote.ProfileApi
import com.acon.acon.data.dto.request.AreaVerificationRequest
import com.acon.acon.data.dto.request.ReplaceVerifiedAreaRequest
import com.acon.acon.data.dto.request.SaveSpotRequest
import com.acon.acon.data.dto.response.area.VerifiedAreaListResponse
import retrofit2.Response
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(
    private val profileApi: ProfileApi
) {
    suspend fun fetchProfile(): ProfileResponse {
        return profileApi.fetchProfile()
    }

    suspend fun getPreSignedUrl(): PreSignedUrlResponse {
        return profileApi.getPreSignedUrl()
    }

    suspend fun validateNickname(nickname: String): Response<Unit> {
        return profileApi.validateNickname(nickname)
    }

    suspend fun updateProfile(fileName: String, nickname: String, birthday: String?): Response<Unit> {
        return profileApi.updateProfile(
            request = UpdateProfileRequest(profileImage = fileName, nickname = nickname, birthDate = formatBirthday(birthday))
        )
    }

    suspend fun fetchSavedSpots() = profileApi.fetchSavedSpots()
    suspend fun saveSpot(saveSpotRequest: SaveSpotRequest) = profileApi.saveSpot(saveSpotRequest)

    suspend fun verifyArea(
        latitude: Double,
        longitude: Double
    ) {
        return profileApi.verifyArea(
            request = AreaVerificationRequest(
                latitude = latitude,
                longitude = longitude
            )
        )
    }

    suspend fun fetchVerifiedAreaList() : VerifiedAreaListResponse {
        return profileApi.fetchVerifiedAreaList()
    }

    suspend fun replaceVerifiedArea(
        previousVerifiedAreaId: Long,
        latitude: Double,
        longitude: Double
    ) {
        return profileApi.replaceVerifiedArea(
            request = ReplaceVerifiedAreaRequest(
                previousVerifiedAreaId = previousVerifiedAreaId,
                latitude = latitude,
                longitude = longitude
            )
        )
    }

    suspend fun deleteVerifiedArea(verifiedAreaId: Long) {
        return profileApi.deleteVerifiedArea(verifiedAreaId)
    }
}

private fun formatBirthday(birthday: String?): String? {
    return birthday?.let {
        "${it.substring(0, 4)}.${it.substring(4, 6)}.${it.substring(6, 8)}"
    }
}
