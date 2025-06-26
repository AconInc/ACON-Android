package com.acon.acon.feature.spot.screen.spotdetail.composable

import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.acon.core.model.spot.SpotDetail
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.core.type.TagType
import com.acon.core.type.TransportMode
import com.acon.core.type.UserType
import com.acon.acon.feature.spot.SpotRoute
import com.acon.core.analytics.amplitude.AconAmplitude
import com.acon.core.analytics.constants.EventNames
import com.acon.core.analytics.constants.PropertyKeys
import com.acon.feature.common.base.BaseContainerHost
import com.acon.feature.common.navigation.spotNavigationParameterNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class SpotDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val spotRepository: SpotRepository,
    savedStateHandle: SavedStateHandle
) : BaseContainerHost<SpotDetailUiState, SpotDetailSideEffect>() {

    private val spotNavData = savedStateHandle.toRoute<SpotRoute.SpotDetail>(
        mapOf(spotNavigationParameterNavType)
    ).spotNavigationParameter

    private val entryTimeMillis = System.currentTimeMillis()

    override fun onCleared() {
        AconAmplitude.trackEvent(
            eventName = EventNames.DETAIL_PAGE,
            property = PropertyKeys.PLACE_DETAIL_DURATION to ((System.currentTimeMillis() - entryTimeMillis) / 1000)
        )
    }

    override val container =
        container<SpotDetailUiState, SpotDetailSideEffect>(SpotDetailUiState.Loading) {
            userType.collect {
                when (it) {
                    UserType.GUEST -> {
                        if (spotNavData.isFromDeepLink == true) {
                            fetchedSpotDetail()
                        } else {
                            reduce { SpotDetailUiState.LoadFailed() }
                        }
                    }

                    else -> {
                        fetchedSpotDetail()
                    }
                }
            }
        }

    private fun fetchedSpotDetail() = intent {
        delay(800)

        // 딥링크 접속 && UserType == GUEST 경우에만 isDeepLink = true
        //val isDeepLinkGuest = userType.value == UserType.GUEST && spotNavData.isFromDeepLink == true
        val isDeepLink = spotNavData.isFromDeepLink == true

        val spotDetailDeferred = viewModelScope.async {
            if (isDeepLink) {
                spotRepository.fetchSpotDetail(
                    spotId = spotNavData.spotId,
                    isDeepLink = true
                )
            } else {
                spotRepository.fetchSpotDetailFromUser(
                    spotId = spotNavData.spotId
                )
            }
        }

        // GUEST 인 경우 빈 리스트
        val verifiedAreaListDeferred = viewModelScope.async {
            if (userType.value != UserType.GUEST) {
                userRepository.fetchVerifiedAreaList()
            } else {
                Result.success(emptyList())
            }
        }

        val spotDetailResult = spotDetailDeferred.await()
        val verifiedAreaListResult = verifiedAreaListDeferred.await()

        reduce {
            val isAreaVerified = verifiedAreaListResult
                .getOrNull()
                .orEmpty()
                .isNotEmpty()

            when (val spotDetail = spotDetailResult.getOrNull()) {
                null -> SpotDetailUiState.LoadFailed()
                else -> {
                    val isDeepLink = spotNavData.isFromDeepLink == true

                    SpotDetailUiState.Success(
                        tags = spotNavData.tags.takeUnless { isDeepLink },
                        transportMode = spotNavData.transportMode,
                        eta = spotNavData.eta,
                        spotDetail = spotDetail,
                        isAreaVerified = isAreaVerified,
                        isFromDeepLink = isDeepLink,
                        navFromProfile = spotNavData.navFromProfile,
                    )
                }
            }
        }
    }

    fun retry() = intent {
        fetchedSpotDetail()
    }

    fun fetchMenuBoardList() = intent {
        runOn<SpotDetailUiState.Success> {
            spotRepository.fetchMenuBoards(spotNavData.spotId).onSuccess {
                reduce {
                    state.copy(
                        menuBoardList = it.menuBoardImageList,
                        menuBoardListLoad = true,
                        showMenuBoardDialog = true
                    )
                }
            }.onFailure {
                reduce {
                    state.copy(
                        menuBoardListLoad = false,
                        showMenuBoardDialog = true
                    )
                }
            }
        }
    }

    fun toggleBookmark() = intent {
        runOn<SpotDetailUiState.Success> {
            if (state.spotDetail.isSaved) {
                deleteBookmark()
            } else {
                addBookmark()
            }
        }
    }

    private fun addBookmark() = intent {
        spotRepository.addBookmark(spotNavData.spotId).onSuccess {
            runOn<SpotDetailUiState.Success> {
                reduce {
                    val updatedDetail = state.spotDetail.copy(isSaved = true)
                    state.copy(spotDetail = updatedDetail)
                }
            }
        }.onFailure {
            postSideEffect(SpotDetailSideEffect.ShowErrorToast)
        }
    }

    private fun deleteBookmark() = intent {
        spotRepository.deleteBookmark(spotNavData.spotId).onSuccess {
            runOn<SpotDetailUiState.Success> {
                reduce {
                    val updatedDetail = state.spotDetail.copy(isSaved = false)
                    state.copy(spotDetail = updatedDetail)
                }
            }
        }.onFailure {
            postSideEffect(SpotDetailSideEffect.ShowErrorToast)
        }
    }

    fun navigateToBack() = intent {
        postSideEffect(
            SpotDetailSideEffect.NavigateToBack
        )
    }

    fun fetchRecentNavigationLocation() = intent {
        spotRepository.fetchRecentNavigationLocation(spotNavData.spotId).onSuccess {
            postSideEffect(
                SpotDetailSideEffect.RecentLocationFetched
            )
        }.onFailure {
            postSideEffect(
                SpotDetailSideEffect.RecentLocationFetched
            )
        }
    }

    fun onFindWay(location: Location) = intent {
        runOn<SpotDetailUiState.Success> {
            // 딥링크, 프로필로 진입한 유저 -> route/public
            if (state.navFromProfile == true || state.isFromDeepLink == true) {
                postSideEffect(
                    SpotDetailSideEffect.OnFindWayButtonClick(
                        start = location,
                        destination = Location("").apply {
                            latitude = state.spotDetail.latitude
                            longitude = state.spotDetail.longitude
                        },
                        destinationName = state.spotDetail.name,
                        transportMode = null,
                        isPublic = true
                    )
                )
            } else {
                postSideEffect(
                    SpotDetailSideEffect.OnFindWayButtonClick(
                        start = location,
                        destination = Location("").apply {
                            latitude = state.spotDetail.latitude
                            longitude = state.spotDetail.longitude
                        },
                        destinationName = state.spotDetail.name,
                        transportMode = state.transportMode ?: TransportMode.WALKING,
                        isPublic = false
                    )
                )
            }
        }
    }

    fun onDismissMenuBoard() = intent {
        runOn<SpotDetailUiState.Success> {
            reduce {
                state.copy(showMenuBoardDialog = false)
            }
        }
    }

    fun onRequestReportErrorModal() = intent {
        runOn<SpotDetailUiState.Success> {
            reduce {
                state.copy(showReportErrorModal = true)
            }
        }
    }

    fun onDismissReportErrorModal() = intent {
        runOn<SpotDetailUiState.Success> {
            reduce {
                state.copy(showReportErrorModal = false)
            }
        }
    }

    fun onRequestFindWayModal() = intent {
        runOn<SpotDetailUiState.Success> {
            reduce {
                state.copy(showFindWayModal = true)
            }
        }
    }

    fun onDismissFindWayModal() = intent {
        runOn<SpotDetailUiState.Success> {
            reduce {
                state.copy(showFindWayModal = false)
            }
        }
    }
}

sealed interface SpotDetailUiState {
    @Immutable
    data class Success(
        val isAreaVerified: Boolean = false,
        val tags: List<TagType>? = emptyList(),
        val transportMode: TransportMode? = TransportMode.WALKING,
        val eta: Int? = 0,
        val isFromDeepLink: Boolean? = false,
        val navFromProfile: Boolean? = null,
        val spotDetail: SpotDetail,
        val menuBoardList: List<String> = emptyList(),
        val menuBoardListLoad: Boolean = false,
        val showMenuBoardDialog: Boolean = false,
        val showReportErrorModal: Boolean = false,
        val showFindWayModal: Boolean = false
    ) : SpotDetailUiState {
        val storeTags: List<TagType>
            get() = if (navFromProfile == true || isFromDeepLink == true) {
                runCatching {
                    spotDetail.tagList.map { TagType.valueOf(it) }
                }.getOrElse { emptyList() }
            } else {
                tags ?: emptyList()
            }

        fun getTransportLabel(): String = when (transportMode) {
            TransportMode.WALKING -> "도보"
            TransportMode.BIKING -> "자전거"
            else -> ""
        }
    }

    data object Loading : SpotDetailUiState
    data class LoadFailed(val isAreaVerified: Boolean = false) : SpotDetailUiState
}

sealed interface SpotDetailSideEffect {
    data object NavigateToBack : SpotDetailSideEffect
    data object RecentLocationFetched : SpotDetailSideEffect
    data class OnFindWayButtonClick(
        val start: Location,
        val destination: Location,
        val destinationName: String,
        val isPublic: Boolean,
        val transportMode: TransportMode? = null
    ) : SpotDetailSideEffect

    data object ShowErrorToast : SpotDetailSideEffect
}