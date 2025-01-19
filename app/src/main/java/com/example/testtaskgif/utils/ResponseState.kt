package com.example.testtaskgif.utils

sealed class ResponseState<T> {
    class Loading<T> : ResponseState<T>()
    data class Success<T> (val data: T, val totalCount: Int? = null) : ResponseState<T>()
    data class Error<T> (val error:Throwable?, val data:T? = null) : ResponseState<T>()
}