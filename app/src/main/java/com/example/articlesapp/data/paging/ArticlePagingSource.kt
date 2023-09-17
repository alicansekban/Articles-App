package com.example.articlesapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.articlesapp.data.dataSource.LocalDataSource
import com.example.articlesapp.data.local.entity.ArticlesEntity
import com.example.articlesapp.data.remote.ArticlesService
import com.example.articlesapp.domain.mapper.DataMapper
import com.example.caseapp.base.ArticlesItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ArticlePagingSource(
    private val api: ArticlesService,
    private val category: String,
    private val country: String,
    private val localDataSource: LocalDataSource,
    private val mapper: DataMapper
) : PagingSource<Int, ArticlesEntity>() {
    override fun getRefreshKey(state: PagingState<Int, ArticlesEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesEntity> {
        val page = params.key ?: 1

        return try {
            val response = api.getTopHeadLines(page,category, country)
            response.articles?.filterNotNull()?.map { mapper.mapToEntity(it, category) }
                ?.let { localDataSource.insertArticleList(it) }

            val articles = withContext(Dispatchers.IO) {
                localDataSource.getArticles(category)
            }

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.articles?.isEmpty() == true) null else page.plus(1)
            )

        } catch (e: IOException) {
            val articles = withContext(Dispatchers.IO) {
                localDataSource.getArticles(category)
            }
            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = null // Hata durumunda veri çekme işlemi sona erer, bir sonraki sayfa yok
            )
        }
    }
}