package com.cxl.cloud.common.util;

import java.util.HashMap;
import java.util.Map;

public class ReturnT extends HashMap<String,Object> {
    private static final long serialVersionUID=108L;
    private int code;
    private String msg;
    public ReturnT(){
        put("code",0);
    }

    public static ReturnT fail(){
        return fail(500,"请联系管理员");
    }

    public static ReturnT fail(String msg) {
        return fail(500,msg);
    }
    public static ReturnT fail(int code, String msg){
        ReturnT returnT =new ReturnT();
        returnT.put("code",code);
        returnT.put("msg",msg);
        return returnT;
    }

    public static ReturnT success(Object msg){
        ReturnT returnT =new ReturnT();
        returnT.put("msg",msg);
        return returnT;
    }

    public static ReturnT success(Map<String,Object> map){
        ReturnT returnT =new ReturnT();
        returnT.putAll(map);
        return returnT;
    }

    public static ReturnT success(){
        return new ReturnT();
    }

    @Override
    public ReturnT put(String key, Object value) {
        super.put(key,value);
        return this;
    }
}
