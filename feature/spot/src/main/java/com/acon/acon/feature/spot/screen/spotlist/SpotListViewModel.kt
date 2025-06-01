package com.acon.acon.feature.spot.screen.spotlist

import android.location.Location
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.spot.Condition
import com.acon.acon.domain.model.spot.Filter
import com.acon.acon.domain.model.spot.v2.Spot
import com.acon.acon.domain.model.spot.v2.SpotList
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.CafeFilterType
import com.acon.acon.domain.type.CategoryType
import com.acon.acon.domain.type.RestaurantFilterType
import com.acon.acon.domain.type.SpotType
import com.acon.acon.domain.type.TransportMode
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.spot.BuildConfig
import com.acon.acon.feature.spot.mock.spotListUiStateCafeMock
import com.acon.acon.feature.spot.mock.spotListUiStateRestaurantMock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
@OptIn(OrbitExperimental::class)
class SpotListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val spotRepository: SpotRepository
) : BaseContainerHost<SpotListUiStateV2, SpotListSideEffectV2>() {

    override val container =
        container<SpotListUiStateV2, SpotListSideEffectV2>(SpotListUiStateV2.Loading)

    val userType = userRepository.getUserType().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserType.GUEST
    )

    fun onNewLocationEmitted(location: Location) = intent {
        runOn<SpotListUiStateV2.Success> {
            reduce {
                state.copy(currentLocation = location)
            }
        }

        runOn<SpotListUiStateV2.Loading> {
//            fetchSpotList(location, Condition(SpotType.RESTAURANT, emptyList())) {
//                SpotListUiStateV2.Success(
//                    transportMode = it.transportMode,
//                    spotList = it.spots,
//                    headTitle = "최고의 선택.",
//                    selectedSpotType = SpotType.RESTAURANT,
//                    currentLocation = location
//                )
//            }

            if (BuildConfig.DEBUG) {
                reduce {
                    spotListUiStateRestaurantMock
                }
            }
        }
    }

    fun onSpotTypeClicked(spotType: SpotType) = intent {
        runOn<SpotListUiStateV2.Success> {
            if (spotType == state.selectedSpotType) return@runOn
            if (userType.value == UserType.GUEST)
                reduce {
                    state.copy(showLoginModal = true)
                }
            else {
//            fetchSpotList(
//                location = state.currentLocation,
//                condition = Condition(spotType, emptyList())
//            ) {
//                SpotListUiStateV2.Success(
//                    transportMode = it.transportMode,
//                    spotList = it.spots,
//                    headTitle = "최고의 선택.",
//                    selectedSpotType = spotType,
//                    currentLocation = state.currentLocation,
//                    selectedRestaurantFilters = emptyMap(),
//                    selectedCafeFilters = emptyMap()
//                )
//            }

                if (BuildConfig.DEBUG) {
                    if (spotType == SpotType.RESTAURANT)
                        reduce {
                            spotListUiStateRestaurantMock
                        }
                    else {
                        reduce {
                            spotListUiStateCafeMock
                        }
                    }
                }
            }
        }
    }

    fun onSpotClicked(spot: Spot) = intent {
        runOn<SpotListUiStateV2.Success> {
            postSideEffect(SpotListSideEffectV2.NavigateToSpotDetailScreen(spot))
        }
    }

    fun onTryFindWay(spot: Spot) = intent {
        runOn<SpotListUiStateV2.Success> {
            postSideEffect(SpotListSideEffectV2.NavigateToExternalMap(
                start = state.currentLocation,
                destination = Location("").apply {
                    latitude = spot.latitude
                    longitude = spot.longitude
                }
            ))
        }
    }

    fun onFilterButtonClicked() = intent {
        runOn<SpotListUiStateV2.Success> {
            if (userType.value == UserType.GUEST)
                reduce {
                    state.copy(showLoginModal = true)
                }
            else
                reduce {
                    state.copy(showFilterModal = true)
                }
        }
    }

    fun onFilterModalDismissed() = intent {
        runOn<SpotListUiStateV2.Success> {
            reduce {
                state.copy(showFilterModal = false)
            }
        }
    }

    fun onRestaurantFilterSaved(
        selectedRestaurantFilters: Map<FilterDetailKey, Set<RestaurantFilterType>>,
    ) = intent {
        runOn<SpotListUiStateV2.Success> {
            reduce {
                state.copy(
                    selectedRestaurantFilters = selectedRestaurantFilters,
                    showFilterModal = false
                )
            }
            fetchSpotList(
                location = state.currentLocation,
                condition = mapCondition(state)
            ) {
                state.copy(
                    transportMode = it.transportMode,
                    spotList = it.spots,
                )
            }
        }
    }

    fun onCafeFilterSaved(
        selectedCafeFilters: Map<FilterDetailKey, Set<CafeFilterType>>,
    ) = intent {
        runOn<SpotListUiStateV2.Success> {
            reduce {
                state.copy(
                    selectedCafeFilters = selectedCafeFilters,
                    showFilterModal = false
                )
            }
            fetchSpotList(
                location = state.currentLocation,
                condition = mapCondition(state)
            ) {
                state.copy(
                    transportMode = it.transportMode,
                    spotList = it.spots,
                )
            }
        }
    }

    fun onRequestLogin() = intent {
        runOn<SpotListUiStateV2.Success> {
            reduce {
                state.copy(showLoginModal = true)
            }
        }
    }

    fun onDismissLoginModal() = intent {
        runOn<SpotListUiStateV2.Success> {
            reduce {
                state.copy(showLoginModal = false)
            }
        }
    }

    private fun fetchSpotList(location: Location, condition: Condition, onSuccess: (SpotList) -> SpotListUiStateV2.Success) = intent {
        spotRepository.fetchSpotList(
            latitude = location.latitude,
            longitude = location.longitude,
            condition = condition
        ).reduceResult(
            syntax = this@intent,
            onSuccess = onSuccess,
            onFailure = {
                SpotListUiStateV2.LoadFailed
            }
        )
    }

    fun retry() = intent {
        runOn<SpotListUiStateV2.LoadFailed> {
            reduce {
                SpotListUiStateV2.Loading
            }
        }
    }

    private fun mapCondition(state: SpotListUiStateV2.Success): Condition {
        return Condition(
            spotType = state.selectedSpotType,
            filterList = if (state.selectedSpotType == SpotType.RESTAURANT) {
                state.selectedRestaurantFilters.map {
                    Filter(
                        category = when (it.key) {
                            RestaurantFilterType.RestaurantType::class -> CategoryType.RESTAURANT_FEATURE
                            RestaurantFilterType.RestaurantOperationType::class -> CategoryType.OPENING_HOUR
                            RestaurantFilterType.RestaurantPriceType::class -> CategoryType.PRICE
                            else -> throw IllegalArgumentException("Unknown filter type")
                        },
                        optionList = it.value.toList()
                    )
                }
            } else {
                state.selectedCafeFilters.map {
                    Filter(
                        category = when (it.key) {
                            CafeFilterType.CafeType::class -> CategoryType.CAFE_FEATURE
                            CafeFilterType.CafeOperationType::class -> CategoryType.OPENING_HOUR
                            else -> throw IllegalArgumentException("Unknown filter type")
                        },
                        optionList = it.value.toList()
                    )
                }
            }
        )
    }
}

sealed interface SpotListUiStateV2 {
    @Immutable
    data class Success(
        val transportMode: TransportMode,
        val spotList: List<Spot>,
        val headTitle: String,
        val selectedSpotType: SpotType,
        val currentLocation: Location,
        val selectedRestaurantFilters: Map<FilterDetailKey, Set<RestaurantFilterType>> = emptyMap(),
        val selectedCafeFilters: Map<FilterDetailKey, Set<CafeFilterType>> = emptyMap(),
        val showFilterModal: Boolean = false,
        val showLoginModal: Boolean = false,
    ) : SpotListUiStateV2
    data object Loading : SpotListUiStateV2
    data object LoadFailed : SpotListUiStateV2
}

sealed interface SpotListSideEffectV2 {
    data object ShowToastMessage : SpotListSideEffectV2
    data class NavigateToExternalMap(val start: Location, val destination: Location) : SpotListSideEffectV2
    data class NavigateToSpotDetailScreen(val spot: Spot) : SpotListSideEffectV2
}

internal typealias FilterDetailKey = KClass<out Enum<*>>
