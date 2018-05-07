package com.laf.manager.service.impl;

import com.laf.manager.SettingsProperties;
import com.laf.manager.dao.AssessmentDao;
import com.laf.manager.dto.Assessment;
import com.laf.manager.querycondition.assessment.AssessmentEditCondition;
import com.laf.manager.querycondition.assessment.AssessmentQueryCondition;
import com.laf.manager.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    @Autowired
    private AssessmentDao assessmentDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Override
    public List<Assessment> getAssessmentList(AssessmentQueryCondition condition) {
        List<Assessment> list = assessmentDao.getAssessmentList(condition);
        return list;
    }

    @Override
    public int getAssessmentCount(AssessmentQueryCondition condition) {
        return assessmentDao.getAssessmentCount(condition);
    }

    @Override
    public Assessment getAssessmentById(int assessmentId) {
        return assessmentDao.getAssessmentById(assessmentId);
    }

    @Override
    public int editAssessment(AssessmentEditCondition conditon) {
        Assessment assessment = assessmentDao.getAssessmentById(conditon.getAssessmentId());

        if (assessment == null) {
            return 0;
        }

        assessment.setReply(conditon.getReply());
        assessment.setStatus(0);
        int result = assessmentDao.updateReply(assessment);
        // TODO: 2018/1/30 评价建议返积分
        return result;
    }

    @Override
    public int delAssessment(int assessmentId) {
        return assessmentDao.delAssessmentById(assessmentId);
    }

    @Override
    public List<Integer> getAssessmentCountMonthly(String year) {
        return assessmentDao.getAssessmentCountMonthly(year, settingsProperties.getMallId());
    }
}
