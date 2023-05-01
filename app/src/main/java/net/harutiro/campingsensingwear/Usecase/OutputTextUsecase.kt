package net.harutiro.campingsensingwear.Usecase

import android.widget.TextView
import net.harutiro.campingsensingwear.Entity.AccDataClass

class OutputTextUsecase {

    var outputText:TextView? = null

    fun init(textView: TextView){
        outputText = textView
    }

    fun write(data:AccDataClass){
        val str = """
            ${data.time},
            ${data.accX},
            ${data.accY},
            ${data.accZ}
        """.trimIndent()

        outputText?.text = str
    }
}