package com.acon.acon.feature.spot.screen.spotdetail.composable

import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.acon.acon.domain.model.spot.SpotDetail
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.domain.type.TagType
import com.acon.acon.domain.type.TransportMode
import com.acon.acon.feature.spot.SpotRoute
import com.acon.feature.common.base.BaseContainerHost
import com.acon.feature.common.navigation.spotNavigationParameterNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class SpotDetailViewModel @Inject constructor(
    private val spotRepository: SpotRepository,
    savedStateHandle: SavedStateHandle
) : BaseContainerHost<SpotDetailUiState, SpotDetailSideEffect>() {

    private val spotNavData = savedStateHandle.toRoute<SpotRoute.SpotDetail>(
        mapOf(spotNavigationParameterNavType)
    ).spotNavigationParameter

    override val container =
        container<SpotDetailUiState, SpotDetailSideEffect>(SpotDetailUiState.Loading) {
            fetchedSpotDetail()
        }

    private fun fetchedSpotDetail() = intent {
        val spotDetailInfoDeferred = viewModelScope.async {
            spotRepository.fetchSpotDetail(
                spotId = spotNavData.spotId
            )
        }
        val spotDetailResult = spotDetailInfoDeferred.await()

        reduce {
            when (val spotDetail = spotDetailResult.getOrNull()) {
                null -> SpotDetailUiState.LoadFailed
                else -> {
                    val isDeepLink = spotNavData.isFromDeepLink == true

                    SpotDetailUiState.Success(
                        tags = spotNavData.tags.takeUnless { isDeepLink },
                        transportMode = spotNavData.transportMode,
                        eta = spotNavData.eta,
                        spotDetail = spotDetail,
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
            if (state.isBookmarkSaved) {
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
                    state.copy(isBookmarkSaved = true)
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
                    state.copy(isBookmarkSaved = false)
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
        val tags: List<TagType>? = emptyList(),
        val transportMode: TransportMode? = TransportMode.WALKING,
        val eta: Int? = 0,
        val isFromDeepLink: Boolean? = false,
        val navFromProfile: Boolean? = null,
        val spotDetail: SpotDetail,
        val isBookmarkSaved: Boolean = false,
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
    data object LoadFailed : SpotDetailUiState
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