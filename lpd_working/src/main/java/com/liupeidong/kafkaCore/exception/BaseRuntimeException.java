package com.liupeidong.kafkaCore.exception;

/**
 * BaseRuntimeException class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:38
 */
public class BaseRuntimeException extends RuntimeException{

    private static final long serialVersionUID = 7830353921973771800L;

    protected Integer errorCode;

    public BaseRuntimeException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(int errorCode, String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }


    public BaseRuntimeException(String msg) {
        super(msg);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
