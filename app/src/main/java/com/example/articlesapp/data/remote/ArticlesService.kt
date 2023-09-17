package com.example.articlesapp.data.remote

import com.example.articlesapp.utils.Constant
import com.example.caseapp.base.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesService {

    @GET("everything?q=football&pageSize=50&sortedBy=publishedAt&apiKey=${Constant.API_KEY}")
    suspend fun getData(
        @Query("from") from : String?,
        @Query("to") to : String?)  : BaseResponse

    @GET("top-headlines?pageSize=50&apiKey=${Constant.API_KEY}")
    suspend fun getTopHeadLines(
        @Query("page") page : Int,
        @Query("category") category : String?,
        @Query("country") country : String?) : BaseResponse
}