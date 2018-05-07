package com.laf.manager;

import com.laf.manager.dao.MallDao;
import com.laf.manager.dto.Mall;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.laf.settings")
@Data
@Slf4j
public class SettingsProperties {

    private String appid;

    private int mallId = 0;

    private String excel = "export.xls";

    @Autowired
    MallDao dao;

    public int getMallId() {
        if (mallId == 0) {
            Mall mall = dao.getMallByAppId(appid);

            if (mall != null) {

                mallId = mall.getMall_id();
                mall = null;

                log.info("+++++++++++db operation+++++++++++++");
            } else {
                log.error("无法获得商场信息，检查【appid】配置");
            }
        }

        return mallId;
    }

}
