package com.acon.android.feature.spot.screen.spotlist

import android.location.Location
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.android.core.utils.feature.base.BaseContainerHost
import com.acon.android.domain.model.spot.Condition
import com.acon.android.domain.model.spot.Spot
import com.acon.android.domain.repository.MapRepository
import com.acon.android.domain.repository.SpotRepository
import com.acon.android.feature.spot.state.ConditionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class SpotListViewModel @Inject constructor(
    private val spotRepository: SpotRepository,
    private val mapRepository: MapRepository
) : BaseContainerHost<SpotListUiState, SpotListSideEffect>() {

    override val container =
        container<SpotListUiState, SpotListSideEffect>(SpotListUiState.Loading) {

        }

    fun fetchInitialSpots(location: Location) = intent {
        val legalAddressNameDeferred = viewModelScope.async {
            mapRepository.fetchLegalAddressName(
                latitude = location.latitude,
                longitude = location.longitude
            ).getOrNull()
        }

        val spotListResultDeferred = viewModelScope.async {
            spotRepository.fetchSpotList(
                latitude = location.latitude,
                longitude = location.longitude,
                condition = Condition.Default,
            )
        }

        val legalAddressName = legalAddressNameDeferred.await()
        val spotListResult = spotListResultDeferred.await()

        if (legalAddressName == null || spotListResult.isFailure)
            reduce {
                SpotListUiState.LoadFailed
            }
        else {
            spotListResult.onSuccess {
                reduce {
                    (state as? SpotListUiState.Success)?.copy(spotList = it)
                        ?: SpotListUiState.Success(
                            spotList = it,
                            legalAddressName = legalAddressName,
                            isRefreshing = false
                        )
                }
            }
        }
    }


    fun onLocationReady(newLocation: Location) = blockingIntent {
        runOn<SpotListUiState.Success> {
            val legalAddressNameDeferred = viewModelScope.async {
                mapRepository.fetchLegalAddressName(
                    latitude = newLocation.latitude,
                    longitude = newLocation.longitude
                ).getOrNull()
            }

            val spotListResultDeferred = viewModelScope.async {
                spotRepository.fetchSpotList(
                    latitude = newLocation.latitude,
                    longitude = newLocation.longitude,
                    condition = state.currentCondition?.toCondition() ?: Condition.Default,
                )
            }

            val legalAddressName = legalAddressNameDeferred.await()
            val spotListResult = spotListResultDeferred.await()

            if (legalAddressName == null || spotListResult.isFailure)
                reduce {
                    SpotListUiState.LoadFailed
                }
            else {
                spotListResult.onSuccess {
                    reduce {
                        (state as? SpotListUiState.Success)?.copy(
                            spotList = it,
                            isRefreshing = false,
                            legalAddressName = legalAddressName
                        )
                            ?: SpotListUiState.Success(
                                spotList = it,
                                legalAddressName = legalAddressName,
                                isRefreshing = false
                            )
                    }
                }
            }
        }
    }

    fun onRefresh(location: Location) = intent {
        runOn<SpotListUiState.Success> {
            reduce {
                state.copy(isRefreshing = true)
            }
            onLocationReady(location)
        }
    }

    fun onFilterBottomSheetStateChange(show: Boolean) = intent {
        runOn<SpotListUiState.Success> {
            reduce {
                state.copy(showFilterBottomSheet = show)
            }
        }
    }

    fun onResetFilter(location: Location) = intent {
        runOn<SpotListUiState.Success> {
            reduce {
                SpotListUiState.Loading
            }
            fetchInitialSpots(location)
        }
    }

    fun onCompleteFilter(location: Location, condition: ConditionState, proceed: () -> Unit) =
        intent {
            runOn<SpotListUiState.Success> {
                reduce {
                    state.copy(isFilteredResultFetching = true, currentCondition = condition)
                }
                onLocationReady(location)
                reduce {
                    state.copy(isFilteredResultFetching = false, showFilterBottomSheet = false)
                        .also {
                            proceed()
                        }
                }
            }
        }

    fun onSpotItemClick(id: Long) = intent {
        postSideEffect(SpotListSideEffect.NavigateToSpotDetail(id))
    }
}

sealed interface SpotListUiState {
    @Immutable
    data class Success(
        val spotList: List<Spot>,
        val legalAddressName: String,
        val isRefreshing: Boolean = false,
        val currentCondition: ConditionState? = null,
        val showFilterBottomSheet: Boolean = false,
        val isFilteredResultFetching: Boolean = false,
    ) : SpotListUiState

    data object Loading : SpotListUiState
    data object LoadFailed : SpotListUiState
}

sealed interface SpotListSideEffect {
    data class NavigateToSpotDetail(val id: Long) : SpotListSideEffect
}