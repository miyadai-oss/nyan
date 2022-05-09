package abatr.nyan.composable

import abatr.nyan.R
import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

enum class PermissionState {
    Checking,
    Granted,
    Denied,
}

@Composable
fun BondedDevicesScreen(onAddDevice: () -> Unit) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    if (permissionState == PermissionState.Granted) {
                        IconButton(onClick = {
                            onAddDevice()
                        }) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = stringResource(id = R.string.add_device)
                            )
                        }
                    }
                }
            )
        }
    ) {
        when (permissionState) {
            PermissionState.Checking -> {
                // NOP.
            }
            PermissionState.Granted -> {
                BondedDevices(bondedDevices)
            }
            PermissionState.Denied -> {
                PermissionGuide()
            }
        }
    }
}