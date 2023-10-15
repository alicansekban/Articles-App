package com.example.articlesapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.articlesapp.customViews.TopBar
import com.example.articlesapp.ui.home.HomeScreen
import com.example.articlesapp.ui.sports.SavedHeadLinesScreen
import com.example.articlesapp.utils.CategorySelectModel
import com.example.articlesapp.utils.CountrySelectModel
import com.example.articlesapp.utils.NavigationModalItem
import com.example.articlesapp.utils.ScreenRoutes
import com.example.articlesapp.utils.theme.ArticlesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArticlesAppTheme {
                val navController = rememberNavController()

                val navigation: (String) -> Unit = { route ->
                    if (route == "-1") {
                        navController.popBackStack()
                    } else {
                        navController.navigate(route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items

                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
                val countryItems = mutableListOf(
                    CountrySelectModel(
                        name = "Türkiye",
                        code = "tr",
                        selected = true
                    ),
                    CountrySelectModel(
                        name = "Amerika",
                        code = "us",
                        selected = false
                    ),
                    CountrySelectModel(
                        name = "Fransa",
                        code = "fr",
                        selected = false
                    ),
                    CountrySelectModel(
                        name = "Britanya",
                        code = "gb",
                        selected = false
                    ),
                )
                val categoryItems = mutableListOf(
                    CategorySelectModel(
                        name = "general",
                        selected = true
                    ),
                    CategorySelectModel(
                        name = "sports",
                        selected = false
                    ),
                    CategorySelectModel(
                        name = "business",
                        selected = false
                    ),
                    CategorySelectModel(
                        name = "science",
                        selected = false
                    ),
                    CategorySelectModel(
                        name = "health",
                        selected = false
                    ),
                    CategorySelectModel(
                        name = "entertainment",
                        selected = false
                    )
                )
                val items = listOf(
                    NavigationModalItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unSelectedIcon = Icons.Outlined.Home,
                        route = ScreenRoutes.HomeRoutes.HomeRoute

                    )
                )
                val scope = rememberCoroutineScope()
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }
                var selectedBottomItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    ModalNavigationDrawer(drawerContent = {
                        ModalDrawerSheet {
                            items.take(2).forEachIndexed { index, item ->
                                NavigationDrawerItem(
                                    label = { Text(text = item.title) },
                                    selected = index == selectedItemIndex,
                                    onClick = {
                                        navigation(item.route)
                                        selectedItemIndex = index
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unSelectedIcon,
                                            contentDescription = "icon"
                                        )
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )

                            }
                        }
                    }, drawerState = drawerState) {
                        Scaffold(
                            topBar = {
                                TopBar(
                                    title = "Home Page",
                                    showBackButton = false,
                                    showMenuButton = true,
                                    onMenuClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                                    onBackClick = { })
                            }
                        ) { paddingValues ->
                            NavHost(
                                navController = navController,
                                startDestination = "home",
                                modifier = Modifier.padding(paddingValues)
                            ) {

                                composable(ScreenRoutes.HomeRoutes.HomeRoute) {
                                    HomeScreen(
                                        countryItems = countryItems
                                    )
                                }
                                composable(ScreenRoutes.HeadLineRoutes.SavedHeadLines) {
                                    SavedHeadLinesScreen(
                                        menuClicked = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}