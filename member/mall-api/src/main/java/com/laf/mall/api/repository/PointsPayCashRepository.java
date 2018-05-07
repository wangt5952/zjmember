package com.laf.mall.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PointsPayCashRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int selectPointsPayCashRuleByType(final int type, final int mallId) {
        final String sql = "SELECT points FROM `T_POINTS_PAY_CASH_RULE` WHERE type=? and mall_id=?";

        int points = jdbcTemplate.query(sql, new Object[]{type, mallId}, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                int _points = 0;

                if (rs.next()) {
                    _points = rs.getInt("points");
                }
                return _points;
            }
        });
        return points;
    }

}
