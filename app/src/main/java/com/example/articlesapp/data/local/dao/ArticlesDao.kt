package com.example.articlesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.articlesapp.data.local.entity.ArticlesEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleList(articleEntity: List<ArticlesEntity>)


    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticle(id: Int): ArticlesEntity

    @Query("select * from articles")
    fun getArticles(): Flow<List<ArticlesEntity>>
}