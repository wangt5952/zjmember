package com.laf.mall.api.utils.weixin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WeixinUtil {
    String url = "https://api.weixin.qq.com/cgi-bin/token";
    //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx3041b222eaad5c8a&secret=fda2fbf4fdec7a43b178f12d4bf36414

    public void getAccessToken() {

//        StringBuffer sb = new StringBuffer();
//        sb.append("grant_type=client_credential");
//        sb.append("&appid=wx3041b222eaad5c8a");
//        sb.append("&secret=fda2fbf4fdec7a43b178f12d4bf36414");

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", "wx3041b222eaad5c8a");
        params.put("secret", "fda2fbf4fdec7a43b178f12d4bf36414");

        RestTemplate template = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON_UTF8);
        header.add("Accept", "*/*");

        HttpEntity httpEntity = new HttpEntity(params, header);
        ResponseEntity<String> responseEntity = template.exchange(url, HttpMethod.GET, httpEntity, String.class);
        log.info("responseEntity=={}", responseEntity);
        log.info("responseEntity.getBody()=={}", responseEntity.getBody());

    }

}
