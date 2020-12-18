package com.szny.energyproject.internet

import android.content.Intent
import com.szny.energyproject.activity.LoginActivity
import com.szny.energyproject.application.MyApplication
import com.szny.energyproject.constant.ConstantValues
import com.szny.energyproject.mvp.exceptions.BaseException
import com.szny.energyproject.utils.SPUtils
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

        if(jsonObject != null){
            //token失效回到登录页面
            if("invalid_token" == jsonObject.optString("error","")){
                val intent = Intent(MyApplication.mContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                MyApplication.mContext.startActivity(intent)
                SPUtils.getInstance().remove(ConstantValues.LOGIN_SUCCESS)
                SPUtils.getInstance().remove(ConstantValues.TOKEN)

                throw BaseException(jsonObject.getString("error"),113)
            }
        }
        return response
    }
}