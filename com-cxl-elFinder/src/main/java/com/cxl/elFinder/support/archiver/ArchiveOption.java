package com.cxl.elFinder.support.archiver;

import com.alibaba.fastjson.JSONObject;

public enum  ArchiveOption {

    INSTANCE;//实例

    public static JSONObject JSON_INSTANCE(){
        JSONObject instance=new JSONObject();
        instance.put("create",getCreate());//创建
        instance.put("extract",getExtract());//拿
        return instance;
    }

    private static String[] getCreate() {
        return ArchiveType.SUPPORT_MIME_TYPES;
    }

    private static String[] getExtract() {
        return ArchiveType.SUPPORT_MIME_TYPES;
    }

}
