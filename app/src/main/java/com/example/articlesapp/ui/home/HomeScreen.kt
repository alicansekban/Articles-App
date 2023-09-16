package com.example.articlesapp.ui.home

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.articlesapp.R
import com.example.articlesapp.customViews.TopBar
import com.example.articlesapp.domain.model.ArticleUIModel
import com.example.articlesapp.domain.model.Error
import com.example.articlesapp.domain.model.Loading
import com.example.articlesapp.domain.model.Success
import com.example.articlesapp.ui.viewmodel.SharedHeadLinesViewModel
import com.example.articlesapp.utils.Constant
import com.example.articlesapp.utils.openChrome
import com.example.articlesapp.utils.toParsedString

@Composable
fun HomeScreen(
    menuClicked: () -> Unit,
    viewModel: SharedHeadLinesViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val country = SPUtils.getInstance().getString(Constant.QUERY_COUNTRY, "tr")
    LaunchedEffect(key1 = Unit) {
        viewModel.getArticles("general", country)
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
    val context = LocalContext.current
    Column {
        TopBar(
            title = "Home Page",
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


@Composable
fun ArticleItem(article: ArticleUIModel, openWeb: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { article.url?.let { openWeb(it) } }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            loadImage(
                url = article.urlToImage ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = article.title ?: "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = article.description ?: "",
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = article.publishedAt?.toParsedString() ?: "",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun loadImage(
    url: String, modifier: Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp)
    ) {
        GlideImage(
            model = url,
            contentDescription = "loadImage",
            modifier = modifier,
            contentScale = ContentScale.FillBounds
        ) {
            it.error(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .load(url)

        }
    }
}
