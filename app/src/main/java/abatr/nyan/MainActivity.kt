package abatr.nyan

import abatr.nyan.composable.BondedDevicesScreen
import abatr.nyan.composable.DeviceScanScreen
import abatr.nyan.ui.theme.NyanTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {
    enum class AppState {
        MAIN,
        DEVICE_SCAN
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var appState by remember { mutableStateOf(AppState.MAIN) }

            NyanTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(id = R.string.app_name)) },
                            actions = {
                                when (appState) {
                                    AppState.MAIN -> {
                                        IconButton(onClick = {
                                            // TODO I want to add some kind of cool screen transition effect.
                                            appState = AppState.DEVICE_SCAN
                                        }) {
                                            Icon(
                                                Icons.Filled.Add,
                                                contentDescription = stringResource(id = R.string.add_device)
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                ) {
                    when (appState) {
                        AppState.MAIN -> {
                            BondedDevicesScreen()
                        }
                        AppState.DEVICE_SCAN -> {
                            DeviceScanScreen()
                            BackHandler {
                                appState = AppState.MAIN
                            }
                        }
                    }
                }
            }
        }
    }
}