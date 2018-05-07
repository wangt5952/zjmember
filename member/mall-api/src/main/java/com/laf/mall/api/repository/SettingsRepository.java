package com.laf.mall.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class SettingsRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

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
}
