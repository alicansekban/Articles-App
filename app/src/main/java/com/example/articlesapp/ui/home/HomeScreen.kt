package com.example.articlesapp.ui.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.articlesapp.customViews.TopBar
import com.example.articlesapp.domain.model.ArticleUIModel
import com.example.articlesapp.domain.model.Error
import com.example.articlesapp.domain.model.Loading
import com.example.articlesapp.domain.model.Success
import com.example.articlesapp.ui.viewmodel.SharedHeadLinesViewModel

@Composable
fun HomeScreen(
    menuClicked: () -> Unit,
    viewModel: SharedHeadLinesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.getArticles("", "tr")
    }
    val articles by viewModel.articles.collectAsStateWithLifecycle()

    when (articles) {
        is Error -> {
            Toast.makeText(
                context,
                (articles as Error<List<ArticleUIModel>>).errorMessage,
                Toast.LENGTH_LONG
            ).show()

        }

        is Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Success -> {
            val response = (articles as Success<List<ArticleUIModel>>).response
            if (response.isNotEmpty()) {
                statelessHomeScreen(
                    response,
                    menuClicked = menuClicked
                )
            } else {
                EmptyScreen(
                    retryClicked = {
                        viewModel.getArticles("", "tr")
                    }
                )
            }
        }
    }

}

@Composable
fun EmptyScreen(
    retryClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { retryClicked() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Theres no articles, try again")
    }
}

@Composable
fun statelessHomeScreen(
    articles: List<ArticleUIModel>,
    menuClicked: () -> Unit
) {

    Column {
        TopBar(
            title = "Home Page",
            showBackButton = false,
            showMenuButton = true,
            onMenuClick = { menuClicked() },
            onBackClick = { })
    }

}
