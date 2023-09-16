package com.example.articlesapp.data.repository

import com.example.articlesapp.data.dataSource.LocalDataSource
import com.example.articlesapp.data.dataSource.RemoteDataSource
import com.example.articlesapp.data.local.entity.ArticlesEntity
import com.example.articlesapp.domain.mapper.DataMapper
import com.example.articlesapp.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val dataMapper: DataMapper
) {

    fun fetchData(category: String, country : String): Flow<ResultWrapper<List<ArticlesEntity>>> {

        return flow {
            // burada yaptığımız remote dan çekip db'ye ekleme işlemi
            emit(ResultWrapper.Loading)
            when (val apiData = remoteDataSource.getDataFromRemote(category,country)) {
                is ResultWrapper.GenericError -> {
                    emit(ResultWrapper.GenericError())
                }

                ResultWrapper.Loading -> {}
               is ResultWrapper.NetworkError -> {
                    emit(ResultWrapper.NetworkError())
                }

                is ResultWrapper.Success -> {
                    val response = apiData.value.articles
                    if (response?.isNotEmpty() == true) {
                        localDataSource.insertArticleList(response.map { dataMapper.mapToEntity(it!!) })
                    }
                }
            }
            // offline first mantığı ile veriyi sadece db'den çekip domain katmanına gönderiyoruz.
            localDataSource.getArticles().collect {
                emit(ResultWrapper.Success(it))
            }
        }
    }
 }