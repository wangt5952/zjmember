package com.laf.mall.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySignUpLog {

    private int asulog_id;

    private int activity_id;

    private int member_id;

    private int mall_id;

    private Date sign_up_time;

    private Date sign_in_time;

    private int sign_type = 1;

    public long getSign_up_time() {
        if (sign_up_time == null) return 0L;

        return sign_up_time.getTime();
    }

    public long getSign_in_time() {
        if (sign_in_time == null) return 0L;

        return sign_in_time.getTime();
    }
}
