package com.example.articlesapp.domain.useCase

import com.example.articlesapp.data.repository.ArticleRepository
import com.example.articlesapp.domain.mapper.DataMapper
import com.example.articlesapp.domain.model.ArticleUIModel
import com.example.articlesapp.domain.model.BaseUIModel
import com.example.articlesapp.domain.model.Error
import com.example.articlesapp.domain.model.Loading
import com.example.articlesapp.domain.model.Success
import com.example.articlesapp.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArticleUseCase @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val dataMapper: DataMapper
) {

    operator fun invoke(
        category: String,
        country: String
    ): Flow<BaseUIModel<List<ArticleUIModel>>> {
        return flow {
            emit(Loading())

            // repositoryden çektiğimiz result'u uimodel'imize çevirip gerekli dataları mapper yapıyoruz.
            articleRepository.fetchData(category, country).collect { data ->
                when (data) {
                    is ResultWrapper.GenericError -> {
                        emit(Error(data.error ?: "something went wrong"))
                    }

                    ResultWrapper.Loading -> {}
                    is ResultWrapper.NetworkError -> {
                        emit(Error(data.error ?: "Network Error"))
                    }

                    is ResultWrapper.Success -> emit(Success(data.value.map { article ->
                        dataMapper.mapToUIModel(
                            article
                        )
                    }))
                }
            }
        }
    }
}