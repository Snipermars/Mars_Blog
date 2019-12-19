package com.liupeidong.spring.entity;

import java.util.List;

/**
 * @creator liupeidong
 * @createTime 2019-11-27 17:15
 * @description
 **/
public class User {
    private static final long serialVersionUID = 8656128523789047171L;

    private String username;//用户名
    private String realname;//用户真实姓名
    private String pwd;//登录密码
    // 角色列表
    // 菜单列表
//
//    public List getMenuList() {
//        return menuList;
//    }
//
//    public void setMenuList(List menuList) {
//        this.menuList = menuList;
//    }

    public List getRoleList() {
        return roleList;
    }

    public void setRoleList(List roleList) {
        this.roleList = roleList;
    }

    private List roleList;//角色列表

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
