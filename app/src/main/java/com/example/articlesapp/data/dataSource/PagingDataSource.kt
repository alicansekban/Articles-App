package com.example.articlesapp.data.dataSource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.articlesapp.data.local.entity.ArticlesEntity
import com.example.articlesapp.data.paging.ArticlePagingSource
import com.example.articlesapp.data.remote.ArticlesService
import com.example.articlesapp.domain.mapper.DataMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagingDataSource @Inject constructor(
    private val api: ArticlesService,
    private val localDataSource: LocalDataSource,
    private val mapper: DataMapper,
) {
    fun getArticles(category: String, country: String): Flow<PagingData<ArticlesEntity>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
            jumpThreshold = Int.MIN_VALUE,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            ArticlePagingSource(api, category, country, localDataSource, mapper)
        }
    ).flow
}