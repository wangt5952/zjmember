package com.laf.mall.api.repository;

import com.laf.mall.api.dto.PromotionPointsQuery;
import com.laf.mall.api.dto.PromotionPointsRule;
import com.laf.mall.api.querycondition.points.PromotionQueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;

@Repository
public class PromotionPointsRuleRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int insertPromotionPointsRule(PromotionPointsRule promotionPointsRule) {

        final String sql = "insert into `T_PROMOTION_POINTS_RULE` " +
                "(start_date,end_date,amount,rule_name,mall_id,birthday_start,birthday_end,rule_content) " +
                "values(?,?,?,?,?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, promotionPointsRule.getStart_date());
                ps.setLong(2, promotionPointsRule.getEnd_date());
                ps.setBigDecimal(3, promotionPointsRule.getAmount());
                ps.setString(4, promotionPointsRule.getRule_name());
                ps.setInt(5, promotionPointsRule.getMall_id());
                ps.setLong(6, promotionPointsRule.getBirthday_start());
                ps.setLong(7, promotionPointsRule.getBirthday_end());
                ps.setString(8, promotionPointsRule.getRule_content());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int insertPromotionPointsRuleWithoutBirthday(PromotionPointsRule promotionPointsRule) {

        final String sql = "insert into `T_PROMOTION_POINTS_RULE` " +
                "(start_date,end_date,amount,rule_name,mall_id,rule_content) " +
                "values(?,?,?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, promotionPointsRule.getStart_date());
                ps.setLong(2, promotionPointsRule.getEnd_date());
                ps.setBigDecimal(3, promotionPointsRule.getAmount());
                ps.setString(4, promotionPointsRule.getRule_name());
                ps.setInt(5, promotionPointsRule.getMall_id());
                ps.setString(6, promotionPointsRule.getRule_content());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public PromotionPointsRule selectRuleContentById(final Integer ruleId) {
        final String sql = "select ppr_id,start_date,end_date,amount,rule_name,mall_id,birthday_start,birthday_end,rule_content " +
                "from `T_PROMOTION_POINTS_RULE` where ppr_id=?";


        final PromotionPointsRule rule = jdbcTemplate.query(sql, new Object[]{ruleId}, new ResultSetExtractor<PromotionPointsRule>() {
            @Override
            public PromotionPointsRule extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    PromotionPointsRule _rule = new PromotionPointsRule();

                    _rule.setPpr_id(rs.getInt("ppr_id"));
                    _rule.setBirthday_start(new Date(rs.getLong("birthday_start")));
                    _rule.setBirthday_end(new Date(rs.getLong("birthday_end")));
                    _rule.setStart_date(new Date(rs.getLong("start_date")));
                    _rule.setEnd_date(new Date(rs.getLong("end_date")));
                    _rule.setMall_id(rs.getInt("mall_id"));
                    _rule.setRule_name(rs.getString("rule_name"));
                    _rule.setRule_content(rs.getString("rule_content"));
                    _rule.setAmount(rs.getBigDecimal("amount"));

                    return _rule;
                }

                return null;
            }
        });

        return rule;
    }

    public int insertDayPointsRule(final Integer ruleId, final Long day) {

        final String sql = "insert into `T_DAY_POINTS_RULES` " +
                "(rule_id,`day`) values(?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, ruleId);
                ps.setLong(2, day);

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int insertPointsQuery(final PromotionPointsQuery promotionPointsQuery) {

        final String sql = "insert into `T_PROMOTION_POINTS_QUERY` " +
                "(rule_id,level_id,shop_id,birthday_start,birthday_end,amount) values(?,?,?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, promotionPointsQuery.getRule_id());
                ps.setInt(2, promotionPointsQuery.getLevel_id());
                ps.setInt(3, promotionPointsQuery.getShop_id());
                ps.setLong(4, promotionPointsQuery.getBirthday_start());
                ps.setLong(5, promotionPointsQuery.getBirthday_end());
                ps.setBigDecimal(6, promotionPointsQuery.getAmount());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public BigDecimal selectPromotionPointsRule(PromotionQueryCondition condition) {
        final String sql = "select min(amount) amount from (select q.rule_id, min(q.amount) amount from `T_DAY_POINTS_RULES` d " +
                "inner join `T_PROMOTION_POINTS_QUERY` q on d.rule_id=q.rule_id " +
                "where d.day=? and q.birthday_start <=? and q.birthday_end >= ? and q.level_id=? and q.shop_id=? " +
                "group by q.rule_id) t";

        final BigDecimal amount = jdbcTemplate.query(sql,
                new Object[]{condition.getToday(),condition.getBirthday(),condition.getBirthday(), condition.getLevelId(),condition.getShopId()},
                new ResultSetExtractor<BigDecimal>() {

            @Override
            public BigDecimal extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    BigDecimal _amount = rs.getBigDecimal("amount");
                    return _amount;
                }

                return null;
            }
        });

        return amount;
    }

    public int deletePromotionRule(final Integer ruleId) {
        final String sql = "delete from `T_PROMOTION_POINTS_RULE` where ppr_id=?";

        return jdbcTemplate.update(sql, ruleId);
    }

    public int deletePromotionQuery(final Integer ruleId) {
        final String sql = "delete from `T_PROMOTION_POINTS_QUERY` where rule_id=?";

        return jdbcTemplate.update(sql, ruleId);
    }

    public int deleteDayPointsRule(final Integer ruleId) {
        final String sql = "delete from `T_DAY_POINTS_RULES` where rule_id=?";

        return jdbcTemplate.update(sql, ruleId);
    }
}
