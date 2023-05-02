package net.harutiro.campingsensingwear.Entity.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass

@Database(entities = [SensorItemDataClass::class] ,version = 1 , exportSchema = false)
abstract class SensorDatabase: RoomDatabase() {
    abstract fun sensorItemDao():SensorItemsDao

}