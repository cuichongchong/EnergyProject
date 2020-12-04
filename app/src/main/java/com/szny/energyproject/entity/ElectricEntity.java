package com.szny.energyproject.entity;

import java.util.List;

/**
 * 首页电量展示实体，测试使用
 * */
public class ElectricEntity {
    private List<ElectricListBean> electric_list;

    public List<ElectricListBean> getElectric_list() {
        return electric_list;
    }

    public void setElectric_list(List<ElectricListBean> electric_list) {
        this.electric_list = electric_list;
    }

    public static class ElectricListBean {
        private String name;
        private String value;

        public ElectricListBean(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
