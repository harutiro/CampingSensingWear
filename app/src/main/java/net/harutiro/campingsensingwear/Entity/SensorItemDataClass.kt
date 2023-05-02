package net.harutiro.campingsensingwear.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SensorItemDataClass (
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var fileName:String = "",
    var filePath:String = "",
    var date:String = ""
)