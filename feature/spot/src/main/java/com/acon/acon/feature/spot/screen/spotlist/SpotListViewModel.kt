package com.acon.acon.feature.spot.screen.spotlist

import android.location.Location
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.spot.Condition
import com.acon.acon.domain.model.spot.Spot
import com.acon.acon.domain.repository.AuthRepository
import com.acon.acon.domain.repository.MapRepository
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.feature.spot.state.ConditionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@OptIn(OrbitExperimental::class)
@HiltViewModel
class SpotListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val spotRepository: SpotRepository,
    private val mapRepository: MapRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseContainerHost<SpotListUiState, SpotListSideEffect>() {

    override val container =
        container<SpotListUiState, SpotListSideEffect>(SpotListUiState.Loading) { }

    fun googleLogin(socialRepository: SocialRepository) = intent {
        socialRepository.signIn()
            .onSuccess {
                postSideEffect(SpotListSideEffect.NavigateToAreaVerification)
            }.onFailure { error ->
                when (error) {
                    is CancellationException -> {
                        reduce { SpotListUiState.Guest() }
                    }
                    is NoSuchElementException -> {
                        reduce { SpotListUiState.Guest() }
                    }
                    is SecurityException -> {
                        reduce { SpotListUiState.Guest() }
                    }
                    else -> {
                        reduce { SpotListUiState.Guest() }
                    }
                }
            }
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
        val isLogin = authRepository.getLoginState().value

        if (legalAddressName == null || spotListResult.isFailure)
            reduce { SpotListUiState.LoadFailed }
        else {
            spotListResult.onSuccess {
                reduce {
                    if (isLogin) {
                        (state as? SpotListUiState.Success)?.copy(
                            spotList = it,
                            isRefreshing = false,
                            legalAddressName = legalAddressName
                        ) ?: SpotListUiState.Success(
                            spotList = it,
                            legalAddressName = legalAddressName,
                            isRefreshing = false
                        )
                    } else {
                        (state as? SpotListUiState.Guest)?.copy(
                            spotList = it,
                            isRefreshing = false,
                            legalAddressName = legalAddressName,
                            showLoginBottomSheet = (state as? SpotListUiState.Guest)?.showLoginBottomSheet ?: false
                        ) ?: SpotListUiState.Guest(
                            spotList = it,
                            legalAddressName = legalAddressName,
                            isRefreshing = false,
                            showLoginBottomSheet = false
                        )
                    }
                }
            }
        }
    }

    private fun onLocationReady(newLocation: Location) = blockingIntent {
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
                condition = (state as? SpotListUiState.Success)?.currentCondition?.toCondition() ?: Condition.Default,
            )
        }

        val legalAddressName = legalAddressNameDeferred.await()
        val spotListResult = spotListResultDeferred.await()
        val isLogin = authRepository.getLoginState().value

        if (legalAddressName == null || spotListResult.isFailure) {
            reduce { SpotListUiState.LoadFailed }
        } else {
            spotListResult.onSuccess {
                reduce {
                    if (isLogin) {
                        (state as? SpotListUiState.Success)?.copy(
                            spotList = it,
                            isRefreshing = false,
                            legalAddressName = legalAddressName
                        ) ?: SpotListUiState.Success(
                            spotList = it,
                            legalAddressName = legalAddressName,
                            isRefreshing = false
                        )
                    } else {
                        (state as? SpotListUiState.Guest)?.copy(
                            spotList = it,
                            isRefreshing = false,
                            legalAddressName = legalAddressName,
                            showLoginBottomSheet = (state as? SpotListUiState.Guest)?.showLoginBottomSheet ?: false
                        ) ?: SpotListUiState.Guest(
                            spotList = it,
                            legalAddressName = legalAddressName,
                            isRefreshing = false,
                            showLoginBottomSheet = false
                        )
                    }
                }
            }
        }
    }

    fun onRefresh(location: Location) = intent {
        reduce {
            when (state) {
                is SpotListUiState.Success -> (state as SpotListUiState.Success).copy(isRefreshing = true)
                is SpotListUiState.Guest -> (state as SpotListUiState.Guest).copy(isRefreshing = true)
                else -> state
            }
        }
        onLocationReady(location)
    }

    fun onLoginBottomSheetShowStateChange(show: Boolean) = intent {
        runOn<SpotListUiState.Guest> {
            reduce {
                state.copy(showLoginBottomSheet = show)
            }
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

    fun onTermOfUse() = intent {
        postSideEffect(SpotListSideEffect.OnTermOfUse)
    }

    fun onPrivatePolicy() = intent {
        postSideEffect(SpotListSideEffect.OnPrivatePolicy)
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

    @Immutable
    data class Guest(
        val spotList: List<Spot> = emptyList(),
        val legalAddressName: String = "",
        val isRefreshing: Boolean = false,
        val currentCondition: ConditionState? = null,
        val showLoginBottomSheet: Boolean = false,
    ) : SpotListUiState

}

sealed interface SpotListSideEffect {
    data object NavigateToAreaVerification : SpotListSideEffect
    data class NavigateToSpotDetail(val id: Long) : SpotListSideEffect
    data object OnTermOfUse : SpotListSideEffect
    data object OnPrivatePolicy : SpotListSideEffect
}