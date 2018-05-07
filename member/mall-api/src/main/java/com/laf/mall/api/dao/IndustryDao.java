package com.laf.mall.api.dao;

import com.laf.mall.api.dto.Industry;
import com.laf.mall.api.repository.IndustryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndustryDao {

    @Autowired
    private IndustryRepository industryRepository;

    public List<Industry> getAllIndustries(final Integer mallId) {
        return industryRepository.selectAllIndustries(mallId);
    }
}
