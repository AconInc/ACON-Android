package com.acon.acon.feature.spot.screen.spotdetail

import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.spot.SpotDetailInfo
import com.acon.acon.domain.model.spot.SpotDetailMenu
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.feature.spot.SpotRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class SpotDetailViewModel @Inject constructor(
    private val spotRepository: SpotRepository,
    savedStateHandle: SavedStateHandle
) : BaseContainerHost<SpotDetailUiState, SpotDetailSideEffect>() {

    private val spotId = savedStateHandle.toRoute<SpotRoute.SpotDetail>().id

    override val container =
        container<SpotDetailUiState, SpotDetailSideEffect>(SpotDetailUiState.Loading) {
            val spotDetailInfoDeferred = viewModelScope.async {
                spotRepository.getSpotDetailInfo(spotId)
            }
            val spotDetailMenuDeferred = viewModelScope.async {
                spotRepository.getSpotMenuList(spotId)
            }

            val spotDetailInfoResult = spotDetailInfoDeferred.await()
            val spotDetailMenuResult = spotDetailMenuDeferred.await()

            reduce {
                if (spotDetailInfoResult.getOrNull() == null) {
                    SpotDetailUiState.LoadFailed
                }
                else if (spotDetailMenuResult.getOrNull() == null) {
                    SpotDetailUiState.LoadFailed
                }
                else {
                    SpotDetailUiState.Success(
                        spotDetailInfo = spotDetailInfoResult.getOrNull()!!,
                        spotDetailMenuList = spotDetailMenuResult.getOrNull()!!
                    )
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

    fun navigateToSpotListView() = intent {
        postSideEffect(
            SpotDetailSideEffect.NavigateToSpotListView
        )
    }

    @OptIn(OrbitExperimental::class)
    fun onFindWay(location: Location) = intent {
        runOn<SpotDetailUiState.Success> {
            postSideEffect(
                SpotDetailSideEffect.OnFindWayButtonClick(
                    destinationLat = state.spotDetailInfo.latitude,
                    destinationLng = state.spotDetailInfo.longitude,
                    destinationName = state.spotDetailInfo.name,
                    location = location
                )
            )
        }
    }
}

sealed interface SpotDetailUiState {
    @Immutable
    data class Success(
        val spotDetailInfo: SpotDetailInfo,
        val spotDetailMenuList: List<SpotDetailMenu>,
    ) : SpotDetailUiState

    data object Loading : SpotDetailUiState
    data object LoadFailed : SpotDetailUiState
}

sealed interface SpotDetailSideEffect {
    data object NavigateToSpotListView : SpotDetailSideEffect
    data object RecentLocationFetched : SpotDetailSideEffect
    data class RecentLocationFetchFailed(val error: Throwable) : SpotDetailSideEffect
    data class OnFindWayButtonClick(
        val destinationLat: Double,
        val destinationLng: Double,
        val destinationName: String,
        val location: Location
    ) : SpotDetailSideEffect
}