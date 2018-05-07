package com.laf.mall.api.querycondition.assessment;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AssessmentSaveCondition {

    private Integer shopId;

    @NotNull(message = "商场id不能为空")
    @Min(value = 1, message = "商场id无效")
    private Integer mallId;

    @NotNull(message = "评价内容不能为空")
    private String comments;

    public Date getActionTime() {

//        return new Date(new DateTimeUtils().getMilliByToDay());
        return new Date();
    }
}
