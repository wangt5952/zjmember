package com.laf.manager.repository;

import com.laf.manager.dto.Reward;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class SettingsRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String selectPointsRule(int mallId) {
        final String sql = "select points from `T_SETTINGS` where mall_id=?";

        final String rule = jdbcTemplate.query(sql, new Object[]{mallId}, new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {

                if (rs.next()) {

                    String _rule = rs.getString("points");

                    return _rule;

                }

                return null;
            }
        });

        return rule;
    }

    public String selectCouponRule(int mallId) {
        final String sql = "select coupon from `T_SETTINGS` WHERE mall_id=?";

        final String rule = jdbcTemplate.query(sql, new Object[] {mallId}, new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {

                if (rs.next()) {

                    String _rule = rs.getString("coupon");

                    return _rule;

                }

                return null;
            }
        });

        return rule;
    }

    public float selectPointsCoefficient(int mallId) {
        final String sql = "select points_coefficient from `T_SETTINGS` where mall_id=?";

        final Float coefficient = jdbcTemplate.query(sql, new Object[] {mallId}, new ResultSetExtractor<Float>() {
            @Override
            public Float extractData(ResultSet rs) throws SQLException, DataAccessException {

                if (rs.next()) {

                    Float _coefficient = rs.getFloat("points_coefficient");

                    return _coefficient;

                }

                return 1.0f;
            }
        });

        return coefficient.floatValue();
    }

    public int updatePointsRule(String rule, int mallId) {
        final String sql = "update `T_SETTINGS` set points=? where mall_id=?";
        return jdbcTemplate.update(sql, rule, mallId);
    }

    public int updateCouponRule(String rule, int mallId) {
        final String sql = "update `T_SETTINGS` set coupon=? where mall_id=?";
        return jdbcTemplate.update(sql, rule, mallId);
    }

    public int updatePointsCoefficient(float pointsCoefficient, int mallId) {
        final String sql = "update `T_SETTINGS` set points_coefficient=? where mall_id=?";
        return jdbcTemplate.update(sql, pointsCoefficient, mallId);
    }

    public int insertSettings(int mallId) {
        final String sql = "insert into `T_SETTINGS` (points,coupon,points_coefficient,mall_id) values(?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "");
                ps.setString(2, "");
                ps.setFloat(3, 1f);
                ps.setInt(4, mallId);

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public Reward selectRewardFromId(int category) {
        final String sql = "select reward_id,reward_type,reward_value from `T_REWARD` where reward_id=?";

        return jdbcTemplate.queryForObject(sql, new Object[] {category},
                (rs, rowNum) -> {
                    Reward reward = new Reward();
                    reward.setReward_id(rs.getInt("reward_id"));
                    reward.setReward_type(rs.getInt("reward_type"));
                    reward.setReward_value(rs.getInt("reward_value"));

                    return reward;
                });
    }

    public int updateReward(Reward reward) {
        final String sql = "UPDATE `T_REWARD` SET reward_type=?,reward_value=? " +
                "WHERE reward_id=?";

        return jdbcTemplate.update(sql, new Object[]{reward.getReward_type(), reward.getReward_value(),reward.getReward_id()});
    }

}
