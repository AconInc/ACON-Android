package com.acon.acon.feature.upload.v2

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Immutable
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.upload.v2.SearchedSpot
import com.acon.acon.domain.repository.UploadRepository
import com.acon.acon.feature.upload.BuildConfig
import com.acon.acon.feature.upload.mock.uploadSearchUiStateMock
import com.acon.feature.common.location.getLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class, FlowPreview::class)
@HiltViewModel
class UploadSearchViewModel @Inject constructor(
    private val uploadRepository: UploadRepository,
    @ApplicationContext private val context: Context,
) : BaseContainerHost<UploadSearchUiState, UploadSearchSideEffect>() {

    @SuppressLint("MissingPermission")
    override val container = container<UploadSearchUiState, UploadSearchSideEffect>(UploadSearchUiState.Success()) {
        if (BuildConfig.DEBUG)
            reduce {
                uploadSearchUiStateMock
            }
        val currentLocation = context.getLocation()
        currentLocation?.let {
            uploadRepository.getSuggestions(it.latitude, it.longitude).onSuccess {
                runOn<UploadSearchUiState.Success> {
//                    reduce {
//                        state.copy(
//                            suggestions = it.suggestions
//                        )
//                    }
                }
            }.onFailure {

            }
        }
        queryFlow
            .debounce(300)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .collectLatest { query ->
                // TODO : 검색 결과 조회
//                val result = runCatching { searchRepository.search(query) }
//                runOn<UploadSearchUiState.Success> {
//                    reduce {
//                        state.copy(
//                            searchedSpots = result.getOrDefault(emptyList())
//                        )
//                    }
//                }
            }
    }

    private val queryFlow = MutableStateFlow("")

    fun onSearchQueryChanged(query: String) = intent {
        runOn<UploadSearchUiState.Success> {
            reduce {
                state.copy(
                    query = query
                )
            }
            queryFlow.value = query
        }
    }

    fun onBackAction() = intent {
        postSideEffect(UploadSearchSideEffect.NavigateBack)
    }

    fun onNextAction() = intent {
        postSideEffect(UploadSearchSideEffect.NavigateToReviewScreen)
    }
}

sealed interface UploadSearchUiState {
    @Immutable
    data class Success(
        val suggestions: List<String> = listOf(),
        val query: String = "",
        val searchedSpots: List<SearchedSpot> = listOf(),
    ) : UploadSearchUiState
    data object LoadFailed : UploadSearchUiState
}

sealed interface UploadSearchSideEffect {
    data object NavigateToReviewScreen : UploadSearchSideEffect
    data object NavigateBack : UploadSearchSideEffect
}