package com.szny.energyproject.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.gyf.barlibrary.ImmersionBar
import com.szny.energyproject.R
import com.szny.energyproject.base.BaseActivity
import com.szny.energyproject.constant.ConstantValues
import com.szny.energyproject.entity.LoginEntity
import com.szny.energyproject.entity.LogoutEntity
import com.szny.energyproject.mvp.iviews.ILoginView
import com.szny.energyproject.mvp.persenters.LoginPresenter
import com.szny.energyproject.utils.SPUtils
import com.szny.energyproject.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登录页面
 * **/
class LoginActivity : BaseActivity(), View.OnClickListener,ILoginView {

    private lateinit var presenter: LoginPresenter
    private lateinit var username: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ImmersionBar.with(this).init()

        initView()
        initEvent()
    }

    private fun initView() {
        presenter = LoginPresenter()
        presenter.attachView(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.btn_login ->{
                username = et_name.text.toString().trim()
                password = et_pwd.text.toString().trim()
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    ToastUtils.showShort(this, "账号或密码不能为空")
                }else{
                    presenter.login(username, password)
                }
            }
        }
    }

    //登录成功返回
    override fun success(loginEntity: LoginEntity) {
        //记录登录成功的状态
        SPUtils.getInstance().saveBoolean(ConstantValues.LOGIN_SUCCESS, true)
        //记录返回的token，请求头中使用
        SPUtils.getInstance().saveString(ConstantValues.TOKEN, loginEntity.access_token)

        //跳转首页
        startActivity(Intent(this,ControllerActivity::class.java))
        finish()
    }

    //登录失败返回
    override fun failed(e: Throwable?) {
        ToastUtils.showShort(mContext,"登录失败")
    }

    override fun refreshToken(data: LoginEntity) {
    }
    override fun logout(data: LogoutEntity) {
    }

    private fun initEvent() {
        btn_login.setOnClickListener(this)

        //输入用户名的监听
        et_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().trim().isEmpty()){
                    tv_name.visibility = View.INVISIBLE
                    btn_login.setBackgroundResource(R.drawable.btn_login_shape1)
                }else{
                    tv_name.visibility = View.VISIBLE
                    if(et_pwd.text?.trim().toString().isNotEmpty()){
                        btn_login.setBackgroundResource(R.drawable.btn_login_shape2)
                    }
                }
            }
        })

        //输入密码的监听
        et_pwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().trim().isEmpty()){
                    tv_pwd.visibility = View.INVISIBLE
                    btn_login.setBackgroundResource(R.drawable.btn_login_shape1)
                }else{
                    tv_pwd.visibility = View.VISIBLE
                    if(et_name.text?.trim().toString().isNotEmpty()){
                        btn_login.setBackgroundResource(R.drawable.btn_login_shape2)
                    }
                }
            }

        })
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }
}