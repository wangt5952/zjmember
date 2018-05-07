package com.laf.manager.repository;


import com.laf.manager.dto.Admin;
import com.laf.manager.utils.db.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AdminRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DbUtils dbUtils;

    public Admin getAdminById(final Integer adminId) {
        final String sql = "select admin_id,account,password,mobile from `T_ADMIN` where admin_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{adminId}, new AdminRowMapper());
    }

    class AdminRowMapper implements RowMapper<Admin> {
        @Override
        public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
            Admin admin = new Admin();

            if (dbUtils.hasColumn(rs, "admin_id"))
                admin.setAdmin_id(rs.getInt("admin_id"));

            if (dbUtils.hasColumn(rs, "account"))
                admin.setAccount(rs.getString("account"));

            if (dbUtils.hasColumn(rs, "password"))
                admin.setPassword(rs.getString("password"));

            if (dbUtils.hasColumn(rs, "mobile"))
                admin.setMobile(rs.getString("mobile"));

            if (dbUtils.hasColumn(rs, "mall_id"))
                admin.setMall_id(rs.getInt("mall_id"));

            return admin;
        }
    }
}
