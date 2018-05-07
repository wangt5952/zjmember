package com.laf.manager.querycondition.assessment;

import lombok.Data;

@Data
public class AssessmentQueryCondition {

    private int mallId;

    private int size = 10;

    private int page = 1;

    public int getOffset() {
        return (page - 1) * size;
    }
}
