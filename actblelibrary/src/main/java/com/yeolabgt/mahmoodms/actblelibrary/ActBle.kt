package com.yeolabgt.mahmoodms.actblelibrary

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log

import java.util.HashMap
import java.util.UUID
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

/**
 * Created by mmahmood31 on 11/8/2017.
 * ActBle Library Object
 */

class ActBle(private val mContext: Context, private val mBluetoothManager: BluetoothManager?,
             private val mActBleListener: ActBleListener) {
    private val bluetoothGattHashMap = HashMap<String, BluetoothGatt>()

    /**
     * Bluetooth LE Gatt Callback Methods.
     */
    private val mBleGattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            mActBleListener.onConnectionStateChange(gatt, status, newState)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            mActBleListener.onServicesDiscovered(gatt, status)
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            Log.d(TAG, "onCharacteristicRead: " + characteristic.uuid.toString())
            removeProcess(characteristic, ActBleProcessQueue.REQUEST_TYPE_READ_CHAR)
            mActBleListener.onCharacteristicRead(gatt, characteristic, status)
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            Log.d(TAG, "onCharacteristicWrite: " + characteristic.uuid.toString())
            removeProcess(characteristic, ActBleProcessQueue.REQUEST_TYPE_WRITE_CHAR)
            mActBleListener.onCharacteristicWrite(gatt, characteristic, status)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            mActBleListener.onCharacteristicChanged(gatt, characteristic)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            Log.d(TAG, "onDescriptorRead: " + descriptor.characteristic.uuid.toString())
            removeProcess(descriptor.characteristic, ActBleProcessQueue.REQUEST_TYPE_READ_DESCRIPTOR)
            mActBleListener.onDescriptorRead(gatt, descriptor, status)
        }


        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            Log.d(TAG, "onDescriptorWrite: " + descriptor.characteristic.uuid.toString())
            removeProcess(descriptor.characteristic, ActBleProcessQueue.REQUEST_TYPE_WRITE_DESCRIPTOR)
            mActBleListener.onDescriptorWrite(gatt, descriptor, status)
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
            mActBleListener.onReadRemoteRssi(gatt, rssi, status)
        }

    }

    /**
     * Removes process from sequential queue once completed, based on request code.
     *
     */
    fun removeProcess(characteristic: BluetoothGattCharacteristic, requestCode: Int) {
        if (ActBleProcessQueue.actBleCharacteristicListSize != 0) {
            Log.d(TAG, "removeProcess: (Characteristic), requestCode: " + requestCode)
            var remove = false
            var position = 0
            for (i in ActBleProcessQueue.getActBleCharacteristicList().indices) {
                if (ActBleProcessQueue.getActBleCharacteristicList()[i].
                        bluetoothGattCharacteristic?.uuid == characteristic.uuid) {
                    if (ActBleProcessQueue.getActBleCharacteristicList()[i].
                            requestCode == requestCode) {
                        remove = true
                        position = i
                        break
                    }
                }
            }
            if (remove) {
                ActBleProcessQueue.removeCharacteristicRequest(position)
                runProcess() //Run next process:
            }
        }
    }

    /**
     * runProcess() will execute queued BLE request if available.
     * No input params; Does not return anything.
     * Should only be called once from the device control activity.
     */
    fun runProcess() {
        if (ActBleProcessQueue.actBleCharacteristicListSize > 0) {
            val executorService = Executors.newSingleThreadExecutor()
            val operationSuccess = executorService.submit(ActBleProcessQueue.SequentialThread())
            try {
                Log.d(TAG, "runProcess: operationSuccess:"
                        + operationSuccess.get().toString())
            } catch (e: InterruptedException) {
                Log.e(TAG, "runProcess = InterruptedException: ", e)
            } catch (e: ExecutionException) {
                Log.e(TAG, "runProcess - ExecutionException: ", e)
            }

        }
    }

    /**
     * Manual characteristic read of Bluetooth LE characteristic
     * @param gatt - Relevant BluetoothGatt object
     * @param characteristic - Relevant characteristic whose data is to be read.
     * Note there may be read restrictions, so double check.
     */
    fun readCharacteristic(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        val actBleCharacteristic = ActBleCharacteristic(ActBleProcessQueue.REQUEST_TYPE_READ_CHAR, gatt, characteristic)
        ActBleProcessQueue.addCharacteristicRequest(actBleCharacteristic)
    }

    /**
     * Method for writing a characteristic value for writable characteristics.
     * @param gatt - Relevant BluetoothGatt object
     * @param characteristic - Relevant characteristic whose notifications are to be written
     * @param bytes - Data to write. Note that there are restrictions on client-side BLE devices,
     * so make sure to double check parameters.
     */
    fun writeCharacteristic(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, bytes: ByteArray) {
        characteristic.value = bytes
        val actBleCharacteristic = ActBleCharacteristic(ActBleProcessQueue.REQUEST_TYPE_WRITE_CHAR, gatt, characteristic)
        ActBleProcessQueue.addCharacteristicRequest(actBleCharacteristic)
        runProcess()
    }

    /**
     * Public access function for setting/disabling characteristic notifications for a particular
     * characteristic:
     * @param gatt - Relevant BluetoothGatt object
     * @param characteristic - Relevant characteristic whose notifications are to be enabled/disabled
     * @param enableCharacteristic - Enable (#true) or disable (#false) characteristic notifications.
     * No return value.
     */
    fun setCharacteristicNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, enableCharacteristic: Boolean) {
        if (enableCharacteristic) {
            enableNotifications(gatt, characteristic)
        } else {
            disableNotifications(gatt, characteristic)
        }
    }

    /**
     * Method to enable notification for a characteristic:
     * @param gatt - Relevant BluetoothGatt object
     * @param characteristic - Relevant characteristic whose notifications are to be enabled
     * Nothing returned
     */
    private fun enableNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        if (!gatt.setCharacteristicNotification(characteristic, true)) return
        val clientConfig = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG) ?: return
        clientConfig.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        val actBleCharacteristic = ActBleCharacteristic(ActBleProcessQueue.REQUEST_TYPE_WRITE_DESCRIPTOR, gatt, clientConfig, characteristic)
        ActBleProcessQueue.addCharacteristicRequest(actBleCharacteristic)
    }

    /**
     * Method to disable notifications for a characteristic:
     * @param gatt - Relevant BluetoothGatt object
     * @param characteristic - Relevant characteristic whose notifications are to be disabled
     * No return value.
     */
    private fun disableNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        // Return if issue with setting notification to disable.
        if (!gatt.setCharacteristicNotification(characteristic, false)) return
        // Get Descriptor: Return if characteristic null
        val clientConfig = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG) ?: return
        // Set descriptor to 'DISABLE_NOTIFICATION_VALUE'
        clientConfig.value = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        // Create new process command/request to be queued
        val actBleCharacteristic = ActBleCharacteristic(ActBleProcessQueue.REQUEST_TYPE_WRITE_DESCRIPTOR, gatt, clientConfig, characteristic)
        // Add to process queue for sequential execution
        ActBleProcessQueue.addCharacteristicRequest(actBleCharacteristic)
    }

    /**
     * Connect to Bluetooth LE device, returns Bluetooth Gatt if present.
     * @param device - A Bluetooth Device object - requires MAC address field and name field
     * @param autoConnect - Whether or not to auto-connect to this device. Default: false
     * @return BluetoothGatt? - returns connected Bluetooth Gatt, else -> null
     */
    fun connect(device: BluetoothDevice?, autoConnect: Boolean = false): BluetoothGatt? {
        if (mBluetoothManager == null || device == null) {
            mActBleListener.onError("Bluetooth Manager is Null")
            return null
        }
        val gatt = bluetoothGattHashMap[device.address]
        if (gatt != null) {
            Log.d(TAG, "Found Device in Hashmap: " + device.address
                    + " :: Attempting to Disconnect")
            //If already in hashmap, disconnect before attempting to reconnect
            gatt.disconnect()
            gatt.close()
        }
        //Check Connection State:
        var bluetoothGatt: BluetoothGatt? = null
        if (mBluetoothManager.getConnectionState(device, BluetoothProfile.GATT) == BluetoothProfile.STATE_DISCONNECTED) {
            //Attempt to Connect:
            bluetoothGatt = device.connectGatt(mContext, autoConnect, mBleGattCallback)
            if (bluetoothGattHashMap.containsKey(device.address)) {
                bluetoothGattHashMap.remove(device.address)
            }
            bluetoothGattHashMap.put(device.address, bluetoothGatt)
        }
        return bluetoothGatt
    }

    /**
     * Disconnect function for Bluetooth LE device.
     * @param bluetoothGatt - Relevant Bluetooth Gatt to disable connection to - a Bluetooth Gatt
     * returned by the {@link #connect(BluetoothDevice?, Boolean)}
     */
    fun disconnect(bluetoothGatt: BluetoothGatt) {
        try {
            bluetoothGatt.disconnect()
            bluetoothGatt.close()
            bluetoothGattHashMap.remove(bluetoothGatt.device.address)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.toString())
            mActBleListener.onError(e.toString())
        }

    }

    /**
     * Interface for BLE Connectivity Callbacks
     */
    interface ActBleListener {
        fun onServicesDiscovered(gatt: BluetoothGatt, status: Int)

        fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int)

        fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int)

        fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic)

        fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int)

        fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int)

        fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int)

        fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int)

        fun onError(errorMessage: String)
    }

    //Constants
    companion object {
        private val TAG = ActBle::class.java.simpleName
        private val CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }
}
