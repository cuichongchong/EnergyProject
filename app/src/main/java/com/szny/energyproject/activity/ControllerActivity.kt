package com.szny.energyproject.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.gyf.barlibrary.ImmersionBar
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szny.energyproject.R
import com.szny.energyproject.adapter.AirCondAdapter
import com.szny.energyproject.adapter.SocketAdapter
import com.szny.energyproject.base.BaseActivity
import com.szny.energyproject.base.RecyclerViewDivider
import com.szny.energyproject.entity.ControlEntity
import com.szny.energyproject.entity.MessageBean
import com.szny.energyproject.entity.RoomEntity
import com.szny.energyproject.mvp.exceptions.BaseException
import com.szny.energyproject.mvp.iviews.IControlView
import com.szny.energyproject.mvp.persenters.ControlPresenter
import com.szny.energyproject.utils.DensityUtil
import com.szny.energyproject.utils.LogUtils
import com.szny.energyproject.utils.ToastUtils
import com.szny.energyproject.widget.CommonDialog
import com.szny.energyproject.widget.SearchableSpinner
import com.szny.energyproject.widget.SignalRManager
import kotlinx.android.synthetic.main.activity_controller.*
import java.math.BigDecimal

/**
 * 实时控制页面
 * */
class ControllerActivity : BaseActivity(), View.OnClickListener, OnRefreshListener,IControlView {

    private lateinit var airCondAdapter:AirCondAdapter
    private lateinit var socketAdapter:SocketAdapter

    private var dialog: CommonDialog? = null

    //用户id
    private var userId = 0

    //房间选择器
    private var mRoomList = arrayListOf<String>()

    //初始化空调开关状态
    private var isAirOpen = false
    //初始化照明开关状态
    private var isBulbOpen = false
    //初始化插座开关状态
    private var isSocketOpen = false
    //初始化商铺总开关状态,只有商品角色进来时才有
    private var isShopOpen = false

    //空调总闸实体
    private lateinit var airGateList:MutableList<ControlEntity.DeviceListBean>
    private lateinit var airGateEntity:ControlEntity.DeviceListBean
    private var airPower = 0.00 //空调总闸的功率
    //空调列表
    private lateinit var airList: MutableList<ControlEntity.DeviceListBean>

    //插座总闸实体
    private lateinit var socketGateList:MutableList<ControlEntity.DeviceListBean>
    private lateinit var socketGateEntity:ControlEntity.DeviceListBean
    private var socketPower = 0.00 //插座总闸的功率
    //插座列表
    private lateinit var socketList:MutableList<ControlEntity.DeviceListBean>

    //照明总闸实体
    private lateinit var bulbGateList:MutableList<ControlEntity.DeviceListBean>
    private lateinit var bulbGateEntity:ControlEntity.DeviceListBean
    private var bulbPower = 0.00 //照明总闸的功率

    private lateinit var presenter: ControlPresenter

    //signal是否连接
    private var isSignalConn = false
    private var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        ImmersionBar.with(this).init()

        initView()
        initData()
        initEvent()
    }

    private fun initView() {
        toolbar.setNavigationIcon(R.mipmap.ic_back_white)
        toolbar.setNavigationOnClickListener { view: View? -> finish() }
        toolbar_title.text = "实时控制"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val tbLinearParams = toolbar.layoutParams //取控件textView当前的布局参数
            toolbar.setPadding(0, DensityUtil.dip2px(this, 20f),                                                                 0, 0)//4个参数按顺序分别是左上右下
            tbLinearParams.height = DensityUtil.dip2px(this, 69f)// 控件的高
            toolbar.layoutParams = tbLinearParams //使设置好的布局参数应用到控件
        }

        presenter = ControlPresenter()
        presenter.attachView(this)

        initSignal()
    }

    private fun initEvent() {
        //srl.setOnRefreshListener(this)
        iv_bulb.setOnClickListener(this)
        iv_socket.setOnClickListener(this)
        iv_air.setOnClickListener(this)
        iv_master.setOnClickListener(this)
    }

    private fun initData() {
        //接收userId
        userId = intent.getIntExtra("userId", 0)

        //获取房间列表
        presenter.getMember(userId,24)

        //空调列表
        airList = arrayListOf()
        airCondAdapter = AirCondAdapter(mContext,airList,isAirOpen)
        rv_air_conditioner.layoutManager = LinearLayoutManager(mContext)
        val divider = RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL)
        divider.setmItemSize(DensityUtil.dip2px(mContext,10f))
        divider.setColor(R.color.color_f5f5f5)
        rv_air_conditioner.addItemDecoration(divider)
        rv_air_conditioner.adapter = airCondAdapter

        //插座列表
        socketList = arrayListOf()
        socketAdapter = SocketAdapter(mContext,socketList,isSocketOpen)
        rv_socket.layoutManager = LinearLayoutManager(mContext)
        rv_socket.addItemDecoration(divider)
        rv_socket.adapter = socketAdapter

        setUpListener()
    }

    //初始化signal
    private fun initSignal() {
        //初始化开启连接
        SignalRManager.getInstance().init()

        //监听连接返回的结果
        SignalRManager.getInstance().setConnectCallBack {
            isSignalConn = it
        }

        //监听接收signalR
        SignalRManager.getInstance().setIcallBack { item ->
            val str = Gson().toJson(item)
            LogUtils.e("doing", "SignalR 接收成功 $str")
            if(item.Code == -1){
                ToastUtils.showShort(mContext,"请求出错")
            }

            //读取实时温度的返回
            if(item.command == "read_finish"){
                val messageBean = Gson().fromJson(Gson().toJson(item.message),MessageBean::class.java)
                LogUtils.e("doing", "实时温度：${messageBean.data}")
                //更新实时温度
                airCondAdapter.data.forEach {
                    it.temp = messageBean.data.toDouble()
                }
                //切换到主线程进行空调列表实时温度刷新
                runOnUiThread {
                    airCondAdapter.notifyDataSetChanged()
                    ToastUtils.showShort(mContext,"更新完成")
                }
            }
            //恢复空调面板的开关由自身回调控制
            airCondAdapter.isGateControl = false
        }
    }

    //下拉刷新回调
    override fun onRefresh(refreshlayout: RefreshLayout) {
    }

    //写控制writeSignal方法
    private fun writeSignal(collector:String,index:Int,tag:String,value:String){
        try {
            if(isSignalConn){
                //发送signalR
                SignalRManager.getInstance().hubConnection.send("WriteTag", collector,index,tag,value)
                LogUtils.e("doing","SignalR 发送成功")
            }else{
                ToastUtils.showShort(mContext,"SignalR连接失败")
            }
        } catch (e: Exception) {
            LogUtils.e("doing","SignalR 异常")
            e.printStackTrace()
        }
    }

    //读readSignal方法
    private fun readSignal(collector:String,index:Int,tag:String){
        try {
            if(isSignalConn){
                //发送signalR
                SignalRManager.getInstance().hubConnection.send("ReadTag", collector,index,tag)
                LogUtils.e("doing","SignalR 发送成功")
            }else{
                ToastUtils.showShort(mContext,"SignalR连接失败")
            }
        } catch (e: Exception) {
            LogUtils.e("doing","SignalR 异常")
            e.printStackTrace()
        }
    }

    //房间列表获取成功
    override fun success(data: MutableList<RoomEntity>) {
        if(data.size > 0){
            //请求获取首页信息接口
            presenter.getInfo(userId,data[data.size-1].id)
            //presenter.getInfo(userId,data[0].id)

            //设置房间列表选择数据
            mRoomList.clear()
            /*for(item in data){
                mRoomList.add(item.name)
            }*/
            for (i in data.indices.reversed()) {
                mRoomList.add(data[i].name)
            }
            val adapter = ArrayAdapter<String>(this, R.layout.item_spinner)
            adapter.addAll(mRoomList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            search_spinner.adapter = adapter
            search_spinner.setTitle("选择房间")
            search_spinner.setPositiveButton("关闭")
            search_spinner.setOnSearchableItemClickListener(
                SearchableSpinner.SearchableItem<String> { item: Any ->
                    Log.e("doing","选择了 $item")
                    for (entity in data){
                        if(item == entity.name){
                            presenter.getInfo(userId,entity.id)
                        }
                    }
                } as SearchableSpinner.SearchableItem<*>
            )
        }
    }

    //首页信息成功
    override fun getInfo(data: ControlEntity) {
        airGateList = arrayListOf()
        airCondAdapter.clearAllData()
        socketGateList = arrayListOf()
        socketAdapter.clearAllData()
        bulbGateList = arrayListOf()

        for(item in data.deviceList){
            when(item.moduleType){
                //获取空调总闸列表
                "空调-费控表" ->{
                    airGateList.add(item)
                }
                //获取空调列表
                "空调-空调面板" ->{
                    //初始化空调状态
                    item.moduleControl.forEach {
                        when(it.name){
                            "运行状态" ->{
                                item.runStatus = it.tagValue.toInt()
                                item.runOldStatus = it.tagValue.toInt()
                            }
                            "运行模式" ->{
                                item.runMode = it.tagValue.toInt()
                            }
                            "显示温度" ->{
                                item.temp = it.tagValue.toDouble()
                                item.setTemp = it.tagValue.toDouble()
                            }
                            "当前风速"->{
                                item.windSped = it.tagValue.toInt()
                            }
                        }
                    }
                    airList.add(item)
                    airCondAdapter.notifyDataSetChanged()
                }
                //获取插座总闸列表
                "插座-费控表" ->{
                    socketGateList.add(item)
                }
                //获取插座列表
                "插座-插座面板"->{
                    socketList.add(item)
                    socketAdapter.notifyDataSetChanged()
                }
                //获取照明总闸列表
                "照明-费控表" ->{
                    bulbGateList.add(item)
                }
            }
        }

        //获取空调总闸实体，若有多个总闸，只取第一个
        if(airGateList.size > 0){
            airGateEntity = airGateList[0]
            airGateEntity.moduleControl.forEach {
                //初始化空调总闸状态
                when(it.name){
                    "开关状态" ->{
                        if(it.tagValue == "1"){
                            isAirOpen = true
                            iv_air.setBackgroundResource(R.mipmap.ic_switch_open)
                            tv_air.text = "合闸"
                        }else if(it.tagValue == "0"){
                            isAirOpen = false
                            iv_air.setBackgroundResource(R.mipmap.ic_switch_close)
                            tv_air.text = "分闸"
                        }
                        airCondAdapter.isGate = isAirOpen
                    }
                    "有功功率" ->{
                        airPower = it.tagValue.toDouble()
                    }
                }
            }
            ll_air_gate.visibility = View.VISIBLE
            if(airCondAdapter.data.size > 0){
                ll_air.visibility = View.VISIBLE
            }else{
                ll_air.visibility = View.GONE
            }
        }else{
            ll_air_gate.visibility = View.GONE
            ll_air.visibility = View.GONE
        }

        //获取插座总闸实体，若有多个总闸，只取第一个
        if(socketGateList.size > 0){
            socketGateEntity = socketGateList[0]
            socketGateEntity.moduleControl.forEach {
                //初始化插座总闸状态
                when(it.name){
                    "开关状态" ->{
                        if(it.tagValue == "1"){
                            isSocketOpen = true
                            iv_socket.setBackgroundResource(R.mipmap.ic_switch_open)
                            tv_socket.text = "合闸"
                            tv_socket_status.text = "工作中"
                        }else if(it.tagValue == "0"){
                            isSocketOpen = false
                            iv_socket.setBackgroundResource(R.mipmap.ic_switch_close)
                            tv_socket.text = "分闸"
                            tv_socket_status.text = "待机中"
                        }
                    }
                    "有功功率" ->{
                        socketPower = it.tagValue.toDouble()
                    }
                }
            }
            ll_socket_gate.visibility = View.VISIBLE
            if(socketAdapter.data.size > 0){
                ll_socket.visibility = View.VISIBLE
            }else{
                ll_socket.visibility = View.GONE
            }
        }else{
            ll_socket_gate.visibility = View.GONE
            ll_socket.visibility = View.GONE
        }

        //获取照明总闸实体，若有多个总闸，只取第一个
        if(bulbGateList.size > 0){
            bulbGateEntity = bulbGateList[0]
            bulbGateEntity.moduleControl.forEach {
                //初始化照明总闸状态
                when(it.name){
                    "开关状态" ->{
                        if(it.tagValue == "1"){
                            isBulbOpen = true
                            iv_bulb.setBackgroundResource(R.mipmap.ic_switch_open)
                            tv_bulb.text = "合闸"
                            tv_bulb_status.text = "灯亮"
                        }else if(it.tagValue == "0"){
                            isBulbOpen = false
                            iv_bulb.setBackgroundResource(R.mipmap.ic_switch_close)
                            tv_bulb.text = "分闸"
                            tv_bulb_status.text = "灯熄"
                        }
                    }
                    "有功功率" ->{
                        bulbPower = it.tagValue.toDouble()
                    }
                }
            }
            ll_bulb_gate.visibility = View.VISIBLE
        }else{
            ll_bulb_gate.visibility = View.GONE
        }

        //获取房间总功率
        val bg = BigDecimal(airPower + socketPower + bulbPower)
        val totalPower: Double = bg.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

    }

    override fun onClick(view: View) {
        when(view.id){
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
            val item =  airCondAdapter.data[position]
            var temp = item.setTemp
            //获取控制的tag值
            var openTag = ""//空调开关的tag
            var modeTag = ""//制冷制热的tag
            var windTag = ""//风速大小的tag
            var tempTag = ""//设定温度的tag
            var indoorTempTag = ""//室内温度的tag
            item.moduleControl.forEach {
                when(it.name){
                    "运行状态" ->{
                        openTag = getControlTag(it.tag)
                    }
                    "运行模式" ->{
                        modeTag = getControlTag(it.tag)
                    }
                    "设定风速" ->{
                        windTag = getControlTag(it.tag)
                    }
                    "设定温度" ->{
                        tempTag = getControlTag(it.tag)
                    }
                    "显示温度" ->{
                        indoorTempTag = getControlTag(it.tag)
                    }
                }
            }
            when(v.id){
                //设置空调开关，0-关机，1-开机
                R.id.set_switch ->{
                    if(checked){
                        item.runStatus = 1
                        item.runOldStatus = 1
                        LogUtils.e("doing","空调开")
                        writeSignal(item.terminalCode,index++,item.code+openTag,"1")
                    }else{
                        item.runStatus = 0
                        item.runOldStatus = 0
                        LogUtils.e("doing","空调关")
                        writeSignal(item.terminalCode,index++,item.code+openTag,"0")
                    }
                }
                //设置模式 1-制冷，4-制热
                R.id.tv_set_model ->{
                    if(item.runStatus == 1){
                        if(item.runMode == 4){
                            LogUtils.e("doing","制冷")
                            writeSignal(item.terminalCode,index++,item.code+modeTag,"1")

                            item.runMode = 1
                        }else if(item.runMode == 1){
                            LogUtils.e("doing","制热")
                            writeSignal(item.terminalCode,index++,item.code+modeTag,"4")

                            item.runMode = 4
                        }
                    }else{
                        ToastUtils.showShort(mContext,"请先打开空调开关")
                    }
                }
                //设置风速 0-自动，1-低档，2-中档，3-高档
                R.id.tv_set_gear ->{
                    if(item.runStatus == 1){
                        when(item.windSped){
                            0 ->{
                                item.windSped = 1
                            }
                            1 ->{
                                item.windSped = 2
                            }
                            2 ->{
                                item.windSped = 3
                            }
                            3 ->{
                                item.windSped = 0
                            }
                        }
                        LogUtils.e("doing","档位：${item.windSped.toString()}")
                        writeSignal(item.terminalCode,index++,item.code+windTag,item.windSped.toString())
                    }else{
                        ToastUtils.showShort(mContext,"请先打开空调开关")
                    }
                }
                //温度减,最低5
                R.id.iv_desc ->{
                    if(item.runStatus == 1){
                        if(temp > 5){
                            temp -= 1
                            item.setTemp = temp
                            LogUtils.e("doing","设定温度：${temp.toString()}")
                            writeSignal(item.terminalCode,index++,item.code+tempTag,temp.toString())
                        }else{
                            ToastUtils.showShort(mContext,"最低为5℃")
                        }
                    }else{
                        ToastUtils.showShort(mContext,"请先打开空调开关")
                    }
                }
                //温度加,最高40
                R.id.iv_add ->{
                    if(item.runStatus == 1){
                        if(temp < 40){
                            temp += 1
                            item.setTemp = temp
                            LogUtils.e("doing","设定温度：${temp.toString()}")
                            writeSignal(item.terminalCode,index++,item.code+tempTag,temp.toString())
                        }else{
                            ToastUtils.showShort(mContext,"最高为40℃")
                        }
                    }else{
                        ToastUtils.showShort(mContext,"请先打开空调开关")
                    }
                }
                //刷新室内温度
                R.id.tv_refresh ->{
                    //获取室内温度
                    readSignal(item.terminalCode,index++,item.code+indoorTempTag)
                }
            }

            if(rv_air_conditioner.isComputingLayout){
                rv_air_conditioner.post {
                    airCondAdapter.notifyDataSetChanged()
                }
            }else{
                airCondAdapter.notifyDataSetChanged()
            }
        }

        //插座适配器的点击事件
        socketAdapter.setOnItemsClickListener { v, position ->
            val entity = socketAdapter.data[position]
            when(v.id){
                /*R.id.iv_socket ->{
                    if(entity.isOpen){
                        //进行分闸
                        entity.isOpen = false
                        entity.statue = "待机中"
                    }else{
                        //进行合闸
                        entity.isOpen = true
                        entity.statue = "工作中"
                    }
                    socketAdapter.notifyDataSetChanged()
                }*/
            }
        }
    }

    //获取控制指令中的tag
    private fun getControlTag(tag:String):String{
        var returnTag = ""
        if(tag.isNotEmpty()){
            when (tag.length) {
                3 -> {
                    returnTag = tag
                }
                2 -> {
                    returnTag = "0$tag"
                }
                1 -> {
                    returnTag = "00$tag"
                }
            }
        }
        return returnTag
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
                            //获取控制照明总闸的tag值/data值
                            var openBulbTag = ""
                            var openData = ""
                            var closeBulbTag = ""
                            var closeData = ""
                            bulbGateEntity.moduleControl.forEach {
                                when(it.name){
                                    "开" ->{
                                        openBulbTag = getControlTag(it.tag)
                                        openData = it.data
                                    }
                                    "关" ->{
                                        closeBulbTag = getControlTag(it.tag)
                                        closeData = it.data
                                    }
                                }
                            }
                            if(isOpen){
                                isBulbOpen = false
                                iv_bulb.setBackgroundResource(R.mipmap.ic_switch_close)
                                tv_bulb.text = "分闸"
                                tv_bulb_status.text = "灯熄"

                                LogUtils.e("doing","灯熄")
                                writeSignal(bulbGateEntity.terminalCode,index++,bulbGateEntity.code+closeBulbTag,closeData)
                            }else{
                                isBulbOpen = true
                                iv_bulb.setBackgroundResource(R.mipmap.ic_switch_open)
                                tv_bulb.text = "合闸"
                                tv_bulb_status.text = "灯亮"

                                LogUtils.e("doing","灯亮")
                                writeSignal(bulbGateEntity.terminalCode,index++,bulbGateEntity.code+openBulbTag,openData)
                            }
                        }
                        //表示插座总闸确认
                        2 ->{
                            //获取控制插座总闸的tag值/data值
                            var openSocketTag = ""
                            var openData = ""
                            var closeSocketTag = ""
                            var closeData = ""
                            socketGateEntity.moduleControl.forEach {
                                when(it.name){
                                    "开" ->{
                                        openSocketTag = getControlTag(it.tag)
                                        openData = it.data
                                    }
                                    "关" ->{
                                        closeSocketTag = getControlTag(it.tag)
                                        closeData = it.data
                                    }
                                }
                            }
                            if(isOpen){
                                isSocketOpen = false
                                iv_socket.setBackgroundResource(R.mipmap.ic_switch_close)
                                tv_socket.text = "分闸"
                                tv_socket_status.text = "待机中"

                                //修改插座列表中总闸状态
                                //socketAdapter.isGate = false
                                //修改每一个插座的开关状态
                                /*socketAdapter.data.forEach {
                                    if(it.isOpen){
                                        it.isOpen = false
                                        it.statue = "停止中"
                                    }
                                }*/

                                LogUtils.e("doing","插座待机")
                                writeSignal(socketGateEntity.terminalCode,index++,socketGateEntity.code+closeSocketTag,closeData)
                            }else{
                                isSocketOpen = true
                                iv_socket.setBackgroundResource(R.mipmap.ic_switch_open)
                                tv_socket.text = "合闸"
                                tv_socket_status.text = "工作中"

                                //修改插座列表中总闸状态
                                //socketAdapter.isGate = true

                                LogUtils.e("doing","插座工作")
                                writeSignal(socketGateEntity.terminalCode,index++,socketGateEntity.code+openSocketTag,openData)
                            }
                            //socketAdapter.notifyDataSetChanged()
                        }
                        //表示空调总闸确认
                        3 ->{
                            //表示是空调总闸控制的空调面板的开关
                            airCondAdapter.isGateControl = true

                            //获取控制空调总闸的tag值/data值
                            var openAirTag = ""
                            var openData = ""
                            var closeAirTag = ""
                            var closeData = ""
                            airGateEntity.moduleControl.forEach {
                                when(it.name){
                                    "开" ->{
                                        openAirTag = getControlTag(it.tag)
                                        openData = it.data
                                    }
                                    "关" ->{
                                        closeAirTag = getControlTag(it.tag)
                                        closeData = it.data
                                    }
                                }
                            }
                            if(isOpen){
                                isAirOpen = false
                                iv_air.setBackgroundResource(R.mipmap.ic_switch_close)
                                tv_air.text = "分闸"

                                //修改空调列表中总闸状态
                                airCondAdapter.isGate = false

                                //修改每一个空调的开关状态
                                airCondAdapter.data.forEach {
                                    if(it.runStatus == 1){
                                        it.runStatus = 0
                                    }
                                }

                                LogUtils.e("doing","空调总闸关闭")
                                writeSignal(airGateEntity.terminalCode,index++,airGateEntity.code+closeAirTag,closeData)
                            }else{
                                isAirOpen = true
                                iv_air.setBackgroundResource(R.mipmap.ic_switch_open)
                                tv_air.text = "合闸"

                                //修改空调列表中总闸状态
                                airCondAdapter.isGate = true

                                //修改每一个空调的开关状态
                                airCondAdapter.data.forEach {
                                    it.runStatus = it.runOldStatus
                                    /*if(it.runStatus == 0){
                                        it.runStatus = 1
                                    }*/
                                }

                                LogUtils.e("doing","空调总闸开启")
                                writeSignal(airGateEntity.terminalCode,index++,airGateEntity.code+openAirTag,openData)
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

    override fun failed(e: Throwable) {
        //srl.finishRefresh()
        //token失效，返回登录页面
        if(e is BaseException && e.errCode == 113){
            ToastUtils.showShort(mContext,"验证失效,请重新登录")
        }else{
            ToastUtils.showShort(mContext,"请求失败")
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
        ImmersionBar.with(this).destroy()
        SignalRManager.getInstance().close()
    }
}