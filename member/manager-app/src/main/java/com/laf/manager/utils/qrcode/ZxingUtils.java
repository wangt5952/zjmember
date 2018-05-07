package com.laf.manager.utils.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

@Slf4j
public class ZxingUtils {

    public static int createQRCode(QRCode qrCode) {

        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        Path file = new File(qrCode.getFolder(), qrCode.getFileName()).toPath();
        log.info("qrcode path is [{}]", file.toString());

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    qrCode.getContent(),
                    BarcodeFormat.QR_CODE,
                    qrCode.getWidth(),
                    qrCode.getHeight(),
                    hints);
            MatrixToImageWriter.writeToPath(bitMatrix, qrCode.getFormat(), file);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

}
