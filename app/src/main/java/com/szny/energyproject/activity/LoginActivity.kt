package com.szny.energyproject.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.gyf.barlibrary.ImmersionBar
import com.szny.energyproject.R
import com.szny.energyproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ImmersionBar.with(this).init()

        initView()
        initEvent()
    }

    private fun initView() {

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

    override fun onClick(view: View) {
        when(view.id){
            R.id.btn_login ->{
                startActivity(Intent(this,ControllerActivity::class.java))
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }
}