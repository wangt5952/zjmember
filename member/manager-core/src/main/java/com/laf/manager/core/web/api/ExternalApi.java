package com.laf.manager.core.web.api;

import com.laf.manager.core.properties.ExternalProperties;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ExternalApi {

    @Autowired
    ExternalProperties externalProperties;

    private Map<String, Object> internalParams;

    public ExternalApi(Map<String, Object> internalParams) {
        this.internalParams = internalParams;
    }

    private String createParameters() {

        String[] params = StringUtils.splitByWholeSeparatorPreserveAllTokens(externalProperties.getParameter().getParams(), ",");

        if (StringUtils.equalsIgnoreCase(externalProperties.getParameter().getMethod(), "get")) {
            return createNormalParameters(params);

        } else {

            if (StringUtils.equals(externalProperties.getParameter().getContentType(), "json")) {
                return createJsonParameters(params);

            } else if (StringUtils.equals(externalProperties.getParameter().getContentType(), "xml")) {
                return createXmlParameters(params);

            } else {
                return createNormalParameters(params);

            }
        }
    }

    private String createNormalParameters(String[] params) {
        String param = "";

        if (!MapUtils.isEmpty(this.internalParams)) {
            for (String $ : params) {
                param += $ + "=" + this.internalParams.get($).toString() + "&";
            }

            param = param.substring(param.length() - 2);
        }

        return param;
    }

    private String createXmlParameters(String[] params) {
        return "";
    }

    private String createJsonParameters(String[] params) {
        return "";
    }
}
