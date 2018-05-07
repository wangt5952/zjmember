package com.laf.mall.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Level {

    private int level_id;

    /**
     * 等级名称
     */
    private String level_name;

    /**
     * 累计积分下限
     */
    private int points_low;

    /**
     * 累计积分上限
     */
    private int points_high;

    /**
     * 累计消费下限
     */
    private BigDecimal amount_low;

    /**
     * 累计消费上限
     */
    private BigDecimal amount_high;

    /**
     * 计算法则
     */
    private int algorithm;

    private int mall_id;

    public Level(int levelId, String levelName) {
        this.level_id = levelId;
        this.level_name = levelName;
    }
}
