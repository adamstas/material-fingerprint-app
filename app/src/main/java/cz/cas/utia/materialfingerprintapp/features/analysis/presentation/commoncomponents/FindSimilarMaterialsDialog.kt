package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.commoncomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomSpacer

@Composable
fun FindSimilarMaterialsDialog(
    onDismissRequest: () -> Unit,
    onLocalDatabaseButtonClick: () -> Unit,
    onRemoteDatabaseButtonClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Find similar materials in:",
                    style = MaterialTheme.typography.titleLarge
                )

                CustomSpacer()

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onLocalDatabaseButtonClick() },
                        modifier = Modifier.weight(1f)) {
                        Text("Local database")
                    }

                    CustomSpacer()

                    Button(
                        onClick = { onRemoteDatabaseButtonClick() },
                        modifier = Modifier.weight(1f)) {
                        Text("Remote database")
                    }
                }
            }
        }
    }
}