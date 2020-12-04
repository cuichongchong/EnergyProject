package com.szny.energyproject.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.CompoundButton
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
import com.szny.energyproject.base.BaseActivity
import com.szny.energyproject.base.RecyclerViewDivider
import com.szny.energyproject.entity.AirCondEntity
import com.szny.energyproject.entity.ElectricEntity
import com.szny.energyproject.utils.DensityUtil
import com.szny.energyproject.utils.ToastUtils
import com.szny.energyproject.widget.CommonDialog
import kotlinx.android.synthetic.main.activity_controller.*

/**
 * 控制管理页面
 * */
class ControllerActivity : BaseActivity(), View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private lateinit var electricAdapter:ElectricAdapter
    private lateinit var electricList:MutableList<ElectricEntity.ElectricListBean>

    private lateinit var airCondAdapter:AirCondAdapter
    private lateinit var airCondList:MutableList<AirCondEntity>

    private var dialog: CommonDialog? = null

    //房间选择器
    private var mRoomList = arrayListOf<String>()
    private var mRoomPickerView: OptionsPickerView<*>? = null

    //模拟空调开关状态
    private var isAirOpen = false

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
    }

    private fun initEvent() {
        iv_about.setOnClickListener(this)
        tv_more.setOnClickListener(this)
        tv_name.setOnClickListener(this)
        iv_change.setOnClickListener(this)
        set_bulb.setOnCheckedChangeListener(this)
        set_socket.setOnCheckedChangeListener(this)
        set_air.setOnCheckedChangeListener(this)
    }

    private fun initData() {
        //添加电量模拟数据
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
        }
    }

    override fun onCheckedChanged(v: CompoundButton, b: Boolean) {
        when(v.id){
            //照明开关切换
            R.id.set_bulb ->{
                if(b){
                    tv_bulb.text = "合闸"
                }else{
                    tv_bulb.text = "分闸"
                    tv_bulb_status.text = "灯熄"
                }
            }
            //插座开关切换
            R.id.set_socket ->{
                if(b){
                    tv_socket.text = "合闸"
                }else{
                    tv_socket.text = "分闸"
                    tv_socket_status.text = "待机中"
                }
            }
            //空调开关切换
            R.id.set_air ->{
                //改变空调总开关在adapter状态
                isAirOpen = b
                airCondAdapter.isGate = b

                if(b){
                    tv_air.text = "合闸"
                }else{
                    tv_air.text = "分闸"
                    //修改每一个空调的开关状态
                    airCondAdapter.data.forEach {
                        if(it.isOpen){
                            it.isOpen = false
                        }
                    }
                }
                airCondAdapter.notifyDataSetChanged()
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
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }
}