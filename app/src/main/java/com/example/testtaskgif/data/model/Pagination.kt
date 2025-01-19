package com.example.testtaskgif.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("offset")
    val offset: Int? = null,
    @SerialName("total_count")
    val totalCount: Int? = null
)