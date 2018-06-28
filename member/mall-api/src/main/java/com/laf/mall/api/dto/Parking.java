package com.laf.mall.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Parking {

    private int parking_id;
    private int member_id;
    private String car_number;
    private int isdefault;
}
