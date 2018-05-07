package com.laf.mall.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Admin {

    private int admin_id;

    private String account;

    private String password;

    private String mobile;

    private int mall_id;

    public Admin(String account, String password, String mobile) {
        this.admin_id = 0;
        this.account = account;
        this.password = password;
        this.mobile = mobile;
        this.mall_id = 1;
    }
}
