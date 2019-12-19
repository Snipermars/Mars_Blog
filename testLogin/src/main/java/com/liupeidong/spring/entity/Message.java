package com.liupeidong.spring.entity;

import java.io.Serializable;

/**
 * @creator liupeidong
 * @createTime 2019-11-27 17:31
 * @description
 **/
public class Message implements Serializable {
    private boolean success;//是否成功
    private String errorCode;//错误代码
    private String msg;//返回信息
    private Object data;//封装返回数据

    public Message(ResponseCodeEnum responseCodeEnum, Object object){
        this.errorCode = responseCodeEnum.getCode();
        this.msg = responseCodeEnum.getMsg();
        this.data = object;
        if(this.errorCode.equals(ResponseCodeEnum.RETURN_CODE_100200.getCode())){
            this.success = true;
        } else {
            this.success = false;
        }
    }

    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
