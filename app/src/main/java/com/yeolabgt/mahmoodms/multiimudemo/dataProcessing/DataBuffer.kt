package com.yeolabgt.mahmoodms.multiimudemo.dataProcessing

import com.google.common.primitives.Doubles

class DataBuffer(slideBufferSize: Int, private var saveTimeStamps: Boolean = false, samplingRate: Double = 0.0, seriesDataPoints: Int, seriesTitle: String="", color: Int):
        GraphAdapter(seriesDataPoints, seriesTitle, color) {
    // Buffer for graphing
    var dataBufferDoubles: DoubleArray? = null
    // For Timestamps:
    private val increment = 1.0 / samplingRate
    var timeStampsDoubles: DoubleArray? = null
    var systemTimeStampsDoubles: DoubleArray? = null
    private var totalDatapointIndex: Long = 0
    // Slide buffer Only
    private var slideBufferEnabled: Boolean = false
    var bufferSize: Int = 0
    var classificationBuffer: DoubleArray? = null

    init {
        if (slideBufferSize != 0) {
            enableSlideBuffer(slideBufferSize)
        } else {
            disableSlideBuffer()
        }
        // GraphAdapter Stuff:
        setPointWidth(5.toFloat())
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
        if (saveTimeStamps) {
            val timeStampsDoubleTemp = DoubleArray(doubleArray.size)
            val systemTimeStampsDoubleTemp = DoubleArray(doubleArray.size)
            for (i in doubleArray.indices) {
                timeStampsDoubleTemp[i] = (totalDatapointIndex + i.toLong()).toDouble() * increment
                systemTimeStampsDoubleTemp[i] = System.currentTimeMillis().toDouble()
            }
            totalDatapointIndex += doubleArray.size
            if (this.timeStampsDoubles != null) {
                this.timeStampsDoubles = Doubles.concat(this.timeStampsDoubles, timeStampsDoubleTemp)
            } else {
                this.timeStampsDoubles = timeStampsDoubleTemp
            }
            if (this.systemTimeStampsDoubles != null) {
                this.systemTimeStampsDoubles = Doubles.concat(this.systemTimeStampsDoubles, systemTimeStampsDoubleTemp)
            } else {
                this.systemTimeStampsDoubles = systemTimeStampsDoubleTemp
            }
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
            System.arraycopy(this.classificationBuffer!!, newDataPoints, this.classificationBuffer, 0, this.bufferSize - newDataPoints)
            // Copy new data to front of data
            dataBufferDouble.copyInto(this.classificationBuffer!!, this.bufferSize - newDataPoints, 0, newDataPoints)
        }
    }

    fun resetBuffer() {
        this.timeStampsDoubles = null
        this.systemTimeStampsDoubles = null
        this.dataBufferDoubles = null
    }

}