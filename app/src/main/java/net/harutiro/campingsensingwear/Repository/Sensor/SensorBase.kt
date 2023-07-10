package net.harutiro.campingsensingwear.Repository.Sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import io.reactivex.Completable
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

    var samplingFrequency = -1.0

    abstract val sensorType:Int
    abstract val sensorName:String

    fun init() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        PreSensor = sensorManager.getDefaultSensor(sensorType)

        this.sensorDBUsecase = SensorDBUsecase()
        sensorDBUsecase?.init(context = context)
    }

    fun start(filename: String , samplingFrequency:Double) {
        queue.clear()
        otherFileStorage = OtherFileStorage(context, "${filename}_${sensorName}", queue)
        otherFileStorage?.saveAtBatch()
        sensorManager.registerListener(this, PreSensor, SensorManager.SENSOR_DELAY_UI)

        this.samplingFrequency = samplingFrequency
    }

    fun stop(): Completable? {
        otherFileStorage?.stop()
        sensorManager.unregisterListener(this)
        val item = SensorItemDataClass(
            id = 0,
            filePath = otherFileStorage?.filePath.toString(),
            fileName = "${DateUtils.getNowDate()}_${sensorName}_PixelWache.csv",
            date = DateUtils.getNowDate()
        )
        return sensorDBUsecase?.insert(item)
    }

    var nowTime:Long = 0
    fun addQueue(sensorName:String,data: String,timeStamp:Long) {

        Log.d(sensorName, "差分:${timeStamp - nowTime}")
        Log.d(sensorName,"周波数:${frequency2second(samplingFrequency) * 1000}")

        if (timeStamp - nowTime > frequency2second(samplingFrequency) * 1000) {
            queue.add(data)
            nowTime = timeStamp
            Log.d(sensorName, data)
        }
    }

    fun frequency2second(frequency:Double):Double{
        return 1.0/frequency
    }

    override fun onSensorChanged(event: SensorEvent) {
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}