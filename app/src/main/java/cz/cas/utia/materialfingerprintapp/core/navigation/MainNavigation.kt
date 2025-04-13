package cz.cas.utia.materialfingerprintapp.core.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // for matching route with type safe Class of which the route is instance of
    fun String.extractClassNameFromRoute(): String {
        return this.substringBefore('/')
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(112.dp)
            ) {

                mainScreens.forEach { screen ->

                    val isSelected = currentDestination?.hierarchy?.any { destination ->
                        val className = destination.route?.extractClassNameFromRoute()
                        className == screen.route::class.qualifiedName || // for main screens
                                (Screen.getGroupByClassName(className) == screen.group) // for other screens
                    } ?: false

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                   saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = {
                            Text(text = screen.label)
                        },
                        icon = {
                            Icon(painter =
                            if (isSelected) painterResource(screen.iconSelectedId)
                            else painterResource(id = screen.iconUnselectedId),
                                contentDescription = screen.label
                            )

                        }
                    )
                }
            }
        }
    )
    { innerPadding -> MainGraph(
        navController = navController
        //  modifier = Modifier.padding(innerPadding)
    )
    }
}