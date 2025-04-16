package com.acon.acon.feature.spot.screen.spotlist

import android.location.Location
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.error.area.GetLegalDongError
import com.acon.acon.domain.error.spot.FetchSpotListError
import com.acon.acon.domain.model.spot.Condition
import com.acon.acon.domain.model.spot.Spot
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.spot.amplitudeClickFilter
import com.acon.acon.feature.spot.state.ConditionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class SpotListViewModel @Inject constructor(
    private val spotRepository: SpotRepository,
    private val userRepository: UserRepository
) : BaseContainerHost<SpotListUiState, SpotListSideEffect>() {

    override val container =
        container<SpotListUiState, SpotListSideEffect>(SpotListUiState.Loading) {
            userType.collectLatest {
                runOn<SpotListUiState.Success> {
                    reduce {
                        state.copy(userType = it)
                    }
                }
            }
        }

    private val userType = userRepository.getUserType().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserType.GUEST
    )

    fun googleLogin(socialRepository: SocialRepository, location: Location) = intent {
        socialRepository.googleLogin()
            .onSuccess {
                if(it.hasVerifiedArea) {
                    fetchSpots(location)
                } else {
                    postSideEffect(SpotListSideEffect.NavigateToAreaVerification)
                }
            }.onFailure {
                postSideEffect(SpotListSideEffect.ShowToastMessage)
            }
    }

    fun fetchSpots(location: Location) = intent {
        val legalAddressNameDeferred = viewModelScope.async {
            spotRepository.getLegalDong(
                latitude = location.latitude,
                longitude = location.longitude
            )
        }

        val spotListResultDeferred = viewModelScope.async {
            spotRepository.fetchSpotList(
                latitude = location.latitude,
                longitude = location.longitude,
                condition = Condition.Default,
            )
        }

        val legalAddressNameResult = legalAddressNameDeferred.await()
        val spotListResult = spotListResultDeferred.await()

        val legalArea = legalAddressNameResult.getOrElse {
            reduce {
                when (it) {
                    is GetLegalDongError.OutOfServiceArea -> SpotListUiState.OutOfServiceArea
                    else -> SpotListUiState.LoadFailed
                }
            }
            return@intent
        }

        spotListResult.reduceResult(
            syntax = this,
            onSuccess = {
                SpotListUiState.Success(
                    spotList = it,
                    legalAddressName = legalArea.area
                )
            }, onFailure = {
                when (it) {
                    is FetchSpotListError.OutOfServiceAreaError -> SpotListUiState.OutOfServiceArea
                    else -> SpotListUiState.LoadFailed
                }
            }
        )
    }

//    private fun onLocationReady(newLocation: Location) = blockingIntent {
//        val legalAddressNameDeferred = viewModelScope.async {
//            spotRepository.getLegalDong(
//                latitude = newLocation.latitude,
//                longitude = newLocation.longitude
//            )
//        }
//
//        val spotListResultDeferred = viewModelScope.async {
//            spotRepository.fetchSpotList(
//                latitude = newLocation.latitude,
//                longitude = newLocation.longitude,
//                condition = (state as? SpotListUiState.Success)?.currentCondition?.toCondition()
//                    ?: Condition.Default,
//            )
//        }
//
//        val legalAddressName = legalAddressNameDeferred.await()
//        val spotListResult = spotListResultDeferred.await()
//        val isLogin = tokenRepository.getIsLogin().getOrElse { false }
//
//        if (legalAddressName.isFailure || spotListResult.isFailure) {
//            legalAddressName.exceptionOrNull()?.let { error ->
//                if (error is FetchSpotListError.OutOfServiceAreaError) {
//                    reduce { SpotListUiState.OutOfServiceArea }
//                }
//            }
//
//            spotListResult.exceptionOrNull()?.let { error ->
//                if (error is FetchSpotListError.OutOfServiceAreaError) {
//                    reduce { SpotListUiState.OutOfServiceArea }
//                }
//
//            }
//
//            reduce { SpotListUiState.LoadFailed }
//        } else {
//            spotListResult.onSuccess { spots ->
//                legalAddressName.onSuccess { legalAddressName ->
//                    reduce {
//                        if (isLogin) {
//                            (state as? SpotListUiState.Success)?.copy(
//                                spotList = spots,
//                                isRefreshing = false,
//                                legalAddressName = legalAddressName.area,
//                                isFilteredListEmpty = spots.isEmpty()
//                            ) ?: SpotListUiState.Success(
//                                spotList = spots,
//                                legalAddressName = legalAddressName.area,
//                                isRefreshing = false
//                            )
//                        } else {
//                            (state as? SpotListUiState.Guest)?.copy(
//                                spotList = spots,
//                                isRefreshing = false,
//                                legalAddressName = legalAddressName.area,
//                                showLoginBottomSheet = (state as? SpotListUiState.Guest)?.showLoginBottomSheet
//                                    ?: false
//                            ) ?: SpotListUiState.Guest(
//                                spotList = spots,
//                                legalAddressName = legalAddressName.area,
//                                isRefreshing = false,
//                                showLoginBottomSheet = false
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }

    fun onRefresh(location: Location) = intent {
        reduce {
            when (state) {
                is SpotListUiState.Success -> (state as SpotListUiState.Success).copy(isRefreshing = true)
                else -> SpotListUiState.Loading
            }
        }
        fetchSpots(location)
    }

    fun onLoginBottomSheetShowStateChange(show: Boolean) = intent {
        runOn<SpotListUiState.Success> {
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
        amplitudeClickFilter()
    }

    fun onResetFilter(location: Location) = intent {
        runOn<SpotListUiState.Success> {
            reduce {
                SpotListUiState.Loading
            }
            fetchSpots(location)
        }
    }

    fun onCompleteFilter(location: Location, condition: ConditionState, proceed: () -> Unit) =
        intent {
            runOn<SpotListUiState.Success> {
                reduce {
                    state.copy(isFilteredResultFetching = true, currentCondition = condition)
                }
                fetchSpots(location)
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
        val userType: UserType = UserType.GUEST,
        val isRefreshing: Boolean = false,
        val isFilteredListEmpty: Boolean = false,
        val currentCondition: ConditionState? = null,
        val showFilterBottomSheet: Boolean = false,
        val showLoginBottomSheet: Boolean = false,
        val isFilteredResultFetching: Boolean = false
    ) : SpotListUiState

    data object Loading : SpotListUiState
    data object LoadFailed : SpotListUiState
    data object OutOfServiceArea : SpotListUiState
}

sealed interface SpotListSideEffect {
    data object ShowToastMessage : SpotListSideEffect
    data object NavigateToAreaVerification : SpotListSideEffect
    data class NavigateToSpotDetail(val id: Long) : SpotListSideEffect
    data object OnTermOfUse : SpotListSideEffect
    data object OnPrivatePolicy : SpotListSideEffect
}