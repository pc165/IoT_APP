package com.android.iotproject.profile

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import com.android.iotproject.profile.callback.DataCallback
import no.nordicsemi.android.ble.livedata.ObservableBleManager
import java.util.*


class ProductManager(context: Context) : ObservableBleManager(context) {
    private val TAG = "MyActivity"
    private var rxCharacteristic: BluetoothGattCharacteristic? = null
    private var txCharacteristic: BluetoothGattCharacteristic? = null
    private val dataCallBack: DataCallback = DataCallback()


    private inner class ProductBleManagerGattCallback : BleManagerGattCallback() {
        override fun initialize() {
            setNotificationCallback(txCharacteristic).with(dataCallBack)
            requestMtu(260).enqueue()
            enableNotifications(txCharacteristic).enqueue()
        }

        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            val service = gatt.getService(UART_UUID_SERVICE)
            if (service != null) {
                rxCharacteristic = service.getCharacteristic(RX_UUID_CHAR)
                txCharacteristic = service.getCharacteristic(TX_UUID_CHAR)
            }
            return rxCharacteristic != null && txCharacteristic != null
        }

        override fun onServicesInvalidated() {
            rxCharacteristic = null
            txCharacteristic = null
        }
    }

    override fun getGattCallback(): BleManagerGattCallback {
        return ProductBleManagerGattCallback()
    }

    fun getPicture() {
        if (rxCharacteristic == null) return
        Log.i(TAG, "Get Picture")
        writeCharacteristic(
            rxCharacteristic,
            OperationCode.getPicture(),
            BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        ).with(dataCallBack).enqueue()
    }

    companion object {
        val UART_UUID_SERVICE: UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
        private val RX_UUID_CHAR: UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E")
        private val TX_UUID_CHAR: UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E")
    }
}


