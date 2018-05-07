package com.laf.manager.dao;

import com.laf.manager.dto.VerificationClerk;
import com.laf.manager.repository.VerificationClerkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VerificationClerkDao {

    @Autowired
    VerificationClerkRepository repository;

    public List<VerificationClerk> getSimpleVerificationClerkListForPage(Integer mallId, Integer size, Integer offset) {
        return repository.selectSimpleVerificationClerkListForPage(mallId, size, offset);
    }

    public VerificationClerk getVerificationClerkById(Integer vcId) {
        return repository.selectVerificationClerkById(vcId);
    }

    public int updateStatus(Integer vcId, Integer status) {
        return repository.updateStatus(vcId, status);
    }

    public int getVerificationClerksCount(Integer mallId) {
        return repository.selectVerificationClerksCount(mallId);
    }

    public int deleteById(final Integer vcId) {
        return repository.deleteById(vcId);
    }
}
