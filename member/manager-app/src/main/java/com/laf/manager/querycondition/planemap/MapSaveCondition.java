package com.laf.manager.querycondition.planemap;

import lombok.Data;

@Data
public class MapSaveCondition {

   private Integer mapId;

   private String mapName;

   private Integer sortId;

   public Integer getMapId() {
      return mapId == null ? 0 : mapId;
   }
}
