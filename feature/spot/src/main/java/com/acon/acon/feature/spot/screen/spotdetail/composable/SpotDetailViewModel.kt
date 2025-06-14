package com.acon.acon.feature.spot.screen.spotdetail.composable

import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.acon.acon.domain.model.spot.SpotDetail
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.feature.spot.SpotRoute
import com.acon.feature.common.base.BaseContainerHost
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

    private val spotId = savedStateHandle.toRoute<SpotRoute.SpotDetail>().id

    override val container =
        container<SpotDetailUiState, SpotDetailSideEffect>(SpotDetailUiState.Loading) {
            val spotDetailInfoDeferred = viewModelScope.async {
                spotRepository.fetchSpotDetail(
                    spotId = spotId,
                    isMain = null //TODO - 딥링크 구현 후 수정
                )
            }

            val spotDetailResult = spotDetailInfoDeferred.await()
            reduce {
                if (spotDetailResult.getOrNull() == null) {
                    SpotDetailUiState.LoadFailed
                }
                else {
                    SpotDetailUiState.Success(
                        spotDetail = spotDetailResult.getOrNull()!!
                    )
                }
            }
        }

    fun fetchMenuBoardList() = intent {
        runOn<SpotDetailUiState.Success> {
            spotRepository.fetchMenuBoards(spotId).onSuccess {
                reduce {
                    state.copy(
                        menuBoardList = it.menuBoardImageList,
                        menuBoardListLoad = true,
                        showMenuBoardDialog = true
                    )
                }
            }.onFailure {
                // TODO - 메뉴 이미지 로딩 실패 UI
                reduce {
                    state.copy(
                        menuBoardListLoad = false,
                        showMenuBoardDialog = true
                    )
                }
            }
        }
    }

    fun fetchRecentNavigationLocation() = intent {
        spotRepository.fetchRecentNavigationLocation(1).onSuccess {
            postSideEffect(
                SpotDetailSideEffect.RecentLocationFetched
            )
        }.onFailure {
            postSideEffect(
                SpotDetailSideEffect.RecentLocationFetchFailed(it)
            )
        }
    }

    fun navigateToBack() = intent {
        postSideEffect(
            SpotDetailSideEffect.NavigateToBack
        )
    }

    @OptIn(OrbitExperimental::class)
    fun onFindWay(location: Location) = intent {
        runOn<SpotDetailUiState.Success> {
            postSideEffect(
                SpotDetailSideEffect.OnFindWayButtonClick(
                    goalDestinationLat = state.spotDetail.latitude,
                    goalDestinationLng = state.spotDetail.longitude,
                    goalDestinationName = state.spotDetail.name,
                    startLocation = location
                )
            )
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
        val spotDetail: SpotDetail,
        val menuBoardList: List<String> = emptyList(),
        val menuBoardListLoad: Boolean = false,
        val showMenuBoardDialog: Boolean = false,
        val showReportErrorModal: Boolean = false,
        val showFindWayModal: Boolean = false
    ) : SpotDetailUiState

    data object Loading : SpotDetailUiState
    data object LoadFailed : SpotDetailUiState
}

sealed interface SpotDetailSideEffect {
    data object NavigateToBack : SpotDetailSideEffect
    data object RecentLocationFetched : SpotDetailSideEffect
    data class RecentLocationFetchFailed(val error: Throwable) : SpotDetailSideEffect
    data class OnFindWayButtonClick(
        val goalDestinationLat: Double,
        val goalDestinationLng: Double,
        val goalDestinationName: String,
        val startLocation: Location
    ) : SpotDetailSideEffect
}