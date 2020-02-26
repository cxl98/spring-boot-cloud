package com.cxl.cloud.common.util;

public class MD5Utils {
    /**
     * byte[]字节数组 转换成 十六进制字符串
     * @param arr 要转换的byte[]字节数组
     * @return String 返回十六进制字符串
     */

    public static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = arr.length; i < length; i++) {
            sb.append(Integer.toHexString((arr[i] & 0xFF)|0x100), 1, 3);
        }
        return sb.toString();
    }

}
