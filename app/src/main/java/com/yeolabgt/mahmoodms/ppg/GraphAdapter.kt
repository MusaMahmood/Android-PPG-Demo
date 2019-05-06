package com.yeolabgt.mahmoodms.ppg

import android.util.Log
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.SimpleXYSeries

/**
 * Created by Musa Mahmood on 5/15/2017.
 * This file controls the AndroidPlot graph.
 */

internal class GraphAdapter
(private var seriesHistoryDataPoints: Int, XYSeriesTitle: String, useImplicitXVals: Boolean, lineAndPointFormatterColor: Int) {
    // Variables
    var series: SimpleXYSeries? = null
    var lineAndPointFormatter: LineAndPointFormatter = LineAndPointFormatter(lineAndPointFormatterColor, null, null, null)
    var sampleRate: Int = 0
    var xAxisIncrement: Double = 0.toDouble()
    var plotData: Boolean = false

    init {
        setPointWidth(5f) //Def value:
        this.series = SimpleXYSeries(XYSeriesTitle)
        if (useImplicitXVals) this.series!!.useImplicitXVals()
        this.plotData = true  // Plot by default
    }

    fun setPointWidth(width: Float) {
        this.lineAndPointFormatter.linePaint.strokeWidth = width
    }

    fun setSeriesHistoryDataPoints(seriesHistoryDataPoints: Int) {
        this.seriesHistoryDataPoints = seriesHistoryDataPoints
    }

    fun addDataPointTimeDomain(data: Double, index: Int) {
        if (this.plotData) plot(index.toDouble() * xAxisIncrement, data)
    }

    fun addDataPointTimeDomainAlt(data: Double, index: Int) {
        if (this.plotData) plotAlt(index.toDouble() * xAxisIncrement, data)
    }

    fun setxAxisIncrementFromSampleRate(sampleRate: Int) {
        this.sampleRate = sampleRate
        setxAxisIncrement(1.toDouble() / sampleRate.toDouble())
    }

    fun setxAxisIncrement(xAxisIncrement: Double) {
        this.xAxisIncrement = xAxisIncrement
    }

    //Graph Stuff:
    fun clearPlot() {
        if (this.series != null) {
            while (this.series!!.size() > 0) {
                try {
                    this.series!!.removeFirst()
                } catch (e :NoSuchElementException) {
                    Log.e(TAG, "No Such Element!!", e)
                }
            }
        }
    }

    private fun plot(x: Double, y: Double) {
        while (series!!.size() > seriesHistoryDataPoints - 1) {
            series!!.removeFirst()
        }
        series!!.addLast(x, y)
    }

    private fun plotAlt(x: Double, y: Double) {
        while (series!!.size() > seriesHistoryDataPoints - 1) {
            series!!.removeLast()
        }
        series!!.addLast(x, y)
    }

    companion object {
        private val TAG = GraphAdapter::class.java.simpleName
    }
}
