package com.laf.manager.service.impl;

import com.laf.manager.SettingsProperties;
import com.laf.manager.dao.VerificationClerkDao;
import com.laf.manager.dto.VerificationClerk;
import com.laf.manager.service.VerificationClerkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationClerkServiceImpl implements VerificationClerkService {

    @Autowired
    VerificationClerkDao verificationClerkDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Override
    public List<VerificationClerk> getAllVerificationClerks(Integer size, Integer offset) {
        return verificationClerkDao.getSimpleVerificationClerkListForPage(settingsProperties.getMallId(), size, offset);
    }

    @Override
    public VerificationClerk getVerificationClerkDetail(Integer vcId) {
        return verificationClerkDao.getVerificationClerkById(vcId);
    }

    @Override
    public int updateVcStatus(Integer vcId, Integer status) {
        return verificationClerkDao.updateStatus(vcId, status);
    }

    @Override
    public int getTotal() {
        return verificationClerkDao.getVerificationClerksCount(settingsProperties.getMallId());
    }

    @Override
    public int deleteById(Integer vcId) {
        return verificationClerkDao.deleteById(vcId);
    }
}
