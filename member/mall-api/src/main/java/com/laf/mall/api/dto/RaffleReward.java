package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RaffleReward {

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private int trr_id;

    private int raffle_id;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private String reward_name;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private int reward_type;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private String reward_picture;

    private int inventory;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private double hit_rate;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private int reward_value;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private int sort_id;

}
