package cz.cas.utia.materialfingerprintapp.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow

//abstraction for navigation handling in root screen composables
@Composable
fun <T> NavigationHandler(
    navigationEventFlow: SharedFlow<T>,
    navigate: (T) -> Unit
) {
    LaunchedEffect(Unit) {
        navigationEventFlow.collect { event ->
            navigate(event)
        }
    }
}