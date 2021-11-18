package com.android.iotproject.profile.callback

import android.bluetooth.BluetoothDevice
import android.util.Log
import no.nordicsemi.android.ble.callback.DataSentCallback
import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback
import no.nordicsemi.android.ble.data.Data


class DataCallback : ProfileDataCallback, DataSentCallback {
    // Should parse sent and received data
    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        Log.i("Data Handler", "Data received $data")
    }

    override fun onDataSent(device: BluetoothDevice, data: Data) {
        Log.i("Data Handler", "Data send $data")
    }

    override fun onInvalidDataReceived(device: BluetoothDevice, data: Data) {
        Log.w("Data Handler", "Data invalid $data")
    }
}