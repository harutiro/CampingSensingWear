package net.harutiro.campingsensingwear.Usecase

import android.util.Log
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
import net.harutiro.campingsensingwear.Utils.ApiResult
import net.harutiro.campingsensingwear.Utils.PostData

class WebDavPostUsecase {

    val TAG = "WebDavPostUsecase"

    fun post(item:SensorItemDataClass, toast:(String)->Unit){
        val folderName = "makino"
        val postData = PostData(fileName= item.fileName, filePath= item.filePath, folderName=folderName)
        postData.run(callback = object : ApiResult {

            // アップロード完了時の処理
            override fun onSuccess(res: String) {
                toast("成功したよ")
                Log.d(TAG,res)
            }

            // アップロード失敗時の処理
            override fun onError(res: String?) {
                toast("ごめん、無理だった。")
                Log.d(TAG,res.toString())
            }
        })
    }
}