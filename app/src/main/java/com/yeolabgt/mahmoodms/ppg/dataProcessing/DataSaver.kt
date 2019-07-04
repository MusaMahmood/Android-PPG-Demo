package com.yeolabgt.mahmoodms.ppg.dataProcessing

import android.os.Environment
import android.util.Log
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

internal class DataSaver(directoryName: String, dataType: String, addressMac: String, fileTimestamp: String, samplingRate: Int, splitFilesAfter: Int = 0) {
    private var fileName: String
    var file: File? = null
    private var csvWriter: CSVWriter? = null
    private var initialized = false
    // For File Splitting
    private var splitFiles: Boolean = splitFilesAfter > 0
    private var fileNumber = 1
    private var totalLinesWritten: Long = 0
    private var totalLinesWrittenCurrentFile: Long = 0

    init {
        fileName = if (splitFiles) {
            "${dataType}_${addressMac.replace(":", "")}_${fileTimestamp}_${samplingRate}_" + "$fileNumber".padStart(3)
        } else {
            if (samplingRate != 0) {
                "${dataType}_${addressMac.replace(":", "")}_${fileTimestamp}_$samplingRate"
            } else {
                "${dataType}_${addressMac.replace(":", "")}_$fileTimestamp"
            }
        }
        createNewFile(directoryName, fileName)
    }

    private fun createNewFile(directory: String, fileName: String) {
        val root = Environment.getExternalStorageDirectory()
        if (root.canWrite()) {
            val dir = File(root.absolutePath + directory)
            val resultMkdir = dir.mkdirs()
            Log.d(TAG, "dir.mkdir = $resultMkdir")
            this.file = File(dir, "$fileName.csv")
            if (this.file!!.exists() && !this.file!!.isDirectory) {
                Log.d(TAG, "File " + this.file.toString()
                        + " already exists - appending data")
                val fileWriter = FileWriter(this.file, true)
                this.csvWriter = CSVWriter(fileWriter)
            } else {
                this.csvWriter = CSVWriter(FileWriter(this.file))
            }
        }
        this.initialized = true
    }

    fun saveDoubleArrays(vararg doubleArrays: DoubleArray) {
        try {
            exportFile(*doubleArrays)
        } catch (e: IOException) {
            Log.e("IOException", e.toString())
        }
    }

    fun saveTimestampedDoubleArray(timestamp: String, doubleArray: DoubleArray) {
        val write = arrayOfNulls<String>(doubleArray.size + 1)
        write[0] = timestamp
        for (i in doubleArray.indices) {
            write[i + 1] = doubleArray[i].toString() + ""
        }
        csvWriter!!.writeNext(write, false)
        this.totalLinesWritten++
        this.totalLinesWrittenCurrentFile++
    }

    fun saveTimestampedDoubles(timestamp: String, double: Double) {
        val write = arrayOfNulls<String>(2)
        write[0] = timestamp
        write[1] = double.toString()
        csvWriter!!.writeNext(write, false)
        this.totalLinesWritten++
        this.totalLinesWrittenCurrentFile++
    }

    @Throws(IOException::class)
    fun exportFile(vararg doubles: DoubleArray) {
        val numDp = doubles[0].size
        val columns = doubles.size
        val writeCSVValue: Array<Array<String?>>
        writeCSVValue = Array(numDp) { arrayOfNulls<String>(columns) }
        for (dp in 0 until numDp) {
            for (ch in 0 until columns) {
                writeCSVValue[dp][ch] = doubles[ch][dp].toString() + ""
            }
            csvWriter!!.writeNext(writeCSVValue[dp], false)
            this.totalLinesWritten++
            this.totalLinesWrittenCurrentFile++
            // TODO: reset after x number lines written
//            if (this.totalLinesWrittenCurrentFile > splitFilesAfter) {
//                terminateDataFileWriter()
//                // TODO: Create new filewriter
//            }
        }
    }

    @Throws(IOException::class)
    fun terminateDataFileWriter() {
        this.totalLinesWrittenCurrentFile = 0
        if (this.initialized) {
            this.csvWriter!!.flush()
            this.csvWriter!!.close()
            this.initialized = false
        }
    }

    companion object {
        private val TAG = DataSaver::class.java.simpleName
    }
}