package net.harutiro.campingsensingwear.Utils

import android.icu.text.SimpleDateFormat
import java.util.*

class DateUtils {

    fun getNowDate(): String? {
        val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
        val date = Date(System.currentTimeMillis())
        return df.format(date)
    }

    fun getTimeStamp():Long {
        return System.currentTimeMillis()
    }
}