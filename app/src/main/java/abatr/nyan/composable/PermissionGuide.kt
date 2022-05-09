package abatr.nyan.composable

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PermissionGuide() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please allow permissions",
            style = MaterialTheme.typography.h5
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "This application requires Bluetooth permissions to work, so please allow access to location information in the settings screen.",
            style = MaterialTheme.typography.body2
        )
        val context = LocalContext.current
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.data = Uri.parse("package:abatr.nyan")
                context.startActivity(intent)
            }
        ) {
            Text(text = "Go to the settings screen")
        }
    }
}