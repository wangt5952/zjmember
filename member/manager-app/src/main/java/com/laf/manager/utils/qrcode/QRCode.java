package com.laf.manager.utils.qrcode;

import lombok.Data;

@Data
public class QRCode {
    private int width;

    private int height;

    private String format;

    private String content;

    private String fileName;

    private String folder;

//    private String url;
}
