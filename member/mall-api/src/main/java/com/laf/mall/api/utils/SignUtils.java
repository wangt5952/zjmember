package com.laf.mall.api.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * SignUtils class
 *
 */
@Slf4j
public class SignUtils
{

    public static String sign(String appSecret, Map<String, String> form)
    {
        return SignUtils.getSign(appSecret, form, form.get("signMethod"));
    }

    /**
     * 签名算法
     * @param appSecret  签名密钥
     * @param parms      请求参数
     * @param signMethod 签名方法
     * @return 签名
     */
    public static String getSign(String appSecret, Map<String, String> params, String signMethod)
    { // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder signStr = new StringBuilder();
        for (String key : keys)
        {
            String value = params.get(key);
            if (value != null && !"".equals(value))
            {
                signStr.append(key).append("=").append(value).append("&");
            }
        }
        // 第三步：拼接签名密钥
        signStr.append("key=").append(appSecret);
        //GooagooLog.info("服务端的签名之前的字符串:" + signStr.toString());
        log.info("服务端的签名之前的字符串:" + signStr.toString());
        // 第四步：签名
        if ("MD5".equals(signMethod))
        {
            return getMD5(signStr.toString());
        }
        else
        {
            return getSHA(signStr.toString());
        }
    }

    /**
     * SHA加密
     * @param str 待加密数据
     * @return 加密后数据
     */
    public static String getSHA(String str)
    {
        return encrypt(str, "SHA");
    }

    /**
     * MD5加密
     * @param str 待加密数据
     * @return 加密后数据
     */
    public static String getMD5(String str)
    {
        return encrypt(str, "MD5");
    }

    /**
     * 加密方法
     * @param str 待加密数据
     * @param algorithm 加密类型 MD5，SHA
     * @return 加密后数据
     */
    private static String encrypt(String str, String algorithm)
    {
        StringBuffer hexstr = new StringBuffer("");
        try
        {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(str.getBytes("UTF-8"));
            byte[] digest = md.digest();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++)
            {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2)
                {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //throw new Exception(e);
        }
        return hexstr.toString().toUpperCase();
    }

    public static void main(String[] args)
    {
        Map<String, String> hm = new HashMap<String, String>();
        hm.put("method", "gogo.bill.amount.query");
        hm.put("timestamp", "20180611121800");
        hm.put("messageFormat", "json");
        hm.put("appKey", "8688458140778369");
        hm.put("v", "1.0");
        hm.put("signMethod", "MD5");
        hm.put("qrCode", "http://f.test.goago.cn/001/B107GGGGHkiikGkjklstljljsikqhllql");
        System.out.println(SignUtils.getSign("1CFMETED5V4LVG001GKP81LV7P164MCA", hm, "MD5"));
    }
}
