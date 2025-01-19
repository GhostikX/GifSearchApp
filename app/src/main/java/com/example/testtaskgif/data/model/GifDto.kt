package com.example.testtaskgif.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GifDto(
    @SerialName("alt_text")
    val altText: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("images")
    val images: Images? = null,
    @SerialName("source")
    val source: String? = null,
    @SerialName("title")
    val title: String? = null,
)

@Serializable
data class Images(
    @SerialName("fixed_width")
    val fixedWidth: FixedWidth? = null,
    @SerialName("original")
    val original: Original? = null
)

@Serializable
data class FixedWidth(
    @SerialName("webp")
    val webp: String? = null
)

@Serializable
data class Original(
    @SerialName("mp4")
    val mp4: String? = null,
    @SerialName("mp4_size")
    val mp4Size: Long? = null,
    @SerialName("webp")
    val webp: String? = null,
    @SerialName("webp_size")
    val webpSize: Long? = null,
)