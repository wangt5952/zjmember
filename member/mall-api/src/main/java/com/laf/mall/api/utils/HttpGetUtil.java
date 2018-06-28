package com.laf.mall.api.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class HttpGetUtil {
    public static String getHttp(String url){
        String result = null;
        try{
            URL u = new URL(url);
            InputStream in = u.openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                byte buf[] = new byte[1024];
                int read = 0;
                while ((read = in.read(buf)) > 0) {
                    out.write(buf, 0, read);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
            byte b[] = out.toByteArray();
            result = new String(b, "utf-8");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static void main(String args[]) {
       // String result = getHttp("http://121.196.208.176:9001/coupon/87?memberId=14846");
        String result = getHttp("http://api.test.goago.cn/oapi/rest?method=gogo.bill.amount.query&" +
                "timestamp=20180611121800&messageFormat=json&appKey=8688458140778369&v=1.0&signMethod=MD5&" +
                "qrCode=http://f.test.goago.cn/001/B107GGGGHkiikGkjklstljljsikqhllql&sign=0C96693ABFF3871593E697BE4CC451F0");
        System.out.println("返回值："+result);
    }
}
