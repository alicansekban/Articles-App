package com.example.articlesapp.data.dataSource

import com.example.articlesapp.data.local.AppDataBase
import com.example.articlesapp.data.local.entity.ArticlesEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val db : AppDataBase) {

    fun getArticles(category:String = "") : List<ArticlesEntity> {
        return db.articlesDao().getArticles(category)
    }

    suspend fun insertArticleList(list : List<ArticlesEntity>) {
        db.articlesDao().insertArticleList(list)
    }
     fun deleteArticleList() {
        db.articlesDao().deleteArticles()
    }

    fun getArticle(id: Int) : ArticlesEntity {
        return db.articlesDao().getArticle(id)
    }
}