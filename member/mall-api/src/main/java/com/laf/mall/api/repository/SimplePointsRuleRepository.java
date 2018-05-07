package com.laf.mall.api.repository;

import com.laf.mall.api.dto.IndustryPointsAssociation;
import com.laf.mall.api.dto.ShopPointsAssociation;
import com.laf.mall.api.dto.SimplePointsQuery;
import com.laf.mall.api.dto.SimplePointsRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SimplePointsRuleRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int insertPointsRule(final SimplePointsRule rule) {
        final String sql = "insert into `T_SIMPLE_POINTS_RULE` " +
                "(level_id,amount,mall_id) values(?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, rule.getLevel_id());
                ps.setBigDecimal(2, rule.getAmount());
                ps.setInt(3, rule.getMall_id());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int insertShopPointsAssociation(final ShopPointsAssociation shopPointsAssociation) {
        final String sql = "insert into `T_SHOP_POINTS` (shop_id,amount,rule_id,rule_type) " +
                "values(?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, shopPointsAssociation.getShop_id());
                ps.setBigDecimal(2, shopPointsAssociation.getAmount());
                ps.setInt(3, shopPointsAssociation.getRule_id());
                ps.setInt(4, shopPointsAssociation.getRule_type());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int insertIndustryPointsAssociation(final IndustryPointsAssociation industryPointsAssociation) {
        final String sql = "insert into `T_INDUSTRY_POINTS` (industry_id,amount,rule_id,rule_type) " +
                "values(?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, industryPointsAssociation.getIndustry_id());
                ps.setBigDecimal(2, industryPointsAssociation.getAmount());
                ps.setInt(3, industryPointsAssociation.getRule_id());
                ps.setInt(4, industryPointsAssociation.getRule_type());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int insertPointsQuery(final SimplePointsRule rule, final Integer shopId) {
        final String sql = "insert into `T_SIMPLE_POINTS_QUERY` (level_id,shop_id,amount,mall_id) " +
                "values(?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, rule.getLevel_id());
                ps.setInt(2, shopId);
                ps.setBigDecimal(3, rule.getAmount());
                ps.setInt(4, rule.getMall_id());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int insertPointsQuery(final SimplePointsQuery simplePointsQuery) {
        final String sql = "insert into `T_SIMPLE_POINTS_QUERY` (level_id,shop_id,amount,mall_id) " +
                "values(?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, simplePointsQuery.getLevel_id());
                ps.setInt(2, simplePointsQuery.getShop_id());
                ps.setBigDecimal(3, simplePointsQuery.getAmount());
                ps.setInt(4, simplePointsQuery.getMall_id());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int updatePointsQuery(final SimplePointsQuery simplePointsQuery) {
        final String sql = "update `T_SIMPLE_POINTS_QUERY` set amount=? " +
                "where level_id=? and shop_id=?";

        return jdbcTemplate.update(sql, simplePointsQuery.getAmount(), simplePointsQuery.getLevel_id(),
                simplePointsQuery.getShop_id());

    }

    public List<SimplePointsRule> selectAllSimpleRule(final Integer mallId) {
        final String sql = "select spr_id,level_id,amount,mall_id from `T_SIMPLE_POINTS_RULE` where mall_id=?";

        final List<SimplePointsRule> list = jdbcTemplate.query(sql, new Object[]{mallId}, new ResultSetExtractor<List<SimplePointsRule>>() {
            @Override
            public List<SimplePointsRule> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<SimplePointsRule> _list = new ArrayList<>();

                while (rs.next()) {

                    SimplePointsRule simplePointsRule = new SimplePointsRule();
                    simplePointsRule.setSpr_id(rs.getInt("spr_id"));
                    simplePointsRule.setLevel_id(rs.getInt("level_id"));
                    simplePointsRule.setAmount(rs.getBigDecimal("amount"));
                    simplePointsRule.setMall_id(rs.getInt("mall_id"));

                    _list.add(simplePointsRule);
                }

                return _list;
            }
        });

        return list;
    }

    public List<SimplePointsQuery> selectSimpleAmountListByIndustry(final Integer mallId) {
        final String sql = "select ip.amount,spr.level_id,s.shop_id,spr.mall_id " +
                "from `T_SIMPLE_POINTS_RULE` spr inner join `T_INDUSTRY_POINTS` ip " +
                "on spr.spr_id=ip.rule_id inner join `T_SHOP` s on ip.industry_id=s.industry " +
                "where ip.rule_type=0 and spr.mall_id=?";

        final List<SimplePointsQuery> list = jdbcTemplate.query(sql, new Object[]{mallId},
                new ResultSetExtractor<List<SimplePointsQuery>>() {

            @Override
            public List<SimplePointsQuery> extractData(ResultSet rs) throws SQLException, DataAccessException {

                List<SimplePointsQuery> _list = new ArrayList<>();

                while (rs.next()) {
                    SimplePointsQuery simplePointsQuery = new SimplePointsQuery();
                    simplePointsQuery.setAmount(rs.getBigDecimal("amount"));
                    simplePointsQuery.setLevel_id(rs.getInt("level_id"));
                    simplePointsQuery.setMall_id(rs.getInt("mall_id"));
                    simplePointsQuery.setShop_id(rs.getInt("shop_id"));

                    _list.add(simplePointsQuery);
                }

                return _list;
            }

        });

        return list;

    }

    public List<SimplePointsQuery> selectSimpleAmountListByShopInMall(final Integer mallId) {
        final String sql = "select sp.shop_id,sp.amount,spr.level_id,spr.mall_id " +
                "from `T_SIMPLE_POINTS_RULE` spr inner join `T_SHOP_POINTS` sp " +
                "on spr.spr_id=sp.rule_id " +
                "where sp.rule_type=0 and spr.mall_id=?";

        final List<SimplePointsQuery> list = jdbcTemplate.query(sql, new Object[]{mallId},
                new ResultSetExtractor<List<SimplePointsQuery>>() {

            @Override
            public List<SimplePointsQuery> extractData(ResultSet rs) throws SQLException, DataAccessException {

                List<SimplePointsQuery> _list = new ArrayList<>();

                while (rs.next()) {
                    SimplePointsQuery simplePointsQuery = new SimplePointsQuery();

                    simplePointsQuery.setShop_id(rs.getInt("shop_id"));
                    simplePointsQuery.setMall_id(rs.getInt("mall_id"));
                    simplePointsQuery.setLevel_id(rs.getInt("level_id"));
                    simplePointsQuery.setAmount(rs.getBigDecimal("amount"));

                    _list.add(simplePointsQuery);
                }

                return _list;
            }
        });

        return list;
    }

    public BigDecimal selectSimpleAmount(final Integer levelId, final Integer shopId) {
        final String sql = "select amount from `T_SIMPLE_POINTS_QUERY` where level_id=? and shop_id=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{levelId, shopId});
        BigDecimal amount = null;

        if (row.first()) {
            amount = row.getBigDecimal("amount");
        }

        return amount;
    }

    public SimplePointsRule selectSimpleRule(final Integer ruleId) {
        final String sql = "select spr_id,level_id,amount,mall_id from `T_SIMPLE_POINTS_RULE` where spr_id=?";

        final SimplePointsRule rule = jdbcTemplate.query(sql, new Object[]{ruleId}, new ResultSetExtractor<SimplePointsRule>() {
            @Override
            public SimplePointsRule extractData(ResultSet rs) throws SQLException, DataAccessException {

                if (rs.next()) {

                    SimplePointsRule simplePointsRule = new SimplePointsRule();
                    simplePointsRule.setSpr_id(rs.getInt("spr_id"));
                    simplePointsRule.setLevel_id(rs.getInt("level_id"));
                    simplePointsRule.setAmount(rs.getBigDecimal("amount"));
                    simplePointsRule.setMall_id(rs.getInt("mall_id"));

                    return simplePointsRule;

                }

                return null;
            }
        });

        return rule;
    }

    public int deleteSimpleRule(final Integer ruleId) {
        final String sql = "delete from `T_SIMPLE_POINTS_RULE` where spr_id=?";

        return jdbcTemplate.update(sql, ruleId);
    }

    public int deleteShopPoints(final Integer ruleId) {
        final String sql = "delete from `T_SHOP_POINTS` where rule_id=?";

        return jdbcTemplate.update(sql, ruleId);
    }

    public int deleteIndustryPoints(final Integer ruleId) {
        final String sql = "delete from `T_INDUSTRY_POINTS` where rule_id=?";

        return jdbcTemplate.update(sql, ruleId);
    }

    public int deleteSimpleQuery(final Integer levelId) {
        final String sql = "delete from `T_SIMPLE_POINTS_QUERY` where level_id=?";

        return jdbcTemplate.update(sql, levelId);
    }

}
