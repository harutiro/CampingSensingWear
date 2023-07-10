package net.harutiro.campingsensingwear

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.google.android.gms.common.util.DataUtils
import net.harutiro.campingsensingwear.Repository.Adapter.SensorItemRViewAdapter
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
import net.harutiro.campingsensingwear.Usecase.PermissionUsecase
import net.harutiro.campingsensingwear.Usecase.SensorDBUsecase
import net.harutiro.campingsensingwear.Usecase.SensorUsecase
import net.harutiro.campingsensingwear.Api.WebDavPostApi
import net.harutiro.campingsensingwear.Utils.DateUtils
import net.harutiro.campingsensingwear.View.CustomScrollingLayoutCallback
import net.harutiro.campingsensingwear.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    val sensorUsecase = SensorUsecase()
    val webDavPostApi = WebDavPostApi()

    val TAG = "MainActivity"

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

        val rView = findViewById<WearableRecyclerView>(R.id.sensorItemRView)
        val adapter = SensorItemRViewAdapter(this, object: SensorItemRViewAdapter.OnItemClickListner{
            override fun onItemClick(item: SensorItemDataClass) {
                Log.d("MainActivity","${item.date}button押した。")
                webDavPostApi.post(item){
                    activity.runOnUiThread {
                        Toast.makeText( context, it, Toast.LENGTH_SHORT).show()
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                    }
                }

                Toast.makeText( context, "送信を開始", Toast.LENGTH_SHORT).show();
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }

            override fun onLongItemClick(item: SensorItemDataClass) {
                AlertDialog.Builder(this@MainActivity) // FragmentではActivityを取得して生成
                    .setTitle("${item.fileName}を消去します")
                    .setMessage("本当に消しても大丈夫ですか？")
                    .setPositiveButton("OK") { _, _ ->
                        sensorDBUsecase.deleteItem(item)
                        Toast.makeText(context, "消去しました。", Toast.LENGTH_SHORT).show();

                    }
                    .setNegativeButton("No") { _, _ ->
                        Toast.makeText(context, "取り消しました。", Toast.LENGTH_SHORT).show();
                    }
                    .show()
            }
        })
        rView.apply {
            isEdgeItemsCenteringEnabled = true
//            layoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager = WearableLinearLayoutManager(this@MainActivity, CustomScrollingLayoutCallback())
            this.adapter = adapter
        }

        //追加したいセンサーを追加
        sensorUsecase.addSensor(this)

        findViewById<Switch>(R.id.sensorSwitch).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                sensorUsecase.start("Pixel7" + DateUtils.getNowDate())
            }else{
                sensorUsecase.stop(){
                    sensorDBUsecase.getAll{ sensorSaveList ->
                        adapter.setList(sensorSaveList)
                    }
                }
            }
        }

        sensorDBUsecase.getAll{ sensorSaveList ->
            adapter.setList(sensorSaveList)
        }

    }

    override fun onPause() {
        super.onPause()
        if(sensorUsecase.sensorStartFlag){
            sensorUsecase.stop(){

            }
        }
    }
}