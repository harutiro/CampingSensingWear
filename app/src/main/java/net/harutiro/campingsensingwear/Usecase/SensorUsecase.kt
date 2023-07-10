package net.harutiro.campingsensingwear.Usecase

import android.content.Context
import net.harutiro.campingsensingwear.Repository.Sensor.AccSensor
import net.harutiro.campingsensingwear.Repository.Sensor.AngularVelocitySensor
import net.harutiro.campingsensingwear.Repository.Sensor.HartRateSensor
import net.harutiro.campingsensingwear.Repository.Sensor.SensorBase
import net.harutiro.campingsensingwear.Repository.SensorRepository

class SensorUsecase () {
    val TAG = "SensorUsecase"

    var targetSensors: MutableList<SensorBase> = mutableListOf()
    var sensorRepository = SensorRepository()

    var sensorStartFlag = false

    //TODO: 画面から設定できるようにする
    //おそらく1~20Hzくらいが妥当少数点も一応OK
    val samplingFrequency:Double = 5.0

    // 読み込みたいセンサーデータを保存
    fun addSensor(context: Context){
        targetSensors.add(AccSensor(context))
        targetSensors.add(AngularVelocitySensor(context))
        targetSensors.add(HartRateSensor(context))
    }

    fun start(fileName:String){
        sensorRepository.sensorStart(
            fileName = fileName,
            sensors = targetSensors,
            samplingFrequency = samplingFrequency
        )
        sensorStartFlag = true
    }
    fun stop(onStopped:() -> Unit){
        sensorRepository.sensorStop(
            sensors = targetSensors,
            onStopped = onStopped
        )
        sensorStartFlag = false
    }

}