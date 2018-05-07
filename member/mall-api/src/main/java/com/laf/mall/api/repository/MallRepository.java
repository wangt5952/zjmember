package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Mall;
import com.laf.mall.api.utils.db.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
@Slf4j
public class MallRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbUtils dbUtils;

    public Mall selectMallById(final Integer mall_id) {
        final String sql = "select m.mall_id,m.mall_name,m.area,m.address,m.pictures," +
                "m.home,m.mall_phone,a.content as intro,m.longitude,m.latitude,m.business_hours," +
                "m.AppID,m.AppSecret" +
                " from `T_MALL` m left join `T_ARTICLES` a on m.intro=a.article_id" +
                " where m.mall_id=?";

        return jdbcTemplate.queryForObject(sql, new Object[]{mall_id}, new MallRowMapper());
    }

    public Mall selectMallByAppId(final String appId) {
        final String sql = "select m.mall_id,m.mall_name,m.area,m.address,m.pictures," +
                "m.home,m.mall_phone,a.content,m.longitude,m.latitude,m.business_hours," +
                "m.AppID,m.AppSecret" +
                " from `T_MALL` m left join `T_ARTICLES` a on m.intro=a.article_id" +
                " where m.AppID=?";

        Mall mall = null;

        try {
             mall = jdbcTemplate.queryForObject(sql, new Object[]{appId}, new MallRowMapper());
        } catch (Exception e) {
        }

        return mall;

    }

    public int saveMall(final Mall mall) {
        final String sql = "insert into `T_MALL` (mall_name,area,address,pictures,home,mall_phone,intro," +
                "longitude,latitude,business_hours,AppId,AppSecret) values (?,?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, mall.getMall_name());
                ps.setString(2, mall.getArea());
                ps.setString(3, mall.getAddress());
                ps.setString(4, mall.getPictures());
                ps.setString(5, mall.getHome());
                ps.setString(6, mall.getMall_phone());
                ps.setInt(7, mall.getIntro_id());
                ps.setDouble(8, mall.getLongitude());
                ps.setDouble(9, mall.getLatitude());
                ps.setString(10, mall.getBusiness_hours());
                ps.setString(11, mall.getAppId());
                ps.setString(12, mall.getAppSecret());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    class MallRowMapper implements RowMapper<Mall> {
        @Override
        public Mall mapRow(ResultSet rs, int rowNum) throws SQLException {

            Mall mall = new Mall();

            if (dbUtils.hasColumn(rs, "mall_id")) mall.setMall_id(rs.getInt("mall_id"));

            if (dbUtils.hasColumn(rs, "mall_name")) mall.setMall_name(rs.getString("mall_name"));

            if (dbUtils.hasColumn(rs, "mall_code")) mall.setMall_code(rs.getString("mall_code"));

            if (dbUtils.hasColumn(rs, "address")) mall.setAddress(rs.getString("address"));

            if (dbUtils.hasColumn(rs, "area")) mall.setArea(rs.getString("area"));

            if (dbUtils.hasColumn(rs, "manager")) mall.setManager(rs.getString("manager"));

            if (dbUtils.hasColumn(rs, "manager_phone")) mall.setManager_phone(rs.getString("manager_phone"));

            if (dbUtils.hasColumn(rs, "pictures")) mall.setPictures(rs.getString("pictures"));

            if (dbUtils.hasColumn(rs, "home")) mall.setHome(rs.getString("home"));

            if (dbUtils.hasColumn(rs, "mall_phone")) mall.setMall_phone(rs.getString("mall_phone"));

            if (dbUtils.hasColumn(rs, "content")) mall.setIntro(rs.getString("content"));

            if (dbUtils.hasColumn(rs, "longitude")) mall.setLongitude(rs.getDouble("longitude"));

            if (dbUtils.hasColumn(rs, "latitude")) mall.setLatitude(rs.getDouble("latitude"));

            if (dbUtils.hasColumn(rs, "business_hours")) mall.setBusiness_hours(rs.getString("business_hours"));

            if (dbUtils.hasColumn(rs, "AppID")) mall.setAppId(rs.getString("AppID"));

            if (dbUtils.hasColumn(rs, "AppSecret")) mall.setAppSecret(rs.getString("AppSecret"));

            if (dbUtils.hasColumn(rs, "group_id")) mall.setGroup_id(rs.getInt("group_id"));

            return mall;
        }
    }

}
