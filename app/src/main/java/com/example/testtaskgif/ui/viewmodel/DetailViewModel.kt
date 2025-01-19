package com.example.testtaskgif.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtaskgif.data.repository.IGifRepository
import com.example.testtaskgif.domain.models.GifDetail
import com.example.testtaskgif.utils.NetworkMonitor
import com.example.testtaskgif.utils.ResponseState
import com.example.testtaskgif.utils.ResponseState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: IGifRepository,
    private val network: NetworkMonitor
) : ViewModel() {

    private val _detailState = MutableStateFlow<ResponseState<GifDetail>>(Loading())
    val detailState: StateFlow<ResponseState<GifDetail>> get() = _detailState
    val isInternetAvailable = network.isInternetConnected
    private var lastGifId: String = ""

    fun fetchDetails(gifId: String) = viewModelScope.launch {
        lastGifId = gifId
        if (gifId.isBlank()) {
            _detailState.value = Error(Throwable("Gif ID cannot be empty"))
            return@launch
        }

        repository.findGifById(gifId).collectLatest { response ->
            _detailState.value = when (response) {
                is Loading -> Loading()
                is Success -> Success(response.data)
                is Error -> Error(response.error)
            }
        }
    }

    val retry = { fetchDetails(lastGifId) }
}