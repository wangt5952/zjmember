package com.laf.manager.querycondition.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelSaveCondition {

    private Integer levelId;

    private String levelName;

    private Integer mallId;

    private Integer pointsLow;

    private Integer pointsHigh;

    private Integer algorithm;
}
