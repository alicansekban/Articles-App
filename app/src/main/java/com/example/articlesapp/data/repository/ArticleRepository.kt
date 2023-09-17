package com.example.articlesapp.data.repository

import androidx.paging.PagingData
import com.example.articlesapp.data.dataSource.LocalDataSource
import com.example.articlesapp.data.dataSource.PagingDataSource
import com.example.articlesapp.data.dataSource.RemoteDataSource
import com.example.articlesapp.data.local.entity.ArticlesEntity
import com.example.articlesapp.domain.mapper.DataMapper
import com.example.articlesapp.utils.ResultWrapper
import com.example.caseapp.base.ArticlesItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val pagingDataSource: PagingDataSource,
    private val dataMapper: DataMapper
) {
    fun fetchData(
        category: String = "",
        country: String
    ): Flow<ResultWrapper<List<ArticlesEntity>>> {

        return flow {
            // burada yaptığımız remote dan çekip db'ye ekleme işlemi
            emit(ResultWrapper.Loading)
            when (val apiData = remoteDataSource.getDataFromRemote(category, country)) {
                is ResultWrapper.GenericError -> {
                    emit(ResultWrapper.GenericError())
                }

                ResultWrapper.Loading -> {}
                is ResultWrapper.NetworkError -> {
                    emit(ResultWrapper.NetworkError())
                }

                is ResultWrapper.Success -> {
                    val response = apiData.value.articles
                    val listFromDb = localDataSource.getArticles(category)
                    val listToAddDb = mutableListOf<ArticlesItem?>()

                    response?.forEach { responseItem ->
                        val hasTheItem = listFromDb.any { db ->
                            db.title == responseItem?.title
                        }
                        if (!hasTheItem) {
                            listToAddDb.add(responseItem)
                        }
                    }
                    localDataSource.insertArticleList(listToAddDb.map {
                        dataMapper.mapToEntity(
                            it!!,
                            category
                        )
                    })


                }
            }
            // offline first mantığı ile veriyi sadece db'den çekip domain katmanına gönderiyoruz.

            emit(ResultWrapper.Success(localDataSource.getArticles(category)))
        }
    }

    fun getPopularMovie(category: String, country: String): Flow<PagingData<ArticlesEntity>> =
        pagingDataSource.getArticles(category, country)

}