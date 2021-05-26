package com.szny.energyproject.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.gyf.barlibrary.ImmersionBar
import com.jude.rollviewpager.hintview.ColorPointHintView
import com.szny.energyproject.R
import com.szny.energyproject.adapter.RollViewPagerAdapter
import com.szny.energyproject.base.BaseActivity
import com.szny.energyproject.constant.ConstantValues
import com.szny.energyproject.entity.LogoutEntity
import com.szny.energyproject.entity.UserEntity
import com.szny.energyproject.mvp.exceptions.BaseException
import com.szny.energyproject.mvp.iviews.IHomeView
import com.szny.energyproject.mvp.persenters.HomePresenter
import com.szny.energyproject.utils.DensityUtil
import com.szny.energyproject.utils.SPUtils
import com.szny.energyproject.utils.ToastUtils
import com.szny.energyproject.widget.CommonDialog
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.collections.ArrayList

/**
 * 首页
 * */
class HomeActivity : BaseActivity(),IHomeView, View.OnClickListener {
    //用户id
    private var userId = -1

    private var dialog: CommonDialog? = null

    private lateinit var rollViewPagerAdapter: RollViewPagerAdapter

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        ImmersionBar.with(this).init()

        initView()
        initData()
        initEvent()
    }

    private fun initView() {
        toolbar_title.text = "统计控制"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val tbLinearParams = toolbar.layoutParams //取控件textView当前的布局参数
            toolbar.setPadding(0, DensityUtil.dip2px(this, 20f),                                                                 0, 0)//4个参数按顺序分别是左上右下
            tbLinearParams.height = DensityUtil.dip2px(this, 69f)// 控件的高
            toolbar.layoutParams = tbLinearParams //使设置好的布局参数应用到控件
        }

        presenter = HomePresenter()
        presenter.attachView(this)
    }

    private fun initData() {
        //获取用户信息
        presenter.userInfo()

        //初始化轮播图
        val urls: ArrayList<Int> = ArrayList()
        urls.add(R.mipmap.ic_rollview1)
        urls.add(R.mipmap.ic_rollview2)
        urls.add(R.mipmap.ic_rollview3)

        rollPager.setPlayDelay(3000)
        rollViewPagerAdapter = RollViewPagerAdapter(rollPager, mContext, urls)
        rollPager.setAdapter(rollViewPagerAdapter)
        rollPager.setHintView(ColorPointHintView(mContext, resources.getColor(R.color.color_333333), resources.getColor(R.color.color_666666)))

    }

    private fun initEvent() {
        tv_name.setOnClickListener(this)
        iv_about.setOnClickListener(this)
        tv_control.setOnClickListener(this)
        tv_statis.setOnClickListener(this)
        tv_analys.setOnClickListener(this)
        tv_co2.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.tv_name ->{
                logOut()
            }
            R.id.iv_about ->{
                toAbout()
            }
            R.id.tv_control ->{
                if(this.userId != -1){
                    startActivity(Intent(this, ControllerActivity::class.java)
                        .putExtra("userId",this.userId))
                }
            }
            R.id.tv_statis ->{
                startActivity(Intent(this,DataActivity::class.java)
                    .putExtra("userId",this.userId))
            }
            R.id.tv_analys ->{
                startActivity(Intent(this,AnalysisActivity::class.java)
                    .putExtra("userId",this.userId))
            }
            R.id.tv_co2 ->{
                startActivity(Intent(this,CarbonEmissionActivity::class.java))
            }
        }
    }

    //获取用户信息
    override fun success(t: UserEntity) {
        this.userId = t.id

        //设置用户名
        tv_name.text = t.userName
    }

    //退出登录成功
    override fun logout(data: LogoutEntity?) {
        //清除登录状态和token缓存
        SPUtils.getInstance().remove(ConstantValues.LOGIN_SUCCESS)
        SPUtils.getInstance().remove(ConstantValues.TOKEN)
        //返回登录页面
        startActivity(Intent(mContext,LoginActivity::class.java))
        //关闭当前页面
        this.finish()
    }

    //关于弹框
    private fun toAbout() {
        if (dialog == null) {
            dialog = CommonDialog(mContext)
        }
        dialog!!.setMessage("1.技术支持:河南省省直能源实业有限公司.\n2.Copyright © 2020 - 2021 河南省省直能源实业有限公司 版权所有.")
            .setTitle("关于")
            .setSingle(true)
            .setNegtive("确认")
            .setOnClickBottomListener(object : CommonDialog.OnClickBottomListener {
                override fun onPositiveClick() {
                    dialog!!.dismiss()
                }
                override fun onNegtiveClick() {
                    dialog!!.dismiss()
                }
            })
            .show()
    }

    //退出登录
    private fun logOut() {
        if (dialog == null) {
            dialog = CommonDialog(mContext)
        }
        dialog!!.setMessage("确认退出登录?")
            .setTitle("提示")
            .setSingle(false)
            .setPositive("确认")
            .setNegtive("取消")
            .setOnClickBottomListener(object : CommonDialog.OnClickBottomListener {
                override fun onPositiveClick() {
                    dialog!!.dismiss()
                    presenter.logout(SPUtils.getInstance().getString(ConstantValues.TOKEN,""))
                }

                override fun onNegtiveClick() {
                    dialog!!.dismiss()
                }
            })
            .show()
    }

    override fun failed(e: Throwable) {
        //token失效，返回登录页面
        if(e is BaseException && e.errCode == 113){
            ToastUtils.showShort(mContext,"验证失效,请重新登录")
        }else{
            ToastUtils.showShort(mContext,"请求失败")
        }
    }

    /**
     * 按两次退出
     */
    private var exitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            oneKeyExit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun oneKeyExit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtils.showShort(mContext,"再按一次退出")
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }
}