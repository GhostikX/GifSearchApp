package com.example.testtaskgif.data.model.response


import com.example.testtaskgif.data.model.GifDto
import com.example.testtaskgif.data.model.Pagination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GifResponse(
    @SerialName("data")
    val `data`: List<GifDto>,
    @SerialName("pagination")
    val pagination: Pagination
) : BaseResponse()

@Serializable
data class GifDetailResponse(
    @SerialName("data")
    val data: GifDto? = null,
    @SerialName("meta")
    val meta: Meta? = null
)