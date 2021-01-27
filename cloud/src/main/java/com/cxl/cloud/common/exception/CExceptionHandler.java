package com.cxl.cloud.common.exception;

import com.cxl.cloud.common.util.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CExceptionHandler {

    private static final Logger LOGGER= LoggerFactory.getLogger(CExceptionHandler.class);

    /**
     * 自定义异常
     */
    @ExceptionHandler(CException.class)
    public ReturnT handleCException(CException e){
        ReturnT returnT =new ReturnT();
        returnT.put("code",e.getCode());
        returnT.put("msg",e.getMsg());
        return returnT;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ReturnT handleDuplicateKeyException(DuplicateKeyException e){
        LOGGER.error(e.getMessage(),e);
        return ReturnT.fail();
    }
}
