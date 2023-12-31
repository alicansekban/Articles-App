package com.example.articlesapp.domain.model

sealed class BaseUIModel<T>

class Loading<T> : BaseUIModel<T>()

data class Success<T>(val response : T) : BaseUIModel<T>()

data class Error<T>(val errorMessage : String) : BaseUIModel<T>()