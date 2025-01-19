package com.example.testtaskgif.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.testtaskgif.data.api.ApiService
import com.example.testtaskgif.data.mapper.GifMapper
import com.example.testtaskgif.data.model.GifDto
import com.example.testtaskgif.domain.models.Gif
import com.example.testtaskgif.domain.models.GifDetail
import com.example.testtaskgif.utils.AppConstants
import com.example.testtaskgif.utils.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GifRepository(
    private val apiService: ApiService,
    private val baseMapper: GifMapper<List<Gif>, List<GifDto>>,
    private val detailMapper: GifMapper<GifDetail, GifDto>,
) : IGifRepository {

    private val pageLimit = AppConstants.PAGE_LIMIT

    override fun getTrendingGifs(): Flow<PagingData<Gif>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageLimit,
                prefetchDistance = 1,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GifPagingSource(apiService = apiService, baseMapper = baseMapper) }
        ).flow
    }

    override fun searchGifs(query: String): Flow<PagingData<Gif>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageLimit,
                prefetchDistance = 1,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GifPagingSource(apiService = apiService, baseMapper = baseMapper, query = query) }
        ).flow
    }

    override fun findGifById(gifId: String): Flow<ResponseState<GifDetail>> = flow {
        emit(ResponseState.Loading())

        try {
            val gifResponse = apiService.findGifById(gifId)

            val gifDetail = gifResponse.data?.let { detailMapper.mapToDomain(it) }

            if (gifDetail != null) {
                emit(ResponseState.Success(gifDetail))
            } else {
                emit(ResponseState.Error(Throwable("No detail available for the given GIF ID.")))
            }
        } catch (e: Exception) {
            emit(ResponseState.Error(e))
        }
    }
}



