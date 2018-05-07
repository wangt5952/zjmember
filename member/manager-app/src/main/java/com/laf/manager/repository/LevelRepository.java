package com.laf.manager.repository;

import com.laf.manager.dto.Level;
import com.laf.manager.querycondition.level.LevelQueryCondition;
import com.laf.manager.utils.db.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class LevelRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DbUtils dbUtils;

    public Level selectLevelByLow(final Integer mall_id) throws EmptyResultDataAccessException {
        final String sql = "select level_id,level_name from `T_MEMBER_LEVEL` where amount_low=0 and mall_id=?";

        final Level level = jdbcTemplate.queryForObject(sql, new Object[]{mall_id},
                (rs, rowNum) -> new Level(rs.getInt("level_id"), rs.getString("level_name")));

        return level;

    }

    public int insertLevelForAmount(final Level level) {
        final String sql = "insert into `T_MEMBER_LEVEL` (level_name,mall_id,amount_low,amount_high,algorithm) " +
                "values(?,?,?,?,1)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, level.getLevel_name());
                ps.setInt(2, level.getMall_id());
                ps.setBigDecimal(3, level.getAmount_low());
                ps.setBigDecimal(4, level.getAmount_high());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int insertLevel(final Level level) {
        final String sql = "insert into `T_MEMBER_LEVEL` (level_name,mall_id,points_low,points_high) " +
                "values(?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, level.getLevel_name());
                ps.setInt(2, level.getMall_id());
                ps.setInt(3, level.getPoints_low());
                ps.setInt(4, level.getPoints_high());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int updateLevel(final Level level) {
        final String sql = "update `T_MEMBER_LEVEL` set level_name=?,mall_id=?,points_low=?,points_high=?" +
                " where level_id=?";

        int result = jdbcTemplate.update(sql, level.getLevel_name(), level.getMall_id(),
                level.getPoints_low(), level.getPoints_high(), level.getLevel_id());

        return result;
    }

    public int updateLevelForAmount(final Level level) {
        final String sql = "update `T_MEMBER_LEVEL` set level_name=?,mall_id=?,amount_low=?,amount_high=?" +
                " where level_id=?";

        int result = jdbcTemplate.update(sql, level.getLevel_name(), level.getMall_id(),
                level.getAmount_low(), level.getAmount_high(), level.getLevel_id());

        return result;
    }

    public int deleteLevelById(final Integer levelId) {

        if (levelId == null || levelId <= 0) return 0;

        String sql = "delete from `T_MEMBER_LEVEL` where level_id=?";

        int result = jdbcTemplate.update(sql, levelId);

        return result;
    }

    public List<Level> selectLevels(LevelQueryCondition condition) {

        if (condition.getMallId() == null || condition.getMallId() <= 0
                || condition.getAlgorithm() == null) return ListUtils.EMPTY_LIST;

        Object[] args;
        if (!StringUtils.isEmpty(condition.getKeyWords())) {
            args = new Object[]{condition.getMallId(), condition.getKeyWords()};
        } else {
            args = new Object[]{condition.getMallId()};
        }

        List<Level> list;
        switch (condition.getAlgorithm()) {
            case 0:
                list = selectAllLevelsForPoints(condition.getKeyWords(), args);
                break;

            case 1:
                list = selectAllLevelsForAmount(condition.getKeyWords(), args);
                break;

            default:
                list = ListUtils.EMPTY_LIST;
        }
        return list;
    }

    private List<Level> selectAllLevelsForPoints(String keyWords, Object[] args) {
        String sql = "select level_id,level_name,points_low,points_high,mall_id from `T_MEMBER_LEVEL`" +
                " where mall_id=?";

        StringBuilder sb = new StringBuilder(sql);
        if (!StringUtils.isEmpty(keyWords)) {
            sb.append(" and text1 like '%' ? '%'");
        }

        List<Level> list = jdbcTemplate.query(sb.toString(), args, new ResultSetExtractor<List<Level>>() {
            @Override
            public List<Level> extractData(ResultSet rs) throws SQLException, DataAccessException {
                ArrayList<Level> list = new ArrayList<>();

                while (rs.next()) {
                    Level level = new Level();
                    level.setLevel_id(rs.getInt("level_id"));
                    level.setLevel_name(rs.getString("level_name"));
                    level.setPoints_low(rs.getInt("points_low"));
                    level.setPoints_high(rs.getInt("points_high"));
                    level.setMall_id(rs.getInt("mall_id"));
                    list.add(level);
                }
                return list;
            }
        });

        return list;
    }

    private List<Level> selectAllLevelsForAmount(String keyWords, Object[] args) {
        String sql = "select level_id,level_name,amount_low,amount_high,mall_id from `T_MEMBER_LEVEL`" +
                " where mall_id=?";

        StringBuilder sb = new StringBuilder(sql);
        if (!StringUtils.isEmpty(keyWords)) {
            sb.append(" and text1 like '%' ? '%'");
        }

        final List<Level> list = jdbcTemplate.query(sb.toString(), args, new ResultSetExtractor<List<Level>>() {
            @Override
            public List<Level> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Level> _list = new ArrayList<>();

                while (rs.next()) {
                    Level level = new Level();
                    level.setLevel_id(rs.getInt("level_id"));
                    level.setLevel_name(rs.getString("level_name"));
                    level.setAmount_high(rs.getBigDecimal("amount_high"));
                    level.setAmount_low(rs.getBigDecimal("amount_low"));
                    level.setMall_id(rs.getInt("mall_id"));

                    _list.add(level);
                }

                return _list;
            }
        });
        return list;
    }

    public Level selectLevelById(final Integer levelId) {

        if (levelId == null || levelId <= 0) return null;

        String sql = "select level_id,level_name,points_low,points_high,amount_low,amount_high,mall_id,algorithm from `T_MEMBER_LEVEL` where level_id=?";

        Level level = jdbcTemplate.query(sql, new Object[]{levelId}, new ResultSetExtractor<Level>() {
            @Override
            public Level extractData(ResultSet rs) throws SQLException, DataAccessException {
                Level level = null;

                if (rs.next()) {
                    level = new Level();
                    level.setLevel_id(rs.getInt("level_id"));
                    level.setLevel_name(rs.getString("level_name"));
                    level.setPoints_low(rs.getInt("points_low"));
                    level.setPoints_high(rs.getInt("points_high"));
                    level.setAmount_low(rs.getBigDecimal("amount_low"));
                    level.setAmount_high(rs.getBigDecimal("amount_high"));
                    level.setMall_id(rs.getInt("mall_id"));
                    level.setAlgorithm(rs.getInt("algorithm"));
                }

                return level;
            }
        });
        return level;
    }

    public List<Level> selectAllLevelsByMall(final Integer mallId) {

        if (mallId == null || mallId <= 0) return new ArrayList<>();

        final String sql = "select level_id,level_name,points_low,points_high,amount_low,amount_high,mall_id from `T_MEMBER_LEVEL` where mall_id=?";

        List<Level> list = jdbcTemplate.query(sql, new Object[]{mallId}, new ResultSetExtractor<List<Level>>() {
            @Override
            public List<Level> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Level> _list = new ArrayList<>();

                while (rs.next()) {
                    Level level = new Level();
                    level.setLevel_id(rs.getInt("level_id"));
                    level.setLevel_name(rs.getString("level_name"));
                    level.setPoints_low(rs.getInt("points_low"));
                    level.setPoints_high(rs.getInt("points_high"));
                    level.setAmount_low(rs.getBigDecimal("amount_low"));
                    level.setAmount_high(rs.getBigDecimal("amount_high"));
                    level.setMall_id(rs.getInt("mall_id"));
                    _list.add(level);
                }

                return _list;
            }
        });

        return list;
    }

    public List<Level> selectAllUsableLevelByMall(final Integer mallId) {
        if (mallId == null || mallId <= 0) return new ArrayList<>();

        final String sql = "select level_id,level_name,points_low,points_high,amount_low,amount_high,mall_id from `T_MEMBER_LEVEL` " +
                "where mall_id=? and used=0";

        List<Level> list = jdbcTemplate.query(sql, new Object[]{mallId}, new ResultSetExtractor<List<Level>>() {
            @Override
            public List<Level> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Level> _list = new ArrayList<>();

                while (rs.next()) {
                    Level level = new Level();
                    level.setLevel_id(rs.getInt("level_id"));
                    level.setLevel_name(rs.getString("level_name"));
                    level.setPoints_low(rs.getInt("points_low"));
                    level.setPoints_high(rs.getInt("points_high"));
                    level.setAmount_low(rs.getBigDecimal("amount_low"));
                    level.setAmount_high(rs.getBigDecimal("amount_high"));
                    level.setMall_id(rs.getInt("mall_id"));
                    _list.add(level);
                }

                return _list;
            }
        });

        return list;
    }

    public int updateLevelUsableStatus(final Integer levelId, final Integer usable) {
        final String sql = "update `T_MEMBER_LEVEL` set used=?" +
                " where level_id=?";

        int result = jdbcTemplate.update(sql, usable, levelId);

        return result;
    }

    public int selectIsAmountInLevel(BigDecimal amount, int levelId) {
        String sql = "select count(1) c from `T_MEMBER_LEVEL` where level_id=? and ? <= amount_high and ? >=amount_low";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{levelId, amount, amount}, new int[]{Types.INTEGER, Types.DECIMAL, Types.DECIMAL});
        rowSet.first();

        return rowSet.getInt("c");
    }

    public List<Level> selectLevelWidthAmount(BigDecimal amount) {
        String sql = "select level_id, level_name from `T_MEMBER_LEVEL` where ? >= amount_low order by amount_low desc";

        List<Level> level = jdbcTemplate.query(sql, new Object[]{amount}, new int[]{Types.DECIMAL}, new ResultSetExtractor<List<Level>>() {
            @Override
            public List<Level> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Level> _level = new ArrayList<>();

                while (rs.next()) {
                    Level l = new Level();
                    l.setLevel_id(rs.getInt("level_id"));
                    l.setLevel_name(rs.getString("level_name"));
                    _level.add(l);
                }
                return _level;
            }
        });

        return level;
    }
//    class LevelRowMapper implements RowMapper<Level> {
//
//        @Override
//        public Level mapRow(ResultSet rs, int rowNum) throws SQLException {
//            Level level = new Level();
//
//            if (dbUtils.hasColumn(rs, "level_id")) level.setLevel_id(rs.getInt("level_id"));
//
//            if (dbUtils.hasColumn(rs, "level_name")) level.setLevel_name(rs.getString("level_name"));
//
//            return level;
//        }
//    }
}
