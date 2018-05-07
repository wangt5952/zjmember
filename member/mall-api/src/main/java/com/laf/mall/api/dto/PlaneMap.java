package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlaneMap {
    public interface PlaneMapAppView {};

    /**
     * 平面地图id
     */
    @JsonView(PlaneMapAppView.class)
    private int map_id;

    /**
     * 平面地图名称
     */
    @JsonView(PlaneMapAppView.class)
    private String map_name;

    /**
     * 平面地图存储地址
     */
    @JsonView(PlaneMapAppView.class)
    private String map_picture;

    /**
     * 相关商城id
     */
    private int mall_id;

    public PlaneMap(int map_id, String map_name, String map_picture) {
        this.map_id = map_id;
        this.map_name = map_name;
        this.map_picture = map_picture;
    }

    public PlaneMap(String map_picture) {
        this.map_picture = map_picture;
    }
}
