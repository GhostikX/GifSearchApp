package com.example.testtaskgif.data.mapper

interface GifMapper<Domain, Dto> {
    fun mapToDomain(entity: Dto) : Domain
}