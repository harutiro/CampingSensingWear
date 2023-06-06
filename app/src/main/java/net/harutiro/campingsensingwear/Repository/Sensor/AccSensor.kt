package net.harutiro.campingsensingwear.Repository.Sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
import net.harutiro.campingsensingwear.Usecase.SensorDBUsecase
import net.harutiro.campingsensingwear.Utils.DateUtils
import net.harutiro.campingsensingwear.Utils.OtherFileStorage

class AccSensor(context: Context): SensorBase(context) {
    override val sensorType = Sensor.TYPE_LINEAR_ACCELERATION
    override val sensorName = "acc"

    val TAG = "AccSensor"

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        queue.add("${DateUtils.getTimeStamp()},${x},${y},${z}")

        Log.d(TAG,"${DateUtils.getTimeStamp()},${x},${y},${z}")
    }
}