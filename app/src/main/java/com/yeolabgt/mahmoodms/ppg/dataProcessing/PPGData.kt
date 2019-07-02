package com.yeolabgt.mahmoodms.ppg.dataProcessing

import android.graphics.Color
import java.util.*

internal class PPGData(bufferSize: Int, addressMac: String, uuid: UUID, fileTimestamp: String, samplingRate: Int = 100, saveData: Boolean = true, channelNumber: Int=1) :
        BaseDataCollector(addressMac, uuid) {
    var dataBuffer: DataBuffer = DataBuffer(bufferSize, true, samplingRate, 120, "PPG Ch$channelNumber", Color.BLUE)
    var dataSaver: DataSaver? = null

    init {
        if (saveData) {
            dataSaver = DataSaver("/PPGData", "PPGData", addressMac, fileTimestamp, samplingRate)
        }
    }

    fun handleNewData(bytes: ByteArray) {
        handleNewPacket(bytes)
        val doubleArrayTemp = DoubleArray(bytes.size / 2)
        for (i in 0 until bytes.size / 2) {
            doubleArrayTemp[i] = bytesToDouble14bit(bytes[2 * i], bytes[2 * i + 1])
        }
        this.dataBuffer.addToBuffer(doubleArrayTemp)
        totalDataPointsReceived += bytes.size / 2
    }

    fun saveAndResetBuffers() {
        this.resetBuffer()
        this.dataSaver?.saveDoubleArrays(dataBuffer.timeStampsDoubles!!, dataBuffer.dataBufferDoubles!!)
        this.dataBuffer.resetBuffer()
    }

    companion object {
        fun bytesToDouble14bit(a1: Byte, a2: Byte, gain: Double=(1.0/6.0), ref: Double = 0.6, msbFirst: Boolean = false): Double {
            val unsigned = unsignedBytesToInt(a1, a2, msbFirst)
            val signed = unsignedToSigned16bit(unsigned).toDouble()
            return signed / 16384.0 / gain * ref
        }
    }
}