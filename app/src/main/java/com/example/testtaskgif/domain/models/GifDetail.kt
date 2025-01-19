package com.example.testtaskgif.domain.models

data class GifDetail(
    val id: String,
    val title: String,
    val description: String,
    val sourceUrl: String,
    val originalUrl: String,
    val size: Long,
)
