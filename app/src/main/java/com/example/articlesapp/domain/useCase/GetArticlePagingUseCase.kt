package com.example.articlesapp.domain.useCase

import androidx.paging.PagingData
import com.example.articlesapp.data.local.entity.ArticlesEntity
import com.example.articlesapp.data.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticlePagingUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(category: String, country: String): Flow<PagingData<ArticlesEntity>> {
        return repository.getPopularMovie(category, country)
    }
}