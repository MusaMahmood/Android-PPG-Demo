package com.yeolabgt.mahmoodms.ppg.dataProcessing

import com.google.common.primitives.Doubles

open class DataBuffer(slideBufferSize: Int) {
    var dataBufferDoubles: DoubleArray? = null
    var slideBufferEnabled: Boolean = false
    var bufferSize: Int = 0
    var classificationBuffer: DoubleArray? = null
    var currentGraphIndex: Int = 0

    init {
        this.currentGraphIndex = 0
        if (slideBufferSize != 0) {
            enableSlideBuffer(slideBufferSize)
        } else {
            disableSlideBuffer()
        }
    }

    fun getCurrentIndexAndIncrement(): Int {
        return this.currentGraphIndex++
    }

    private fun enableSlideBuffer(bufferSize: Int) {
        this.slideBufferEnabled = true
        this.classificationBuffer = DoubleArray(bufferSize)
    }

    private fun disableSlideBuffer() {
        this.slideBufferEnabled = false
        this.classificationBuffer = null
    }

    fun addToBuffer(doubleArray: DoubleArray) {
        if (slideBufferEnabled) {
            addToDoublesBuffer(doubleArray)
        }
        if (this.dataBufferDoubles != null) {
            this.dataBufferDoubles = Doubles.concat(this.dataBufferDoubles, doubleArray)
        } else {
            this.dataBufferDoubles = doubleArray
        }
    }

    private fun addToDoublesBuffer(dataBufferDouble: DoubleArray) {
        if (this.bufferSize > 0) {
            val newDataPoints = dataBufferDouble.size
            // Shift Data Backwards by N Amount
            System.arraycopy(this.classificationBuffer, newDataPoints, this.classificationBuffer, 0, this.bufferSize - newDataPoints)
            // Copy new data to front of data
            dataBufferDouble.copyInto(this.classificationBuffer!!, this.bufferSize - newDataPoints, 0, newDataPoints)
        }
    }

    fun resetBuffer() {
        this.dataBufferDoubles = null
    }

}