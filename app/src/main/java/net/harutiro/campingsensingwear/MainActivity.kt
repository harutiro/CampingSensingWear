package net.harutiro.campingsensingwear

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.harutiro.campingsensingwear.Adapter.SensorItemRViewAdapter
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
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

        val rView = findViewById<RecyclerView>(R.id.sensorItemRView)
        val adapter = SensorItemRViewAdapter(this, object: SensorItemRViewAdapter.OnItemClickListner{
            override fun onItemClick(item: SensorItemDataClass) {
                Log.d("MainActivity","${item.date}button押した。")
            }
        })
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter

        adapter.setList(
            mutableListOf(
                SensorItemDataClass(
                    fileName = "hogehoge",
                    filePath = "1234",
                    date = "2022/01/02"
                ),
                SensorItemDataClass(
                    fileName = "hogehoge",
                    filePath = "1234",
                    date = "2022/02/03"
                ),
                SensorItemDataClass(
                    fileName = "hogehoge",
                    filePath = "1234",
                    date = "2022/03/04"
                )

            )
        )


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