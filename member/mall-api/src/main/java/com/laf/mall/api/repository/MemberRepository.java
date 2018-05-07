package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Member;
import com.laf.mall.api.dto.MemberVisitLog;
import com.laf.mall.api.utils.db.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class MemberRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbUtils dbUtils;

    public int insertMemeber(final Member member) {

        final String sql = "insert into `T_MEMBER` (`name`,mobile,sex,birthday,occupation,address," +
                "degree_of_education,income_range,interest,wechat_account,`level`,level_id," +
                "email) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        log.info("[sql = {}]", sql);

        KeyHolder holder = new GeneratedKeyHolder();
log.info("{}", jdbcTemplate);
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
               log.info("{}", ps);
                ps.setString(1, member.getName());
                ps.setString(2, member.getMobile());
                ps.setInt(3, member.getSex());
                ps.setLong(4, member.getBirthday());
                ps.setInt(5, -1);
                ps.setString(6, member.getAddress());
                ps.setInt(7, -1);
                ps.setInt(8, -1);
                ps.setString(9, member.getInterest());
                ps.setString(10, member.getWechat_account());
                ps.setString(11, member.getLevel());
                ps.setInt(12, member.getLevel_id());
                ps.setString(13, member.getEmail());

                return ps;
            }
        }, holder);
log.info("holder.getKey(){}", holder.getKey());
        log.info("intvalue{}", holder.getKey().intValue());
        Number key = holder.getKey();
        return holder.getKey().intValue();
    }

    public int insertMemberInMall(final Integer memberId, final Integer mallId, final String openId) {
        final String sql = "insert into `T_MALL_MEMBER` (member_id,mall_id,regist_date,open_id) values(?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, memberId);
                ps.setInt(2, mallId);
                ps.setLong(3, new java.util.Date().getTime());
                ps.setString(4, openId);

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public Member selectMemberById(final Integer memberId, final Integer mallId) {
        final String sql = "select m.member_id,m.name,m.mobile,m.sex,m.birthday,m.occupation,m.address," +
                "m.degree_of_education,m.income_range,m.interest,m.wechat_account,m.enable_public_wa," +
                "m.cumulate_points,m.usable_points,m.cumulate_amount,m.level,m.level_id,mm.open_id," +
                "m.member_card_no,mm.regist_date,mm.mall_id,m.email,m.edit_flag,m.birthday_modified " +
                "from `T_MEMBER` m inner join `T_MALL_MEMBER` mm on m.member_id=mm.member_id " +
                "where m.member_id=? and mm.mall_id=?";

        final Member member = jdbcTemplate.query(sql, new Object[]{memberId, mallId}, new ResultSetExtractor<Member>() {
            @Override
            public Member extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMember_id(rs.getInt("member_id"));
                    member.setName(rs.getString("name"));
                    member.setMobile(rs.getString("mobile"));
                    member.setSex(rs.getInt("sex"));
                    member.setBirthday(new Date(rs.getLong("birthday")));
                    member.setOccupation(rs.getInt("occupation"));
                    member.setAddress(rs.getString("address"));
                    member.setDegree_of_education(rs.getInt("degree_of_education"));
                    member.setIncome_range(rs.getInt("income_range"));
                    member.setInterest(rs.getString("interest"));
                    member.setWechat_account(rs.getString("wechat_account"));
                    member.setEnable_public_wa(rs.getBoolean("enable_public_wa"));
                    member.setCumulate_points(rs.getInt("cumulate_points"));
                    member.setUsable_points(rs.getInt("usable_points"));
                    member.setMall_id(rs.getInt("mall_id"));
                    member.setCumulate_amount(rs.getBigDecimal("cumulate_amount"));
                    member.setLevel(rs.getString("level"));
                    member.setLevel_id(rs.getInt("level_id"));
                    member.setMember_card_no(rs.getString("member_card_no"));
                    member.setRegist_date(new Date(rs.getLong("regist_date")));
                    member.setOpen_id(rs.getString("open_id"));
                    member.setEmail(rs.getString("email"));
                    member.setEdit_flag(rs.getInt("edit_flag"));
                    member.setBirthday_modified(rs.getInt("birthday_modified"));

                    return member;
                }

                return null;
            }
        });

        return member;
    }

    public Member selectMemberByMobile(final String mobile) {
        final String sql = "select m.member_id,m.name,m.mobile,m.sex,m.birthday,m.occupation,m.address," +
                "m.degree_of_education,m.income_range,m.interest,m.wechat_account,m.enable_public_wa," +
                "m.cumulate_points,m.usable_points,m.cumulate_amount,m.level,m.level_id,mm.open_id," +
                "m.member_card_no,mm.regist_date,mm.mall_id,m.email,m.edit_flag,m.birthday_modified " +
                "from `T_MEMBER` m inner join `T_MALL_MEMBER` mm on m.member_id=mm.member_id " +
                "where m.mobile=?";

        final Member member = jdbcTemplate.query(sql, new Object[]{mobile}, new ResultSetExtractor<Member>() {
            @Override
            public Member extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMember_id(rs.getInt("member_id"));
                    member.setName(rs.getString("name"));
                    member.setMobile(rs.getString("mobile"));
                    member.setSex(rs.getInt("sex"));
                    member.setBirthday(new Date(rs.getLong("birthday")));
                    member.setOccupation(rs.getInt("occupation"));
                    member.setAddress(rs.getString("address"));
                    member.setDegree_of_education(rs.getInt("degree_of_education"));
                    member.setIncome_range(rs.getInt("income_range"));
                    member.setInterest(rs.getString("interest"));
                    member.setWechat_account(rs.getString("wechat_account"));
                    member.setEnable_public_wa(rs.getBoolean("enable_public_wa"));
                    member.setCumulate_points(rs.getInt("cumulate_points"));
                    member.setUsable_points(rs.getInt("usable_points"));
                    member.setMall_id(rs.getInt("mall_id"));
                    member.setCumulate_amount(rs.getBigDecimal("cumulate_amount"));
                    member.setLevel(rs.getString("level"));
                    member.setLevel_id(rs.getInt("level_id"));
                    member.setMember_card_no(rs.getString("member_card_no"));
                    member.setRegist_date(new Date(rs.getLong("regist_date")));
                    member.setOpen_id(rs.getString("open_id"));
                    member.setEmail(rs.getString("email"));
                    member.setEdit_flag(rs.getInt("edit_flag"));
                    member.setBirthday_modified(rs.getInt("birthday_modified"));

                    return member;
                }

                return null;
            }
        });

        return member;
    }

    public Member selectMemberById(final Integer memberId) {
        final String sql = "select member_id,`name`,mobile,sex,birthday,occupation,address," +
                "degree_of_education,income_range,interest,wechat_account,enable_public_wa," +
                "cumulate_points,usable_points,cumulate_amount,`level`,level_id," +
                "member_card_no,email,edit_flag,birthday_modified " +
                "from `T_MEMBER` where member_id=?";

        final Member member = jdbcTemplate.query(sql, new Object[]{memberId}, new ResultSetExtractor<Member>() {
            @Override
            public Member extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMember_id(rs.getInt("member_id"));
                    member.setName(rs.getString("name"));
                    member.setMobile(rs.getString("mobile"));
                    member.setSex(rs.getInt("sex"));
                    member.setBirthday(new Date(rs.getLong("birthday")));
                    member.setOccupation(rs.getInt("occupation"));
                    member.setAddress(rs.getString("address"));
                    member.setDegree_of_education(rs.getInt("degree_of_education"));
                    member.setIncome_range(rs.getInt("income_range"));
                    member.setInterest(rs.getString("interest"));
                    member.setWechat_account(rs.getString("wechat_account"));
                    member.setEnable_public_wa(rs.getBoolean("enable_public_wa"));
                    member.setCumulate_points(rs.getInt("cumulate_points"));
                    member.setUsable_points(rs.getInt("usable_points"));
                    member.setCumulate_amount(rs.getBigDecimal("cumulate_amount"));
                    member.setLevel(rs.getString("level"));
                    member.setLevel_id(rs.getInt("level_id"));
                    member.setMember_card_no(rs.getString("member_card_no"));
                    member.setEmail(rs.getString("email"));
                    member.setEdit_flag(rs.getInt("edit_flag"));
                    member.setBirthday_modified(rs.getInt("birthday_modified"));

                    return member;
                }

                return null;
            }
        });

        return member;
    }

    public Member selectSimpleMemberByMobile(final String mobile) {
        final String sql = "select member_id,`name`,mobile,sex,birthday,occupation,address," +
                "degree_of_education,income_range,interest,wechat_account,enable_public_wa," +
                "cumulate_points,usable_points,cumulate_amount,`level`,level_id," +
                "member_card_no,email,edit_flag,birthday_modified " +
                "from `T_MEMBER` where mobile=?";

        final Member member = jdbcTemplate.query(sql, new Object[]{mobile}, new ResultSetExtractor<Member>() {
            @Override
            public Member extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMember_id(rs.getInt("member_id"));
                    member.setName(rs.getString("name"));
                    member.setMobile(rs.getString("mobile"));
                    member.setSex(rs.getInt("sex"));
                    member.setBirthday(new Date(rs.getLong("birthday")));
                    member.setOccupation(rs.getInt("occupation"));
                    member.setAddress(rs.getString("address"));
                    member.setDegree_of_education(rs.getInt("degree_of_education"));
                    member.setIncome_range(rs.getInt("income_range"));
                    member.setInterest(rs.getString("interest"));
                    member.setWechat_account(rs.getString("wechat_account"));
                    member.setEnable_public_wa(rs.getBoolean("enable_public_wa"));
                    member.setCumulate_points(rs.getInt("cumulate_points"));
                    member.setUsable_points(rs.getInt("usable_points"));
                    member.setCumulate_amount(rs.getBigDecimal("cumulate_amount"));
                    member.setLevel(rs.getString("level"));
                    member.setLevel_id(rs.getInt("level_id"));
                    member.setMember_card_no(rs.getString("member_card_no"));
                    member.setEmail(rs.getString("email"));
                    member.setEdit_flag(rs.getInt("edit_flag"));
                    member.setBirthday_modified(rs.getInt("birthday_modified"));

                    return member;
                }

                return null;
            }
        });

        return member;
    }

    public Member selectMemberByOpenIdInMall(final String appId, final String openId) {
        String sql = "SELECT m.member_id,m.name,m.mobile,m.sex,m.birthday,m.occupation,m.address," +
                "m.degree_of_education,m.income_range,m.interest,m.wechat_account,m.enable_public_wa," +
                "m.cumulate_points,m.usable_points,m.cumulate_amount,m.level,m.level_id,mm.open_id," +
                "m.member_card_no,mm.regist_date,mm.mall_id,m.email,m.edit_flag,m.birthday_modified " +
                "FROM `T_MEMBER` m INNER JOIN `T_MALL_MEMBER` mm ON m.member_id=mm.member_id " +
                "WHERE mm.mall_id=(SELECT mall_id FROM `T_MALL` where AppID=?) AND mm.open_id=?";

        final Member member = jdbcTemplate.query(sql, new Object[]{appId, openId}, new int[]{Types.VARCHAR, Types.VARCHAR},
                new ResultSetExtractor<Member>() {
                    @Override
                    public Member extractData(ResultSet rs) throws SQLException, DataAccessException {
                        if (rs.next()) {
                            Member member = new Member();
                            member.setMember_id(rs.getInt("member_id"));
                            member.setName(rs.getString("name"));
                            member.setMobile(rs.getString("mobile"));
                            member.setSex(rs.getInt("sex"));
                            member.setBirthday(new Date(rs.getLong("birthday")));
                            member.setOccupation(rs.getInt("occupation"));
                            member.setAddress(rs.getString("address"));
                            member.setDegree_of_education(rs.getInt("degree_of_education"));
                            member.setIncome_range(rs.getInt("income_range"));
                            member.setInterest(rs.getString("interest"));
                            member.setWechat_account(rs.getString("wechat_account"));
                            member.setEnable_public_wa(rs.getBoolean("enable_public_wa"));
                            member.setCumulate_points(rs.getInt("cumulate_points"));
                            member.setUsable_points(rs.getInt("usable_points"));
                            member.setMall_id(rs.getInt("mall_id"));
                            member.setCumulate_amount(rs.getBigDecimal("cumulate_amount"));
                            member.setLevel(rs.getString("level"));
                            member.setLevel_id(rs.getInt("level_id"));
                            member.setMember_card_no(rs.getString("member_card_no"));
                            member.setRegist_date(new Date(rs.getLong("regist_date")));
                            member.setOpen_id(rs.getString("open_id"));
                            member.setEmail(rs.getString("email"));
                            member.setEdit_flag(rs.getInt("edit_flag"));
                            member.setBirthday_modified(rs.getInt("birthday_modified"));

                            return member;
                        }

                        return null;
                    }
                });

        return member;
    }

    public int updateMember(final Member member) {
        final String sql = "update `T_MEMBER` set `name`=?,sex=?,birthday=?,occupation=?," +
                "address=?,degree_of_education=?,income_range=?,interest=?,enable_public_wa=?," +
                "edit_flag=?,birthday_modified=?,cumulate_points=?,usable_points=?,wechat_account=?,mobile=?" +
                " where member_id=?";

        return jdbcTemplate.update(sql, member.getName(), member.getSex(), member.getBirthday(),
                member.getOccupation(), member.getAddress(), member.getDegree_of_education(), member.getIncome_range(),
                member.getInterest(), member.isEnable_public_wa(), member.getEdit_flag(), member.getBirthday_modified(),
                member.getCumulate_points(), member.getUsable_points(), member.getWechat_account(), member.getMobile(), member.getMember_id());
    }

    public int updateMobile(final String mobile, final Integer memberId) {
        final String sql = "update `T_MEMBER` set mobile=? where member_id=?";

        return jdbcTemplate.update(sql, mobile, memberId);
    }

    public int selectMobileCount(final String mobile) {

        final String sql = "select count(1) as cnt from `T_MEMBER` where mobile=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{mobile});
        row.first();
        int count = row.getInt("cnt") >= 1 ? 1 : 0;

        return count;
    }

    public int selectUsablePoints4Member(final Integer memberId) {
        String sql = "SELECT usable_points FROM `T_MEMBER` WHERE member_id=?";

        int points = jdbcTemplate.query(sql, new Object[]{memberId}, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException {
                int points = 0;
                if (rs.next()) {
                    points = rs.getInt("usable_points");
                }
                return points;
            }
        });

        return points;
    }

    public long selectRegistTime4Member(final Integer memberId, final Integer mallId) {
        String sql = "select regist_date from `T_MALL_MEMBER` where member_id=? and mall_id=?";

        long registDate = jdbcTemplate.query(sql, new Object[]{memberId, mallId}, new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet rs) throws SQLException {
                long date = 0;
                if (rs.next()) {
                    date = rs.getLong("regist_date");
                }
                return date;
            }
        });

        return registDate;
    }

    public int updatePoints(int memberId, int cumulatePoints, int useablePoints) {
        final String sql = "update `T_MEMBER` set cumulate_points=?,usable_points=? where member_id=?";

        return jdbcTemplate.update(sql, cumulatePoints, useablePoints, memberId);
    }

    public int insertMemberVisitLog(MemberVisitLog log) {
        final String sql = "INSERT INTO `T_MEMBER_VISIT_LOG` (member_id,mall_id,open_id,visit_time,simple_time) VALUES (?,?,?,?,?)";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                ps.setInt(1, log.getMember_id());
                ps.setInt(2, log.getMall_id());
                ps.setString(3, log.getOpen_id());
                ps.setLong(4, log.getVisit_time());
                ps.setLong(5, log.getSimple_time());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }
}
