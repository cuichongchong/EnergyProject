package com.szny.energyproject.entity;

public class UserEntity {

    /**
     * userName : njj
     * id : 7
     * roleId : 5
     */

    private String userName;
    private int id;
    private int roleId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
