package com.laf.manager.service;

import com.laf.manager.dto.Assessment;
import com.laf.manager.querycondition.assessment.AssessmentEditCondition;
import com.laf.manager.querycondition.assessment.AssessmentQueryCondition;

import java.util.List;

public interface AssessmentService {

    List<Assessment> getAssessmentList(AssessmentQueryCondition condition);

    int getAssessmentCount(AssessmentQueryCondition condition);

    Assessment getAssessmentById(final int assessmentId);

    int editAssessment(AssessmentEditCondition conditon);

    int delAssessment(final int assessmentId);

    List<Integer> getAssessmentCountMonthly(String year);

}
