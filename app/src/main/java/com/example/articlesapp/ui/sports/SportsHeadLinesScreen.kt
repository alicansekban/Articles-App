package com.example.articlesapp.ui.sports

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.articlesapp.customViews.TopBar
import com.example.articlesapp.ui.viewmodel.SharedHeadLinesViewModel

@Composable
fun SportsHeadLinesScreen(
    menuClicked: () -> Unit,
    viewModel: SharedHeadLinesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getArticles("sports","tr")
    }
    val articles by viewModel.articles.collectAsStateWithLifecycle()
    StatelessSportsScreen(
        menuClicked = menuClicked
    )

}

@Composable
fun StatelessSportsScreen(
    menuClicked: () -> Unit
) {

    Column {
        TopBar(
            title = "Sports Headlines",
            showBackButton = false,
            showMenuButton = true,
            onMenuClick = { menuClicked() },
            onBackClick = { })
    }
}