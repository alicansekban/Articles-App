package com.example.articlesapp.data.dataSource

import com.example.articlesapp.data.remote.ArticlesService
import com.example.articlesapp.utils.ResultWrapper
import com.example.articlesapp.utils.safeApiCall
import com.example.caseapp.base.BaseResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: ArticlesService) {
    suspend fun getDataFromRemote(category : String,country : String): ResultWrapper<BaseResponse> {
        return safeApiCall(Dispatchers.IO) {
            api.getTopHeadLines(
                category, country
            )
        }
    }
}