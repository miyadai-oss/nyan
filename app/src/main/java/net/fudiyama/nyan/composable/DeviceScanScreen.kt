package net.fudiyama.nyan.composable

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import net.fudiyama.nyan.R
import java.util.*

@SuppressLint("MissingPermission")
@Composable
fun DeviceScanScreen() {
    val context = LocalContext.current
    val devices = remember { mutableStateListOf<BluetoothDevice>() }

    Column {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        DeviceList(devices)
    }

    DeviceReceiver(
        onFound = { device ->
            if (device != null) {
                if (!devices.contains(device)) {
                    for (uuid in device.uuids) {
                        if (uuid.uuid.equals(UUID.fromString(context.getString(R.string.rfcomm_service_uuid)))) {
                            devices.add(device)
                        }
                    }
                } else {
                    val position = devices.indexOf(device)
                    devices.removeAt(position)
                    devices.add(position, device)
                }
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