package com.szny.energyproject.entity;

import java.util.List;

/**
 * 控制页面实体
 * */
public class ControlEntity {

    /**
     * todayEle : 0.0
     * monthEle : 0.0
     * currentPower : 0.0
     * deviceList : [{"id":45,"name":"522温控器","terminalCode":"410105A00104","code":"5045","moduleControl":[{"flag":"false","isCtrl":"false","data":"","tagValue":"0","name":"当前风速","tag":"6"},{"flag":"false","isCtrl":"true","data":"","tagValue":"0","name":"设定风速","tag":"5"},{"flag":"false","isCtrl":"true","data":"","tagValue":"0","name":"运行状态","tag":"3"},{"flag":"false","isCtrl":"false","data":"","tagValue":"24.5","name":"显示温度","tag":"4"},{"flag":"false","isCtrl":"true","data":"","tagValue":"4","name":"运行模式","tag":"1"},{"flag":"false","isCtrl":"true","data":"","tagValue":"27","name":"设定温度","tag":"2"}],"moduleType":"空调-空调面板","moduleName":"春泉温控面板"},{"id":46,"name":"522空调","terminalCode":"410105A00104","code":"4030","moduleControl":[{"flag":"false","isCtrl":"false","data":"","tagValue":"221.2514343261719","name":"电压","tag":"1"},{"flag":"false","isCtrl":"false","data":"","tagValue":"0","name":"电流","tag":"2"},{"flag":"false","isCtrl":"false","data":"","tagValue":"0","name":"有功功率","tag":"3"},{"flag":"false","isCtrl":"false","data":"","tagValue":"1.13","name":"有功点能","tag":"4"},{"flag":"true","isCtrl":"true","data":"0xFF00","tagValue":"0","name":"开","tag":"8"},{"flag":"true","isCtrl":"true","data":"0x00FF","tagValue":"0","name":"关","tag":"9"},{"flag":"false","isCtrl":"false","data":"","tagValue":"1","name":"开关状态","tag":"6"}],"moduleType":"空调-费控表","moduleName":"中电320-空调"}]
     */

    private String todayEle;
    private String monthEle;
    private List<DeviceListBean> deviceList;

    public String getTodayEle() {
        return todayEle;
    }

    public void setTodayEle(String todayEle) {
        this.todayEle = todayEle;
    }

    public String getMonthEle() {
        return monthEle;
    }

    public void setMonthEle(String monthEle) {
        this.monthEle = monthEle;
    }

    public List<DeviceListBean> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceListBean> deviceList) {
        this.deviceList = deviceList;
    }

    public static class DeviceListBean {
        /**
         * id : 45
         * name : 522温控器
         * terminalCode : 410105A00104
         * code : 5045
         * moduleControl : [{"flag":"false","isCtrl":"false","data":"","tagValue":"0","name":"当前风速","tag":"6"},{"flag":"false","isCtrl":"true","data":"","tagValue":"0","name":"设定风速","tag":"5"},{"flag":"false","isCtrl":"true","data":"","tagValue":"0","name":"运行状态","tag":"3"},{"flag":"false","isCtrl":"false","data":"","tagValue":"24.5","name":"显示温度","tag":"4"},{"flag":"false","isCtrl":"true","data":"","tagValue":"4","name":"运行模式","tag":"1"},{"flag":"false","isCtrl":"true","data":"","tagValue":"27","name":"设定温度","tag":"2"}]
         * moduleType : 空调-空调面板
         * moduleName : 春泉温控面板
         */

        private int id;
        private String name;
        private String terminalCode;
        private String code;
        private String moduleType;
        private String moduleName;
        private List<ModuleControlBean> moduleControl;

        private int runStatus;//空调开关状态
        private int runOldStatus;//记录空调总闸控制之前空调的开关状态
        private int runMode;//制热制冷模式
        private Double temp;//当前温度
        private Double setTemp;//设置的温度
        private int windSped;//当前档位

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTerminalCode() {
            return terminalCode;
        }

        public void setTerminalCode(String terminalCode) {
            this.terminalCode = terminalCode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getModuleType() {
            return moduleType;
        }

        public void setModuleType(String moduleType) {
            this.moduleType = moduleType;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public List<ModuleControlBean> getModuleControl() {
            return moduleControl;
        }

        public void setModuleControl(List<ModuleControlBean> moduleControl) {
            this.moduleControl = moduleControl;
        }

        public int getRunStatus() {
            return runStatus;
        }

        public void setRunStatus(int runStatus) {
            this.runStatus = runStatus;
        }

        public int getRunMode() {
            return runMode;
        }

        public void setRunMode(int runMode) {
            this.runMode = runMode;
        }

        public Double getTemp() {
            return temp;
        }

        public void setTemp(Double temp) {
            this.temp = temp;
        }

        public Double getSetTemp() {
            return setTemp;
        }

        public void setSetTemp(Double setTemp) {
            this.setTemp = setTemp;
        }

        public int getWindSped() {
            return windSped;
        }

        public void setWindSped(int windSped) {
            this.windSped = windSped;
        }

        public int getRunOldStatus() {
            return runOldStatus;
        }

        public void setRunOldStatus(int runOldStatus) {
            this.runOldStatus = runOldStatus;
        }

        public static class ModuleControlBean {
            /**
             * flag : false
             * isCtrl : false
             * data :
             * tagValue : 0
             * name : 当前风速
             * tag : 6
             */

            private String flag;
            private String isCtrl;
            private String data;
            private String tagValue;
            private String name;
            private String tag;

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }

            public String getIsCtrl() {
                return isCtrl;
            }

            public void setIsCtrl(String isCtrl) {
                this.isCtrl = isCtrl;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getTagValue() {
                return tagValue;
            }

            public void setTagValue(String tagValue) {
                this.tagValue = tagValue;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }
        }
    }
}
