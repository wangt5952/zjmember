package com.laf.mall.api.dao;

import com.laf.mall.api.dto.Assessment;
import com.laf.mall.api.querycondition.assessment.AssessmentListCondition;
import com.laf.mall.api.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssessmentDao {

    @Autowired
    AssessmentRepository repository;

    public int saveAssessment(Assessment assessment) {
        return repository.insertAssessment(assessment);
    }

    public List<Assessment> getAssessmentList(final AssessmentListCondition condition, final Integer memberId) {
        return repository.selectAssessmentList(condition, memberId);
    }

    public Assessment getAssessmentById(final Integer assessmentId) {
        return repository.selectAssessmentById(assessmentId);
    }
}
