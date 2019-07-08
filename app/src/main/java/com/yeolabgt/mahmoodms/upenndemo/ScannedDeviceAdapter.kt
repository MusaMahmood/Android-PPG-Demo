package com.yeolabgt.mahmoodms.upenndemo

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanRecord
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by mahmoodms on 5/31/2016.
 *
 */

internal class ScannedDeviceAdapter//Constructor
(context: Context, private val resId: Int, private val list: MutableList<ScannedDevice>) : ArrayAdapter<ScannedDevice>(context, resId, list) {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView = view
        val item = getItem(position)
        if (convertView == null) {
            convertView = inflater.inflate(resId, null)
        }
        val deviceNameTextView = convertView!!.findViewById<TextView>(R.id.device_name)
        val deviceAddress = convertView.findViewById<TextView>(R.id.device_address)
        val deviceRSSI = convertView.findViewById<TextView>(R.id.device_rssi)
        if (item != null) {
            deviceNameTextView.text = item.displayName
            deviceAddress.text = item.device!!.address
            val currentRSSI = item.rssi.toString() + " dB"
            deviceRSSI.text = currentRSSI
        }
        return convertView
    }

    fun update(newDevice: BluetoothDevice?, rssi: Int, scanRecord: ScanRecord) {
        if (newDevice == null || newDevice.address == null) return
        var contains = false
        for (device in list) {
            if (newDevice.address.equals(device.device!!.address, ignoreCase = true)) {
                contains = true
                device.rssi = rssi
                break
            }
        }
        Log.d(TAG, "update: ScanRecord,$scanRecord")
        if (!contains) {
            list.add(ScannedDevice(newDevice, rssi))
        }
    }

    fun remove(index: Int) {
        list.removeAt(index)
    }

    companion object {
        private val TAG = ScannedDeviceAdapter::class.java.simpleName
    }
}
