package com.example.testtaskgif.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testtaskgif.ui.common.ErrorView
import com.example.testtaskgif.ui.components.GifGrid
import com.example.testtaskgif.ui.components.SearchBar
import com.example.testtaskgif.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen (
    homeViewModel: HomeViewModel = hiltViewModel(),
    onGifClick: (id: String) -> Unit,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val isInternetConnected by homeViewModel.isInternetAvailable.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }
    val trendingGifs = homeViewModel.trendingGifs.collectAsLazyPagingItems()
    val searchResult = homeViewModel.searchResults.collectAsLazyPagingItems()

    if (isInternetConnected) {
        Column (modifier = modifier) {
            SearchBar(
                query = query,
                onSearchQueryChanged = { newQuery ->
                    query = newQuery
                },
                onSearch = { homeViewModel.updateQuery(query) },
                modifier = Modifier.padding(top = 20.dp)
            )

            if (query.isNotBlank()) {
                GifGrid(gifs = searchResult, onGifClick = onGifClick)
            } else {
                GifGrid(gifs = trendingGifs, onGifClick = onGifClick)
            }
        }
    } else {
        ErrorView(errorMessage = "No Internet Connection", onClick = { trendingGifs.retry() })
    }
}