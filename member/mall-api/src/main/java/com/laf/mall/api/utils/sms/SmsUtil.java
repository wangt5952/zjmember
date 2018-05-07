package com.laf.mall.api.utils.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

@Component
public class SmsUtil {

    @Autowired
    SmsProperties smsProperties;

    public ResponseEntity<String> sendSmsByTemplate(final String mobile, final String content) throws UnsupportedEncodingException {
        String url = smsProperties.getUrl();

        StringBuffer sb = new StringBuffer("method=sendMsg"); // 写死
        sb.append("&username=").append(smsProperties.getUsername());
        sb.append("&password=").append(smsProperties.getPassword());
        sb.append("&veryCode=").append(smsProperties.getVeryCode());
        sb.append("&msgtype=2"); // 写死
        sb.append("&tempid=").append(smsProperties.getTempid());
        sb.append("&mobile=").append(mobile);
        sb.append("&content=").append(URLEncoder.encode("@1@=" + content, "UTF-8"));
        sb.append("&code=utf-8"); // 写死

        RestTemplate restTemplate = new RestTemplate();

        String bodyValTemplate = sb.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(bodyValTemplate, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        return responseEntity;
    }

    public String getValidCode() {
        Random random = new Random();
        int x = random.nextInt(899999);
        x += 100000;
        return String.valueOf(x);
    }
}
