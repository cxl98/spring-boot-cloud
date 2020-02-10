package com.cxl.cloud.common.util;

import java.util.HashMap;
import java.util.Map;

public class ResultT extends HashMap<String,Object> {
    private static final long serialVersionUID=108L;
    private int code;
    private String msg;
    public ResultT(){
        put("code",0);
    }

    public static ResultT fail(){
        return fail(500,"请联系管理员");
    }

    private static ResultT fail( String msg) {
        return fail(500,msg);
    }
    public static ResultT fail(int code,String msg){
        ResultT resultT=new ResultT();
        resultT.put("code",code);
        resultT.put("msg",msg);
        return resultT;
    }

    public static ResultT success(Object msg){
        ResultT resultT=new ResultT();
        resultT.put("msg",msg);
        return resultT;
    }

    public static ResultT success(Map<String,Object> map){
        ResultT resultT=new ResultT();
        resultT.putAll(map);
        return resultT;
    }

    public static ResultT success(){
        return new ResultT();
    }

    @Override
    public ResultT put(String key, Object value) {
        super.put(key,value);
        return this;
    }
}
