package cz.cas.utia.materialfingerprintapp.core.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(
    message: String,
    exception: Throwable,
    onAction: () -> Unit,
    buttonText: String,
) {

    BackHandler {
        onAction() // override default Android device back button so when pressing it user does not lose state
    }

    Scaffold(
        topBar = {
            TopBarTitle(
                title = "An error occurred"
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.error),
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = "Exception Details:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .padding(horizontal = 16.dp)
                )

                val exceptionText = exception.localizedMessage ?: exception.toString()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 248.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    SelectionContainer { // selection container makes the text selectable in case user wants to copy and paste the exception somewhere
                        Text(
                            text = exceptionText,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }

                Button(
                    onClick = onAction
                ) {
                    Text(text = buttonText)
                }
            }
        }
    )
}