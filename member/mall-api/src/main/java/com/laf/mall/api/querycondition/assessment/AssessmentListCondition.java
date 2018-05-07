package com.laf.mall.api.querycondition.assessment;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AssessmentListCondition {

    @NotNull(message = "商场id不能为空")
    @Min(value = 1, message = "id无效")
    private Integer mallId;

    private Integer page = 1;

    private Integer size = 5;

    public int getOffset() {
        return (page - 1) * size;
    }
}
