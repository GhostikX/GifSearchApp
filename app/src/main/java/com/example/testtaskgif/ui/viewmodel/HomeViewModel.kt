package com.example.testtaskgif.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.testtaskgif.domain.models.Gif
import com.example.testtaskgif.domain.useCase.GetTrendingGifsUseCase
import com.example.testtaskgif.domain.useCase.SearchGifsUseCase
import com.example.testtaskgif.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val trendingUseCase: GetTrendingGifsUseCase,
    private val searchUseCase: SearchGifsUseCase,
    private val network: NetworkMonitor
) : ViewModel() {

    private val _query = MutableStateFlow("")

    val trendingGifs = trendingUseCase.invoke().cachedIn(viewModelScope)

    val isInternetAvailable = network.isInternetConnected

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<PagingData<Gif>> = _query
        .debounce(700)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                emptyFlow()
            } else {
                searchUseCase.invoke(query)
            }
        }
        .cachedIn(viewModelScope)

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }
}
