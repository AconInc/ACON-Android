package com.acon.acon.feature.spot.screen.spotlist

import android.content.Context
import android.location.Location
import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.fastForEach
import com.acon.acon.domain.error.spot.FetchSpotListError
import com.acon.acon.core.model.model.spot.Condition
import com.acon.acon.core.model.model.spot.Filter
import com.acon.acon.core.model.model.spot.Spot
import com.acon.acon.core.model.model.spot.SpotList
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.core.model.type.CafeFilterType
import com.acon.acon.core.model.type.CategoryType
import com.acon.acon.core.model.type.RestaurantFilterType
import com.acon.acon.core.model.type.SpotType
import com.acon.acon.core.model.type.TagType
import com.acon.acon.core.model.type.TransportMode
import com.acon.acon.domain.usecase.IsDistanceExceededUseCase
import com.acon.core.analytics.amplitude.AconAmplitude
import com.acon.core.analytics.constants.EventNames
import com.acon.core.analytics.constants.PropertyKeys
import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.core.ui.android.NavigationAppHandler
import com.acon.acon.core.ui.android.isInKorea
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
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
        container<SpotListUiStateV2, SpotListSideEffectV2>(SpotListUiStateV2.Loading(com.acon.acon.core.model.type.SpotType.RESTAURANT))

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
                    fetchSpotList(location,
                        com.acon.acon.core.model.model.spot.Condition(
                            state.selectedSpotType,
                            emptyList()
                        )
                    ) {
                        SpotListUiStateV2.Success(
                            transportMode = it.transportMode,
                            spotList = it.spots,
                            headTitle = "최고의 선택.",
                            selectedSpotType = state.selectedSpotType,
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

    fun onSpotTypeClicked(spotType: com.acon.acon.core.model.type.SpotType) = intent {
        val captureState = state as? SpotListUiStateV2.Success
        if (spotType == state.selectedSpotType) return@intent

        runOn<SpotListUiStateV2.Success> {
            reduce {
                SpotListUiStateV2.Loading(selectedSpotType = spotType)
            }
        }
        AconAmplitude.trackEvent(
            eventName = EventNames.MAIN_MENU,
            property = PropertyKeys.CLICK_TOGGLE to true
        )

        delay(500L)
        if (captureState != null) {
            fetchSpotList(
                location = captureState.currentLocation,
                condition = com.acon.acon.core.model.model.spot.Condition(spotType, emptyList())
            ) {
                SpotListUiStateV2.Success(
                    transportMode = it.transportMode,
                    spotList = it.spots,
                    headTitle = "최고의 선택.",
                    selectedSpotType = spotType,
                    currentLocation = captureState.currentLocation,
                    selectedRestaurantFilters = emptyMap(),
                    selectedCafeFilters = emptyMap()
                )
            }
        }
    }

    fun onSpotClicked(spot: com.acon.acon.core.model.model.spot.Spot, rank: Int) = intent {
        runOn<SpotListUiStateV2.Success> {
            val tags = spot.tags.toMutableList()
            when (rank) {
                1 -> tags.add(com.acon.acon.core.model.type.TagType.TOP_1)
                2 -> tags.add(com.acon.acon.core.model.type.TagType.TOP_2)
                3 -> tags.add(com.acon.acon.core.model.type.TagType.TOP_3)
                4 -> tags.add(com.acon.acon.core.model.type.TagType.TOP_4)
                5 -> tags.add(com.acon.acon.core.model.type.TagType.TOP_5)
                else -> {}
            }
            if (tags.isEmpty()) {
                AconAmplitude.trackEvent(
                    eventName = EventNames.MAIN_MENU,
                    property = PropertyKeys.CLICK_DETAIL_TAG_NONE to true
                )
            } else {
                tags.fastForEach { tag ->
                    AconAmplitude.trackEvent(
                        eventName = EventNames.MAIN_MENU,
                        property = when(tag) {
                            com.acon.acon.core.model.type.TagType.NEW -> PropertyKeys.CLICK_DETAIL_TAG_NEW to true
                            com.acon.acon.core.model.type.TagType.LOCAL -> PropertyKeys.CLICK_DETAIL_TAG_LOCAL to true
                            else -> PropertyKeys.CLICK_DETAIL_TAG_RANK to true
                        }
                    )
                }
            }

            postSideEffect(
                SpotListSideEffectV2.NavigateToSpotDetailScreen(
                    spot.copy(
                        tags = tags
                    ), state.transportMode
                )
            )
        }
    }

    fun onTryFindWay(spot: com.acon.acon.core.model.model.spot.Spot) = intent {
        runOn<SpotListUiStateV2.Success> {
            reduce {
                state.copy(
                    showChooseNavigationAppModal = true
                )
            }
        }
    }

    fun onNavigationAppChosen(handler: NavigationAppHandler) = intent {
        AconAmplitude.trackEvent(
            eventName = EventNames.MAIN_MENU,
            property = PropertyKeys.CLICK_HOME_NAVIGATION to true
        )

        runOn<SpotListUiStateV2.Success> {
            postSideEffect(SpotListSideEffectV2.NavigateToExternalMap(handler))
            reduce {
                state.copy(
                    showChooseNavigationAppModal = false
                )
            }
        }
    }

    fun onChooseNavigationAppModalDismissed() = intent {
        runOn<SpotListUiStateV2.Success> {
            reduce {
                state.copy(showChooseNavigationAppModal = false)
            }
        }
    }

    fun onFilterButtonClicked() = intent {
        AconAmplitude.trackEvent(
            eventName = EventNames.MAIN_MENU,
            property = PropertyKeys.CLICK_FILTER to true
        )

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
        selectedRestaurantFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.RestaurantFilterType>>,
    ) = intent {
        val captureState = state as? SpotListUiStateV2.Success
        reduce {
            SpotListUiStateV2.Loading(
                selectedSpotType = state.selectedSpotType,
                selectedRestaurantFilters = selectedRestaurantFilters,
            )
        }

        delay(500L)
        if (captureState != null) {
            fetchSpotList(
                location = captureState.currentLocation,
                condition = mapCondition(state)
            ) {
                SpotListUiStateV2.Success(
                    transportMode = it.transportMode,
                    spotList = it.spots,
                    headTitle = "최고의 선택.",
                    selectedSpotType = captureState.selectedSpotType,
                    currentLocation = captureState.currentLocation,
                    selectedRestaurantFilters = state.selectedRestaurantFilters,
                    selectedCafeFilters = emptyMap()
                )
            }
        }
    }

    fun onCafeFilterSaved(
        selectedCafeFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.CafeFilterType>>,
    ) = intent {
        val captureState = state as? SpotListUiStateV2.Success
        reduce {
            SpotListUiStateV2.Loading(
                selectedSpotType = state.selectedSpotType,
                selectedCafeFilters = selectedCafeFilters,
            )
        }

        delay(500L)
        if (captureState != null) {
            fetchSpotList(
                location = captureState.currentLocation,
                condition = mapCondition(state)
            ) {
                SpotListUiStateV2.Success(
                    transportMode = it.transportMode,
                    spotList = it.spots,
                    headTitle = "최고의 선택.",
                    selectedSpotType = captureState.selectedSpotType,
                    currentLocation = captureState.currentLocation,
                    selectedRestaurantFilters = emptyMap(),
                    selectedCafeFilters = state.selectedCafeFilters
                )
            }
        }
    }

    private fun fetchSpotList(location: Location, condition: com.acon.acon.core.model.model.spot.Condition, onSuccess: (com.acon.acon.core.model.model.spot.SpotList) -> SpotListUiStateV2.Success) = intent {
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

    private fun mapCondition(state: SpotListUiStateV2): com.acon.acon.core.model.model.spot.Condition {
        return com.acon.acon.core.model.model.spot.Condition(
            spotType = state.selectedSpotType,
            filterList = if (state.selectedSpotType == com.acon.acon.core.model.type.SpotType.RESTAURANT) {
                state.selectedRestaurantFilters.map {
                    com.acon.acon.core.model.model.spot.Filter(
                        category = when (it.key) {
                            com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType::class -> com.acon.acon.core.model.type.CategoryType.RESTAURANT_FEATURE
                            com.acon.acon.core.model.type.RestaurantFilterType.RestaurantOperationType::class -> com.acon.acon.core.model.type.CategoryType.OPENING_HOURS
                            com.acon.acon.core.model.type.RestaurantFilterType.RestaurantPriceType::class -> com.acon.acon.core.model.type.CategoryType.PRICE
                            else -> throw IllegalArgumentException("Unknown filter type")
                        },
                        optionList = it.value.toList()
                    )
                }
            } else {
                state.selectedCafeFilters.map {
                    com.acon.acon.core.model.model.spot.Filter(
                        category = when (it.key) {
                            com.acon.acon.core.model.type.CafeFilterType.CafeType::class -> com.acon.acon.core.model.type.CategoryType.CAFE_FEATURE
                            com.acon.acon.core.model.type.CafeFilterType.CafeOperationType::class -> com.acon.acon.core.model.type.CategoryType.OPENING_HOURS
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
    val selectedSpotType: com.acon.acon.core.model.type.SpotType
    val selectedRestaurantFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.RestaurantFilterType>>
    val selectedCafeFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.CafeFilterType>>

    @Immutable
    data class Success(
        override val selectedSpotType: com.acon.acon.core.model.type.SpotType,
        val transportMode: com.acon.acon.core.model.type.TransportMode,
        val spotList: List<com.acon.acon.core.model.model.spot.Spot>,
        val headTitle: String,
        val currentLocation: Location,
        override val selectedRestaurantFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.RestaurantFilterType>> = emptyMap(),
        override val selectedCafeFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.CafeFilterType>> = emptyMap(),
        val showFilterModal: Boolean = false,
        val showRefreshPopup: Boolean = false,
        val showChooseNavigationAppModal: Boolean = false
    ) : SpotListUiStateV2

    data class Loading(
        override val selectedSpotType: com.acon.acon.core.model.type.SpotType,
        override val selectedRestaurantFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.RestaurantFilterType>> = emptyMap(),
        override val selectedCafeFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.CafeFilterType>> = emptyMap(),
    ) : SpotListUiStateV2

    data class LoadFailed(
        override val selectedSpotType: com.acon.acon.core.model.type.SpotType,
        override val selectedRestaurantFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.RestaurantFilterType>> = emptyMap(),
        override val selectedCafeFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.CafeFilterType>> = emptyMap(),
    ) : SpotListUiStateV2

    data class OutOfServiceArea(
        override val selectedSpotType: com.acon.acon.core.model.type.SpotType,
        override val selectedRestaurantFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.RestaurantFilterType>> = emptyMap(),
        override val selectedCafeFilters: Map<FilterDetailKey, Set<com.acon.acon.core.model.type.CafeFilterType>> = emptyMap(),
    ) : SpotListUiStateV2
}

sealed interface SpotListSideEffectV2 {
    data object ShowToastMessage : SpotListSideEffectV2
    data class NavigateToExternalMap(val handler: NavigationAppHandler) : SpotListSideEffectV2
    data class NavigateToSpotDetailScreen(val spot: com.acon.acon.core.model.model.spot.Spot, val transportMode: com.acon.acon.core.model.type.TransportMode) : SpotListSideEffectV2
}

internal typealias FilterDetailKey = KClass<out Enum<*>>
