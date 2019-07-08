package com.yeolabgt.mahmoodms.upenndemo

import android.view.View

import com.androidplot.xy.BoundaryMode
import com.androidplot.xy.XYGraphWidget
import com.androidplot.xy.XYPlot
import com.androidplot.xy.StepMode

import java.text.DecimalFormat

/**
 * Created by mahmoodms on 5/15/2017.
 *
 */

internal class XYPlotAdapter {

    //    private final static String TAG = XYPlotAdapter.class.getSimpleName();
    var xyPlot: XYPlot? = null

    /**
     * This function implies that plotImplicitXVals is false. Therefore domain parameters need to be specified:
     * @param findViewByID R.id in /res/
     * @param domainLabel x-axis label
     * @param rangeLabel y-axis label
     * @param domainIncrement x-axis increment
     */
    constructor(findViewByID: View, domainLabel: String, rangeLabel: String, domainIncrement: Double) {
        this.xyPlot = findViewByID as XYPlot
        this.xyPlot!!.setDomainBoundaries(0, 1, BoundaryMode.AUTO) //Default
        this.xyPlot!!.domainStepMode = StepMode.INCREMENT_BY_VAL
        this.xyPlot!!.domainStepValue = domainIncrement
        //Default Config:
        this.xyPlot!!.rangeStepMode = StepMode.INCREMENT_BY_VAL
        this.xyPlot!!.setDomainLabel(domainLabel)
        this.xyPlot!!.setRangeLabel(rangeLabel)
        this.xyPlot!!.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = DecimalFormat("#.###")
        this.xyPlot!!.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("#")
        this.xyPlot!!.setRangeBoundaries(-0.004, 0.004, BoundaryMode.AUTO)
        this.xyPlot!!.setRangeStep(StepMode.SUBDIVIDE, 5.0)
    }

    constructor(findViewByID: View, plotImplicitXVals: Boolean, historySize: Int, sampleRate: Int=250, domainLabel: String="Time (s)", rangeLabel: String="Voltage (V)") {
        this.xyPlot = findViewByID as XYPlot
        val historySeconds = historySize / sampleRate
        if (plotImplicitXVals) {
            this.xyPlot!!.setDomainBoundaries(0, historySize, BoundaryMode.FIXED)
            this.xyPlot!!.domainStepMode = StepMode.INCREMENT_BY_VAL
            this.xyPlot!!.domainStepValue = (historySize / 5).toDouble()
        } else {
            this.xyPlot!!.setDomainBoundaries(0, historySeconds, BoundaryMode.AUTO)
            this.xyPlot!!.domainStepMode = StepMode.INCREMENT_BY_VAL
            this.xyPlot!!.domainStepValue = (historySeconds / 4).toDouble()
        }
        //Default Config:
        this.xyPlot!!.rangeStepMode = StepMode.INCREMENT_BY_VAL
        this.xyPlot!!.setDomainLabel(domainLabel)
        this.xyPlot!!.setRangeLabel(rangeLabel)
        this.xyPlot!!.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = DecimalFormat("#.###")
        this.xyPlot!!.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("#")
        this.xyPlot!!.setRangeBoundaries(-0.004, 0.004, BoundaryMode.AUTO)
        this.xyPlot!!.setRangeStep(StepMode.SUBDIVIDE, 5.0)
    }

    fun setXyPlotDomainIncrement(domainIncrement: Double) {
        this.xyPlot!!.domainStepValue = domainIncrement
    }

    fun setXyPlotVisibility(visible: Boolean) {
        this.xyPlot!!.visibility = if (visible) View.VISIBLE else View.GONE
    }
}