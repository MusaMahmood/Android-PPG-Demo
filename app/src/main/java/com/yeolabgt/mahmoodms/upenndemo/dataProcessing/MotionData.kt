package com.yeolabgt.mahmoodms.upenndemo.dataProcessing

import android.graphics.Color
import java.util.*

internal class MotionData(bufferSize: Int, addressMac: String, uuid: UUID, fileTimestamp: String, samplingRate: Int = 1000, MSBFirst: Boolean = true, saveData: Boolean = true) :
        BaseDataCollector(addressMac, uuid) {
    var dataBufferAccX: DataBuffer = DataBuffer(bufferSize, true, samplingRate, 375, "AccX", Color.RED)
    var dataBufferAccY: DataBuffer = DataBuffer(bufferSize, false, samplingRate, 375, "AccY", Color.GREEN)
    var dataBufferAccZ: DataBuffer = DataBuffer(bufferSize, false, samplingRate, 375, "AccZ", Color.BLUE)
    var dataBufferGyrX: DataBuffer = DataBuffer(bufferSize, false, samplingRate, 375, "GyrX", Color.RED)
    var dataBufferGyrY: DataBuffer = DataBuffer(bufferSize,false, samplingRate, 375, "GyrY", Color.GREEN)
    var dataBufferGyrZ: DataBuffer = DataBuffer(bufferSize,false, samplingRate, 375, "GyrZ", Color.BLUE)
    // File data saver
    var dataSaver: DataSaver? = null

    // Parameters
    init {
        Companion.MSBFirst = MSBFirst
        if (saveData) {
            dataSaver = DataSaver("/MotionData", "ICMData", addressMac, fileTimestamp, samplingRate)
        }
    }

    fun handleNewData(bytes: ByteArray) {
        handleNewPacket(bytes)
        val tempDoubleArrayAccX = DoubleArray(bytes.size/12)
        val tempDoubleArrayAccY = DoubleArray(bytes.size/12)
        val tempDoubleArrayAccZ = DoubleArray(bytes.size/12)
        val tempDoubleArrayGyrX = DoubleArray(bytes.size/12)
        val tempDoubleArrayGyrY = DoubleArray(bytes.size/12)
        val tempDoubleArrayGyrZ = DoubleArray(bytes.size/12)
        for (i in 0 until bytes.size / 12) {
            tempDoubleArrayAccX[i] = bytesToDoubleMPUAccel(bytes[12 * i + 0], bytes[12 * i + 1])
            tempDoubleArrayAccY[i] = bytesToDoubleMPUAccel(bytes[12 * i + 2], bytes[12 * i + 3])
            tempDoubleArrayAccZ[i] = bytesToDoubleMPUAccel(bytes[12 * i + 4], bytes[12 * i + 5])
            tempDoubleArrayGyrX[i] = bytesToDoubleMPUGyro(bytes[12 * i + 6], bytes[12 * i + 7])
            tempDoubleArrayGyrY[i] = bytesToDoubleMPUGyro(bytes[12 * i + 8], bytes[12 * i + 9])
            tempDoubleArrayGyrZ[i] = bytesToDoubleMPUGyro(bytes[12 * i + 10], bytes[12 * i + 11])
        }
        this.dataBufferAccX.addToBuffer(tempDoubleArrayAccX)
        this.dataBufferAccY.addToBuffer(tempDoubleArrayAccY)
        this.dataBufferAccZ.addToBuffer(tempDoubleArrayAccZ)
        this.dataBufferGyrX.addToBuffer(tempDoubleArrayGyrX)
        this.dataBufferGyrY.addToBuffer(tempDoubleArrayGyrY)
        this.dataBufferGyrZ.addToBuffer(tempDoubleArrayGyrZ)
        totalDataPointsReceived += bytes.size / 12
    }

    fun setGyroRange(range: Double) {
        gyroRange = range
    }

    fun setAccelRange(range: Double) {
        accelRange = range
    }

    fun setMSB(msb: Boolean) {
        MSBFirst = msb
    }

    fun saveAndResetBuffers() {
        this.resetBuffer()
        // Save data to file before resetting
        this.dataSaver?.saveDoubleArrays(dataBufferAccX.systemTimeStampsDoubles!!, dataBufferAccX.dataBufferDoubles!!, dataBufferAccY.dataBufferDoubles!!, dataBufferAccZ.dataBufferDoubles!!, dataBufferGyrX.dataBufferDoubles!!, dataBufferGyrY.dataBufferDoubles!!, dataBufferGyrZ.dataBufferDoubles!!)
        this.dataBufferAccX.resetBuffer()
        this.dataBufferAccY.resetBuffer()
        this.dataBufferAccZ.resetBuffer()
        this.dataBufferGyrX.resetBuffer()
        this.dataBufferGyrY.resetBuffer()
        this.dataBufferGyrZ.resetBuffer()
    }

    companion object {
        private var MSBFirst: Boolean = false
        var gyroRange = 4000.0
        var accelRange = 16.0

        fun bytesToDoubleMPUAccel(a1: Byte, a2: Byte): Double {
            val unsigned: Int = unsignedBytesToInt(a1, a2, MSBFirst)
            return unsignedToSigned16bit(unsigned).toDouble() / 32767.0 * accelRange
        }

        fun bytesToDoubleMPUGyro(a1: Byte, a2: Byte): Double {
            val unsigned: Int = unsignedBytesToInt(a1, a2, MSBFirst)
            return unsignedToSigned16bit(unsigned).toDouble() / 32767.0 * gyroRange
        }
    }
}