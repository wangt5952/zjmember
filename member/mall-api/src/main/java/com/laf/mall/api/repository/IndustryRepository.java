package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Industry;
import com.laf.mall.api.utils.db.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class IndustryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbUtils dbUtils;

    public List<Industry> selectAllIndustries(final Integer mall_id) {
        String sql = "select industry_id, industry_name from `T_INDUSTRIES` where mall_id=?";

        return jdbcTemplate.query(sql, new Object[]{mall_id}, new int[]{Types.INTEGER}, new IndustryRowMapper());
    }

    class IndustryRowMapper implements RowMapper<Industry> {

        @Override
        public Industry mapRow(ResultSet rs, int rowNum) throws SQLException {
            Industry industry = new Industry();
            if (dbUtils.hasColumn(rs, "industry_id")) industry.setIndustry_id(rs.getInt("industry_id"));
            if (dbUtils.hasColumn(rs, "industry_name")) industry.setIndustry_name(rs.getString("industry_name"));

            return industry;
        }
    }
}
