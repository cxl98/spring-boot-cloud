package com.cxl.cloud.common.exception;

import lombok.Data;

/**
 * 自定义异常
 */
@Data
public class CException extends RuntimeException {
    private static final long serialVersionUID=98L;

    private String msg;

    private int code=500;

    public CException(String msg) {
        this.msg = msg;
    }

    public CException(String msg, Throwable cause) {
        super(msg,cause);
        this.msg = msg;
    }

    public CException(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

}
