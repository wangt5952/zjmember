package com.laf.manager.querycondition.settings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardEditCondition {

   private int rewardCategory = 0;

   private int rewardType = 0;

   private int rewardValue = 0;
}
