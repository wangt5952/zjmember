package com.laf.manager.core.properties;

import lombok.Data;

@Data
public class ResponseProperties {

    private String contentType = "json";

    private String fields;
}
