package com.example.testtaskgif.domain.useCase

import com.example.testtaskgif.data.repository.IGifRepository
import javax.inject.Inject

class GetTrendingGifsUseCase @Inject constructor(
    private val repository: IGifRepository
) {
    operator fun invoke() = repository.getTrendingGifs()
}