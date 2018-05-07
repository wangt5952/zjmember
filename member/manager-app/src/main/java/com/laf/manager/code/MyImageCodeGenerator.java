package com.laf.manager.code;

import com.laf.manager.core.validate.code.ImageCode;
import com.laf.manager.core.validate.code.ValidateCodeGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

@Component("imageCodeGenerator") // 必须与默认配置Bean的名称一致，{@See ValidateCodeBeanConfig}
public class MyImageCodeGenerator implements ValidateCodeGenerator {

    @Override
    public ImageCode generate(ServletWebRequest request) {
        return null;
    }
}
