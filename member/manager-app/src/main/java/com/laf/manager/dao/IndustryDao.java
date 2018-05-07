package com.laf.manager.dao;

import com.laf.manager.dto.Industry;
import com.laf.manager.repository.IndustryRepository;
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

    public Industry getIndustryById(final Integer id) {
        return industryRepository.selectIndustryById(id);
    }

    public int saveIndustry(final Industry industry) {
        return industryRepository.insertIndustry(industry);
    }

    public int editIndustry(final Industry industry) {
        return industryRepository.updateIndustry(industry);
    }

    public int deleteIndustryById(final Integer industryId) {
        return industryRepository.deleteIndustryById(industryId);
    }
}
