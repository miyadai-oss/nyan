package abatr.nyan.composable

import abatr.nyan.R
import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat

@Composable
fun DeviceList(devices: MutableList<BluetoothDevice>) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(devices) { device ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    // I'd like to refactor it a bit better.
                    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(
                            LocalContext.current,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED) || (Build.VERSION.SDK_INT < Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(
                            LocalContext.current, Manifest.permission.BLUETOOTH
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        Text(
                            if (device.name != null) device.name else stringResource(R.string.unknown),
                            style = MaterialTheme.typography.h6
                        )
                        Text(device.address, style = MaterialTheme.typography.body1)
                    }
                }
            }
        }
    }
}