package com.example.testtaskgif.di

import android.content.Context
import com.example.testtaskgif.data.api.ApiService
import com.example.testtaskgif.data.mapper.BaseMapper
import com.example.testtaskgif.data.mapper.DetailMapper
import com.example.testtaskgif.data.mapper.GifMapper
import com.example.testtaskgif.data.model.GifDto
import com.example.testtaskgif.data.repository.GifRepository
import com.example.testtaskgif.data.repository.IGifRepository
import com.example.testtaskgif.domain.models.Gif
import com.example.testtaskgif.domain.models.GifDetail
import com.example.testtaskgif.utils.AppConstants
import com.example.testtaskgif.utils.NetworkMonitor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesRetrofit() : Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val httpClient = OkHttpClient().newBuilder().apply {
            addInterceptor(httpLoggingInterceptor)
        }

        val json = Json {
            ignoreUnknownKeys = true
        }

        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(AppConstants.API_BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideNetwork(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitor(context)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGifRepository(
        apiService: ApiService,
        baseMapper: GifMapper<List<Gif>, List<GifDto>>,
        detailMapper:  GifMapper<GifDetail, GifDto>,
    ) : IGifRepository = GifRepository(apiService, baseMapper, detailMapper)

    @Provides
    @Singleton
    fun provideTrendingMapper() : GifMapper<List<Gif>, List<GifDto>> = BaseMapper()

    @Provides
    @Singleton
    fun provideDetailMapper() : GifMapper<GifDetail, GifDto> = DetailMapper()
}