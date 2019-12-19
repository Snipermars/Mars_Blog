package com.liupeidong.spring.entity;

/**
 * @creator liupeidong
 * @createTime 2019-11-27 17:31
 * @description
 **/
public enum ResponseCodeEnum {
    /**
     * 定义返回码
     * */

    RETURN_CODE_100001("100001","用户名或密码错误"),
    RETURN_CODE_100002("100002","未登录"),
    RETURN_CODE_100003("100003","权限不足"),
    RETURN_CODE_100004("100004","入参错误"),
    RETURN_CODE_100005("100005","该职位未关联项目"),

    RETURN_CODE_100007("100007","OAMUID不正确或解密失败"),
    RETURN_CODE_100011("100011","解密的OAMUID为空"),
    RETURN_CODE_100008("100008","调用OSP用户接口报错"),
    RETURN_CODE_100010("100010","获取osp密码为空"),
    RETURN_CODE_100009("100009","获取Tonken报错"),
    RETURN_CODE_100200("100200","请求成功"),
    RETURN_CODE_100500("100500","系统异常");


    private String code;
    private String msg;
    private ResponseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public String getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
