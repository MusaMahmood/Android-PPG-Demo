package com.yeolabgt.mahmoodms.multiimudemo

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NavUtils
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton

import com.androidplot.util.Redrawer
import com.yeolabgt.mahmoodms.actblelibrary.ActBle
import com.yeolabgt.mahmoodms.multiimudemo.dataProcessing.*
import com.yeolabgt.mahmoodms.multiimudemo.dataProcessing.MotionData
import com.yeolabgt.mahmoodms.multiimudemo.dataProcessing.PPGData

import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by mahmoodms on 5/31/2016.
 * Android Activity for Controlling Bluetooth LE Device Connectivity
 */

class DeviceControlActivity : Activity(), ActBle.ActBleListener {
    // Graphing Variables:
    private var mXYPlotInitializedBoolean = false
    private lateinit var mAccelPlotArray: Array<XYPlotAdapter?>
    private lateinit var mGyroPlotArray: Array<XYPlotAdapter?>
    private var mExGPlot: XYPlotAdapter? = null
    private var mAccelPlotCh1: XYPlotAdapter? = null
    private var mAccelPlotCh2: XYPlotAdapter? = null
    private var mAccelPlotCh3: XYPlotAdapter? = null
    private var mAccelPlotCh4: XYPlotAdapter? = null
    private var mGyroPlotCh1: XYPlotAdapter? = null
    private var mGyroPlotCh2: XYPlotAdapter? = null
    private var mGyroPlotCh3: XYPlotAdapter? = null
    private var mGyroPlotCh4: XYPlotAdapter? = null
    //Device Information
    private var mBleInitializedBoolean = false
    private lateinit var mBluetoothGattArray: Array<BluetoothGatt?>
    private lateinit var mSamplingRateArray: Array<Double?>
    private var mAddressToSamplingRateMap: Map<String, Double?>? = null
    private var mActBle: ActBle? = null
    private var mDeviceName: String? = null
    private var mDeviceAddress: String? = null
    private var mConnected: Boolean = false
    //Connecting to Multiple Devices
    private var deviceMacAddresses: Array<String>? = null
    private var mNumberOfDevices = 0
    //UI Elements - TextViews, Buttons, etc
    private lateinit var mBatteryLevelTextViews: Array<TextView?>
    private var mDataRate: TextView? = null
    private var mChannelSelect: ToggleButton? = null
    private var menu: Menu? = null
    //Data throughput counter
    private var mLastTime: Long = 0
    private var points = 0
    private val mTimerHandler = Handler()
    private var mTimerEnabled = false
    //Data Variables:
    private val batteryWarning = 20
    private var dataRate: Double = 0.toDouble()
    // ArrayList<DataType>
    private var mSpO2ArrayList = ArrayList<SpO2Data>()
    private var mExGArrayList = ArrayList<ExGData>()
    private var mPPGArrayList = ArrayList<PPGData>()
    private var mICMArrayList = ArrayList<MotionData>()
    //
    private var emgPresentFlag = false

    private val mTimeStamp: String
        get() = SimpleDateFormat("yyyy.MM.dd_HH.mm.ss", Locale.US).format(Date())

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Receive Intents:
        val intent = intent
        deviceMacAddresses = intent.getStringArrayExtra(MainActivity.INTENT_DEVICES_KEY)
        mNumberOfDevices = deviceMacAddresses!!.size
        //Set orientation of device based on screen type/size:
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val deviceDisplayNames = intent.getStringArrayExtra(MainActivity.INTENT_DEVICES_NAMES)
        // Set content view based on number of devices connected:
        for (deviceName in deviceDisplayNames) {
            if (deviceName.toLowerCase(Locale.ROOT).contains("emg") ||
                    deviceName.toLowerCase(Locale.ROOT).contains("ecg")) {
                // Contains EMG Data:
                emgPresentFlag = true
                break
            }
        }
        when (mNumberOfDevices) {
            1 -> setContentView(R.layout.activity_device_control_1)
            2 -> setContentView(R.layout.activity_device_control_2)
            else -> setContentView(R.layout.activity_device_control_multi)
        }
        mDeviceName = deviceDisplayNames[0]
        mDeviceAddress = deviceMacAddresses!![0]
        Log.d(TAG, "Device Names: " + Arrays.toString(deviceDisplayNames))
        Log.d(TAG, "Device MAC Addresses: " + Arrays.toString(deviceMacAddresses))
        Log.d(TAG, Arrays.toString(deviceMacAddresses))
        //Set up action bar:
        if (actionBar != null) {
            actionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        val actionBar = actionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#6078ef")))
        //Flag to keep screen on (stay-awake):
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //Set up TextViews
        val mExportButton = findViewById<Button>(R.id.button_export)
        mDataRate = findViewById(R.id.dataRate)
        mDataRate!!.text = "..."
        val ab = getActionBar()
        ab!!.title = mDeviceName
        ab.subtitle = mDeviceAddress
        //Initialize Bluetooth
        if (!mBleInitializedBoolean) initializeBluetoothArray()
        mLastTime = System.currentTimeMillis()
        //UI Listeners
        mChannelSelect = findViewById(R.id.toggleButtonGraph)
        mChannelSelect!!.setOnCheckedChangeListener { _, b ->
            for (exgData in mExGArrayList) {
                exgData.dataBuffer.clearPlot()
                exgData.dataBuffer.plotData = b
            }
            for (ppgData in mPPGArrayList) {
                ppgData.dataBuffer.clearPlot()
                ppgData.dataBuffer.plotData = b
            }
            for (motionData in mICMArrayList) {
                motionData.dataBufferAccX.clearPlot()
                motionData.dataBufferAccX.plotData = b
                motionData.dataBufferAccY.clearPlot()
                motionData.dataBufferAccY.plotData = b
                motionData.dataBufferAccZ.clearPlot()
                motionData.dataBufferAccZ.plotData = b
                motionData.dataBufferGyrX.clearPlot()
                motionData.dataBufferGyrX.plotData = b
                motionData.dataBufferGyrY.clearPlot()
                motionData.dataBufferGyrY.plotData = b
                motionData.dataBufferGyrZ.clearPlot()
                motionData.dataBufferGyrZ.plotData = b
            }
            for (spo2data in mSpO2ArrayList) {
                spo2data.dataBuffer.clearPlot()
                spo2data.dataBuffer.plotData = b
            }
            if (b) mRedrawer!!.pause()
            else mRedrawer!!.start()
        }
        mExportButton.setOnClickListener { exportData() }
    }

    private fun exportData() {
        try {
            terminateDataFileWriter()
        } catch (e: IOException) {
            Log.e(TAG, "IOException in saveDataFile")
            e.printStackTrace()
        }
        val files = ArrayList<Uri>()
        val context = applicationContext
        for (ppg in mPPGArrayList) {
            val uii = FileProvider.getUriForFile(context, context.packageName + ".provider", ppg.dataSaver?.file!!)
            files.add(uii)
        }
        for (icm in mICMArrayList) {
            val uii = FileProvider.getUriForFile(context, context.packageName + ".provider", icm.dataSaver?.file!!)
            files.add(uii)
        }
        for (exg in mExGArrayList) {
            val uii = FileProvider.getUriForFile(context, context.packageName + ".provider", exg.dataSaver?.file!!)
            files.add(uii)
        }
        for (spo2 in mSpO2ArrayList) {
            val uii = FileProvider.getUriForFile(context, context.packageName + ".provider", spo2.dataSaver?.file!!)
            files.add(uii)
        }
        val exportData = Intent(Intent.ACTION_SEND_MULTIPLE)
        exportData.putExtra(Intent.EXTRA_SUBJECT, "Sensor Data Export Details")
        exportData.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
        exportData.type = "text/html"
        startActivity(exportData)
    }

    @Throws(IOException::class)
    private fun terminateDataFileWriter() {
        //Terminate all files
        for (exgData in mExGArrayList) {
            exgData.dataSaver?.terminateDataFileWriter()
        }
        for (ppgData in mPPGArrayList) {
            ppgData.dataSaver?.terminateDataFileWriter()
        }
        for (motionData in mICMArrayList) {
            motionData.dataSaver?.terminateDataFileWriter()
        }
        for (spo2data in mSpO2ArrayList) {
            spo2data.dataSaver?.terminateDataFileWriter()
        }
    }

    public override fun onResume() {
        jmainInitialization(true)
        if (mRedrawer != null) {
            mRedrawer!!.start()
        }
        super.onResume()
    }

    override fun onPause() {
        if (mRedrawer != null) mRedrawer!!.pause()
        super.onPause()
    }

    private fun initializeBluetoothArray() {
        val mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val mBluetoothDeviceArray = arrayOfNulls<BluetoothDevice>(deviceMacAddresses!!.size)
        Log.d(TAG, "Device Addresses: " + Arrays.toString(deviceMacAddresses))
        if (deviceMacAddresses != null) {
            for (i in deviceMacAddresses!!.indices) {
                mBluetoothDeviceArray[i] = mBluetoothManager.adapter.getRemoteDevice(deviceMacAddresses!![i])
            }
        } else {
            Log.e(TAG, "No Devices Queued, Restart!")
            Toast.makeText(this, "No Devices Queued, Restart!", Toast.LENGTH_SHORT).show()
        }
        mActBle = ActBle(this, mBluetoothManager, this)
        mBluetoothGattArray = Array(deviceMacAddresses!!.size) { i -> mActBle!!.connect(mBluetoothDeviceArray[i]) }
        mSamplingRateArray = Array(deviceMacAddresses!!.size) {0.0}
//        val mMap  = mapOf(mBluetoothGattArray to mSamplingRateArray)
//        val mMap = Map<> = mBluetoothGattArray.zip(mSamplingRateArray)
        for (i in mBluetoothDeviceArray.indices) {
            Log.e(TAG, "Connecting to Device: " + (mBluetoothDeviceArray[i]!!.name + " " + mBluetoothDeviceArray[i]!!.address))
            val str = mBluetoothDeviceArray[i]!!.name.toLowerCase(Locale.ROOT)
            if (str.contains("icm8")) { // timer is set to
                mSamplingRateArray[i] = 125.0
            } else if (str.contains("icm4")) {
                mSamplingRateArray[i] = 250.0
            } else {
                mSamplingRateArray[i] = 250.0
            }
            Log.e(TAG, "Device: ${mBluetoothDeviceArray[i]!!.name}, Sampling: ${mSamplingRateArray[i]}")
            mAddressToSamplingRateMap = deviceMacAddresses!!.zip(mSamplingRateArray).toMap()
        }
        if (!mXYPlotInitializedBoolean) setupXYPlot()
        mBleInitializedBoolean = true
    }

    private fun setupXYPlot() {
        // Initialize our XYPlot reference:
        mAccelPlotCh1 = XYPlotAdapter(findViewById(R.id.accelPlot1), "Time (s)", "Acc (g)", 4)
        mGyroPlotCh1 = XYPlotAdapter(findViewById(R.id.gyroPlot1), "Time (s)", "Ang. Velocity (°/s)", 4)
        if (mNumberOfDevices > 1) {
            mAccelPlotCh2 = XYPlotAdapter(findViewById(R.id.accelPlot2), "Time (s)", "Acc (g)", 4)
            mGyroPlotCh2 = XYPlotAdapter(findViewById(R.id.gyroPlot2), "Time (s)", "Ang. Velocity (°/s)", 4)
        }
        if (mNumberOfDevices > 2) {
            mAccelPlotCh3 = XYPlotAdapter(findViewById(R.id.accelPlot3), "Time (s)", "Acc (g)", 4)
            mGyroPlotCh3 = XYPlotAdapter(findViewById(R.id.gyroPlot3), "Time (s)", "Ang. Velocity (°/s)", 4)
            mAccelPlotCh4 = XYPlotAdapter(findViewById(R.id.accelPlot4), "Time (s)", "Acc (g)", 4)
            mGyroPlotCh4 = XYPlotAdapter(findViewById(R.id.gyroPlot4), "Time (s)", "Ang. Velocity (°/s)", 4)
        }
        mBatteryLevelTextViews = arrayOf(findViewById(R.id.battery1), findViewById(R.id.battery2), findViewById(R.id.battery3), findViewById(R.id.battery4))
        mBatteryLevelTextViews = mBatteryLevelTextViews.filterNotNull().toTypedArray()
        mAccelPlotArray = arrayOf(mAccelPlotCh1, mAccelPlotCh2, mAccelPlotCh3, mAccelPlotCh4)
        mGyroPlotArray = arrayOf(mGyroPlotCh1, mGyroPlotCh2, mGyroPlotCh3, mGyroPlotCh4)
        val xyPlotList = listOf(mAccelPlotCh1?.xyPlot, mAccelPlotCh2?.xyPlot, mGyroPlotCh1?.xyPlot, mGyroPlotCh2?.xyPlot)
        mRedrawer = Redrawer(xyPlotList.filterNotNull(), 24f, false)
        mRedrawer!!.start()
        mXYPlotInitializedBoolean = true
    }

    private fun addGraphToPlot(graphAdapter: GraphAdapter, plotAdapter: XYPlotAdapter) {
        plotAdapter.xyPlot!!.addSeries(graphAdapter.series, graphAdapter.lineAndPointFormatter)
    }

    private fun setNameAddress(name_action: String?, address_action: String?) {
        val name = menu!!.findItem(R.id.action_title)
        val address = menu!!.findItem(R.id.action_address)
        name.title = name_action
        address.title = address_action
        invalidateOptionsMenu()
    }

    override fun onDestroy() {
        mRedrawer?.finish()
        disconnectAllBLE()
        try {
            terminateDataFileWriter()
        } catch (e: IOException) {
            Log.e(TAG, "IOException in saveDataFile")
            e.printStackTrace()
        }

        stopMonitoringRssiValue()
        jmainInitialization(false) //Just a technicality, doesn't actually do anything
        super.onDestroy()
    }

    private fun disconnectAllBLE() {
        if (mActBle != null) {
            for (bluetoothGatt in mBluetoothGattArray) {
                mActBle!!.disconnect(bluetoothGatt!!)
                mConnected = false
                resetMenuBar()
            }
        }
    }

    private fun resetMenuBar() {
        runOnUiThread {
            if (menu != null) {
                menu!!.findItem(R.id.menu_connect).isVisible = true
                menu!!.findItem(R.id.menu_disconnect).isVisible = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_device_control, menu)
        menuInflater.inflate(R.menu.actionbar_item, menu)
        if (mConnected) {
            menu.findItem(R.id.menu_connect).isVisible = false
            menu.findItem(R.id.menu_disconnect).isVisible = true
        } else {
            menu.findItem(R.id.menu_connect).isVisible = true
            menu.findItem(R.id.menu_disconnect).isVisible = false
        }
        this.menu = menu
        setNameAddress(mDeviceName, mDeviceAddress)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_connect -> {
                if (mActBle != null) {
                    initializeBluetoothArray()
                }
                connect()
                return true
            }
            R.id.menu_disconnect -> {
                if (mActBle != null) {
                    disconnectAllBLE()
                }
                return true
            }
            android.R.id.home -> {
                if (mActBle != null) {
                    disconnectAllBLE()
                }
                NavUtils.navigateUpFromSameTask(this)
                onBackPressed()
                return true
            }
            R.id.action_settings -> {
                launchSettingsMenu()
                return true
            }
            R.id.action_export -> {
                exportData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchSettingsMenu() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivityForResult(intent, 1)
    }

    private fun connect() {
        runOnUiThread {
            val menuItem = menu!!.findItem(R.id.action_status)
            menuItem.title = "Connecting..."
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        Log.i(TAG, "onServicesDiscovered")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            for (service in gatt.services) {
                if (service == null || service.uuid == null) {
                    continue
                }
                if (AppConstant.SERVICE_DEVICE_INFO == service.uuid) {
                    //Read the device serial number (if available)
                    if (service.getCharacteristic(AppConstant.CHAR_SERIAL_NUMBER) != null) {
                        mActBle!!.readCharacteristic(gatt, service.getCharacteristic(AppConstant.CHAR_SERIAL_NUMBER))
                    }
                    //Read the device software version (if available)
                    if (service.getCharacteristic(AppConstant.CHAR_SOFTWARE_REV) != null) {
                        mActBle!!.readCharacteristic(gatt, service.getCharacteristic(AppConstant.CHAR_SOFTWARE_REV))
                    }
                }

                if (AppConstant.SERVICE_EEG_SIGNAL == service.uuid) {
                    if (service.getCharacteristic(AppConstant.CHAR_EEG_CH1_SIGNAL) != null) {
                        mActBle!!.setCharacteristicNotifications(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH1_SIGNAL), true)
                        mExGArrayList.add(ExGData(0, gatt.device.address, AppConstant.CHAR_EEG_CH1_SIGNAL, mTimeStamp))
                        addGraphToPlot(mExGArrayList[mExGArrayList.lastIndex].dataBuffer, mExGPlot!!)
                    }
                    if (service.getCharacteristic(AppConstant.CHAR_EEG_CH2_SIGNAL) != null) {
                        mActBle!!.setCharacteristicNotifications(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH2_SIGNAL), true)
                    }
                    if (service.getCharacteristic(AppConstant.CHAR_EEG_CH3_SIGNAL) != null) {
                        mActBle!!.setCharacteristicNotifications(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH3_SIGNAL), true)
                    }
                    if (service.getCharacteristic(AppConstant.CHAR_EEG_CH4_SIGNAL) != null) {
                        mActBle!!.setCharacteristicNotifications(gatt, service.getCharacteristic(AppConstant.CHAR_EEG_CH4_SIGNAL), true)
                    }
                }

                if (AppConstant.SERVICE_STRAIN_GAUGE == service.uuid) {
                    if (service.getCharacteristic(AppConstant.CHAR_STRAIN_GAUGE) != null) {
                        mActBle!!.setCharacteristicNotifications(gatt, service.getCharacteristic(AppConstant.CHAR_STRAIN_GAUGE), true)
                        mPPGArrayList.add(PPGData(0, gatt.device.address, AppConstant.CHAR_STRAIN_GAUGE, mTimeStamp))
                    }
                }

                if (AppConstant.SERVICE_BATTERY_LEVEL == service.uuid) { //Read the device battery percentage
                    mActBle!!.readCharacteristic(gatt, service.getCharacteristic(AppConstant.CHAR_BATTERY_LEVEL))
                    mActBle!!.setCharacteristicNotifications(gatt, service.getCharacteristic(AppConstant.CHAR_BATTERY_LEVEL), true)
                }

                if (AppConstant.SERVICE_MPU == service.uuid) {
                    mActBle!!.setCharacteristicNotifications(gatt, service.getCharacteristic(AppConstant.CHAR_MPU_COMBINED), true)
                    //Add to arrayList of devices/types
                    mICMArrayList.add(MotionData(0, gatt.device.address, AppConstant.CHAR_MPU_COMBINED, mTimeStamp, mAddressToSamplingRateMap!![gatt.device.address] ?: error("Value Null!")))
                    // TODO: TEMP
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Adding Device: ${gatt.device.address}, with Sampling Rate: ${mAddressToSamplingRateMap!![gatt.device.address] ?: error("Value Null!")}", Toast.LENGTH_LONG).show()
                    }
                    for (i in mICMArrayList.indices) {
                        // Add ICM→GraphAdapters to corresponding plots.
                        if (gatt.device.address == deviceMacAddresses!![i]) {
                            addGraphToPlot(mICMArrayList[mICMArrayList.lastIndex].dataBufferAccX, mAccelPlotArray[i]!!)
                            addGraphToPlot(mICMArrayList[mICMArrayList.lastIndex].dataBufferAccY, mAccelPlotArray[i]!!)
                            addGraphToPlot(mICMArrayList[mICMArrayList.lastIndex].dataBufferAccZ, mAccelPlotArray[i]!!)
                            addGraphToPlot(mICMArrayList[mICMArrayList.lastIndex].dataBufferGyrX, mGyroPlotArray[i]!!)
                            addGraphToPlot(mICMArrayList[mICMArrayList.lastIndex].dataBufferGyrY, mGyroPlotArray[i]!!)
                            addGraphToPlot(mICMArrayList[mICMArrayList.lastIndex].dataBufferGyrZ, mGyroPlotArray[i]!!)
                        }
                    }
                }
            }
            //Run process only once:
            mActBle?.runProcess()
        }
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
        Log.i(TAG, "onCharacteristicRead")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (AppConstant.CHAR_BATTERY_LEVEL == characteristic.uuid) {
                if (characteristic.value != null) {
                    val batteryLevel = PPGData.bytesToDouble14bit(characteristic.value[0], characteristic.value[1])
                    updateBatteryStatus(batteryLevel, gatt.device.address)
                    Log.i(TAG, "Battery Level :: $batteryLevel")
                }
            }
        } else {
            Log.e(TAG, "onCharacteristic Read Error$status")
        }
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        if (AppConstant.CHAR_BATTERY_LEVEL == characteristic.uuid) {
            val batteryLevel = PPGData.bytesToDouble14bit(characteristic.value[0], characteristic.value[1])
            updateBatteryStatus(batteryLevel, gatt.device.address)
        }

        if (AppConstant.CHAR_EEG_CH1_SIGNAL == characteristic.uuid) {
            val data = characteristic.value
            getDataRateBytes(data.size)
            for (i in mExGArrayList.indices) {
                if (mExGArrayList[i].mAddress == gatt.device.address) {
                    if (checkSyncAll(mExGArrayList, mICMArrayList)) {
                        mExGArrayList[mExGArrayList.lastIndex].handleNewData(data)
                        if (mExGArrayList[mExGArrayList.lastIndex].packetGraphingCounter.toInt() == 4) {
                            addToGraphBuffer(mExGArrayList[mExGArrayList.lastIndex].dataBuffer, mExGArrayList[mExGArrayList.lastIndex].dataBuffer.timeStampsDoubles!!)
                            mExGArrayList[mExGArrayList.lastIndex].saveAndResetBuffers()
                        }
                        return
                    } else {
                        mExGArrayList[i].sync = true
                    }
                }
            }
        }

        if (AppConstant.CHAR_STRAIN_GAUGE == characteristic.uuid) {
            val data = characteristic.value
            getDataRateBytes(data.size)
            for (i in deviceMacAddresses!!.indices) {
                if (deviceMacAddresses!![i] == gatt.device.address) {
                    mPPGArrayList[i].handleNewData(data)
                    if (mPPGArrayList[i].packetGraphingCounter.toInt() == 1) {
                        addToGraphBuffer(mPPGArrayList[i].dataBuffer, mPPGArrayList[i].dataBuffer.timeStampsDoubles!!)
                        mPPGArrayList[i].saveAndResetBuffers()
                        return // we can return here because there's nothing left to do with this char
                    }
                }
            }
        }

        if (AppConstant.CHAR_MPU_COMBINED == characteristic.uuid) {
            val dataPacket = characteristic.value
            getDataRateBytes(dataPacket.size) //+=240
            for (i in mICMArrayList.indices) {
                if (mICMArrayList[i].mAddress == gatt.device.address) {
                    if (checkSyncAll(mExGArrayList, mICMArrayList)) {
                    mICMArrayList[i].handleNewData(dataPacket)
                        if (mICMArrayList[i].packetGraphingCounter.toInt() == 4) {
                            addToGraphBuffer(mICMArrayList[i].dataBufferAccX, mICMArrayList[i].dataBufferAccX.timeStampsDoubles!!, downsample = true)
                            addToGraphBuffer(mICMArrayList[i].dataBufferAccY, mICMArrayList[i].dataBufferAccX.timeStampsDoubles!!, downsample = true)
                            addToGraphBuffer(mICMArrayList[i].dataBufferAccZ, mICMArrayList[i].dataBufferAccX.timeStampsDoubles!!, downsample = true)
                            addToGraphBuffer(mICMArrayList[i].dataBufferGyrX, mICMArrayList[i].dataBufferAccX.timeStampsDoubles!!, downsample = true)
                            addToGraphBuffer(mICMArrayList[i].dataBufferGyrY, mICMArrayList[i].dataBufferAccX.timeStampsDoubles!!, downsample = true)
                            addToGraphBuffer(mICMArrayList[i].dataBufferGyrZ, mICMArrayList[i].dataBufferAccX.timeStampsDoubles!!, downsample = true)
                            mICMArrayList[i].saveAndResetBuffers()
                        }
                    } else {
                        mICMArrayList[i].sync = true
                    }
                }
            }
        }

        if (AppConstant.CHAR_SPO2_CH1 == characteristic.uuid) {
            val dataPacket = characteristic.value
            getDataRateBytes(dataPacket.size)
            for (i in mSpO2ArrayList.indices) {
                if (mSpO2ArrayList[i].mUUID == characteristic.uuid) {
                    mSpO2ArrayList[i].handleNewData(dataPacket)
                    addToGraphBuffer(mSpO2ArrayList[i].dataBuffer, mSpO2ArrayList[i].dataBuffer.timeStampsDoubles!!)
                    mSpO2ArrayList[i].saveAndResetBuffers()
                }
            }
        }
        if (AppConstant.CHAR_SPO2_CH2 == characteristic.uuid) {
            val dataPacket = characteristic.value
            getDataRateBytes(dataPacket.size)
            for (i in mSpO2ArrayList.indices) {
                if (mSpO2ArrayList[i].mUUID == characteristic.uuid) {
                    mSpO2ArrayList[i].handleNewData(dataPacket)
                    addToGraphBuffer(mSpO2ArrayList[i].dataBuffer, mSpO2ArrayList[i].dataBuffer.timeStampsDoubles!!)
                    mSpO2ArrayList[i].saveAndResetBuffers()
                }
            }
        }
    }

    private fun checkSyncAll(exgDataArrayList: ArrayList<ExGData>, motionDataArrayList: ArrayList<MotionData>): Boolean {
        var sum = 0
        for (i in motionDataArrayList.indices) {
            sum += if (motionDataArrayList[i].sync) 1 else 0
        }
        for (i in exgDataArrayList.indices) {
            sum += if (exgDataArrayList[i].sync) 1 else 0
        }
        return sum == (motionDataArrayList.size + exgDataArrayList.size)
    }

    private fun addToGraphBuffer(dataBuffer: DataBuffer, dataXValues: DoubleArray, downsample: Boolean = false) {
        if (!downsample) {
            for (i in dataBuffer.dataBufferDoubles!!.indices) {
                dataBuffer.addDataPointTimeDomain(dataXValues[i], dataBuffer.dataBufferDoubles!![i])
            }
        } else {
            for (i in 0 until dataBuffer.dataBufferDoubles!!.size/4) {
                dataBuffer.addDataPointTimeDomain(dataXValues[i*4], dataBuffer.dataBufferDoubles!![i*4])
            }
        }
    }

    private fun getDataRateBytes(bytes: Int) {
        val mCurrentTime = System.currentTimeMillis()
        points += bytes
        if (mCurrentTime > mLastTime + 5000) {
            dataRate = (points / 5).toDouble()
            points = 0
            mLastTime = mCurrentTime
            Log.e(" DataRate:", "$dataRate Bytes/s")
            runOnUiThread {
                val s = "$dataRate Bytes/s"
                mDataRate!!.text = s
            }
        }
    }

    override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
        uiRssiUpdate(rssi)
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {
                mConnected = true
                runOnUiThread {
                    if (menu != null) {
                        menu!!.findItem(R.id.menu_connect).isVisible = false
                        menu!!.findItem(R.id.menu_disconnect).isVisible = true
                    }
                }
                Log.i(TAG, "Connected")
                updateConnectionState(getString(R.string.connected))
                invalidateOptionsMenu()
                runOnUiThread {
                    mDataRate!!.setTextColor(Color.BLACK)
                    mDataRate!!.setTypeface(null, Typeface.NORMAL)
                }
                //Start the service discovery:
                gatt.discoverServices()
                startMonitoringRssiValue()
            }
            BluetoothProfile.STATE_CONNECTING -> {
            }
            BluetoothProfile.STATE_DISCONNECTING -> {
            }
            BluetoothProfile.STATE_DISCONNECTED -> {
                mConnected = false
                runOnUiThread {
                    if (menu != null) {
                        menu!!.findItem(R.id.menu_connect).isVisible = true
                        menu!!.findItem(R.id.menu_disconnect).isVisible = false
                    }
                }
                Log.i(TAG, "Disconnected")
                runOnUiThread {
                    mDataRate!!.setTextColor(Color.RED)
                    mDataRate!!.setTypeface(null, Typeface.BOLD)
                    mDataRate!!.text = HZ
                }
                updateConnectionState(getString(R.string.disconnected))
                stopMonitoringRssiValue()
                invalidateOptionsMenu()
            }
            else -> {
            }
        }
    }

    private fun startMonitoringRssiValue() {
        readPeriodicallyRssiValue(true)
    }

    private fun stopMonitoringRssiValue() {
        readPeriodicallyRssiValue(false)
    }

    private fun readPeriodicallyRssiValue(repeat: Boolean) {
        mTimerEnabled = repeat
        // check if we should stop checking RSSI value
        if (!mConnected || !mTimerEnabled) {
            mTimerEnabled = false
            return
        }

        mTimerHandler.postDelayed(Runnable {
            if (!mConnected) {
                mTimerEnabled = false
                return@Runnable
            }
            // request RSSI value
            mBluetoothGattArray[0]!!.readRemoteRssi()
            // add call it once more in the future
            readPeriodicallyRssiValue(mTimerEnabled)
        }, RSSI_UPDATE_TIME_INTERVAL.toLong())
    }

    override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
        Log.i(TAG, "onCharacteristicWrite :: Status:: $status")
    }

    override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {}

    override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
        Log.i(TAG, "onDescriptorRead :: Status:: $status")
    }

    override fun onError(errorMessage: String) {
        Log.e(TAG, "Error:: $errorMessage")
    }

    private fun updateConnectionState(status: String) {
        runOnUiThread {
            if (status == getString(R.string.connected)) {
                Toast.makeText(applicationContext, "Device Connected!", Toast.LENGTH_SHORT).show()
            } else if (status == getString(R.string.disconnected)) {
                Toast.makeText(applicationContext, "Device Disconnected!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateBatteryStatus(voltage: Double, address: String) {
        val status: String = address
        val finalVoltage = voltage * 2.0 + 0.5 // Double voltage due to voltage divider in circuit
        val finalPercent: Double = when {
            finalVoltage >= 4.0 -> 100.0
            finalVoltage < 4.0 && finalVoltage >= 3.6 -> ((finalVoltage - 3.6) / 0.4) * 99 + 1
            else -> 1.0 // <3.6V
        }
        Log.e(TAG, "Device $address, BattVoltage: " + String.format(Locale.US, "%.3f", finalVoltage) + "V : " + String.format(Locale.US, "%.3f", finalPercent) + "%")
        for (i in 0 until deviceMacAddresses?.size!!) {
            if (address == deviceMacAddresses!![i]) {
                runOnUiThread {
                    if (finalPercent <= batteryWarning) {
                        mBatteryLevelTextViews[i]!!.setTextColor(Color.RED)
                        mBatteryLevelTextViews[i]!!.setTypeface(null, Typeface.BOLD)
//                        Toast.makeText(applicationContext, "Charge Battery, Battery Low $status", Toast.LENGTH_SHORT).show()
                    } else {
                        mBatteryLevelTextViews[i]!!.setTextColor(Color.GREEN)
                        mBatteryLevelTextViews[i]!!.setTypeface(null, Typeface.BOLD)
                    }
                    mBatteryLevelTextViews[i]!!.text = status
                }
                break
            }
        }
    }

    private fun uiRssiUpdate(rssi: Int) {
        runOnUiThread {
            val menuItem = menu!!.findItem(R.id.action_rssi)
            val statusActionItem = menu!!.findItem(R.id.action_status)
            val valueOfRSSI = "$rssi dB"
            menuItem.title = valueOfRSSI
            if (mConnected) {
                val newStatus = "Status: " + getString(R.string.connected)
                statusActionItem.title = newStatus
            } else {
                val newStatus = "Status: " + getString(R.string.disconnected)
                statusActionItem.title = newStatus
            }
        }
    }

    private external fun jmainInitialization(initialize: Boolean): Int

    companion object {
        //RSSI:
        private const val RSSI_UPDATE_TIME_INTERVAL = 2000
        const val HZ = "0 Hz"
        private val TAG = DeviceControlActivity::class.java.simpleName
        var mRedrawer: Redrawer? = null

        init {
            System.loadLibrary("ecg-lib")
        }
    }
}