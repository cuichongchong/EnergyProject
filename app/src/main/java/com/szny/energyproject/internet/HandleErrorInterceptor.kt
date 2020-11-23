package com.szny.energyproject.internet

import android.content.Intent
import com.szny.energyproject.application.MyApplication
import okhttp3.Response
import org.json.JSONObject

class HandleErrorInterceptor : ResponseBodyInterceptor() {

    override fun intercept(response: Response, url: String?, body: String?): Response {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        /*if (jsonObject != null) {
           if(jsonObject.optInt("code",-1) == 401){
                //token失效回到登录页面
                val intent = Intent(MyApplication.mContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                MyApplication.mContext.startActivity(intent)
                SPUtils.getInstance().remove(ConstantValues.LOGIN_SUCCESS)
                SPUtils.getInstance().remove(ConstantValues.TOKEN)
            }else if (jsonObject.optInt("code", -1) != 200 && jsonObject.has("msg")){
                throw BaseException(jsonObject.getString("msg"),jsonObject.getString("code"))
            }
        }*/
        return response
    }
}