package com.acon.acon.feature.spot.screen.spotlist

import android.content.Context
import android.location.Location
import androidx.compose.runtime.Immutable
import com.acon.acon.domain.error.spot.FetchSpotListError
import com.acon.acon.domain.model.spot.Condition
import com.acon.acon.domain.model.spot.Filter
import com.acon.acon.domain.model.spot.v2.Spot
import com.acon.acon.domain.model.spot.v2.SpotList
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.domain.type.CafeFilterType
import com.acon.acon.domain.type.CategoryType
import com.acon.acon.domain.type.RestaurantFilterType
import com.acon.acon.domain.type.SpotType
import com.acon.acon.domain.type.TransportMode
import com.acon.acon.domain.usecase.IsDistanceExceededUseCase
import com.acon.feature.common.base.BaseContainerHost
import com.acon.feature.common.location.isInKorea
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
@OptIn(OrbitExperimental::class)
class SpotListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val spotRepository: SpotRepository,
    private val isDistanceExceededUseCase: IsDistanceExceededUseCase
) : BaseContainerHost<SpotListUiStateV2, SpotListSideEffectV2>() {

    override val container =
        container<SpotListUiStateV2, SpotListSideEffectV2>(SpotListUiStateV2.Loading(SpotType.RESTAURANT))

    private lateinit var initialLocation: Location

    override fun onNewLocation(location: Location) {
        intent {
            runOn<SpotListUiStateV2.Success> {
                reduce {
                    state.copy(currentLocation = location)
                }
                if (isDistanceExceededUseCase(
                        initialLocation.latitude,
                        initialLocation.longitude,
                        location.latitude,
                        location.longitude
                    )
                ) {
                    reduce {
                        state.copy(showRefreshPopup = true)
                    }
                }
            }

            runOn<SpotListUiStateV2.Loading> {
                initialLocation = location
                if (location.isInKorea(context)) {
                    fetchSpotList(location, Condition(SpotType.RESTAURANT, emptyList())) {
                        SpotListUiStateV2.Success(
                            transportMode = it.transportMode,
                            spotList = it.spots,
                            headTitle = "최고의 선택.",
                            selectedSpotType = SpotType.RESTAURANT,
                            currentLocation = location
                        )
                    }
                } else {
                    reduce {
                        SpotListUiStateV2.OutOfServiceArea(state.selectedSpotType)
                    }
                }
            }
        }
    }

    fun onSpotTypeClicked(spotType: SpotType) = intent {
        runOn<SpotListUiStateV2.Success> {
            if (spotType == state.selectedSpotType) return@runOn
            reduce {
                state.copy(selectedSpotType = spotType)
            }

            fetchSpotList(
                location = state.currentLocation,
                condition = Condition(spotType, emptyList())
            ) {
                SpotListUiStateV2.Success(
                    transportMode = it.transportMode,
                    spotList = it.spots,
                    headTitle = "최고의 선택.",
                    selectedSpotType = spotType,
                    currentLocation = state.currentLocation,
                    selectedRestaurantFilters = emptyMap(),
                    selectedCafeFilters = emptyMap()
                )
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

    private fun fetchSpotList(location: Location, condition: Condition, onSuccess: (SpotList) -> SpotListUiStateV2.Success) = intent {
        spotRepository.fetchSpotList(
            latitude = location.latitude,
            longitude = location.longitude,
            condition = condition
        ).reduceResult(
            onSuccess = onSuccess,
            onFailure = { e ->
                if (e is FetchSpotListError.OutOfServiceArea)
                    SpotListUiStateV2.OutOfServiceArea(state.selectedSpotType)
                else
                    SpotListUiStateV2.LoadFailed(state.selectedSpotType)
            }
        )
    }

    fun retry() = intent {
        reduce {
            SpotListUiStateV2.Loading(state.selectedSpotType)
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
                            RestaurantFilterType.RestaurantOperationType::class -> CategoryType.OPENING_HOURS
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
                            CafeFilterType.CafeOperationType::class -> CategoryType.OPENING_HOURS
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
    val selectedSpotType: SpotType

    @Immutable
    data class Success(
        override val selectedSpotType: SpotType,
        val transportMode: TransportMode,
        val spotList: List<Spot>,
        val headTitle: String,
        val currentLocation: Location,
        val selectedRestaurantFilters: Map<FilterDetailKey, Set<RestaurantFilterType>> = emptyMap(),
        val selectedCafeFilters: Map<FilterDetailKey, Set<CafeFilterType>> = emptyMap(),
        val showFilterModal: Boolean = false,
        val showRefreshPopup: Boolean = false
    ) : SpotListUiStateV2

    data class Loading(
        override val selectedSpotType: SpotType,
    ) : SpotListUiStateV2

    data class LoadFailed(
        override val selectedSpotType: SpotType,
    ) : SpotListUiStateV2

    data class OutOfServiceArea(
        override val selectedSpotType: SpotType,
    ) : SpotListUiStateV2
}

sealed interface SpotListSideEffectV2 {
    data object ShowToastMessage : SpotListSideEffectV2
    data class NavigateToExternalMap(val start: Location, val destination: Location) : SpotListSideEffectV2
    data class NavigateToSpotDetailScreen(val spot: Spot) : SpotListSideEffectV2
}

internal typealias FilterDetailKey = KClass<out Enum<*>>
