package com.laf.mall.api;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 加载yaml配置文件的方法
 * spring-boot更新到1.5.2版本后locations属性无法使用
 * @PropertySource注解只可以加载proprties文件,无法加载yaml文件
 * 故现在把数据放到application.yml文件中,spring-boot启动时会加载
 */
@Component
@ConfigurationProperties(prefix = "myYml")
public class YmlConfig {

    private Map<String, String> gouagou = new HashMap<>(); //接收gouagou里面的属性值

    public Map<String, String> getGouagou() {
        return gouagou;
    }

    public void setGouagou(Map<String, String> gouagou) {
        this.gouagou = gouagou;
    }
}
