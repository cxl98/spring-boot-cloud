package com.cxl.cloud.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MD5Utils {
    /**
     * byte[]字节数组 转换成 十六进制字符串
     *
     * @param arr 要转换的byte[]字节数组
     * @return String 返回十六进制字符串
     */

    public static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = arr.length; i < length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString();
    }

    /**
     * MD5加密,并把结果由字节数组转换成十六进制字符串
     *
     * @param string 要加密的内容
     * @return String 返回加密后的十六进制字符串
     */
    private static String md5Hex(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(string.getBytes());
            return hex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 生成含有随机盐的密码
     *
     * @param passWord 要加密的密码
     * @return String 含有随机盐的密码
     */
    public static String getSaltMD5(String passWord) {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer(16);
        buffer.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int len = buffer.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                buffer.append("0");
            }
        }
        // 生成最终的加密盐
        String salt = buffer.toString();
        passWord = md5Hex(passWord + salt);
        char[] ch = new char[48];
        for (int i = 0; i < 48; i += 3) {
            ch[i] = passWord.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            ch[i + 1] = c;
            ch[i + 2] = passWord.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(ch);
    }

    /**
     * 验证加盐后是否和原密码一致
     *
     * @param passWord 原密码
     * @param passWord 加密之后的密码
     * @return boolean true表示和原密码一致 false表示和原密码不一致
     */
    public static boolean getSalivaryMD5(String passWord, String md5str) {
        char[] c1 = new char[32];
        char[] c2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            c1[i / 3 * 2] = md5str.charAt(i);
            c1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
            c2[i / 3] = md5str.charAt(i + 1);
        }
        String salt = new String(c2);
        return md5Hex(passWord + salt).equals(String.valueOf(c1));
    }

    public static void main(String[] args) {
        String plaintext = "admin";
        String hex = MD5Utils.getSaltMD5(plaintext);
        System.out.println("加盐后的MD5" + hex);
        boolean saltverifyMD5 = MD5Utils.getSalivaryMD5(plaintext, hex);
        System.out.println(saltverifyMD5);
    }
}
