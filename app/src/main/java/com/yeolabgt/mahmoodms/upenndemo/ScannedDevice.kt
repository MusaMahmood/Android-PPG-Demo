package com.yeolabgt.mahmoodms.upenndemo

import android.bluetooth.BluetoothDevice

/**
 * Created by mahmoodms on 5/31/2016.
 * Class for holding scanned device Bluetooth LE data.
 */

internal class ScannedDevice(val device: BluetoothDevice?, var rssi: Int) {
    var displayName: String? = null
        private set
    val deviceMac: String

    init {
        if (device == null) {
            throw IllegalArgumentException("BluetoothDevice == Null")
        }
        displayName = device.name

        if(displayName == null || displayName!!.isEmpty()) {
            displayName = UNKNOWN
        }
        deviceMac = device.address
    }

    companion object {
        private val UNKNOWN = "Unknown"
    }
}
