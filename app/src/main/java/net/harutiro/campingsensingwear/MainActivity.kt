package net.harutiro.campingsensingwear

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Switch
import net.harutiro.campingsensingwear.Usecase.SensorUsecase
import net.harutiro.campingsensingwear.Utils.PermissionUtils
import net.harutiro.campingsensingwear.databinding.ActivityMainBinding

class MainActivity : Activity() , SensorEventListener {

    private lateinit var binding: ActivityMainBinding

    val sensorUsecase = SensorUsecase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permissionUtils = PermissionUtils()
        permissionUtils.requestPermissions(this,this)

        sensorUsecase.init(this , binding)

        findViewById<Switch>(R.id.sensorSwitch).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                sensorUsecase.start(this)
            }else{
                sensorUsecase.stop(this)
            }
        }

    }

    override fun onSensorChanged(event: SensorEvent) {
        sensorUsecase.getValue(event)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onPause() {
        super.onPause()
        sensorUsecase.stop(this)
    }
}