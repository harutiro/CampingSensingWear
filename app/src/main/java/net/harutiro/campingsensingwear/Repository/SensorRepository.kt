package net.harutiro.campingsensingwear.Repository

import android.util.Log
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    fun sensorStop(sensors: MutableList<SensorBase>, onStopped:() -> Unit) {
        val a = Completable.concat(sensors.map { sensor ->
            Completable.defer { sensor.stop() }
                .subscribeOn(Schedulers.io())
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(TAG, "センサー停止 成功")
                    //センサーが終了した時にMainActivityに伝える。
                    onStopped()
                },
                { e ->
                    Log.e(TAG, "センサー停止 失敗", e)
                }
            )
    }

}