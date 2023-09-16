package com.example.articlesapp.graph

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.articlesapp.utils.NavigationModalItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun drawerModalGraph(navController: NavController) {

    val navigation: (String) -> Unit = { route ->
        if (route == "-1") {
            navController.popBackStack()
        } else {
            navController.navigate(route)
        }
    }
    val scope = rememberCoroutineScope()
    val items = listOf(
        NavigationModalItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home,
            route = "home"

        ),
        NavigationModalItem(
            title = "Sport Headlines",
            selectedIcon = Icons.Filled.Favorite,
            unSelectedIcon = Icons.Outlined.FavoriteBorder,
            route = "sportHeadlines"
        ),
        NavigationModalItem(
            title = "Technology Headlines",
            selectedIcon = Icons.Filled.Favorite,
            unSelectedIcon = Icons.Outlined.FavoriteBorder,
            route = "technologyHeadlines"
        ),
        NavigationModalItem(
            title = "Business Headlines",
            selectedIcon = Icons.Filled.Favorite,
            unSelectedIcon = Icons.Outlined.FavoriteBorder,
            route = "businessHeadlines"
        ),
        NavigationModalItem(
            title = "Health Headlines",
            selectedIcon = Icons.Filled.Favorite,
            unSelectedIcon = Icons.Outlined.FavoriteBorder,
            route = "healthHeadlines"
        ),
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

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
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding))

            }
        }
    }, drawerState = drawerState) {
        
    }
}