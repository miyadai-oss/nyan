package abatr.nyan.composable

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
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
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Manifest.permission.BLUETOOTH_CONNECT
    } else {
        Manifest.permission.BLUETOOTH
    }

    var permissionState by remember { mutableStateOf(PermissionState.Checking) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
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
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    permissionState = PermissionState.Granted
                    bondedDevices.clear()
                    bondedDevices.addAll((context.getSystemService(ComponentActivity.BLUETOOTH_SERVICE) as BluetoothManager).adapter.bondedDevices)
                } else {
                    permissionState = PermissionState.Checking
                    launcher.launch(permission)
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