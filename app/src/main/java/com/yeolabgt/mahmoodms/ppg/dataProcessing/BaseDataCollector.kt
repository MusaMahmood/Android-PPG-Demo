package com.yeolabgt.mahmoodms.ppg.dataProcessing

import com.google.common.primitives.Bytes
import java.util.*

/**
 * Handles raw data in Bytes, directly from
 */

open class BaseDataCollector(addressMac: String, uuid: UUID) {
    private var byteBuffer: ByteArray? = null
    var packetGraphingCounter: Short = 0 // Used for updating graph, up to 255 packets/refresh
    var classificationCounter: Int = 0
    // Metrics
    private var totalBytesRecieved: Long = 0
    private var totalPacketsRecieved: Int = 0
    var totalDataPointsReceived: Long = 0
    // Identifying info
    val mAddress: String
    private val mUUID: UUID
    // Graph and storage synchronization:
    var sync: Boolean = false

    init {
        this.packetGraphingCounter = 0
        this.totalBytesRecieved = 0
        this.totalPacketsRecieved = 0
        this.mAddress = addressMac
        this.mUUID = uuid
    }

    internal fun handleNewPacket(newDataPacket: ByteArray) {
        if (this.byteBuffer != null) {
            this.byteBuffer = Bytes.concat(this.byteBuffer, newDataPacket)
        } else {
            this.byteBuffer = newDataPacket
        }
        packetGraphingCounter++
        totalPacketsRecieved++
        totalBytesRecieved+=newDataPacket.size
    }

    internal fun resetBuffer() {
        this.byteBuffer = null
        packetGraphingCounter = 0
    }

    internal fun resetClassificationBuffer() {
        this.classificationCounter = 0
    }

    companion object {
        fun unsignedToSigned16bit(unsigned: Int): Int {
            return if (unsigned and 0x8000 != 0)
                -1 * (0x8000 - (unsigned and 0x8000 - 1))
            else
                unsigned
        }

        fun unsignedBytesToInt(b0: Byte, b1: Byte, MSBFirst: Boolean): Int {
            return if (MSBFirst)
                (unsignedByteToInt(b0) shl 8) + unsignedByteToInt(b1)
            else
                unsignedByteToInt(b0) + (unsignedByteToInt(b1) shl 8)
        }

        private fun unsignedByteToInt(b: Byte): Int {
            return (b.toInt() and 0xFF)
        }
    }
}