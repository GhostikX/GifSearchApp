package com.example.testtaskgif.domain.useCase

import com.example.testtaskgif.data.repository.IGifRepository
import javax.inject.Inject

class SearchGifsUseCase @Inject constructor(
    private val repository: IGifRepository
) {
    operator fun invoke(query: String) = repository.searchGifs(query)
}