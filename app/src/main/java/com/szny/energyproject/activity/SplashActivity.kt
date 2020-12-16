package com.szny.energyproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import com.gyf.barlibrary.ImmersionBar
import com.szny.energyproject.base.BaseActivity
import com.szny.energyproject.constant.ConstantValues
import com.szny.energyproject.utils.SPUtils

/**
 * 启动页面
 * */
class SplashActivity : BaseActivity() {

    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 ->
                    //前往首页
                    mContext.startActivity(Intent(mContext, ControllerActivity::class.java))
                2 ->
                    //前往登录页
                    mContext.startActivity(Intent(mContext, LoginActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImmersionBar.with(this).init()

        //判断是否登陆成功
        val isLoginSuccess: Boolean =
            SPUtils.getInstance().getBoolean(ConstantValues.LOGIN_SUCCESS, false)
        if (isLoginSuccess) {
            //登陆成功过直接跳转到首页
            mHandler.sendEmptyMessageDelayed(1, 1000)
        } else {
            //没有登陆成功过跳转登录页
            mHandler.sendEmptyMessageDelayed(2, 1000)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        ImmersionBar.with(this).destroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}