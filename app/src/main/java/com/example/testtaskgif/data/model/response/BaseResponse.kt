package com.example.testtaskgif.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class BaseResponse{
    @SerialName("meta")
    val meta: Meta? = null
    fun isSuccessful() = meta?.status == 200
    fun getErrorMessage() = meta?.msg ?: "Unknown error"
}

@Serializable
data class Meta(
    @SerialName("msg")
    val msg: String? = null,
    @SerialName("status")
    val status: Int? = null
)
