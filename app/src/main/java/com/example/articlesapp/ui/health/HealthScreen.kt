package com.example.articlesapp.ui.health

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.articlesapp.customViews.TopBar
import com.example.articlesapp.ui.viewmodel.SharedHeadLinesViewModel

@Composable
fun HealthScreen(
    menuClicked: () -> Unit,
    viewModel: SharedHeadLinesViewModel = hiltViewModel()
) {
    StatelessHealthScreen(
        menuClicked = menuClicked
    )

}

@Composable
fun StatelessHealthScreen(menuClicked: () -> Unit) {
    Column {
        TopBar(
            title = "Health Headlines",
            showBackButton = false,
            showMenuButton = true,
            onMenuClick = { menuClicked() },
            onBackClick = { })
    }
}
