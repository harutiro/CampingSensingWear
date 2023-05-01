package net.harutiro.campingsensingwear.Usecase

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.common.util.DataUtils
import net.harutiro.campingsensingwear.Entity.AccDataClass
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
    var dateUtils = DateUtils()
    val outputTextUsecase = OutputTextUsecase()

    var date = ""

    fun init(context: Context , binding: ActivityMainBinding){
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE)!! as SensorManager
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        otherFileStorage = OtherFileStorage(context)

        outputTextUsecase.init(binding.sensorOutputText)

        date = dateUtils.getNowDate().toString()


    }

    fun start(sensorEventListener: SensorEventListener){
        sensorManager.registerListener(sensorEventListener, accSensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop(sensorEventListener: SensorEventListener){
        sensorManager.unregisterListener(sensorEventListener)
        otherFileStorage?.close(date)

        val folderName = "makino"
        val filePath = otherFileStorage?.finalPath.toString()
        val fileName = "testData.csv"
        val postData = PostData(fileName= fileName, filePath= filePath, folderName=folderName)
        postData.run(callback = object : ApiResult {

            // アップロード完了時の処理
            override fun onSuccess(res: String) {
                Log.d(TAG,res)
            }

            // アップロード失敗時の処理
            override fun onError(res: String?) {
                Log.d(TAG,res.toString())
            }
        })


    }

    fun getValue(event: SensorEvent){
        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val data = AccDataClass(
                dateUtils.getTimeStamp(),
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