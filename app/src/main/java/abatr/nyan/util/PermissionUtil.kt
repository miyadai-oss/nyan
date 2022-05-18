package abatr.nyan.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class PermissionUtil {
    companion object {
        fun isBluetoothPermitted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Manifest.permission.BLUETOOTH_CONNECT
                } else {
                    Manifest.permission.BLUETOOTH
                }
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}