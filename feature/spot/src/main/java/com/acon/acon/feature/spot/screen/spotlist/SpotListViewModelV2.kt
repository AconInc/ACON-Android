package com.acon.acon.feature.spot.screen.spotlist

import android.content.Context
import android.location.Location
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.spot.v2.SpotV2
import com.acon.acon.domain.repository.SpotRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.SpotType
import com.acon.acon.domain.type.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
@OptIn(OrbitExperimental::class)
class SpotListViewModelV2 @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val spotRepository: SpotRepository
) : BaseContainerHost<SpotListUiStateV2, SpotListSideEffectV2>() {

    override val container = container<SpotListUiStateV2, SpotListSideEffectV2>(SpotListUiStateV2.Loading) {
        runOn<SpotListUiStateV2.Success> {
            with(viewModelScope) {
                launch {
                    userRepository.getUserType().collectLatest {
                        reduce {
                            state.copy(userType = it)
                        }
                    }
                }
            }
        }
    }

    fun onNewLocationEmitted(location: Location) = intent {
        runOn<SpotListUiStateV2.Loading> {
            // Repository Call
        }
        runOn<SpotListUiStateV2.Success> {
            reduce {
                state.copy(currentLocation = location)
            }
        }
    }

    fun onSpotTypeChanged(spotType: SpotType) = intent {
        runOn<SpotListUiStateV2.Success> {
            reduce {
                state.copy(selectedSpotType = spotType)
            }
        }
    }
}

sealed interface SpotListUiStateV2 {
    @Immutable
    data class Success(
        val spotList: List<SpotV2>,
        val headTitle: String,
        val selectedSpotType: SpotType,
        val currentLocation: Location,
        val userType: UserType = UserType.GUEST,
    ) : SpotListUiStateV2
    data object Loading : SpotListUiStateV2
    data object LoadFailed : SpotListUiStateV2
}

sealed interface SpotListSideEffectV2 {
    data object ShowToastMessage : SpotListSideEffectV2
    data object NavigateToExternalMap : SpotListSideEffectV2
    data object NavigateToSpotDetailScreen : SpotListSideEffectV2
}
