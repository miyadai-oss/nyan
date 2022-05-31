package net.fudiyama.nyan.composable

import net.fudiyama.nyan.util.PermissionUtil
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

enum class PermissionState {
    Checking,
    Granted,
    Denied,
}

@Composable
fun BondedDevicesScreen() {
    val context = LocalContext.current
    val bondedDevices = remember { mutableStateListOf<BluetoothDevice>() }
    var permissionState by remember { mutableStateOf(PermissionState.Checking) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                @SuppressLint("MissingPermission")
                if (isGranted) {
                    permissionState = PermissionState.Granted
                    bondedDevices.clear()
                    bondedDevices.addAll((context.getSystemService(ComponentActivity.BLUETOOTH_SERVICE) as BluetoothManager).adapter.bondedDevices)
                } else {
                    permissionState = PermissionState.Denied
                }
            }
        )

        val lifecycleObserver = remember {
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    @SuppressLint("MissingPermission")
                    if (PermissionUtil.isBluetoothPermitted(context)) {
                        permissionState = PermissionState.Granted
                        bondedDevices.clear()
                        bondedDevices.addAll((context.getSystemService(ComponentActivity.BLUETOOTH_SERVICE) as BluetoothManager).adapter.bondedDevices)
                    } else {
                        permissionState = PermissionState.Checking
                        launcher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                    }
                }
            }
        }

        val lifecycle = LocalLifecycleOwner.current.lifecycle
        DisposableEffect(lifecycle, lifecycleObserver) {
            lifecycle.addObserver(lifecycleObserver)
            // TODO It is not well understood when onDispose is called, so check.
            onDispose {
                lifecycle.removeObserver(lifecycleObserver)
            }
        }
    } else {
        permissionState = PermissionState.Granted
    }

    when (permissionState) {
        PermissionState.Checking -> {
            // NOP.
        }
        PermissionState.Granted -> {
            DeviceList(bondedDevices)
        }
        PermissionState.Denied -> {
            PermissionGuide()
        }
    }
}