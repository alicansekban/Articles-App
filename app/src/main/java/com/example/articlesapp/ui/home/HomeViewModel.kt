package com.example.articlesapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.articlesapp.domain.model.ArticleUIModel
import com.example.articlesapp.domain.model.BaseUIModel
import com.example.articlesapp.domain.model.Loading
import com.example.articlesapp.domain.useCase.GetArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: GetArticleUseCase
) : ViewModel() {

    private val _articles = MutableStateFlow<BaseUIModel<List<ArticleUIModel>>>(Loading())
    val articles: StateFlow<BaseUIModel<List<ArticleUIModel>>> get() = _articles

    init {
        getArticles("", "tr")
    }

    fun getArticles(category: String, country: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase(category, country).collect {
                _articles.emit(it)
            }
        }
    }
}