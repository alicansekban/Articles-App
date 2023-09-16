package com.example.articlesapp.domain.model

import java.util.Date

data class ArticleUIModel(
    val id : Int,
    val publishedAt: Date?,
    val urlToImage: String?,
    val description: String?,
    val url:String?,
    val title: String?,
    val content: String?
)
