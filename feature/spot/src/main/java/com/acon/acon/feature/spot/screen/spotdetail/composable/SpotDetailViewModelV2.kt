package com.acon.acon.feature.spot.screen.spotdetail.composable

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

@OptIn(OrbitExperimental::class)
@HiltViewModel
class SpotDetailViewModelV2 @Inject constructor(
    private val spotRepository: SpotRepository,
    savedStateHandle: SavedStateHandle
) : BaseContainerHost<SpotDetailUiStateV2, SpotDetailSideEffectV2>() {

    private val spotId = savedStateHandle.toRoute<SpotRoute.SpotDetail>().id

    override val container =
        container<SpotDetailUiStateV2, SpotDetailSideEffectV2>(SpotDetailUiStateV2.Loading) {
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
                    SpotDetailUiStateV2.LoadFailed
                }
                else if (spotDetailMenuResult.getOrNull() == null) {
                    SpotDetailUiStateV2.LoadFailed
                }
                else {
                    SpotDetailUiStateV2.Success(
                        spotDetailInfo = spotDetailInfoResult.getOrNull()!!,
                        spotDetailMenuList = spotDetailMenuResult.getOrNull()!!
                    )
                }
            }
        }

    fun fetchRecentNavigationLocation() = intent {
        spotRepository.fetchRecentNavigationLocation(1).onSuccess {
            postSideEffect(
                SpotDetailSideEffectV2.RecentLocationFetched
            )
        }.onFailure {
            postSideEffect(
                SpotDetailSideEffectV2.RecentLocationFetchFailed(it)
            )
        }
    }

    fun navigateToBack() = intent {
        postSideEffect(
            SpotDetailSideEffectV2.NavigateToBack
        )
    }

    @OptIn(OrbitExperimental::class)
    fun onFindWay(location: Location) = intent {
        runOn<SpotDetailUiStateV2.Success> {
            postSideEffect(
                SpotDetailSideEffectV2.OnFindWayButtonClick(
                    goalDestinationLat = state.spotDetailInfo.latitude,
                    goalDestinationLng = state.spotDetailInfo.longitude,
                    goalDestinationName = state.spotDetailInfo.name,
                    startLocation = location
                )
            )
        }
    }
}

sealed interface SpotDetailUiStateV2 {
    @Immutable
    data class Success(
        val spotDetailInfo: SpotDetailInfo,
        val spotDetailMenuList: List<SpotDetailMenu>,
    ) : SpotDetailUiStateV2

    data object Loading : SpotDetailUiStateV2
    data object LoadFailed : SpotDetailUiStateV2
}

sealed interface SpotDetailSideEffectV2 {
    data object NavigateToBack : SpotDetailSideEffectV2
    data object RecentLocationFetched : SpotDetailSideEffectV2
    data class RecentLocationFetchFailed(val error: Throwable) : SpotDetailSideEffectV2
    data class OnFindWayButtonClick(
        val goalDestinationLat: Double,
        val goalDestinationLng: Double,
        val goalDestinationName: String,
        val startLocation: Location
    ) : SpotDetailSideEffectV2
}