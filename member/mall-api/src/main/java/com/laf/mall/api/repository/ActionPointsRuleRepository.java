package com.laf.mall.api.repository;

import com.laf.mall.api.dto.ActionPointsRule;
import com.laf.mall.api.utils.db.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Types;

@Repository
@Slf4j
public class ActionPointsRuleRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DbUtils dbUtils;

    public int selectActionPointsByType(final Integer ruleType, final Integer mallId) {
        final String sql = "select points from `T_ACTION_POINTS_RULE` " +
                "where rule_type=? and on_off=1 and mall_id=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{ruleType, mallId});
        int points = 0;
        if (row.first()) points = row.getInt("points");

        return points;
    }

    public ActionPointsRule selectActionPointsRuleByOption(final Integer ruleType, final Integer mallId) {
        String sql = "SELECT points, coupon_id, options " +
                "FROM `T_ACTION_POINTS_RULE` WHERE rule_type=? AND on_off=1 and mall_id=?";

        SqlRowSet rowset = jdbcTemplate.queryForRowSet(sql, new Object[]{ruleType, mallId}, new int[]{Types.INTEGER, Types.INTEGER});

        ActionPointsRule rule = null;

        try {
            if (rowset.first()) {
                rule = new ActionPointsRule();
                if (dbUtils.hasColumn(rowset, "points")) rule.setPoints(rowset.getInt("points"));
                if (dbUtils.hasColumn(rowset, "coupon_id")) rule.setCoupon_id(rowset.getInt("coupon_id"));
                if (dbUtils.hasColumn(rowset, "options")) rule.setOptions(rowset.getInt("options"));
            }
        } catch (Exception e) {

        }
        return rule;
    }
}
