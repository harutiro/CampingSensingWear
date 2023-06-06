package net.harutiro.campingsensingwear.Repository.Sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
import net.harutiro.campingsensingwear.Usecase.SensorDBUsecase
import net.harutiro.campingsensingwear.Utils.DateUtils
import net.harutiro.campingsensingwear.Utils.OtherFileStorage

abstract class SensorBase(val context: Context): SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var PreSensor: Sensor? = null
    val queue: ArrayDeque<String> = ArrayDeque(listOf())
    var otherFileStorage: OtherFileStorage? = null
    var sensorDBUsecase : SensorDBUsecase? = null

    abstract val sensorType:Int
    abstract val sensorName:String

    fun init() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        PreSensor = sensorManager.getDefaultSensor(sensorType)

        this.sensorDBUsecase = SensorDBUsecase()
        sensorDBUsecase?.init(context = context)
    }

    fun start(filename: String) {
        queue.clear()
        otherFileStorage = OtherFileStorage(context, "${filename}_${sensorName}", queue)
        otherFileStorage?.saveAtBatch()
        sensorManager.registerListener(this, PreSensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop() {
        otherFileStorage?.stop()
        sensorManager.unregisterListener(this)
        val item = SensorItemDataClass(
            id = 0,
            filePath = otherFileStorage?.filePath.toString(),
            fileName = "${DateUtils.getNowDate()}_${sensorName}_PixelWache.csv",
            date = DateUtils.getNowDate()
        )
        sensorDBUsecase?.insert(item)
    }

    override fun onSensorChanged(event: SensorEvent) {
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}