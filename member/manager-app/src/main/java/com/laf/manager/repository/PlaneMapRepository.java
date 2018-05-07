package com.laf.manager.repository;

import com.laf.manager.dto.PlaneMap;
import com.laf.manager.utils.db.DbUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class PlaneMapRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbUtils dbUtils;

    public List<PlaneMap> selectAllPlaneMaps(final Integer mall_id) {

        if (mall_id == null || mall_id <= 0) return ListUtils.EMPTY_LIST;

        String sql = "select map_id, map_name, map_picture, sort_id from `T_PLANE_MAP` where mall_id=? order by sort_id asc";
        return jdbcTemplate.query(sql, new Object[]{mall_id}, new PlaneMapRowMapper());
    }

    public PlaneMap selectPlaneMapById(final Integer map_id) {

        if (map_id == null || map_id <= 0) return null;

        String sql = "select map_id, map_name, map_picture, sort_id, mall_id from `T_PLANE_MAP` where map_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{map_id}, new PlaneMapRowMapper());
    }

    public int insertPlaneMap(PlaneMap map) {
        String sql = "insert into `T_PLANE_MAP` (map_name,map_picture,sort_id,mall_id) values (?,?,?,?)";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, map.getMap_name());
                ps.setString(2, map.getMap_picture());
                ps.setInt(3, map.getSort_id());
                ps.setInt(4, map.getMall_id());
                return ps;
            }
        }, holder);

        int newId = holder.getKey().intValue();

        return newId;
    }

    public int updatePlaneMap(PlaneMap map) {
        String sql = "update `T_PLANE_MAP` set map_name=?,map_picture=?,sort_id=?,mall_id=? where map_id=?";

        return jdbcTemplate.update(sql, map.getMap_name(), map.getMap_picture(), map.getSort_id(), map.getMall_id(), map.getMap_id());
    }

    public int deletePlaneMap(final Integer map_id) {
        String sql = "delete from `T_PLANE_MAP` where map_id=?";

        return jdbcTemplate.update(sql, map_id);
    }

    class PlaneMapRowMapper implements RowMapper<PlaneMap> {

        @Override
        public PlaneMap mapRow(ResultSet rs, int rowNum) throws SQLException {
            PlaneMap planeMap = new PlaneMap();
            if (dbUtils.hasColumn(rs, "map_id")) planeMap.setMap_id(rs.getInt("map_id"));
            if (dbUtils.hasColumn(rs, "map_name")) planeMap.setMap_name(rs.getString("map_name"));
            if (dbUtils.hasColumn(rs, "map_picture")) planeMap.setMap_picture(rs.getString("map_picture"));
            if (dbUtils.hasColumn(rs, "sort_id")) planeMap.setSort_id(rs.getInt("sort_id"));
            return planeMap;
        }
    }
}
