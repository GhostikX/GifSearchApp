package com.example.testtaskgif.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import com.example.testtaskgif.R
import com.example.testtaskgif.domain.models.Gif
import com.example.testtaskgif.ui.common.ErrorView
import com.example.testtaskgif.ui.common.LoadingView

@Composable
fun GifGrid(
    gifs: LazyPagingItems<Gif>,
    onGifClick: (id: String) -> Unit,
) {

    val loadState = gifs.loadState

    when {
        loadState.refresh is LoadState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingView(modifier = Modifier)
            }
        }

        loadState.refresh is LoadState.Error -> {
            val error = (loadState.refresh as LoadState.Error).error
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                error.localizedMessage?.let {
                    ErrorView(
                        errorMessage = it,
                        onClick = { gifs.retry() }
                    )
                }
            }
        }

        loadState.refresh is LoadState.NotLoading && gifs.itemCount == 0 -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No results found.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        else -> {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 16.dp,
            ) {

                if (loadState.prepend is LoadState.Loading) {
                    item {
                        LoadingView(modifier = Modifier)
                    }
                }

                if (loadState.prepend is LoadState.Error) {
                    item {
                        ErrorView("Ops something happened", { gifs.retry() })
                    }
                }

                if (loadState.refresh is LoadState.NotLoading) {
                    if (gifs.itemCount != 0) {
                        items(count = gifs.itemCount) { index ->
                            GifComposable(
                                gif = gifs[index]!!,
                                onClick = { gifs[index]?.let { onGifClick(it.id) } })
                        }
                    }
                }

                if (loadState.append is LoadState.Loading) {
                    item {
                        LoadingView(modifier = Modifier)
                    }
                }

                if (loadState.append is LoadState.Error) {
                    item {
                        ErrorView("Ops something happened", { gifs.retry() })
                    }
                }
            }
        }
    }
}

@Composable
fun GifComposable(gif: Gif, onClick: () -> Unit) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        model = gif.imageUrl,
        contentDescription = gif.title,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.placeholder),
        error = painterResource(R.drawable.placeholder),
        onError = {
            Log.e("GIFLoadError", "Error loading GIF: ${it.result.throwable}")
        }
    )
}