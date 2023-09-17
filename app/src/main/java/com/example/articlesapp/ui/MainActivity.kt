package com.example.articlesapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.articlesapp.base.sharedViewModel
import com.example.articlesapp.customViews.TopBar
import com.example.articlesapp.graph.drawerModalGraph
import com.example.articlesapp.ui.business.BusinessScreen
import com.example.articlesapp.ui.health.HealthScreen
import com.example.articlesapp.ui.home.HomeScreen
import com.example.articlesapp.ui.sports.SportsHeadLinesScreen
import com.example.articlesapp.ui.technology.TechnologyScreen
import com.example.articlesapp.ui.viewmodel.SharedHeadLinesViewModel
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
                val items = listOf(
                    NavigationModalItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unSelectedIcon = Icons.Outlined.Home,
                        route = ScreenRoutes.HomeRoutes.HomeRoute

                    ),
                    NavigationModalItem(
                        title = "Sport Headlines",
                        selectedIcon = Icons.Filled.Favorite,
                        unSelectedIcon = Icons.Outlined.FavoriteBorder,
                        route = ScreenRoutes.HeadLineRoutes.SportHeadLines
                    ),
                    NavigationModalItem(
                        title = "Technology Headlines",
                        selectedIcon = Icons.Filled.Favorite,
                        unSelectedIcon = Icons.Outlined.FavoriteBorder,
                        route = ScreenRoutes.HeadLineRoutes.TechnologyHeadLines
                    ),
                    NavigationModalItem(
                        title = "Business Headlines",
                        selectedIcon = Icons.Filled.Favorite,
                        unSelectedIcon = Icons.Outlined.FavoriteBorder,
                        route = ScreenRoutes.HeadLineRoutes.BusinessHeadLines
                    ),
                    NavigationModalItem(
                        title = "Health Headlines",
                        selectedIcon = Icons.Filled.Favorite,
                        unSelectedIcon = Icons.Outlined.FavoriteBorder,
                        route = ScreenRoutes.HeadLineRoutes.HealthHeadLines
                    ),
                )
                val scope = rememberCoroutineScope()
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    ModalNavigationDrawer(drawerContent = {
                        ModalDrawerSheet {
                            items.forEachIndexed { index, item ->
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
                                composable(ScreenRoutes.HeadLineRoutes.SportHeadLines) {
                                    SportsHeadLinesScreen(
                                        menuClicked = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    )
                                }
                                composable(ScreenRoutes.HeadLineRoutes.TechnologyHeadLines) {
                                    TechnologyScreen(
                                        menuClicked = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    )
                                }
                                composable(ScreenRoutes.HeadLineRoutes.BusinessHeadLines) {
                                    BusinessScreen(
                                        menuClicked = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    )
                                }
                                composable(ScreenRoutes.HeadLineRoutes.HealthHeadLines) {
                                    HealthScreen(
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