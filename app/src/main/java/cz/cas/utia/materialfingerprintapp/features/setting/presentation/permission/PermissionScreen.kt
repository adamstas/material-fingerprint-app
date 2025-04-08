package cz.cas.utia.materialfingerprintapp.features.setting.presentation.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomHorizontalDivider
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomSpacer
import cz.cas.utia.materialfingerprintapp.core.ui.components.TopBarTitle

@Composable
fun PermissionScreenRoot(
    viewModel: PermissionViewModel = hiltViewModel(),
    onAllPermissionsGranted: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    PermissionScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onAllPermissionsGranted = onAllPermissionsGranted
    )
}

@Composable
fun PermissionScreen(
    state: PermissionsScreenState,
    onEvent: (PermissionsEvent) -> Unit,
    onAllPermissionsGranted: () -> Unit
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    // launcher for asking user for permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            state.visiblePermissionInDialog.let { permission ->
                if (permission.isNotEmpty()) {
                    onEvent(PermissionsEvent.OnPermissionResult(permission, isGranted))
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopBarTitle(title = "Permissions")
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(25.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please provide following permissions for the app to work on your device:",
                fontWeight = FontWeight.Bold
            )

            CustomHorizontalDivider()

            state.permissions.forEach { permissionStatus ->
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        onEvent(PermissionsEvent.OnPermissionResult(permissionStatus.permission, isGranted))

                        // check if permission is permanently declined after user granted/did not grant it in Android popup
                        if (!isGranted && activity != null) {
                            val isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                activity,
                                permissionStatus.permission
                            )
                            if (isPermanentlyDeclined) { // show dialog if permission is permanently denied (that is when user denies it twice)
                                onEvent(PermissionsEvent.ShowPermissionDialog(permissionStatus.permission))
                            }
                        }
                    }
                )

                PermissionItem(
                    permissionStatus = permissionStatus,
                    onRequestPermission = {
                        if (activity != null && !permissionStatus.isGranted) {
                            if (shouldShowRequestPermissionRationale(activity, permissionStatus.permission)) {
                                onEvent(PermissionsEvent.ShowPermissionDialog(permissionStatus.permission))
                            } else {
                                launcher.launch(permissionStatus.permission)
                            }
                        }
                    }
                )

                CustomHorizontalDivider()
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAllPermissionsGranted,
                enabled = state.allPermissionsGranted(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceed")
            }
        }
    }

    activity?.let { act ->
        state.visiblePermissionInDialog.let { permission ->
            if (permission.isNotEmpty()) {
                PermissionDialog(
                    permissionTextProvider = when (permission) {
                        Manifest.permission.CAMERA -> CameraPermissionTextProvider()
                        else -> CameraPermissionTextProvider()
                    },
                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(act, permission),
                    onDismiss = { onEvent(PermissionsEvent.DismissPermissionsDialog) },
                    onOkClick = {
                        onEvent(PermissionsEvent.DismissPermissionsDialog)
                        permissionLauncher.launch(permission)
                    },
                    onGoToAppSettingsClick = { act.openAppSettings() }
                )
            }
        }
    }
}

@Composable
fun PermissionItem(
    permissionStatus: PermissionStatus,
    onRequestPermission: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = getPermissionTitle(permissionStatus.permission),
            style = MaterialTheme.typography.titleMedium
        )

        Icon(
            painter = painterResource(getPermissionIcon(permissionStatus.permission),),
            contentDescription = getPermissionTitle(permissionStatus.permission)
        )
    }

    CustomSpacer()

    Text(text = getPermissionDescription(permissionStatus.permission))

    CustomSpacer()

    Button(
        onClick = {
            if (!permissionStatus.isGranted) {
                onRequestPermission()
            }
        },
        enabled = !permissionStatus.isGranted,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = if (permissionStatus.isGranted) "Granted" else "Grant permission")
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}