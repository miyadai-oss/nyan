package net.fudiyama.nyan

import net.fudiyama.nyan.composable.BondedDevicesScreen
import net.fudiyama.nyan.composable.DeviceScanScreen
import net.fudiyama.nyan.ui.theme.NyanTheme
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
            var serviceEnabled by remember { mutableStateOf(NyanService.isEnabled(this)) }

            NyanTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(id = R.string.app_name)) },
                            actions = {
                                when (appState) {
                                    AppState.MAIN -> {
                                        // FIXME These composables should be separated.
                                        Switch(
                                            checked = serviceEnabled,
                                            onCheckedChange = { checked ->
                                                NyanService.setEnabled(this@MainActivity, checked)
                                                serviceEnabled = checked
                                                if (serviceEnabled) {
                                                    NyanService.startNyanService(this@MainActivity)
                                                } else {
                                                    NyanService.stopNyanService(this@MainActivity)
                                                }
                                            })
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
                                    else -> {
                                        // Nothing to display at this time.
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