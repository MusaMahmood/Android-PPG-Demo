package com.yeolabgt.mahmoodms.upenndemo.dataProcessing

import android.graphics.Color
import java.util.*

internal class ExGData(bufferSize: Int, addressMac: String, uuid: UUID, fileTimestamp: String, samplingRate: Int = 250, saveData: Boolean = true, channelNumber: Int = 1) :
        BaseDataCollector(addressMac, uuid) {
    var dataBuffer: DataBuffer = DataBuffer(bufferSize, true, samplingRate, 2000, "EMG Ch$channelNumber", Color.BLUE)
    var dataSaver: DataSaver? = null

    init {
        if (saveData) {
            dataSaver = DataSaver("/EMGData", "EMGData", addressMac, fileTimestamp, samplingRate)
        }
    }

    fun handleNewData(bytes: ByteArray) {
        handleNewPacket(bytes)
        val doubleArrayTemp = DoubleArray(bytes.size / 3)
        for (i in 0 until bytes.size / 3) {
            doubleArrayTemp[i] = bytesToDouble24bit(bytes[3 * i], bytes[3 * i + 1], bytes[3 * i + 2])
        }
        this.dataBuffer.addToBuffer(doubleArrayTemp)
        totalDataPointsReceived += bytes.size / 3
    }

    fun saveAndResetBuffers() {
        this.resetBuffer()
        this.dataSaver?.saveDoubleArrays(dataBuffer.timeStampsDoubles!!, dataBuffer.dataBufferDoubles!!)
        this.dataBuffer.resetBuffer()
    }

    companion object {
        fun bytesToDouble24bit(a1: Byte, a2: Byte, a3: Byte, gain: Double = 12.0, msbFirst: Boolean = true): Double {
            val unsigned = unsignedBytesToInt(a1, a2, a3, msbFirst)
            val signed = unsignedToSigned24bit(unsigned).toDouble()
            return signed / 8388607.0 / gain * 2.42
        }
    }
}