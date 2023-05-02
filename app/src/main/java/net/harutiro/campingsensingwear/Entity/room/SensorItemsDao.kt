package net.harutiro.campingsensingwear.Entity.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass

@Dao
interface SensorItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item:SensorItemDataClass): Completable

    //クエリの中身
    @Query("select * from SensorItemDataClass")
    fun getAll(): Flowable<List<SensorItemDataClass>>
    //変更があったときに自動で取得をしてくれる。

    @Query("delete from SensorItemDataClass")
    fun removeAll()
}