package com.laf.mall.api.service;

import com.laf.mall.api.dto.GagBill;

public interface GagService {

    GagBill getBill(String qrCode);

    int uploadBill(int memberId, String qrCode);
}
