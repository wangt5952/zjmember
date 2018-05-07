package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RaffleInfo {
    public interface RaffleInfoView {
    }

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private int raffle_id;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private String title;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private String picture;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private Date raffle_time_start;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private Date raffle_time_end;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private int required_points;

    private int intro_id;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private String intro;

    private boolean is_active;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private int mall_id;

    private int times;

//    private int daily;

    private String activity_code;

    @JsonView(RaffleInfo.RaffleInfoView.class)
    private List<RaffleReward> rewards;

    public long getRaffle_time_start() {
        return raffle_time_start == null ? 0L : raffle_time_start.getTime();
    }

    public long getRaffle_time_end() {
        return raffle_time_end == null ? 0L : raffle_time_end.getTime();
    }
}
