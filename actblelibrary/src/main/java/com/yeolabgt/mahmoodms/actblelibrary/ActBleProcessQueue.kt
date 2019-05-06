package com.yeolabgt.mahmoodms.actblelibrary

import android.util.Log

import java.util.ArrayList
import java.util.concurrent.Callable

/**
 * Created by mmahmood31 on 11/8/2017.
 *
 */
internal object ActBleProcessQueue {
    // Immutable Values
    private val TAG = ActBleProcessQueue::class.java.simpleName
    val REQUEST_TYPE_READ_CHAR = 1
    val REQUEST_TYPE_WRITE_CHAR = 2
    val REQUEST_TYPE_READ_DESCRIPTOR = 3
    val REQUEST_TYPE_WRITE_DESCRIPTOR = 4
    // ArrayList is queue of commands to be executed
    private val actBleCharacteristicList = ArrayList<ActBleCharacteristic>()

    val actBleCharacteristicListSize: Int
        get() = if (!actBleCharacteristicList.isEmpty()) {
            actBleCharacteristicList.size
        } else 0

    // Variables:
    var numberOfBluetoothGattCommunications = 0

    /**
     * Adds characteristic request to queue list
     * @param actBleCharacteristic - ActBleCharacteristic that contains a single descriptor or
     * characteristic read/write operation.
     */
    fun addCharacteristicRequest(actBleCharacteristic: ActBleCharacteristic) {
        actBleCharacteristicList.add(actBleCharacteristic)
    }

    /**
     * Removes characteristic request from queue list after executing
     * @param index - index of ActBleCharacteristic that contains a single descriptor or
     * characteristic read/write operation in the actBleCharacteristicList
     */
    fun removeCharacteristicRequest(index: Int) {
        actBleCharacteristicList.removeAt(index)
    }

    /**
     * Retrieves actBleCharacteristicList, containing all the queued commands
     * @return actBleCharacteristicList
     */
    fun getActBleCharacteristicList(): List<ActBleCharacteristic> {
        return actBleCharacteristicList
    }

    /**
     * Executes request depending on the request code. Either a read/write of a characteristic or
     * descriptor
     * @param actBleCharacteristic - Queued request from list
     * @return boolean value of operation success.
     */
    private fun executeRequest(actBleCharacteristic: ActBleCharacteristic): Boolean {
        val bluetoothGatt = actBleCharacteristic.bluetoothGatt
        when (actBleCharacteristic.requestCode) {
            REQUEST_TYPE_READ_CHAR -> return bluetoothGatt!!.readCharacteristic(actBleCharacteristic.bluetoothGattCharacteristic)
            REQUEST_TYPE_WRITE_CHAR -> return bluetoothGatt!!.writeCharacteristic(actBleCharacteristic.bluetoothGattCharacteristic)
            REQUEST_TYPE_READ_DESCRIPTOR -> return bluetoothGatt!!.readDescriptor(actBleCharacteristic.bluetoothGattDescriptor)
            REQUEST_TYPE_WRITE_DESCRIPTOR -> return bluetoothGatt!!.writeDescriptor(actBleCharacteristic.bluetoothGattDescriptor)
            else -> return false
        }
    }

    /**
     * Internal Callable function that sequentially executes commands on a single thread.
     * Used for sequential Bluetooth Gatt connection/disconnection requests to ensure callback is
     * received and connections are successful.
     * No Input params
     * @return Boolean: success of 'executeRequest' operation, or null if nothing is queued.
     */
    internal class SequentialThread : Callable<Boolean> {
        @Throws(Exception::class)
        override fun call(): Boolean? {
            if (actBleCharacteristicList.isNotEmpty()) {
                Log.e(TAG, "Executing Bluetooth LE Command#" + (++numberOfBluetoothGattCommunications).toString())
                return executeRequest(actBleCharacteristicList[0])
            }
            return null
        }
    }
}
