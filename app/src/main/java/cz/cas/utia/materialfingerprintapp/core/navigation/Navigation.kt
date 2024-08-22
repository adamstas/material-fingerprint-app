package cz.cas.utia.materialfingerprintapp.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun MainNavigation() {

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
            NavigationBar {
                mainNavigationScreens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedScreenIndex == index,
                        onClick = {
                            selectedScreenIndex = index
                            //navController.navigate(item.title)
                        },
                        label = {
                            Text(text = capitalizeFirstLetter(screen.route))
                        },
                        icon = {
                            Icon(
                                painter = if (selectedScreenIndex == index) painterResource(screen.iconSelectedId) else painterResource(id = screen.iconUnselectedId),
                                contentDescription = capitalizeFirstLetter(screen.route) //todo zmenit na nejaky sofistikovanejsi popis?
                            )

                        }
                    )
                }
            }
        }
    )
    {} //todo scaffold content?
}