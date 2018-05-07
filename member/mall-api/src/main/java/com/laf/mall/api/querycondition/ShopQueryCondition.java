package com.laf.mall.api.querycondition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ShopQueryCondition {

    /**
     * 商城id
     */
    @NotNull(message = "商场id不能为空")
    @Min(value = 1, message = "商场id无效")
    private Integer mall_id;

    /**
     * 搜索关键字
     */
    private String keywords;

    /**
     * 楼层
     */
    @ApiModelProperty(value = "平面地图ID")
    private int map_id;

    /**
     * 业态id
     */
    @ApiModelProperty(value = "业态ID")
    private int industry_id;

    /**
     * 排序条件：points 积分热度；首拼；sort 排序号
     */
    private String sort = "sort";

    /**
     * 排序顺序：asc 升序；desc 降序
     */
    private String direction = "asc";

    /**
     * 分页
     */
    private int page = 1;
    private int size = 5;
//    private int offset;

    public int getOffset() {
        return (page - 1) * size;
    }

    public String getSort() {
        String result = "sort";

        if (sort.equals("0")) result = "sort";
        else if (sort.equals("1")) result = "points";
        else if (sort.equals("2")) result = "acronym";

        return result;
    }
}
