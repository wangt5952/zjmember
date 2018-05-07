package com.laf.manager.service;

import com.laf.manager.dto.VerificationClerk;

import java.util.List;

public interface VerificationClerkService {
    List<VerificationClerk> getAllVerificationClerks(Integer size, Integer offset);

    VerificationClerk getVerificationClerkDetail(Integer vcId);

    int updateVcStatus(Integer vcId, Integer status);

    int getTotal();

    int deleteById(final Integer vcId);
}
