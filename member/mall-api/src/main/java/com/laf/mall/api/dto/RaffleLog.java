package com.laf.mall.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RaffleLog {
    private int trlog_id;

    private int raffle_id;

    private int member_id;

    private String member_name;

    private String member_mobile;

    private int trr_id;

    private Date action_time;

    public long getAction_time() {
        return action_time == null ? 0L : action_time.getTime();
    }
}
