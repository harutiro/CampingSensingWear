package net.harutiro.campingsensingwear.Utils

import android.content.Context
import android.os.Environment
import android.util.Log
import net.harutiro.campingsensingwear.Entity.AccDataClass
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter

class OtherFileStorage(private val context: Context) {

    val fileAppend: Boolean = true //true=追記, false=上書き
    var fileName: String = "SensorLog.csv"
    val filePath: String =
        context.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
            .plus("/") //内部ストレージのDocumentのURL

    var finalPath = ""




    val tmpDataList = mutableListOf<AccDataClass>()

    var pw: PrintWriter? = null

    private fun writeText(text: String) {
        pw?.println(text)

    }

    private fun openFile(path: String) {
        val fil = FileWriter(path, fileAppend)
        pw = PrintWriter(BufferedWriter(fil))

        Log.d("OtherFileStorage", path)
        finalPath = path
    }

    private fun closeFile() {
        pw?.close()
        tmpDataList.clear()
    }

    fun doLog(data: AccDataClass, date: String) {

        tmpDataList.add(data)

        if (tmpDataList.size >= 10) {
            openFile(filePath.plus(date).plus(fileName))

            tmpDataList.forEach() { item ->
                val text = "${item.time},${item.accX},${item.accY},${item.accZ}"
                writeText(text)
            }

            closeFile()
        }
    }

    fun close(date: String) {

        openFile(filePath.plus(date).plus(fileName))

        tmpDataList.forEach() { item ->
            val text = "${item.time},${item.accX},${item.accY},${item.accZ}"
            writeText(text)
        }

        closeFile()
    }


}