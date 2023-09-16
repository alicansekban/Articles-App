package com.example.articlesapp.ui.technology

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.articlesapp.customViews.TopBar
import com.example.articlesapp.ui.viewmodel.SharedHeadLinesViewModel

@Composable
fun TechnologyScreen(
    menuClicked: () -> Unit,
    viewModel : SharedHeadLinesViewModel = hiltViewModel()
) {
    StatelessTechnologyScreen(
        menuClicked = menuClicked
    )
}


@Composable
fun StatelessTechnologyScreen(
    menuClicked: () -> Unit
) {

    Column {
        TopBar(
            title = "Technology Headlines",
            showBackButton = false,
            showMenuButton = true,
            onMenuClick = { menuClicked() },
            onBackClick = { })
    }
}