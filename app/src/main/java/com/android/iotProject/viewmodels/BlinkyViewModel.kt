/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.android.iotProject.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.iotProject.profile.BlinkyManager
import android.bluetooth.BluetoothDevice
import no.nordicsemi.android.ble.ConnectRequest
import androidx.lifecycle.LiveData
import com.android.iotProject.adapter.DiscoveredBluetoothDevice
import no.nordicsemi.android.log.LogSession
import no.nordicsemi.android.ble.callback.AfterCallback
import no.nordicsemi.android.ble.livedata.state.ConnectionState
import no.nordicsemi.android.log.Logger

class BlinkyViewModel(application: Application) : AndroidViewModel(application) {
    private val blinkyManager: BlinkyManager
    private var device: BluetoothDevice? = null
    private var connectRequest: ConnectRequest? = null
    val connectionState: LiveData<ConnectionState>
        get() = blinkyManager.state
    val buttonState: LiveData<Boolean>
        get() = blinkyManager.buttonState
    val ledState: LiveData<Boolean>
        get() = blinkyManager.ledState

    /**
     * Connect to the given peripheral.
     *
     * @param target the target device.
     */
    fun connect(target: DiscoveredBluetoothDevice) {
        // Prevent from calling again when called again (screen orientation changed).
        if (device == null) {
            device = target.device
            val logSession = Logger
                .newSession(getApplication(), null, target.address, target.name)
            blinkyManager.setLogger(logSession)
            reconnect()
        }
    }

    /**
     * Reconnects to previously connected device.
     * If this device was not supported, its services were cleared on disconnection, so
     * reconnection may help.
     */
    fun reconnect() {
        if (device != null) {
            connectRequest = blinkyManager.connect(device!!)
                .retry(3, 100)
                .useAutoConnect(false)
                .then { d: BluetoothDevice? -> connectRequest = null }
            connectRequest!!.enqueue()
        }
    }

    /**
     * Disconnect from peripheral.
     */
    private fun disconnect() {
        device = null
        if (connectRequest != null) {
            connectRequest!!.cancelPendingConnection()
        } else if (blinkyManager.isConnected) {
            blinkyManager.disconnect().enqueue()
        }
    }

    /**
     * Sends a command to turn ON or OFF the LED on the nRF5 DK.
     *
     * @param on true to turn the LED on, false to turn it OFF.
     */
    fun setLedState(on: Boolean) {
        blinkyManager.turnLed(on)
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

    init {

        // Initialize the manager.
        blinkyManager = BlinkyManager(getApplication())
    }
}