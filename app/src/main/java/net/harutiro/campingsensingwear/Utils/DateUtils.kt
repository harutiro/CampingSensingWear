package net.harutiro.campingsensingwear.Utils

import android.icu.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object{
        fun getNowDate(): String {
            val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
            val date = Date(System.currentTimeMillis())
            return df.format(date)
        }

        fun stringToDate(dateString: String):Long{
            val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
            val date = df.parse(dateString)
            val absoluteTime = date.time

            return absoluteTime
        }

        fun getTimeStamp():Long {
            return System.currentTimeMillis()
        }
    }
}