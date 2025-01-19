package com.example.testtaskgif.data.repository

import androidx.paging.PagingData
import com.example.testtaskgif.data.model.GifDto
import com.example.testtaskgif.domain.models.Gif
import com.example.testtaskgif.domain.models.GifDetail
import com.example.testtaskgif.utils.ResponseState
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface IGifRepository {
    fun getTrendingGifs(): Flow<PagingData<Gif>>
    fun searchGifs(query: String): Flow<PagingData<Gif>>
    fun findGifById(gifId: String) : Flow<ResponseState<GifDetail>>
}