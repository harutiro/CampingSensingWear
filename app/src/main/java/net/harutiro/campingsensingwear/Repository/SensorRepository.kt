package net.harutiro.campingsensingwear.Repository

import android.util.Log
import net.harutiro.campingsensingwear.Repository.Sensor.SensorBase


class SensorRepository {

    val TAG = "SensorRepository"

    fun sensorStart(fileName:String,sensors: MutableList<SensorBase>) {
        for (sensor in sensors) {
            sensor.init()
            sensor.start(fileName)
            Log.d(TAG, "fileName = ${fileName}")
        }
    }

    fun sensorStop(sensors: MutableList<SensorBase>) {
        for (sensor in sensors) {
            sensor.stop()
        }
    }

}