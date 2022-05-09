package abatr.nyan

import abatr.nyan.composable.BondedDevicesScreen
import abatr.nyan.composable.DeviceScanDialog
import abatr.nyan.ui.theme.NyanTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NyanTheme {
                var isDialogShown by remember { mutableStateOf(false) }

                BondedDevicesScreen(onAddDevice = {
                    isDialogShown = true
                })
                DeviceScanDialog(isDialogShown, onDismissRequest = { isDialogShown = false })
            }
        }
    }
}