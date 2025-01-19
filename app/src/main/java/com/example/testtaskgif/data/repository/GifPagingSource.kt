package com.example.testtaskgif.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.testtaskgif.data.api.ApiService
import com.example.testtaskgif.data.mapper.GifMapper
import com.example.testtaskgif.data.model.GifDto
import com.example.testtaskgif.domain.models.Gif

class GifPagingSource(
    private val apiService: ApiService,
    private val baseMapper: GifMapper<List<Gif>, List<GifDto>>,
    private val query: String = ""
) : PagingSource<Int, Gif>() {

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(anchorPosition = position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        val position = params.key ?: 0

        val response = if (query.isBlank()) {
            apiService.getTrendingGifs(limit = params.loadSize, offset = position)
        } else {
            apiService.searchGifs(query = query, limit = params.loadSize, offset = position)
        }

        return try {
            if (response.isSuccessful()) {
                LoadResult.Page(
                    data = baseMapper.mapToDomain(response.data),
                    prevKey = if (position == 0) null else position - params.loadSize,
                    nextKey = if (response.data.isEmpty()) null else position + params.loadSize,
                )
            } else {
                val exception = Exception(response.getErrorMessage())
                LoadResult.Error(exception)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}