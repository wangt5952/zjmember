package com.laf.mall.api.querycondition.giftcoupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
public class GiftCouponQueryCondition {

    /**
     * 搜索关键字
     */
    @ApiModelProperty(value = "搜索关键字")
    private String keywords;

    /**
     * 商城id
     */
    @NotNull(message = "商场id不能为空")
    @Min(value = 1, message = "商场id无效")
    private Integer mallId;

    /**
     * 会员id
     */
    @Min(value = 1, message = "会员id无效")
    private Integer memberId;
//    /**
//     * 当前时间
//     */
//    private Long currentTime;

//    /**
//     * 优惠券分类标记
//     */
//    @ApiModelProperty(value = "积分兑换 0;免费领取 2;")
//    private int receiveMethod = -1;

    private Integer page = 1;

    private Integer size = 5;

    public long getCurrentTime() {

        LocalDate localDate = LocalDate.now();

        LocalDateTime time = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0, 0);

        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

        long ct = zdt.toInstant().toEpochMilli();

        return ct;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public String getSort() {
        return "sort_id";
    }

    public String getDirection() {
        return "ASC";
    }
}
