package com.example.articlesapp.utils

import android.os.Message

sealed class ResultWrapper<out T> {
    object Loading : ResultWrapper<Nothing>()
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null) : ResultWrapper<Nothing>()
    data class  NetworkError(val error: String? = null) : ResultWrapper<Nothing>()
}
