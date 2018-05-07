package com.laf.manager.repository;

import com.laf.manager.dto.VerificationClerk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
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
        } catch (DataAccessException e) {}

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
                    verificationClerk.getIs_manager(),
                    verificationClerk.getReg_date());
        } catch (DataAccessException e) {}

        return result;
    }

    public List<VerificationClerk> selectSimpleVerificationClerkListForPage(Integer mallId, Integer size, Integer offset) {
        final String sql = "select vc_id,vc_name,v.phone,vc_type,v.shop_id,shop_name,v.mall_id,reg_date,v.member_id,m.`name` member_name,is_manager,v.status " +
                "from `T_VERIFICATION_CLERK` v inner join `T_MEMBER` m on v.member_id=m.member_id " +
                "left join `T_SHOP` s on v.shop_id=s.shop_id where v.mall_id=? limit ?,?";

        final List<VerificationClerk> list = jdbcTemplate.query(sql, new Object[]{mallId, offset, size}, new ResultSetExtractor<List<VerificationClerk>>() {
            @Override
            public List<VerificationClerk> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<VerificationClerk> _list = new ArrayList<>();

                while (rs.next()) {
                    VerificationClerk vc = new VerificationClerk();
                    vc.setVc_id(rs.getInt("vc_id"));
                    vc.setVc_name(rs.getString("vc_name"));
                    vc.setPhone(rs.getString("phone"));
                    vc.setVc_type(rs.getInt("vc_type"));
                    vc.setShop_id(rs.getInt("shop_id"));
                    vc.setShop_name(rs.getString("shop_name"));
                    vc.setMall_id(rs.getInt("mall_id"));
                    vc.setReg_date(new Date(rs.getLong("reg_date")));
                    vc.setMember_id(rs.getInt("member_id"));
                    vc.setMember_name(rs.getString("member_name"));
                    vc.setIs_manager(rs.getBoolean("is_manager"));
                    vc.setStatus(rs.getInt("status"));

                    _list.add(vc);
                }

                return _list;
            }
        });

        return list;
    }

    public VerificationClerk selectVerificationClerkById(final Integer vcId) {
        final String sql = "select vc_id,vc_name,v.phone,vc_type,v.shop_id,shop_name,v.mall_id,reg_date,v.member_id,m.`name` member_name,is_manager,v.status " +
                "from `T_VERIFICATION_CLERK` v inner join `T_MEMBER` m on v.member_id=m.member_id " +
                "inner join `T_SHOP` s on v.shop_id=s.shop_id where vc_id=?";

        final VerificationClerk vc = jdbcTemplate.query(sql, new Object[]{vcId}, new ResultSetExtractor<VerificationClerk>() {
            @Override
            public VerificationClerk extractData(ResultSet rs) throws SQLException, DataAccessException {

                if (rs.next()) {
                    VerificationClerk _vc = new VerificationClerk();
                    _vc.setVc_id(rs.getInt("vc_id"));
                    _vc.setVc_name(rs.getString("vc_name"));
                    _vc.setPhone(rs.getString("phone"));
                    _vc.setVc_type(rs.getInt("vc_type"));
                    _vc.setShop_id(rs.getInt("shop_id"));
                    _vc.setShop_name(rs.getString("shop_name"));
                    _vc.setMall_id(rs.getInt("mall_id"));
                    _vc.setReg_date(new Date(rs.getLong("reg_date")));
                    _vc.setMember_id(rs.getInt("member_id"));
                    _vc.setMember_name(rs.getString("member_name"));
                    _vc.setIs_manager(rs.getBoolean("is_manager"));
                    _vc.setStatus(rs.getInt("status"));

                    return _vc;
                }

                return null;
            }
        });

        return vc;
    }

    public int updateStatus(final Integer vdId, final Integer status) {
        final String sql = "update `T_VERIFICATION_CLERK` set status=? where vc_id=?";

        return jdbcTemplate.update(sql, status, vdId);
    }

    public int selectVerificationClerksCount(final Integer mallId) {
        final String sql = "select count(1) cnt from `T_VERIFICATION_CLERK` v inner join `T_MEMBER` m on v.member_id=m.member_id where v.mall_id=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{mallId});
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }


    public int deleteById(final Integer vcId) {
        final String sql = "delete from `T_VERIFICATION_CLERK` where vc_id=?";

        return jdbcTemplate.update(sql, vcId);
    }
}
