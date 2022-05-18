package abatr.nyan.composable

import abatr.nyan.R
import abatr.nyan.util.PermissionUtil
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
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
                    @SuppressLint("MissingPermission")
                    if (PermissionUtil.isBluetoothPermitted(LocalContext.current)) {
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