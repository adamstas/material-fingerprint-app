package cz.cas.utia.materialfingerprintapp.core.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CustomHorizontalDivider() {
    HorizontalDivider(
        thickness = 1.5.dp,
        modifier = Modifier.padding(vertical = 20.dp)
    )
}

@Composable
fun CustomSpacer() {
    Spacer(modifier = Modifier.size(16.dp))
}