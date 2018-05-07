package com.laf.manager.dto;

import lombok.*;

import java.util.List;

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
    private int map_id;

    /**
     * 平面地图名称
     */
    private String map_name;

    /**
     * 平面地图存储地址
     */
    private String map_picture;

    /**
     * 相关商城id
     */
    private int mall_id;

    /**
     * 排序号
     */
    private int sort_id;

    private List<Shop> shops;

    public PlaneMap(int map_id, String map_name) {
        this.map_id = map_id;
        this.map_name = map_name;
    }

    public PlaneMap(String map_picture) {
        this.map_picture = map_picture;
    }
}
