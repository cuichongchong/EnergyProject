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
     * deviceList : [{"id":36,"name":"503空调总闸","terminalCode":"411701A02002","code":"1001","address":"","moduleControl":{"rows":[{"flag":true,"data":"0xFF00","isCtrl":true,"name":"开","id":2000,"tag":"13"},{"flag":true,"data":"0x00FF","isCtrl":true,"name":"关","id":2001,"tag":"13"}],"seq":2002},"moduleType":"空调-费控表","moduleName":"中电320-空调"},{"id":37,"name":"503空调列表1","terminalCode":"411701A02002","code":"1002","address":"","moduleControl":{"rows":[{"flag":true,"data":"0xFF00","isCtrl":true,"name":"空调开","id":2000,"tag":"1"},{"flag":true,"data":"0x00FF","isCtrl":true,"name":"空调关","id":2001,"tag":"1"},{"flag":false,"data":"","isCtrl":false,"name":"温度","id":2002,"tag":"13"}],"seq":2003},"moduleType":"空调-空调面板","moduleName":"春泉温控面板"},{"id":38,"name":"503空调列表2","terminalCode":"411701A02002","code":"1003","address":"","moduleControl":{"rows":[{"flag":true,"data":"0xFF00","isCtrl":true,"name":"空调开","id":2000,"tag":"1"},{"flag":true,"data":"0x00FF","isCtrl":true,"name":"空调关","id":2001,"tag":"1"},{"flag":false,"data":"","isCtrl":false,"name":"温度","id":2002,"tag":"13"}],"seq":2003},"moduleType":"空调-空调面板","moduleName":"春泉温控面板"},{"id":39,"name":"503插座总闸","terminalCode":"411701A02002","code":"1004","address":"","moduleControl":{"rows":[{"flag":true,"data":"0xFF00","isCtrl":true,"name":"开","id":2000,"tag":"13"},{"flag":true,"data":"0x00FF","isCtrl":true,"name":"关","id":2001,"tag":"13"}],"seq":2002},"moduleType":"插座-费控表","moduleName":"中电320-插座"},{"id":40,"name":"503插座列表1","terminalCode":"411701A02002","code":"1005","address":"","moduleControl":{"rows":[{"flag":true,"data":"0xFF00","isCtrl":true,"name":"开","id":2000,"tag":"1"},{"flag":true,"data":"0x00FF","isCtrl":true,"name":"关","id":2001,"tag":"1"},{"flag":false,"data":"","isCtrl":false,"name":"读","id":2002,"tag":"2"}],"seq":2003},"moduleType":"插座-插座面板","moduleName":"春泉云插座"},{"id":41,"name":"503插座列表2","terminalCode":"411701A02002","code":"1006","address":"","moduleControl":{"rows":[{"flag":true,"data":"0xFF00","isCtrl":true,"name":"开","id":2000,"tag":"1"},{"flag":true,"data":"0x00FF","isCtrl":true,"name":"关","id":2001,"tag":"1"},{"flag":false,"data":"","isCtrl":false,"name":"读","id":2002,"tag":"2"}],"seq":2003},"moduleType":"插座-插座面板","moduleName":"春泉云插座"},{"id":42,"name":"503照明总闸","terminalCode":"411701A02002","code":"1007","address":"","moduleControl":{"rows":[{"flag":true,"data":"0xFF00","name":"开","id":2000,"tag":"13"},{"flag":true,"data":"0x00FF","name":"关","id":2001,"tag":"13"}],"seq":2002},"moduleType":"照明-费控表","moduleName":"中电320-照明"}]
     */

    private String todayEle;
    private String monthEle;
    private String currentPower;
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

    public String getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(String currentPower) {
        this.currentPower = currentPower;
    }

    public List<DeviceListBean> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceListBean> deviceList) {
        this.deviceList = deviceList;
    }

    public static class DeviceListBean {
        /**
         * id : 36
         * name : 503空调总闸
         * terminalCode : 411701A02002
         * code : 1001
         * address :
         * moduleControl : {"rows":[{"flag":true,"data":"0xFF00","isCtrl":true,"name":"开","id":2000,"tag":"13"},{"flag":true,"data":"0x00FF","isCtrl":true,"name":"关","id":2001,"tag":"13"}],"seq":2002}
         * moduleType : 空调-费控表
         * moduleName : 中电320-空调
         */

        private int id;
        private String name;
        private String terminalCode;
        private String code;
        private String address;
        private ModuleControlBean moduleControl;
        private String moduleType;
        private String moduleName;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public ModuleControlBean getModuleControl() {
            return moduleControl;
        }

        public void setModuleControl(ModuleControlBean moduleControl) {
            this.moduleControl = moduleControl;
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

        public static class ModuleControlBean {
            /**
             * rows : [{"flag":true,"data":"0xFF00","isCtrl":true,"name":"开","id":2000,"tag":"13"},{"flag":true,"data":"0x00FF","isCtrl":true,"name":"关","id":2001,"tag":"13"}]
             * seq : 2002
             */

            private int seq;
            private List<RowsBean> rows;

            public int getSeq() {
                return seq;
            }

            public void setSeq(int seq) {
                this.seq = seq;
            }

            public List<RowsBean> getRows() {
                return rows;
            }

            public void setRows(List<RowsBean> rows) {
                this.rows = rows;
            }

            public static class RowsBean {
                /**
                 * flag : true
                 * data : 0xFF00
                 * isCtrl : true
                 * name : 开
                 * id : 2000
                 * tag : 13
                 */

                private boolean flag;
                private String data;
                private boolean isCtrl;
                private String name;
                private int id;
                private String tag;

                public boolean isFlag() {
                    return flag;
                }

                public void setFlag(boolean flag) {
                    this.flag = flag;
                }

                public String getData() {
                    return data;
                }

                public void setData(String data) {
                    this.data = data;
                }

                public boolean isIsCtrl() {
                    return isCtrl;
                }

                public void setIsCtrl(boolean isCtrl) {
                    this.isCtrl = isCtrl;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
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
}
