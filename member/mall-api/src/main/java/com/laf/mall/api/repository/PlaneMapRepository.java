package com.laf.mall.api.repository;

import com.laf.mall.api.dto.PlaneMap;
import com.laf.mall.api.utils.db.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PlaneMapRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbUtils dbUtils;

    public List<PlaneMap> selectAllPlaneMaps(final Integer mall_id) {
        String sql = "select map_id, map_name, map_picture from `T_PLANE_MAP` where mall_id=? order by sort_id asc";
        return jdbcTemplate.query(sql, new Object[]{mall_id}, new PlaneMapRowMapper());
    }

    class PlaneMapRowMapper implements RowMapper<PlaneMap> {

        @Override
        public PlaneMap mapRow(ResultSet rs, int rowNum) throws SQLException {
            PlaneMap planeMap = new PlaneMap();
            if (dbUtils.hasColumn(rs, "map_id")) planeMap.setMap_id(rs.getInt("map_id"));
            if (dbUtils.hasColumn(rs, "map_name")) planeMap.setMap_name(rs.getString("map_name"));
            if (dbUtils.hasColumn(rs, "map_picture")) planeMap.setMap_picture(rs.getString("map_picture"));
            return planeMap;
        }
    }
}
