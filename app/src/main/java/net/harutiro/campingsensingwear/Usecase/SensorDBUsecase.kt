package net.harutiro.campingsensingwear.Usecase

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
import net.harutiro.campingsensingwear.Entity.room.SensorDatabase
import net.harutiro.campingsensingwear.Entity.room.SensorItemsDao

class SensorDBUsecase {
    private lateinit var db:SensorDatabase
    lateinit var sensorItemDao:SensorItemsDao

    fun init(context: Context){
        this.db = Room.databaseBuilder(
            context,
            SensorDatabase::class.java,
            "sensorItems.db"
        ).build()

        this.sensorItemDao = this.db.sensorItemDao()
    }

    fun insert(item:SensorItemDataClass){
        val a = sensorItemDao.insert(item).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.d("User", "INSERT 成功")},
                { e -> Log.e("User", "INSERT 失敗", e) }
            )
    }

    fun getAll(func:(List<SensorItemDataClass>) -> Unit){
        val disposable = sensorItemDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    func(it)
                },
                { e -> Log.e("UserView", "SELECT 失敗", e) }
            )
    }

    fun insertTestData(){
        val list = mutableListOf(
            SensorItemDataClass(
                id = 0,
                fileName = "hogehoge",
                filePath = "1234",
                date = "2022/01/02"
            ),
            SensorItemDataClass(
                id = 1,
                fileName = "hogehoge",
                filePath = "1234",
                date = "2022/02/03"
            ),
            SensorItemDataClass(
                id = 2,
                fileName = "hogehoge",
                filePath = "1234",
                date = "2022/03/04"
            )

        )

        list.forEach{
            CoroutineScope(Dispatchers.IO).launch {
                sensorItemDao.insert(it).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { Log.d("User", "INSERT 成功")},
                        { e -> Log.e("User", "INSERT 失敗", e) }
                    )
            }
        }


    }

}