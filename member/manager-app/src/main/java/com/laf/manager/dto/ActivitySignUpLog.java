package com.laf.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivitySignUpLog {

    private int asulog_id;

    private int activity_id;

    private String title;

    private Date activity_time_start;

    private Date activity_time_end;

    private boolean is_sign_in;

    private boolean is_sign_up;

    private int member_id;

    private String name;

    private String mobile;

    private int mall_id;

    private Date sign_up_time;

    private Date sign_in_time;

    private int sign_type = 1;

    private String typeName;

    public long getSign_up_time() {
        if (sign_up_time == null) return 0L;

        return sign_up_time.getTime();
    }

    public long getSign_in_time() {
        if (sign_in_time == null) return 0L;

        return sign_in_time.getTime();
    }

    public String getTypeName() {
        String str = "";

        switch (sign_type) {

            case 1:
                str = "已报名";
                break;
            case 2:
                str = "已签到";
                break;
        }
        return str;
    }

    public boolean isIs_sign_in() {
        return is_sign_in;
    }

    public boolean isIs_sign_up() {
        return is_sign_up;
    }
}
