package com.laf.mall.api.vo;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MyRewardInfo {
    public interface MyRewardInfoListView{}

    @JsonView(MyRewardInfo.MyRewardInfoListView.class)
    private Date action_time;

    @JsonView(MyRewardInfo.MyRewardInfoListView.class)
    private int reward_type;

    @JsonView(MyRewardInfo.MyRewardInfoListView.class)
    private int reward_value;

    @JsonView(MyRewardInfo.MyRewardInfoListView.class)
    private String coupon_name;

    @JsonView(MyRewardInfo.MyRewardInfoListView.class)
    private BigDecimal price;

    @JsonView(MyRewardInfo.MyRewardInfoListView.class)
    private String reward_picture;

    public long getAction_time() {
        return action_time == null ? 0L : action_time.getTime();
    }

}
