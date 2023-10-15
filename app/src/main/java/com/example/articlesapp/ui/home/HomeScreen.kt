package com.example.articlesapp.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.blankj.utilcode.util.SPUtils
import com.example.articlesapp.R
import com.example.articlesapp.customViews.BottomSheet
import com.example.articlesapp.domain.model.ArticleUIModel
import com.example.articlesapp.domain.model.Error
import com.example.articlesapp.domain.model.Loading
import com.example.articlesapp.domain.model.Success
import com.example.articlesapp.ui.viewmodel.SharedHeadLinesViewModel
import com.example.articlesapp.utils.Constant
import com.example.articlesapp.utils.CountrySelectModel
import com.example.articlesapp.utils.openChrome
import com.example.articlesapp.utils.toParsedString

@Composable
fun HomeScreen(
    countryItems: List<CountrySelectModel>,
    viewModel: SharedHeadLinesViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val country = SPUtils.getInstance().getString(Constant.QUERY_COUNTRY, "tr")
    val articles by viewModel.articles.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.getArticles("general", country)
    }
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
                    countryItems = countryItems,
                    countrySelected = {
                        viewModel.getArticles("general", it.code)
                    }
                )
            } else {
                EmptyScreen(
                    retryClicked = {
                        viewModel.getArticles("general", country)
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
    countryItems: List<CountrySelectModel>,
    countrySelected: (CountrySelectModel) -> Unit = {}
) {
    val context = LocalContext.current
    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    if (showBottomSheet) {
        BottomSheet(
            onDismissRequest = { showBottomSheet = false },
            countrySelected = { selectedCountry ->
                countryItems.forEach {
                    it.selected = false
                }
                selectedCountry.selected = true
                countrySelected(selectedCountry)
                showBottomSheet = false
            },
            items = countryItems
        )
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showBottomSheet = true
                }
                .padding(10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(imageVector = Icons.Default.FilterAlt, contentDescription = "filter")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.SpaceBetween
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
            .height(120.dp)
            .padding(top = 4.dp)
            .clickable { article.url?.let { openWeb(it) } },
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(10.dp),
    ) {

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.icons8_news_50),
                contentDescription = null,
                modifier = Modifier
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.secondary),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth().fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.title ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )

                Text(
                    text = article.description ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
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
            }
        }

    }
}

@Composable
fun loadImage(
    url: String, modifier: Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp)
    ) {
        AsyncImage(
            model = url,
            contentDescription = "loadImage",
            modifier = modifier,
            contentScale = ContentScale.FillBounds
        )
    }
}
