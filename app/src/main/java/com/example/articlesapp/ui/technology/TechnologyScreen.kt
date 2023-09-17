package com.example.articlesapp.ui.technology

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.SPUtils
import com.example.articlesapp.customViews.TopBar
import com.example.articlesapp.ui.viewmodel.SharedHeadLinesViewModel
import com.example.articlesapp.utils.Constant

@Composable
fun TechnologyScreen(
    menuClicked: () -> Unit,
    viewModel : SharedHeadLinesViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val country = SPUtils.getInstance().getString(Constant.QUERY_COUNTRY, "tr")
    LaunchedEffect(key1 = Unit) {
        viewModel.getArticles("technology", country)
    }
    StatelessTechnologyScreen(
        menuClicked = menuClicked
    )
}


@Composable
fun StatelessTechnologyScreen(
    menuClicked: () -> Unit
) {

    Column {
    }
}