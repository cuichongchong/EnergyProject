package com.szny.energyproject.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.gyf.barlibrary.ImmersionBar
import com.szny.energyproject.R
import com.szny.energyproject.adapter.AirCondAdapter
import com.szny.energyproject.adapter.ElectricAdapter
import com.szny.energyproject.adapter.SocketAdapter
import com.szny.energyproject.base.BaseActivity
import com.szny.energyproject.base.RecyclerViewDivider
import com.szny.energyproject.constant.ConstantValues
import com.szny.energyproject.entity.*
import com.szny.energyproject.mvp.exceptions.BaseException
import com.szny.energyproject.mvp.iviews.IControlView
import com.szny.energyproject.mvp.persenters.ControlPresenter
import com.szny.energyproject.utils.DensityUtil
import com.szny.energyproject.utils.LogUtils
import com.szny.energyproject.utils.SPUtils
import com.szny.energyproject.utils.ToastUtils
import com.szny.energyproject.widget.CommonDialog
import com.szny.energyproject.widget.SignalRManager
import kotlinx.android.synthetic.main.activity_controller.*

/**
 * 控制管理页面
 * */
class ControllerActivity : BaseActivity(), View.OnClickListener, IControlView {

    private lateinit var electricAdapter:ElectricAdapter
    private lateinit var electricList:MutableList<ElectricEntity.ElectricListBean>

    private lateinit var airCondAdapter:AirCondAdapter
    private lateinit var airCondList:MutableList<AirCondEntity>

    private lateinit var socketAdapter:SocketAdapter
    private lateinit var socketList:MutableList<SocketEntity>

    private var dialog: CommonDialog? = null

    //房间选择器
    private var mRoomList = arrayListOf<String>()
    private var mRoomPickerView: OptionsPickerView<*>? = null

    //初始化空调开关状态
    private var isAirOpen = false
    //初始化照明开关状态
    private var isBulbOpen = false
    //初始化插座开关状态
    private var isSocketOpen = false
    //初始化商铺总开关状态,只有商品角色进来时才有
    private var isShopOpen = false

    private lateinit var presenter: ControlPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        ImmersionBar.with(this).init()

        initView()
        initData()
        initEvent()
    }

    private fun initView() {
        toolbar_title.text = "房间管理"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val tbLinearParams = toolbar.layoutParams //取控件textView当前的布局参数
            toolbar.setPadding(0, DensityUtil.dip2px(this, 20f),                                                                 0, 0)//4个参数按顺序分别是左上右下
            tbLinearParams.height = DensityUtil.dip2px(this, 69f)// 控件的高
            toolbar.layoutParams = tbLinearParams //使设置好的布局参数应用到控件
        }

        presenter = ControlPresenter()
        presenter.attachView(this)
    }

    private fun initEvent() {
        iv_about.setOnClickListener(this)
        tv_more.setOnClickListener(this)
        tv_name.setOnClickListener(this)
        iv_change.setOnClickListener(this)
        iv_bulb.setOnClickListener(this)
        iv_socket.setOnClickListener(this)
        iv_air.setOnClickListener(this)
        iv_master.setOnClickListener(this)
    }

    private fun initData() {
        //获取用户信息
        presenter.userInfo()

        try {
            //测试发送signalR
            SignalRManager.getInstance().hubConnection.send("Test", "410105A00101",1,"测试发送连接")
            LogUtils.e("doing","SignalR 发送成功")

            //测试监听接收signalR
            SignalRManager.getInstance().setIcallBack {
                LogUtils.e("doing", "SignalR 接收成功 $it")
            }
        } catch (e: Exception) {
            LogUtils.e("doing","SignalR 失败")
        }

        //电量模拟数据
        electricList = ArrayList<ElectricEntity.ElectricListBean>()
        electricList.add(ElectricEntity.ElectricListBean("今日电量(度)","1.0"))
        electricList.add(ElectricEntity.ElectricListBean("当月电量(度)","2.0"))
        electricList.add(ElectricEntity.ElectricListBean("当前功率(kw)","3.0"))

        electricAdapter = ElectricAdapter(electricList)
        rv_electric.layoutManager = GridLayoutManager(mContext, 3)
        rv_electric.adapter = electricAdapter

        //空调模拟数据
        airCondList = ArrayList<AirCondEntity>()
        airCondList.add(AirCondEntity("办公室格力空调",24,24,true,false,2,false))
        airCondList.add(AirCondEntity("办公室美的空调",26,26,false,true,3,false))
        //airCondList.add(AirCondEntity("办公室三菱空调",22))

        airCondAdapter = AirCondAdapter(mContext,airCondList,isAirOpen)
        rv_air_conditioner.layoutManager = LinearLayoutManager(mContext)
        val divider = RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL)
        divider.setmItemSize(DensityUtil.dip2px(mContext,10f))
        divider.setColor(R.color.color_f5f5f5)
        rv_air_conditioner.addItemDecoration(divider)
        rv_air_conditioner.adapter = airCondAdapter

        //插座模拟数据
        socketList = ArrayList<SocketEntity>()
        socketList.add(SocketEntity("云插座1",false,"停止中"))
        socketList.add(SocketEntity("云插座2",false,"停止中"))
        socketList.add(SocketEntity("云插座3",false,"停止中"))

        socketAdapter = SocketAdapter(mContext,socketList,isSocketOpen)
        rv_socket.layoutManager = LinearLayoutManager(mContext)
        rv_socket.addItemDecoration(divider)
        rv_socket.adapter = socketAdapter

        setUpListener()

        //初始化房间数据
        mRoomList.add("房间101")
        mRoomList.add("房间102")
        mRoomList.add("房间103")
        mRoomList.add("房间104")
        mRoomList.add("房间105")
        mRoomList.add("房间106")
        //初始化房间选择器
        initRoomOptionPicker()
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.tv_name ->{
                logOut()
            }
            R.id.iv_about ->{
                toAbout()
            }
            R.id.iv_change ->{
                mRoomPickerView?.show()
            }
            R.id.tv_more ->{
                startActivity(Intent(this,
                    DataActivity::class.java))
            }
            //照明总闸开关切换
            R.id.iv_bulb ->{
                if(isBulbOpen){
                    sureDialog("照明总闸是否确认分闸?",1,true)
                }else{
                    sureDialog("照明总闸是否确认合闸?",1,false)
                }
            }
            //插座总闸开关切换
            R.id.iv_socket ->{
                if(isSocketOpen){
                    sureDialog("插座总闸是否确认分闸?",2,true)
                }else{
                    sureDialog("插座总闸是否确认合闸?",2,false)
                }
            }
            //空调总闸开关切换
            R.id.iv_air ->{
                if(isAirOpen){
                    sureDialog("空调总闸是否确认分闸?",3,true)
                }else{
                    sureDialog("空调总闸是否确认合闸?",3,false)
                }
            }
            //商铺总闸开关切换
            R.id.iv_master ->{
                if(isShopOpen){
                    sureDialog("入户总闸是否确认分闸?",4,true)
                }else{
                    sureDialog("入户总闸是否确认合闸?",4,false)
                }
            }
        }
    }

    private fun setUpListener() {
        //空调适配器的点击事件
        airCondAdapter.setOnItemsClickListener { v, position,checked ->
            val data = airCondAdapter.data
            var temp = data[position].setTemp
            when(v.id){
                //开关
                R.id.set_switch ->{
                    data[position].isOpen = checked
                    if(checked){
                        ToastUtils.showShort(mContext,"开")
                    }else{
                        ToastUtils.showShort(mContext,"关")
                    }
                }
                //设置模式
                R.id.tv_set_model ->{
                    data[position].isHot = !data[position].isHot
                }
                //设置风速
                R.id.tv_set_gear ->{
                    if(data[position].isAuto){
                        data[position].isAuto = false
                        data[position].gear = 1
                    }else{
                        when(data[position].gear){
                            1 ->{
                                data[position].gear = 2
                            }
                            2 ->{
                                data[position].gear = 3
                            }
                            3 ->{
                                data[position].isAuto = true
                            }
                        }
                    }
                }
                //温度减
                R.id.iv_desc ->{
                    if(temp > 0){
                        temp -= 1
                        airCondAdapter.data[position].setTemp = temp
                    }
                }
                //温度加
                R.id.iv_add ->{
                    temp += 1
                    airCondAdapter.data[position].setTemp = temp
                }
            }
            airCondAdapter.notifyDataSetChanged()
        }

        //插座适配器的点击事件
        socketAdapter.setOnItemsClickListener { v, position ->
            val entity = socketAdapter.data[position]
            when(v.id){
                R.id.iv_socket ->{
                    if(entity.isOpen){
                        //进行分闸
                        entity.isOpen = false
                        entity.statue = "停止中"
                    }else{
                        //进行合闸
                        entity.isOpen = true
                        entity.statue = "工作中"
                    }
                    socketAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    //照明、插座、空调总闸确认弹框操作
    private fun sureDialog(title:String,type:Int,isOpen:Boolean){
        if (dialog == null) {
            dialog = CommonDialog(mContext)
        }
        dialog!!.setMessage(title)
            .setTitle("提示")
            .setSingle(false)
            .setPositive("确认")
            .setNegtive("取消")
            .setOnClickBottomListener(object : CommonDialog.OnClickBottomListener {
                override fun onPositiveClick() {
                    dialog!!.dismiss()
                    when(type){
                        //表示照明总闸确认
                        1 ->{
                            if(isOpen){
                                isBulbOpen = false
                                iv_bulb.setBackgroundResource(R.mipmap.ic_switch_close)
                                tv_bulb.text = "分闸"
                                tv_bulb_status.text = "灯熄"
                            }else{
                                isBulbOpen = true
                                iv_bulb.setBackgroundResource(R.mipmap.ic_switch_open)
                                tv_bulb.text = "合闸"
                            }
                        }
                        //表示插座总闸确认
                        2 ->{
                            if(isOpen){
                                isSocketOpen = false
                                iv_socket.setBackgroundResource(R.mipmap.ic_switch_close)
                                tv_socket.text = "分闸"
                                tv_socket_status.text = "待机中"

                                //修改插座列表中总闸状态
                                socketAdapter.isGate = false
                                //修改每一个插座的开关状态
                                socketAdapter.data.forEach {
                                    if(it.isOpen){
                                        it.isOpen = false
                                        it.statue = "停止中"
                                    }
                                }
                            }else{
                                isSocketOpen = true
                                iv_socket.setBackgroundResource(R.mipmap.ic_switch_open)
                                tv_socket.text = "合闸"

                                //修改插座列表中总闸状态
                                socketAdapter.isGate = true
                            }
                            socketAdapter.notifyDataSetChanged()
                        }
                        //表示空调总闸确认
                        3 ->{
                            if(isOpen){
                                isAirOpen = false
                                iv_air.setBackgroundResource(R.mipmap.ic_switch_close)
                                tv_air.text = "分闸"

                                //修改空调列表中总闸状态
                                airCondAdapter.isGate = false
                                //修改每一个空调的开关状态
                                airCondAdapter.data.forEach {
                                    if(it.isOpen){
                                        it.isOpen = false
                                    }
                                }
                            }else{
                                isAirOpen = true
                                iv_air.setBackgroundResource(R.mipmap.ic_switch_open)
                                tv_air.text = "合闸"

                                //修改空调列表中总闸状态
                                airCondAdapter.isGate = true
                            }
                            airCondAdapter.notifyDataSetChanged()
                        }
                        //表示入户总闸确认
                        4 ->{
                            if(isOpen){
                                isShopOpen = false
                                iv_master.setBackgroundResource(R.mipmap.ic_switch_close)
                                tv_master.text = "分闸"
                            }else{
                                isShopOpen = true
                                iv_master.setBackgroundResource(R.mipmap.ic_switch_open)
                                tv_master.text = "合闸"
                            }
                        }
                    }

                }

                override fun onNegtiveClick() {
                    dialog!!.dismiss()
                }
            })
            .show()
    }

    //获取用户信息成功返回
    override fun success(t: UserEntity) {
        //设置用户名
        tv_name.text = t.userName
    }

    //退出登录成功
    override fun logout(data: LogoutEntity) {
        //清除登录状态和token缓存
        SPUtils.getInstance().remove(ConstantValues.LOGIN_SUCCESS)
        SPUtils.getInstance().remove(ConstantValues.TOKEN)
        //返回登录页面
        startActivity(Intent(mContext,LoginActivity::class.java))
        //关闭当前页面
        this.finish()
    }

    override fun failed(e: Throwable) {
        //token失效，返回登录页面
        if(e is BaseException && e.errCode == 113){
            ToastUtils.showShort(mContext,"验证失效,请重新登录")
        }else{
            ToastUtils.showShort(mContext,"请求失败")
        }
    }

    //初始化房间选择器
    private fun initRoomOptionPicker() {
        mRoomPickerView = OptionsPickerBuilder(this,
            OnOptionsSelectListener { option1, option2, option3, v -> //返回的分别是三个级别的选中位置
                val tx: String? = mRoomList[option1]
                ToastUtils.showShort(mContext,tx)
            })
            .setDecorView(findViewById<RelativeLayout>(R.id.activity_rootview)) //必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
            .setCancelText("取消") //取消按钮文字
            .setCancelColor(resources.getColor(R.color.color_111111)) //取消按钮文字颜色
            .setSubmitText("完成") //确认按钮文字
            .setSubmitColor(resources.getColor(R.color.color_1790FF)) //确定按钮文字颜色
            .setContentTextSize(16) //滚轮文字大小
            .setTextColorCenter(resources.getColor(R.color.color_111111)) //设置选中文本的颜色值
            .setLineSpacingMultiplier(2.2f) //行间距
            .setDividerColor(resources.getColor(R.color.color_e8e8e8)) //设置分割线的颜色
            .setSelectOptions(0) //设置选择的值
            .setLayoutRes(
                R.layout.item_picker_room
            ) { v ->
                val tvFinish = v.findViewById<TextView>(R.id.tv_finish)
                tvFinish.setOnClickListener {
                    mRoomPickerView!!.returnData()
                    mRoomPickerView!!.dismiss()
                }
            }
            .build<Any>()
        (mRoomPickerView as OptionsPickerView<Any>?)!!.setPicker(mRoomList as List<Any>?)
    }

    //关于弹框
    private fun toAbout() {
        if (dialog == null) {
            dialog = CommonDialog(mContext)
        }
        dialog!!.setMessage("1.技术支持:河南省省直能源实业有限公司.\n2.Copyright © 2019 - 2020 河南省省直能源实业有限公司 版权所有.")
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
        SignalRManager.getInstance().close()
    }
}