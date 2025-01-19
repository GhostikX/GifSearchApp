package com.example.testtaskgif.data.api

import com.example.testtaskgif.BuildConfig
import com.example.testtaskgif.data.model.response.GifResponse
import com.example.testtaskgif.data.model.response.GifDetailResponse
import com.example.testtaskgif.utils.AppConstants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("v1/gifs/trending")
    suspend fun getTrendingGifs(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("bundle") bundle: String = AppConstants.BUNDLE_PARAM,
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
    ) : GifResponse

    @GET("v1/gifs/search")
    suspend fun searchGifs(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("bundle") bundle: String = AppConstants.BUNDLE_PARAM,
        @Query("api_key") apiKey: String = BuildConfig.apiKey
    ) : GifResponse

    @GET("v1/gifs/{gif_id}")
    suspend fun findGifById(
        @Path("gif_id") gifId: String,
        @Query("api_key") apiKey: String = BuildConfig.apiKey
    ) : GifDetailResponse
}