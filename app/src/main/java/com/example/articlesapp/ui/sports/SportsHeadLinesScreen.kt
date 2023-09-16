package com.example.articlesapp.ui.sports

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blankj.utilcode.util.SPUtils
import com.example.articlesapp.customViews.TopBar
import com.example.articlesapp.domain.model.ArticleUIModel
import com.example.articlesapp.domain.model.Error
import com.example.articlesapp.domain.model.Loading
import com.example.articlesapp.domain.model.Success
import com.example.articlesapp.ui.home.ArticleItem
import com.example.articlesapp.ui.viewmodel.SharedHeadLinesViewModel
import com.example.articlesapp.utils.Constant
import com.example.articlesapp.utils.openChrome

@Composable
fun SportsHeadLinesScreen(
    menuClicked: () -> Unit,
    viewModel: SharedHeadLinesViewModel = hiltViewModel()
) {
    val country = SPUtils.getInstance().getString(Constant.QUERY_COUNTRY, "tr")

    LaunchedEffect(key1 = Unit) {
        viewModel.getArticles("sports", country)
    }
    val context = LocalContext.current
    val articles by viewModel.articles.collectAsStateWithLifecycle()
    when (articles) {
        is Error -> {
            Toast.makeText(
                context,
                (articles as Error<List<ArticleUIModel>>).errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }

        is Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Success -> {
            val response = (articles as Success<List<ArticleUIModel>>).response
            StatelessSportsScreen(
                menuClicked = menuClicked,
                response
            )
        }
    }


}

@Composable
fun StatelessSportsScreen(
    menuClicked: () -> Unit,
    articles: List<ArticleUIModel>
) {

    val context = LocalContext.current
    Column {
        TopBar(
            title = "Sports Headlines",
            showBackButton = false,
            showMenuButton = true,
            onMenuClick = { menuClicked() },
            onBackClick = { })

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            state = rememberLazyListState()
        ) {

            items(
                items = articles,
                key = { article ->
                    article.id
                }) { value ->
                ArticleItem(value, openWeb = {
                    context.openChrome(it)
                })
            }
        }
    }
}