package com.example.articlesapp.domain.mapper

import com.example.articlesapp.data.local.entity.ArticlesEntity
import com.example.articlesapp.utils.toDate
import com.example.caseapp.base.ArticlesItem
import com.example.articlesapp.domain.model.ArticleUIModel
import javax.inject.Inject

class DataMapper @Inject constructor() {

    // ileride kullanma ihtimaline karşı response olduğu gibi entity'e dönüştürülüp db ekleniyor
    fun mapToEntity(item: ArticlesItem,category : String): ArticlesEntity {
        return ArticlesEntity(
            publishedAt = item.publishedAt?.toDate(),
            author = item.author,
            urlToImage = item.urlToImage,
            description = item.description,
            sourceName = item.source?.name,
            sourceId = item.source?.id,
            title = item.title,
            url = item.url,
            content = item.content,
            category = category
        )
    }

    // ui'ın ihtiyacı olduğu kadar parametre dönüştürülüyor.
    fun mapToUIModel(item: ArticlesEntity): ArticleUIModel {
        return ArticleUIModel(
            publishedAt = item.publishedAt,
            urlToImage = item.urlToImage,
            description = item.description,
            title = item.title,
            content = item.content,
            id = item.id,
            url = item.url
        )
    }
}