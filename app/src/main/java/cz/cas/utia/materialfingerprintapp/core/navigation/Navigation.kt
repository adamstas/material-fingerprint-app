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
fun MainNavigation() { //todo rename this WHOLE file to MainNavigation?

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
                                restoreState = true //todo toto je super ze kdyz jdu z analytics do camera a pak zpet do analytics, tak v analytics zustane state
                                                    // ale kdyz jdu z camera section do analytics a predtim jsem aspon jednou byl v photosummary, tak me to pak namisto do camera hazi do photosummary
                            }
                        },
                        label = {
                            Text(text = screen.label)
                        },
                        icon = {
                            Icon(painter =
                            if (isSelected) painterResource(screen.iconSelectedId)
                            else painterResource(id = screen.iconUnselectedId),
                                //todo toto lze predelat podle oficialniho tutorial na bottom bar navigaci (ted kdyz clovek byl v camera a jde do settings a da na mobilni spodni liste "back" tak ho to hodi zpatky d ocamera, ale v bottom navbaru stale sviti settings)
                                contentDescription = screen.label //todo zmenit na nejaky sofistikovanejsi popis?
                            )

                        }
                    )
                }
            }
        }
    )
    { innerPadding -> MainGraph(navController = navController)
    } //todo scaffold content?
}