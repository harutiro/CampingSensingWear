package net.harutiro.campingsensingwear.Utils

interface ApiResult {
    fun onSuccess(res: String)
    fun onError(res: String?)
}