package net.harutiro.campingsensingwear.Repository.Sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.util.Log
import net.harutiro.campingsensingwear.Utils.DateUtils

class HartRateSensor(context: Context) : SensorBase(context) {
    override val sensorType = Sensor.TYPE_HEART_RATE
    override val sensorName = "HartRate"

    val myTAG = "HartRateSensor"

    override fun onSensorChanged(event: SensorEvent) {
        val hartRate = event.values[0]

        val time = DateUtils.getTimeStamp()
        val data = "${time},${hartRate}"

        addQueue(
            sensorName = sensorName,
            data = data,
            timeStamp = time
        )
    }
}