package abatr.nyan.composable

import abatr.nyan.R
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun DeviceScanDialog(isDialogShown: Boolean, onDismissRequest: () -> Unit) {
    if (isDialogShown) {
        // TODO Implement a dialog that detects and displays devices.
        AlertDialog(
            onDismissRequest = {
                onDismissRequest()
            },
            title = {
                Text(text = stringResource(id = R.string.select_device))
            },
            confirmButton = {
                TextButton(onClick = {
                    onDismissRequest()
                }) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            }
        )
    }
}