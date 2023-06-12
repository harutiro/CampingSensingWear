package net.harutiro.campingsensingwear.Usecase

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
import net.harutiro.campingsensingwear.Entity.room.SensorDatabase
import net.harutiro.campingsensingwear.Entity.room.SensorItemsDao
import net.harutiro.campingsensingwear.Utils.DateUtils

class SensorDBUsecase {
    private lateinit var db:SensorDatabase
    lateinit var sensorItemDao:SensorItemsDao

    val TAG = "SensorDBUsecase"

    fun init(context: Context){
        this.db = Room.databaseBuilder(
            context,
            SensorDatabase::class.java,
            "sensorItems.db"
        ).build()

        this.sensorItemDao = this.db.sensorItemDao()
    }

    fun insert(item:SensorItemDataClass): Completable {
        return sensorItemDao.insert(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { Log.d(TAG, "INSERT 成功") }
            .doOnError { e -> Log.e(TAG, "INSERT 失敗 ${item.fileName}", e) }
    }

    fun getAll(func:(List<SensorItemDataClass>) -> Unit){
        val disposable = sensorItemDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {getList ->
                    val list = sortDay(getList)
                    func(list)
                },
                { e -> Log.e(TAG, "SELECT 失敗", e) }
            )
    }

    fun sortDay(getList :List<SensorItemDataClass>): MutableList<SensorItemDataClass> {

        val list = getList.toMutableList()
        list.sortBy{
            DateUtils.stringToDate(it.date)
        }
        list.reverse()

        return list

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
                        { Log.d(TAG, "INSERT 成功")},
                        { e -> Log.e(TAG, "INSERT 失敗", e) }
                    )
            }
        }


    }

}