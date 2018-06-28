package com.laf.mall.api.querycondition.points;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class PointsQueryCondition {

    @NotNull(message = "商场id不能为空")
    @Min(value = 1, message = "商场id无效")
    @ApiModelProperty(value = "相关商城ID")
    private Integer mallId;

//    @NotNull(message = "会员id不能为空")
//    @Min(value = 1, message = "会员id无效")
//    @ApiModelProperty(value = "相关会员ID")
//    private Integer memberId;

//    private String sort = "shopping_date";

    private int page = 1;

    private int size = 5;

    /**
     * 排序顺序：asc 升序；desc 降序
     */
//    private String direction = "desc";

    public int getOffset() {
        return (page - 1) * size;
    }

    public String getSort() {
        return "handle_date";
    }

    public String getDirection() {
        return "desc";
    }
}
