package net.fudiyama.nyan

import net.fudiyama.nyan.util.PermissionUtil
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class RfcommServer(private val context: Context) {
    private var serverSocket: BluetoothServerSocket? = null
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.STATE_OFF
                )) {
                    BluetoothAdapter.STATE_OFF -> {
                        stopServer()
                    }
                    BluetoothAdapter.STATE_ON -> {
                        startServer()
                    }
                }
            }
        }
    }

    fun start() {
        context.registerReceiver(receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
        startServer()
    }

    fun stop() {
        context.unregisterReceiver(receiver)
        stopServer()
    }

    private fun startServer() {
        val adapter =
            (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        if (adapter.isEnabled) {
            @SuppressLint("MissingPermission")
            if (PermissionUtil.isBluetoothPermitted(context)) {
                serverSocket = adapter.listenUsingRfcommWithServiceRecord(
                    context.getString(R.string.rfomm_service_name),
                    java.util.UUID.fromString(context.getString(R.string.rfcomm_service_uuid))
                )
            }
        }
    }

    private fun stopServer() {
        serverSocket?.close()
    }
}