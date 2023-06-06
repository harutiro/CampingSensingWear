package net.harutiro.campingsensingwear

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.harutiro.campingsensingwear.Repository.Adapter.SensorItemRViewAdapter
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
import net.harutiro.campingsensingwear.Usecase.PermissionUsecase
import net.harutiro.campingsensingwear.Usecase.SensorDBUsecase
import net.harutiro.campingsensingwear.Usecase.SensorUsecase
import net.harutiro.campingsensingwear.Api.WebDavPostApi
import net.harutiro.campingsensingwear.Utils.DateUtils
import net.harutiro.campingsensingwear.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    val sensorUsecase = SensorUsecase()
    val webDavPostApi = WebDavPostApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //スリープにさせないコード
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //センサーのDBをみる
        val sensorDBUsecase = SensorDBUsecase()
        sensorDBUsecase.init(context = this)

        //PermissionRequest
        val permissionUsecase = PermissionUsecase()
        permissionUsecase.permissionRequest(
            activity = this,
            permissions = permissionUsecase.permissionsFileWrite
        )

        val context = this.applicationContext
        val activity = this

        val rView = findViewById<RecyclerView>(R.id.sensorItemRView)
        val adapter = SensorItemRViewAdapter(this, object: SensorItemRViewAdapter.OnItemClickListner{
            override fun onItemClick(item: SensorItemDataClass) {
                Log.d("MainActivity","${item.date}button押した。")
                webDavPostApi.post(item){
                    activity.runOnUiThread {
                        Toast.makeText( context, it, Toast.LENGTH_LONG).show()
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                    }
                }

                Toast.makeText( context, "送信を開始", Toast.LENGTH_LONG).show();
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
        })
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter

        //追加したいセンサーを追加
        sensorUsecase.addSensor(this)

        findViewById<Switch>(R.id.sensorSwitch).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                sensorUsecase.start("Pixel7")
            }else{
                sensorUsecase.stop()

                sensorDBUsecase.getAll{ sensorSaveList ->
                    val list = sensorSaveList.toMutableList()
                    list.sortBy{
                        DateUtils.stringToDate(it.date)
                    }
                    list.reverse()
                    adapter.setList(list)
                }
            }
        }

        sensorDBUsecase.getAll{ sensorSaveList ->
            val list = sensorSaveList.toMutableList()
            list.sortBy{
                DateUtils.stringToDate(it.date)
            }
            list.reverse()
            adapter.setList(list)
        }

    }

    override fun onPause() {
        super.onPause()
        if(sensorUsecase.sensorStartFlag){
            sensorUsecase.stop()
        }
    }
}