package abatr.nyan.composable

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("MissingPermission")
@Composable
fun BondedDevices(bondedDevices: MutableList<BluetoothDevice>) {
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(bondedDevices) { device ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(device.name, style = MaterialTheme.typography.h6)
                    Text(device.address, style = MaterialTheme.typography.body1)
                }
            }
        }
    }
}