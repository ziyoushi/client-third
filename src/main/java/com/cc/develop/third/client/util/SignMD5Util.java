package com.cc.develop.third.client.util;

import java.security.MessageDigest;

/**
 * com.cc.develop.third.client.util
 *
 * @author changchen
 * @email java@mail.com
 * @date 2020-12-22 10:22:42
 */
public class SignMD5Util {

    public static char newHexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f' };

    public static String getMD5(String s) {
        try {
            byte[] btInput = s.getBytes("UTF-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = newHexDigits[byte0 >>> 4 & 0xf];
                str[k++] = newHexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * sha1消息摘要
     *
     * @param input     输入内容
     * @param algorithm 算法
     * @return 消息摘要后的字符串
     * @throws Exception
     */
    public static String getDigest(String input, String algorithm) throws Exception {
        // 创建消息摘要对象
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        // 执行消息摘要算法
        byte[] digest1 = digest.digest(input.getBytes());
        return toHex(digest1);
    }

    private static String toHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        // 对密文进行迭代
        for (byte b : digest) {
            // 把密文转换成16进制
            String s = Integer.toHexString(b & 0xff);
            // 判断如果密文的长度是1，需要在高位进行补0
            if (s.length() == 1) {
                s = "0" + s;
            }
            sb.append(s);
        }
        // 使用base64进行转码
        return sb.toString();
    }
    /**
     * 根据appId、AppKey、当前时间戳计算数字摘要
     *
     * @param appId
     * @param appKey
     * @param timestamp
     * @return
     */
    public static String getSha1String(String appId, String appKey, String timestamp) {

        StringBuffer sbf = new StringBuffer();
        sbf.append("AppId=");
        String appIdMd5 = getMD5(appId);
        sbf.append(appIdMd5);
        sbf.append("&");
        sbf.append("AppKey=");
        String appKeyMd5 = getMD5(appKey);
        sbf.append(appKeyMd5);
        sbf.append("&");
        sbf.append("TimeStamp=");
        sbf.append(timestamp);

        try {
            String digest = getDigest(sbf.toString(), "SHA-1");
            System.out.println("摘要加密" + digest);
            return digest;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


/*    public static void main(String[] args) {
//        System.out.println(getMD5("o6zGrZ3Laal3XzlpaqxdNGMQmC3oeP25"+"peccancy"));
//        System.out.println(getMD5("51c21c23fdef495bdf0c635beabbab01"+"peccancy"));

        //MD5Util.getMD5(key + parkingNumber);
//        System.out.println(getMD5("51c21c23fdef495bdf0c635beabbab01"+"peccancy"));

//        System.out.println(MD5Util.getMD5("24c21c23fdef495bdf0c635beabbacf1" + "PT010156"));

        System.out.println(SignMD5Util.getMD5("24c21c23fdef495bdf0c635beabbacf1" + "PT010128"));

    }*/


}
