package com.laf.manager.dao;

import com.laf.manager.dto.Assessment;
import com.laf.manager.querycondition.assessment.AssessmentQueryCondition;
import com.laf.manager.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssessmentDao {

    @Autowired
    private AssessmentRepository assessmentRepository;

    public List<Assessment> getAssessmentList(AssessmentQueryCondition condition) {
        return assessmentRepository.selectAssessmentList(condition);
    }

    public Assessment getAssessmentById(final int assessmentId) {
        return assessmentRepository.selectAssessmentById(assessmentId);
    }

    public int updateReply(final Assessment assessment) {
        return assessmentRepository.updateReply(assessment);
    }

    public int delAssessmentById(final int assessmentId) {
        return assessmentRepository.deleteAssessmentById(assessmentId);
    }

    public int getAssessmentCount(AssessmentQueryCondition condition) {
        return assessmentRepository.selectAssessmentCount(condition);
    }

    public List<Integer> getAssessmentCountMonthly(String year, int mallId) {
        return assessmentRepository.selectAssessmentCountMonthly(year, mallId);
    }
}
