package com.laf.manager.utils.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.laf.upload.file")
@Data
public class FileProperties {

    private String path;

    private String base;

    private String domain;

}
