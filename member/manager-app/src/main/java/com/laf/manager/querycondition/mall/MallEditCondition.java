package com.laf.manager.querycondition.mall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MallEditCondition {

    private String mallName;

    private Integer mallId;

    private String phone;

    private String address;

    private Double longitude;

    private Double latitude;

    private String businessHours;

    private String appid;

    private String appsecret;

    private String intro;

    private String pictures;

    private String home;

    private String mallImages;
}
