package abatr.nyan.composable

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@SuppressLint("MissingPermission")
@Composable
fun DeviceScanScreen() {
    val devices = remember { mutableStateListOf<BluetoothDevice>() }

    LazyColumn {
        items(devices) { device ->
            // TODO Implement the layout of the device display.
            Text(if (device.name != null) device.name else device.address)
        }
    }

    DeviceReceiver(
        onFound = { device ->
            if (device != null && !devices.contains(device)) {
                devices.add(device)
            }
        }
    )

    // TODO Implement processing of runtime permissions required for device detection.
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceReceiver(onFound: (device: BluetoothDevice?) -> Unit) {
    val context = LocalContext.current
    val adapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    val currentOnFound by rememberUpdatedState(onFound)

    DisposableEffect(context) {
        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    when (intent.action) {
                        BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                            adapter.startDiscovery()
                        }
                        BluetoothDevice.ACTION_FOUND -> {
                            val device =
                                intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                            currentOnFound(device)
                        }
                    }
                }
            }
        }

        context.registerReceiver(receiver, filter)
        adapter.startDiscovery()

        onDispose {
            context.unregisterReceiver(receiver)
            adapter.cancelDiscovery()
        }
    }
}