package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Level;
import com.laf.mall.api.utils.db.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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

    public Level selectLevelByLow(final Integer mall_id) {
        final String sql = "select level_id,level_name from `T_MEMBER_LEVEL` where amount_low=0 and mall_id=?";

//        final Level level = jdbcTemplate.queryForObject(sql, new Object[]{mall_id}, new LevelRowMapper());

       final Level level = jdbcTemplate.query(sql, new Object[]{mall_id}, new ResultSetExtractor<Level>() {
           @Override
           public Level extractData(ResultSet rs) throws SQLException, DataAccessException {
               if (rs.next()) {
                   Level level = new Level();
                   if (dbUtils.hasColumn(rs, "level_id")) level.setLevel_id(rs.getInt("level_id"));
                   if (dbUtils.hasColumn(rs, "level_name")) level.setLevel_name(rs.getString("level_name"));

                   return level;
               }

               return null;
           }
       });

       return level;
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

    public List<Level> selectAllLevelsByMall(final Integer mallId) {
        final String sql = "select level_id,level_name,mall_id from `T_MEMBER_LEVEL` where mall_id=?";

        final List<Level> list = jdbcTemplate.query(sql, new Object[]{mallId}, new ResultSetExtractor<List<Level>>() {
            @Override
            public List<Level> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Level> _list = new ArrayList<>();

                while (rs.next()) {
                    Level level = new Level();
                    level.setLevel_id(rs.getInt("level_id"));
                    level.setLevel_name(rs.getString("level_name"));
                    level.setMall_id(rs.getInt("mall_id"));

                    _list.add(level);
                }

                return _list;
            }
        });

        return list;
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

    class LevelRowMapper implements RowMapper<Level> {

        @Override
        public Level mapRow(ResultSet rs, int rowNum) throws SQLException {
            Level level = new Level();

            if (dbUtils.hasColumn(rs, "level_id")) level.setLevel_id(rs.getInt("level_id"));

            if (dbUtils.hasColumn(rs, "level_name")) level.setLevel_name(rs.getString("level_name"));

            return level;
        }
    }
}
