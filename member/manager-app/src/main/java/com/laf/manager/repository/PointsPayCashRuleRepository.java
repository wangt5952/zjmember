package com.laf.manager.repository;

import com.laf.manager.dto.PointsPayCashRule;
import com.laf.manager.enums.PointsPayCashType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PointsPayCashRuleRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public List<PointsPayCashRule> selectPointsPayCashRuleList(final Integer mallId) {
        final String sql = "SELECT ppcr_id,points,type_name FROM `T_POINTS_PAY_CASH_RULE` WHERE mall_id=?";

        List<PointsPayCashRule> list = jdbcTemplate.query(sql, new Object[]{mallId}, new ResultSetExtractor<List<PointsPayCashRule>>() {
            @Override
            public List<PointsPayCashRule> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<PointsPayCashRule> _list = new ArrayList<>();

                while (rs.next()) {
                    PointsPayCashRule rule = new PointsPayCashRule();
                    rule.setPpcr_id(rs.getInt("ppcr_id"));
                    rule.setPoints(rs.getInt("points"));
                    rule.setType_name(rs.getString("type_name"));
                    _list.add(rule);
                }
                return _list;
            }
        });
        return list;
    }

    public PointsPayCashRule selectPointsPayCashRuleById(final Integer ppcrId) {
        final String sql = "SELECT ppcr_id,points,mall_id,type_name FROM `T_POINTS_PAY_CASH_RULE` WHERE ppcr_id=?";

        PointsPayCashRule rule = jdbcTemplate.query(sql, new Object[]{ppcrId}, new ResultSetExtractor<PointsPayCashRule>() {
            @Override
            public PointsPayCashRule extractData(ResultSet rs) throws SQLException, DataAccessException {
                PointsPayCashRule _rule = null;

                if (rs.next()) {
                    _rule = new PointsPayCashRule();
                    _rule.setPpcr_id(rs.getInt("ppcr_id"));
                    _rule.setPoints(rs.getInt("points"));
                    _rule.setMall_id(rs.getInt("mall_id"));
                    _rule.setType_name(rs.getString("type_name"));
                }
                return _rule;
            }
        });
        return rule;
    }

    public int insertPointsPayCashRule(PointsPayCashRule rule) {
        final String sql = "INSERT INTO `T_POINTS_PAY_CASH_RULE` (points,mall_id,type_name) VALUES (?,?,?)";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, rule.getPoints());
                ps.setInt(2, rule.getMall_id());
                ps.setString(3, rule.getType_name());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int updatePointsPayCashRule(PointsPayCashRule rule) {
        final String sql = "UPDATE `T_POINTS_PAY_CASH_RULE` SET points=?,mall_id=?,type_name=? WHERE ppcr_id=?";

        return jdbcTemplate.update(sql, rule.getPoints(), rule.getMall_id(), rule.getType_name(), rule.getPpcr_id());
    }

    public int deletePointsPayCashRule(final Integer ppcrId) {
        final String sql = "DELETE FROM `T_POINTS_PAY_CASH_RULE` WHERE ppcr_id=?";

        return jdbcTemplate.update(sql, ppcrId);
    }
}
