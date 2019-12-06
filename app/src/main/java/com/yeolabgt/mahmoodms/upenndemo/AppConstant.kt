package com.yeolabgt.mahmoodms.upenndemo

import java.util.UUID

/**
 * Created by mahmoodms on 6/30/2016.
 * Constants
 */
// To prevent someone from accidentally instantiating the AppConstant class,
// give it an empty constructor.
class AppConstant {
    companion object {
        /**
         * For strain gauge:
         */
        internal val SERVICE_STRAIN_GAUGE = UUID.fromString("228ba730-35fd-875f-39fe-b2a394d28057")
        // Char:
        internal val CHAR_STRAIN_GAUGE = UUID.fromString("0000a731-0000-1000-8000-00805f9b34fb")
        /**
         * SpO2 Device
         */
        internal val SERVICE_SPO2 = UUID.fromString("228baec0-35fd-875f-39fe-b2a394d28057")
        internal val CHAR_SPO2_CH1 = UUID.fromString("0000aec1-0000-1000-8000-00805f9b34fb")
        internal val CHAR_SPO2_CH2 = UUID.fromString("0000aec2-0000-1000-8000-00805f9b34fb")
        /**
         * For EEG:
         */
        internal val SERVICE_EEG_SIGNAL = UUID.fromString("228beef0-35fd-875f-39fe-b2a394d28057")
        //Characteristic:
        internal val CHAR_EEG_CH1_SIGNAL = UUID.fromString("0000eef1-0000-1000-8000-00805f9b34fb")
        internal val CHAR_EEG_CH2_SIGNAL = UUID.fromString("0000eef2-0000-1000-8000-00805f9b34fb")
        internal val CHAR_EEG_CH3_SIGNAL = UUID.fromString("0000eef3-0000-1000-8000-00805f9b34fb")
        internal val CHAR_EEG_CH4_SIGNAL = UUID.fromString("0000eef4-0000-1000-8000-00805f9b34fb")

        /** TO READ THE BLE DEVICE'S INFORMATION  */

        // To read the device information for the device information service.
        internal var SERVICE_DEVICE_INFO = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb")
        // Characteristic UUID to read the each BLE Device's serial number.
        internal var CHAR_SERIAL_NUMBER = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb")
        // Characteristic UUID to read the BLE Device's software version.
        internal var CHAR_SOFTWARE_REV = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb")

        /** TO READ THE BLE DEVICE'S BATTERY LEVEL  */

        // To read the battery information form the Battery information service.
        internal val SERVICE_BATTERY_LEVEL = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb")
        // Characteristic to read the battery status value.
        internal val CHAR_BATTERY_LEVEL = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")

        internal val SERVICE_MPU = UUID.fromString("228ba3a0-35fd-875f-39fe-b2a394d28057");

        internal val CHAR_MPU_COMBINED = UUID.fromString("0000a3a5-0000-1000-8000-00805f9b34fb")

        /**
         * For BLE Wheelchair Control
         */
        internal val SERVICE_WHEELCHAIR_CONTROL = UUID.fromString("00009923-1212-efde-1523-785feabcd123")
        internal val CHAR_WHEELCHAIR_CONTROL = UUID.fromString("00009925-1212-efde-1523-785feabcd123")
    }

}
