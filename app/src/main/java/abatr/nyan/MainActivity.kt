package abatr.nyan

import abatr.nyan.composable.PermissionRequiredScreen
import abatr.nyan.ui.theme.NyanTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NyanTheme {
                PermissionRequiredScreen()
            }
        }
    }
}