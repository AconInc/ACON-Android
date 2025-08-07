package com.acon.acon.feature.upload.screen

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.model.model.upload.Feature
import com.acon.acon.core.model.model.upload.SearchedSpotByMap
import com.acon.acon.core.model.type.CafeFeatureType
import com.acon.acon.core.model.type.CategoryType
import com.acon.acon.core.model.type.PriceFeatureType
import com.acon.acon.core.model.type.RestaurantFeatureType
import com.acon.acon.core.model.type.SpotType
import com.acon.acon.domain.repository.MapSearchRepository
import com.acon.acon.domain.repository.UploadRepository
import com.acon.acon.feature.upload.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ConnectionPool
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class UploadPlaceViewModel @Inject constructor(
    private val mapSearchRepository: MapSearchRepository,
    private val uploadRepository: UploadRepository,
    application: Application
) : AndroidViewModel(application), ContainerHost<UploadPlaceUiState, UploadPlaceSideEffect> {

    override val container =
        container<UploadPlaceUiState, UploadPlaceSideEffect>(UploadPlaceUiState()) {
            viewModelScope.launch {
                queryFlow
                    .debounce(100)
                    .distinctUntilChanged()
                    .collect { query ->
                        if (query.isBlank()) {
                            reduce {
                                state.copy(
                                    recommendMenu = "",
                                )
                            }
                        } else {
                            reduce {
                                state.copy(
                                    recommendMenu = query,
                                )
                            }
                        }
                    }
            }

            viewModelScope.launch {
                searchPlaceQueryFlow
                    .debounce(100)
                    .distinctUntilChanged()
                    .collect { query ->
                        if (query.isBlank()) {
                            reduce {
                                state.copy(
                                    searchedSpotsByMap = emptyList(),
                                    showSearchedSpotsByMap = false,
                                    isNextBtnEnabled = false
                                )
                            }
                        } else {
                            mapSearchRepository.fetchMapSearch(query).onSuccess {
                                reduce {
                                    state.copy(
                                        searchedSpotsByMap = it
                                    )
                                }
                            }.onFailure {
                                if (it.message?.contains("HTTP 429") == true) {
                                    reduce {
                                        state.copy(
                                            showUploadPlaceLimitDialog = true
                                        )
                                    }
                                }
                            }
                        }
                    }
            }
        }

    private val queryFlow = MutableStateFlow("")
    private val searchPlaceQueryFlow = MutableStateFlow("")

    fun onSearchQueryChanged(query: String) = intent {
        queryFlow.value = query
    }

    fun onSearchQueryOrSelectionChanged(query: String, isSelection: Boolean) = intent {
        reduce {
            if (isSelection)
                state.copy(
                    showSearchedSpotsByMap = false
                )
            else
                state.copy(
                    selectedSpotByMap = state.selectedSpotByMap?.takeIf { it.title == query },
                    showSearchedSpotsByMap = query.isNotBlank()
                )
        }
        searchPlaceQueryFlow.value = query
    }

    fun onSearchSpotByMapClicked(searchedSpotByMap: SearchedSpotByMap, onUpdateTextField: () -> Unit) = intent {
        onUpdateTextField()
        reduce {
            state.copy(
                selectedSpotByMap = searchedSpotByMap,
                showSearchedSpotsByMap = false,
                isNextBtnEnabled = true
            )
        }
    }

    fun onPreviousBtnDisabled() = intent {
        reduce { state.copy(isPreviousBtnEnabled = false)}
    }

    fun onPreviousBtnEnabled() = intent {
        reduce { state.copy(isPreviousBtnEnabled = true)}
    }

    fun updateNextBtnEnabled(isEnabled: Boolean) = intent {
        reduce { state.copy(isNextBtnEnabled = isEnabled)}
    }

    fun updateSpotType(spotType: SpotType) = intent {
        reduce { state.copy(selectedSpotType = spotType) }
    }

    fun updateCafeOptionType(cafeOption: CafeFeatureType.CafeType) = intent {
        reduce { state.copy(selectedCafeOption = cafeOption) }
    }

    fun updatePriceOptionType(priceOption: PriceFeatureType.PriceOptionType) = intent {
        reduce { state.copy(selectedPriceOption = priceOption) }
    }

    fun updateRestaurantType(type: RestaurantFeatureType.RestaurantType) = intent {
        reduce {
            val currentSelectedTypes = state.selectedRestaurantTypes.toMutableList()

            if (currentSelectedTypes.contains(type)) {
                currentSelectedTypes.remove(type)
            } else {
                currentSelectedTypes.add(type)
            }
            state.copy(selectedRestaurantTypes = currentSelectedTypes)
        }
    }

    fun onAddImageUris(uris: List<Uri>) = intent {
        reduce {
            val currentUris = state.selectedImageUris ?: emptyList()
            val canAddCount = state.maxImageCount - currentUris.size

            if (canAddCount <= 0) {
                state
            } else {
                val toAdd = uris.take(canAddCount)
                state.copy(selectedImageUris = currentUris.plus(toAdd))
            }
        }
    }

    fun onRemoveImageUri(uri: Uri) = intent {
        val currentUris = state.selectedImageUris?.toMutableList()
        val removedSuccessfully = currentUris?.remove(uri)

        if (removedSuccessfully == true) {
            reduce {
                state.copy(selectedImageUris = currentUris)
            }
        }
    }

    fun onRequestRemoveUploadPlaceImageDialog(uri: Uri) = intent {
        reduce {
            state.copy(
                showRemoveUploadPlaceImageDialog = true,
                selectedUriToRemove = uri
            )
        }
    }

    fun onDismissRemoveUploadPlaceImageDialog() = intent {
        reduce {
            state.copy(
                showRemoveUploadPlaceImageDialog = false,
                selectedUriToRemove = null
            )
        }
    }

    fun goToNextStep(lastStepIndex: Int) = intent {
        if (state.currentStep < lastStepIndex) {
            reduce {
                state.copy(currentStep = state.currentStep + 1)
            }
        }
    }

    fun goToPreviousStep() = intent {
        if (state.currentStep > 0) {
            reduce {
                state.copy(currentStep = state.currentStep - 1)
            }
        }
    }

    fun onRequestExitUploadPlaceDialog() = intent {
        reduce {
            state.copy(showExitUploadPlaceDialog = true)
        }
    }

    fun onDismissExitUploadPlaceDialog() = intent {
        reduce {
            state.copy(showExitUploadPlaceDialog = false)
        }
    }

    fun onHideSearchedPlaceList() = intent {
        reduce {
            state.copy(
                showSearchedSpotsByMap = false
            )
        }
    }

    fun onRequestUploadPlaceLimitPouUp() = intent {
        reduce {
            state.copy(
                showUploadPlaceLimitPouUp = true
            )
        }

        viewModelScope.launch {
            delay(3500)
            intent {
                reduce {
                    state.copy(showUploadPlaceLimitPouUp = false)
                }
            }
        }
    }

    fun onSlideAnimationEnd(route: String) = intent {
        reduce {
            val updatedMap = state.hasAnimated.toMutableMap().apply { this[route] = true }
            state.copy(hasAnimated = updatedMap)
        }
    }

    fun onNavigateToBack() = intent {
        postSideEffect(UploadPlaceSideEffect.OnNavigateToBack)
    }

    fun onClickReportPlace() = intent {
        postSideEffect(UploadPlaceSideEffect.OnMoveToReportPlace)
    }

    private fun createFeatureList() = intent {
        val featureRequests = mutableListOf<Feature>()

        when(state.selectedRestaurantTypes.isEmpty()) {
            true -> {
                state.selectedCafeOption?.let { cafeOption ->
                    if (cafeOption == CafeFeatureType.CafeType.NOT_GOOD_FOR_WORK) {
                        featureRequests.add(
                            Feature(
                                category = CategoryType.CAFE_FEATURE,
                                optionList = emptyList()
                            )
                        )
                    } else {
                        featureRequests.add(
                            Feature(
                                category = CategoryType.CAFE_FEATURE,
                                optionList = listOf(cafeOption)
                            )
                        )
                    }
                }
            }
            false -> {
                featureRequests.add(
                    Feature(
                        category = CategoryType.RESTAURANT_FEATURE,
                        optionList = state.selectedRestaurantTypes.map { it }
                    )
                )
            }
        }

        state.selectedPriceOption?.let { priceOption ->
            featureRequests.add(
                Feature(
                    category = CategoryType.PRICE,
                    optionList = listOf(priceOption)
                )
            )
        }

        reduce {
            state.copy(
                featureList = featureRequests.toImmutableList()
            )
        }
    }

    fun onSubmitUploadPlace(onSuccess:() -> Unit) = intent {
        createFeatureList()
        reduce { state.copy(isNextBtnEnabled = false) }
        when (state.selectedImageUris?.isEmpty()) {
            true -> {
                submitUploadPlace(onSuccess)
            }

            false -> {
                uploadAllImagesAndSubmit(onSuccess)
            }

            null -> {}
        }
    }

    private fun submitUploadPlace(
        onSuccess:() -> Unit,
        imageList: List<String> = emptyList()
    ) = intent {
        uploadRepository.submitUploadPlace(
            spotName = state.selectedSpotByMap?.title ?: "",
            address = state.selectedSpotByMap?.address ?: "",
            spotType = state.selectedSpotType ?: SpotType.CAFE,
            featureList = state.featureList ?: emptyList(),
            recommendedMenu = state.recommendMenu ?: "",
            imageList = imageList
        ).onSuccess {
            reduce { state.copy(isNextBtnEnabled = true) }
            onSuccess()
        }.onFailure {
            reduce { state.copy(isNextBtnEnabled = true) }
            postSideEffect(UploadPlaceSideEffect.ShowToastUploadFailed)
        }
    }

    private fun uploadAllImagesAndSubmit(onSuccess: () -> Unit) = intent {
        val uris = state.selectedImageUris ?: emptyList()

        if (uris.isEmpty()) {
            submitUploadPlace(onSuccess = onSuccess, imageList = emptyList())
            return@intent
        }

        // Step 1: Get all presigned URLs concurrently (like iOS DispatchGroup)
        val presignedResults = runCatching {
            coroutineScope {
                (0 until uris.size).map {
                    async(Dispatchers.IO) {
                        uploadRepository.getUploadPlacePreSignedUrl().getOrThrow()
                    }
                }.awaitAll()
            }
        }.onFailure {
            postSideEffect(UploadPlaceSideEffect.ShowToastUploadImageFailed)
            return@intent
        }.getOrThrow()

        // Step 2: Upload all images concurrently
        val uploadSuccessful = runCatching {
            coroutineScope {
                uris.zip(presignedResults).map { (imageUri, presignedResult) ->
                    async(Dispatchers.IO) {
                        putPlaceImageToPreSignedUrlOptimized(imageUri, presignedResult.preSignedUrl)
                    }
                }.awaitAll().all { it }
            }
        }.onFailure {
            postSideEffect(UploadPlaceSideEffect.ShowToastUploadImageFailed)
            return@intent
        }.getOrThrow()

        if (!uploadSuccessful) {
            postSideEffect(UploadPlaceSideEffect.ShowToastUploadImageFailed)
            return@intent
        }

        val bucketUrls = presignedResults.map { "${BuildConfig.BUCKET_URL}${it.fileName}" }
        submitUploadPlace(onSuccess = onSuccess, imageList = bucketUrls)
    }

    private suspend fun putPlaceImageToPreSignedUrlOptimized(
        imageUri: Uri,
        preSignedUrl: String
    ): Boolean = withContext(Dispatchers.IO) {
        val context = getApplication<Application>().applicationContext

        return@withContext try {
            val byteArray: ByteArray
            val mimeType: String

            if (imageUri.scheme == "content") {
                context.contentResolver.openInputStream(imageUri).use { inputStream ->
                    byteArray = inputStream?.readBytes()
                        ?: throw IllegalArgumentException("이미지 읽기 실패")
                }
                mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
            } else {
                Timber.tag(TAG).e("지원하지 않는 URI scheme: %s", imageUri.toString())
                throw IllegalArgumentException("지원하지 않는 URI scheme")
            }

            val fileBody = byteArray.toRequestBody(mimeType.toMediaTypeOrNull(), 0, byteArray.size)
            val request = Request.Builder()
                .url(preSignedUrl)
                .put(fileBody)
                .addHeader("Content-Type", mimeType)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Timber.tag(TAG).d("이미지 업로드 성공")
                    true
                } else {
                    Timber.tag(TAG).e("이미지 업로드 실패, code: ${response.code}")
                    false
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "이미지 업로드 과정에서 예외 발생: ${e.message}")
            false
        }
    }

    companion object {
        private val client: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .connectionPool(ConnectionPool(20, 5, TimeUnit.MINUTES))
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()
        }

        const val TAG = "UploadPlaceViewModel"
    }
}

@Immutable
data class UploadPlaceUiState(
    val hasAnimated: Map<String, Boolean> = emptyMap(),
    val isPreviousBtnEnabled: Boolean = false,
    val isNextBtnEnabled: Boolean = false,
    val showExitUploadPlaceDialog: Boolean = false,
    val showRemoveUploadPlaceImageDialog: Boolean = false,
    val showUploadPlaceLimitDialog: Boolean = false,
    val showUploadPlaceLimitPouUp: Boolean = false,
    val showSearchedSpotsByMap: Boolean = false,
    val searchedSpotsByMap: List<SearchedSpotByMap> = listOf(),

    val featureList :List<Feature>? = emptyList(),
    val selectedSpotByMap: SearchedSpotByMap? = null,
    val selectedSpotType: SpotType? = null,
    val selectedPriceOption: PriceFeatureType.PriceOptionType? = null,
    val selectedCafeOption: CafeFeatureType.CafeType? = null,
    val selectedRestaurantTypes: List<RestaurantFeatureType.RestaurantType> = emptyList(),
    val recommendMenu: String? = "",
    val selectedImageUris: List<Uri>? = emptyList(),
    val uploadFileName: String = "",

    val selectedUriToRemove: Uri? = null,
    val maxImageCount: Int = 10,
    val currentStep: Int = 0
)

sealed interface UploadPlaceSideEffect {
    data object ShowToastUploadFailed : UploadPlaceSideEffect
    data object ShowToastUploadImageFailed : UploadPlaceSideEffect
    data object OnNavigateToBack : UploadPlaceSideEffect
    data object OnMoveToReportPlace : UploadPlaceSideEffect
}
