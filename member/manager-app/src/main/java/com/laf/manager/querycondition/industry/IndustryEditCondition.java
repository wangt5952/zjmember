package com.laf.manager.querycondition.industry;

import lombok.Data;

@Data
public class IndustryEditCondition {

    private Integer sortId;

    private String industryName;

    private Integer industryId;

    public Integer getIndustryId() {
        return industryId == null ? 0 : industryId;
    }
}
