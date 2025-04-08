package cz.cas.utia.materialfingerprintapp.features.setting.presentation.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(PermissionsScreenState())
    val state: StateFlow<PermissionsScreenState> = _state.asStateFlow()

    init {
        checkPermissions()
    }

    private fun checkPermissions() {
        val permissions = requiredPermissions.map { permission ->
            PermissionStatus(
                permission = permission,
                isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED,
            )
        }
        _state.update {
            it.copy(
                permissions = permissions
            )
        }
    }

    fun onEvent(event: PermissionsEvent) {
        when (event) {
            is PermissionsEvent.OnPermissionResult -> onPermissionResult(event.permission, event.isGranted)
            PermissionsEvent.DismissPermissionsDialog -> dismissPermissionDialog()
            is PermissionsEvent.ShowPermissionDialog -> showPermissionDialog(event.permission)
        }
    }

    private fun onPermissionResult(permission: String, isGranted: Boolean) {
        val updatedPermissions = _state.value.permissions.map {
            if (it.permission == permission) {
                it.copy(isGranted = isGranted)
            } else {
                it
            }
        }
        _state.update {
            it.copy(
                permissions = updatedPermissions
            )
        }
    }

    private fun showPermissionDialog(permission: String) {
        _state.update {
            it.copy(
                visiblePermissionInDialog = permission,
            )
        }
    }

    private fun dismissPermissionDialog() {
        _state.update {
            it.copy(
                visiblePermissionInDialog = "",
            )
        }
    }
}