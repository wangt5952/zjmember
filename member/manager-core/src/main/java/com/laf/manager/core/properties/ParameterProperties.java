package com.laf.manager.core.properties;

import lombok.Data;

@Data
public class ParameterProperties {

    private String method = "get";

    private String pattern = "webservices";

    private String contentType = "normal";

    private String params;
}
