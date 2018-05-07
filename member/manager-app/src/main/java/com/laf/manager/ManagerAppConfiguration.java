package com.laf.manager;

import com.laf.manager.utils.file.FileProperties;
import com.laf.manager.utils.qrcode.WechatProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        FileProperties.class,
        SettingsProperties.class,
        WechatProperties.class
})
public class ManagerAppConfiguration {
}
