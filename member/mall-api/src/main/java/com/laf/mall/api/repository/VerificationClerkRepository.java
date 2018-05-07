package com.laf.mall.api.repository;

import com.laf.mall.api.dto.VerificationClerk;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
@Slf4j
public class VerificationClerkRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int insertVerificationClerkForServices(VerificationClerk verificationClerk) {
        final String sql = "insert into `T_VERIFICATION_CLERK` (" +
                "vc_name," +
                "phone," +
                "vc_type," +
                "department," +
                "mall_id," +
                "member_id," +
                "reg_date" +
                ") values(?,?,?,?,?,?,?)";


        int result = 0;

        try {
            result = jdbcTemplate.update(sql,
                    verificationClerk.getVc_name(),
                    verificationClerk.getPhone(),
                    verificationClerk.getVc_type(),
                    verificationClerk.getDepartment(),
                    verificationClerk.getMall_id(),
                    verificationClerk.getMember_id(),
                    verificationClerk.getReg_date());
        } catch (DataAccessException e) {
        }

        return result;
    }

    public int insertVerificationClerkForClerk(VerificationClerk verificationClerk) {
        final String sql = "insert into `T_VERIFICATION_CLERK` (" +
                "vc_name," +
                "phone," +
                "vc_type," +
                "shop_id," +
                "mall_id," +
                "member_id," +
                "is_manager," +
                "reg_date" +
                ") values(?,?,?,?,?,?,?,?)";
        log.info("[{}]", sql);

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, verificationClerk.getVc_name());
                ps.setString(2, verificationClerk.getPhone());
                ps.setInt(3, verificationClerk.getVc_type());
                ps.setInt(4, verificationClerk.getShop_id());
                ps.setInt(5, verificationClerk.getMall_id());
                ps.setInt(6, verificationClerk.getMember_id());
                ps.setBoolean(7, verificationClerk.is_manager());
                ps.setLong(8, verificationClerk.getReg_date());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    /**
     * 获取核销人员信息
     *
     * @param memberId
     * @return
     */
    public VerificationClerk selectVerificationClerk(final int memberId) {
        String sql = "select vc_id,vc_name,phone,shop_id,status from `T_VERIFICATION_CLERK` where member_id=? and vc_type=1";

        VerificationClerk vc = jdbcTemplate.query(sql, new Object[]{memberId}, new int[]{Types.INTEGER}, new ResultSetExtractor<VerificationClerk>() {
            @Override
            public VerificationClerk extractData(ResultSet rs) throws SQLException, DataAccessException {
                VerificationClerk verificationClerk = null;

                if (rs.next()) {
                    verificationClerk = new VerificationClerk();
                    verificationClerk.setVc_id(rs.getInt("vc_id"));
                    verificationClerk.setVc_name(rs.getString("vc_name"));
                    verificationClerk.setPhone(rs.getString("phone"));
                    verificationClerk.setShop_id(rs.getInt("shop_id"));
                    verificationClerk.setStatus(rs.getInt("status"));
                }
                return verificationClerk;
            }
        });
        return vc;
    }

    /**
     * 获取工作人员信息
     *
     * @param memberId
     * @return
     */
    public VerificationClerk selectClerk(final int memberId, final int vcType, final int mallId) { //,0,
        String sql = "SELECT vc_id,vc_name,phone,shop_id,status FROM `T_VERIFICATION_CLERK` WHERE member_id=? AND vc_type=? AND status=0 AND mall_id=?";

        VerificationClerk vc = jdbcTemplate.query(sql, new Object[]{memberId, vcType, mallId},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER},
                new ResultSetExtractor<VerificationClerk>() {
                    @Override
                    public VerificationClerk extractData(ResultSet rs) throws SQLException, DataAccessException {
                        VerificationClerk verificationClerk = null;

                        if (rs.next()) {
                            verificationClerk = new VerificationClerk();
                            verificationClerk.setVc_id(rs.getInt("vc_id"));
                            verificationClerk.setVc_name(rs.getString("vc_name"));
                            verificationClerk.setPhone(rs.getString("phone"));
                            verificationClerk.setShop_id(rs.getInt("shop_id"));
                            verificationClerk.setStatus(rs.getInt("status"));
                        }
                        return verificationClerk;
                    }
                });
        return vc;
    }
}
