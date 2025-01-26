package cz.cas.utia.materialfingerprintapp.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation() { //todo rename this WHOLE file to MainNavigation?

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination //todo je to potreba?

    var selectedScreenIndex by rememberSaveable { //todo toto tu zustane?
        mutableStateOf(0)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(112.dp)
            ) {
                mainScreens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedScreenIndex == index,
                        onClick = {
                            selectedScreenIndex = index
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
                            Icon(
                                painter = if (selectedScreenIndex == index) painterResource(screen.iconSelectedId) else painterResource(id = screen.iconUnselectedId),
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