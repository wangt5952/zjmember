package com.laf.manager.repository;

import com.laf.manager.dto.Industry;
import com.laf.manager.utils.db.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class IndustryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbUtils dbUtils;

    public List<Industry> selectAllIndustries(final Integer mall_id) {
        final String sql = "select industry_id,industry_name,sort_id,mall_id from `T_INDUSTRIES` " +
                "where mall_id=? order by sort_id";

        return jdbcTemplate.query(sql, new Object[]{mall_id}, new int[]{Types.INTEGER}, new IndustryRowMapper());
    }

    public int insertIndustry(final Industry industry) {
        final String sql = "insert into `T_INDUSTRIES` (industry_name,sort_id,mall_id) " +
                "values(?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, industry.getIndustry_name());
                ps.setInt(2, industry.getSort_id());
                ps.setInt(3, industry.getMall_id());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public Industry selectIndustryById(final Integer id) {
        final String sql = "select industry_id,industry_name,sort_id,mall_id from `T_INDUSTRIES` " +
                "where industry_id=?";

        final Industry industry = jdbcTemplate.query(sql, new Object[]{id}, new ResultSetExtractor<Industry>() {
            @Override
            public Industry extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    Industry _industry = new Industry();
                    _industry.setIndustry_id(rs.getInt("industry_id"));
                    _industry.setIndustry_name(rs.getString("industry_name"));
                    _industry.setMall_id(rs.getInt("mall_id"));
                    _industry.setSort_id(rs.getInt("sort_id"));

                    return _industry;
                }

                return null;
            }
        });

        return industry;
    }

    public int updateIndustry(final Industry industry) {
        final String sql = "update `T_INDUSTRIES` set industry_name=?,sort_id=? where industry_id=?";

        return jdbcTemplate.update(sql, industry.getIndustry_name(), industry.getSort_id(), industry.getIndustry_id());

    }

    public int deleteIndustryById(final Integer industryId) {
        final String sql = "delete from `T_INDUSTRIES` where industry_id=?";

        return jdbcTemplate.update(sql, industryId);
    }

    class IndustryRowMapper implements RowMapper<Industry> {

        @Override
        public Industry mapRow(ResultSet rs, int rowNum) throws SQLException {
            Industry industry = new Industry();
            if (dbUtils.hasColumn(rs, "industry_id")) industry.setIndustry_id(rs.getInt("industry_id"));
            if (dbUtils.hasColumn(rs, "industry_name")) industry.setIndustry_name(rs.getString("industry_name"));
            if (dbUtils.hasColumn(rs, "mall_id")) industry.setMall_id(rs.getInt("mall_id"));
            if (dbUtils.hasColumn(rs, "sort_id")) industry.setSort_id(rs.getInt("sort_id"));

            return industry;
        }
    }
}
