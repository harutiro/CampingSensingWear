package net.harutiro.campingsensingwear

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.harutiro.campingsensingwear.Adapter.SensorItemRViewAdapter
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
import net.harutiro.campingsensingwear.Usecase.SensorDBUsecase
import net.harutiro.campingsensingwear.Usecase.SensorUsecase
import net.harutiro.campingsensingwear.Usecase.WebDavPostUsecase
import net.harutiro.campingsensingwear.Utils.PermissionUtils
import net.harutiro.campingsensingwear.databinding.ActivityMainBinding

class MainActivity : Activity() , SensorEventListener {

    private lateinit var binding: ActivityMainBinding

    val sensorUsecase = SensorUsecase()
    val webDavPostUsecase = WebDavPostUsecase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sensorDBUsecase = SensorDBUsecase()
        sensorDBUsecase.init(this)

        val permissionUtils = PermissionUtils()
        permissionUtils.requestPermissions(this,this)

        sensorUsecase.init(this , binding ,sensorDBUsecase)

        findViewById<Switch>(R.id.sensorSwitch).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                sensorUsecase.start(this)
            }else{
                sensorUsecase.stop(this)
            }
        }

        val context = this.applicationContext
        val activity = this

        val rView = findViewById<RecyclerView>(R.id.sensorItemRView)
        val adapter = SensorItemRViewAdapter(this, object: SensorItemRViewAdapter.OnItemClickListner{
            override fun onItemClick(item: SensorItemDataClass) {
                Log.d("MainActivity","${item.date}button押した。")
                webDavPostUsecase.post(item){
                    activity.runOnUiThread {
                        Toast.makeText( context, it, Toast.LENGTH_LONG).show()
                    }
                }

                Toast.makeText( context, "送信を開始", Toast.LENGTH_LONG).show();
            }
        })
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter

//        sensorDBUsecase.insertTestData()

        sensorDBUsecase.getAll{
            adapter.setList(it)
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