package com.example.testtaskgif.data.mapper

import com.example.testtaskgif.data.model.GifDto
import com.example.testtaskgif.domain.models.Gif

class BaseMapper : GifMapper<List<Gif>, List<GifDto>> {

    override fun mapToDomain(dto: List<GifDto>): List<Gif> {
        return dto.map { gif ->
            Gif(
                id = gif.id ?: "",
                title = gif.title ?: "",
                imageUrl = gif.images?.fixedWidth?.webp ?: ""
            )
        }
    }
}