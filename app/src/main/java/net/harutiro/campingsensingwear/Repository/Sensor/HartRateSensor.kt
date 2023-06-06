package net.harutiro.campingsensingwear.Repository.Sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.util.Log
import net.harutiro.campingsensingwear.Utils.DateUtils

class HartRateSensor(context: Context) : SensorBase(context) {
    override val sensorType = Sensor.TYPE_HEART_RATE
    override val sensorName = "HartRate"

    val TAG = "HartRateSensor"

    override fun onSensorChanged(event: SensorEvent) {
        val hartRate = event.values[0]
        super.queue.add(DateUtils.getTimeStamp().toString().plus(",").plus(hartRate))
        Log.d(TAG,"hartRate:$hartRate")
    }
}