package com.acon.acon

import androidx.lifecycle.ViewModel
import com.acon.acon.domain.repository.AconAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val aconAppRepository: AconAppRepository
) : ViewModel() {

    suspend fun fetchShouldUpdateApp(): Boolean {
        return aconAppRepository.fetchShouldUpdateApp().getOrElse { false }
    }
}