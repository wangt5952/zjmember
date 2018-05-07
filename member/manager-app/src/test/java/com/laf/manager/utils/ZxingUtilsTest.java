package com.laf.manager.utils;

import com.laf.manager.utils.qrcode.ZxingUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ZxingUtilsTest {

    @Test
    public void createQRCode() throws Exception {
        ZxingUtils zxingUtils = new ZxingUtils();
//        zxingUtils.createQRCode();
    }

}
