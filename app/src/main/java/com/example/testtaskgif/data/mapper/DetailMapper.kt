package com.example.testtaskgif.data.mapper

import com.example.testtaskgif.data.model.GifDto
import com.example.testtaskgif.domain.models.GifDetail

class DetailMapper : GifMapper<GifDetail, GifDto> {
    override fun mapToDomain(entity: GifDto): GifDetail {
        return GifDetail(
            id = entity.id ?: "",
            title = entity.title ?: "",
            description = entity.altText ?: "No description available",
            sourceUrl = entity.source ?: "",
            originalUrl = entity.images?.original?.webp ?: "",
            size = entity.images?.original?.webpSize ?: 0,
        )
    }
}