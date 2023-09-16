package com.example.articlesapp.ui.business

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.articlesapp.customViews.TopBar
import com.example.articlesapp.ui.viewmodel.SharedHeadLinesViewModel

@Composable
fun BusinessScreen(
    menuClicked: () -> Unit,
    viewModel: SharedHeadLinesViewModel = hiltViewModel()
) {
    StatelessBusinessScreen(
        menuClicked = menuClicked
    )
}



@Composable
fun StatelessBusinessScreen(
    menuClicked: () -> Unit
) {

    Column {
        TopBar(
            title = "Business Headlines",
            showBackButton = false,
            showMenuButton = true,
            onMenuClick = { menuClicked() },
            onBackClick = { })
    }
}