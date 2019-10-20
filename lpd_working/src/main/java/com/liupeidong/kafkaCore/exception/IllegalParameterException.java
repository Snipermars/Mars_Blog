package com.liupeidong.kafkaCore.exception;

/**
 * IllegalParameterException class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:42
 */
public class IllegalParameterException extends BaseRuntimeException {

    public IllegalParameterException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public IllegalParameterException(int errorCode, String msg, Throwable cause) {
        super(errorCode, msg, cause);
    }

    public IllegalParameterException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IllegalParameterException(String msg) {
        super(msg);
    }

}
