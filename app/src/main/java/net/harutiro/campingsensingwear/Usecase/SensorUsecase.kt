package net.harutiro.campingsensingwear.Usecase

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import net.harutiro.campingsensingwear.Entity.AccDataClass
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
import net.harutiro.campingsensingwear.Utils.ApiResult
import net.harutiro.campingsensingwear.Utils.DateUtils
import net.harutiro.campingsensingwear.Utils.OtherFileStorage
import net.harutiro.campingsensingwear.Utils.PostData
import net.harutiro.campingsensingwear.databinding.ActivityMainBinding

class SensorUsecase () {
    val TAG = "SensorUsecase"

    lateinit var sensorManager: SensorManager
    var accSensor: Sensor? = null
    var otherFileStorage : OtherFileStorage? = null
    val outputTextUsecase = OutputTextUsecase()
    var sensorDBUsecase :SensorDBUsecase? = null


    var date = ""

    fun init(context: Context, binding: ActivityMainBinding, sensorDBUsecase: SensorDBUsecase){
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE)!! as SensorManager
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        otherFileStorage = OtherFileStorage(context)

        outputTextUsecase.init(binding.sensorOutputText)

        date = DateUtils.getNowDate()

        this.sensorDBUsecase = sensorDBUsecase
    }

    fun start(sensorEventListener: SensorEventListener){
        sensorManager.registerListener(sensorEventListener, accSensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop(sensorEventListener: SensorEventListener){
        sensorManager.unregisterListener(sensorEventListener)
        otherFileStorage?.close(date)

        val item = SensorItemDataClass(
            id = 0,
            filePath = otherFileStorage?.finalPath.toString(),
            fileName = "${DateUtils.getNowDate()}_PixelWache.csv",
            date = DateUtils.getNowDate()
        )

        sensorDBUsecase?.insert(item)

        //ここでは直接送らないようにする
//        webDavPostUsecase.post(otherFileStorage?.finalPath.toString())

    }

    fun getValue(event: SensorEvent){
        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val data = AccDataClass(
                DateUtils.getTimeStamp(),
                event.values[0],
                event.values[1],
                event.values[2]
            )

            outputTextUsecase.write(data)

            otherFileStorage?.doLog(
                data,
                date
            )
        }
    }
}