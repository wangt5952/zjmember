package com.laf.manager.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.laf.external")
@Data
public class ExternalProperties {

    private ParameterProperties parameter = new ParameterProperties();

    private ResponseProperties response = new ResponseProperties();

}
