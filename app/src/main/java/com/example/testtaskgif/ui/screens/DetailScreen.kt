package com.example.testtaskgif.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testtaskgif.ui.common.ErrorView
import com.example.testtaskgif.ui.common.LoadingView
import com.example.testtaskgif.ui.components.DetailContent
import com.example.testtaskgif.ui.viewmodel.DetailViewModel
import com.example.testtaskgif.utils.ResponseState

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize(),
    gifId: String
) {

    val state by detailViewModel.detailState.collectAsStateWithLifecycle()
    val isInternetConnected by detailViewModel.isInternetAvailable.collectAsStateWithLifecycle()

    if (isInternetConnected) {
        LaunchedEffect(gifId) {
            detailViewModel.fetchDetails(gifId)
        }
    } else {
        ErrorView(errorMessage = "No Internet Connection", onClick = { detailViewModel.retry })
    }

    when (state) {
        is ResponseState.Loading -> {
            LoadingView()
        }

        is ResponseState.Success -> {
            val data = (state as ResponseState.Success).data
            DetailContent(data = data, modifier = modifier)
        }

        is ResponseState.Error -> {
            val error = (state as ResponseState.Error)
            error.error?.localizedMessage?.let { ErrorView(errorMessage = it, onClick =  { detailViewModel.retry }) }
        }
    }
}