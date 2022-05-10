package abatr.nyan.composable

import abatr.nyan.R
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun DeviceScanDialog(isDialogShown: Boolean, onDismissRequest: () -> Unit) {
    if (isDialogShown) {
        val devices = remember { mutableStateListOf<BluetoothDevice>() }

        AlertDialog(
            onDismissRequest = {
                onDismissRequest()
            },
            title = {
                Text(text = stringResource(R.string.select_device))
            },
            text = {
                Column {
                    if (devices.isEmpty()) {
                        Text(text = stringResource(R.string.no_devices))
                    } else {
                        // TODO When devices are detected, they are listed.
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator()
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onDismissRequest()
                }) {
                    Text(text = stringResource(android.R.string.cancel))
                }
            }
        )
        LaunchedEffect(true) {
            // TODO Implement the device detection process.
        }
    }
}