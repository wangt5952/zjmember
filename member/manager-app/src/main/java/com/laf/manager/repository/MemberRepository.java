package com.laf.manager.repository;

import com.laf.manager.dto.Member;
import com.laf.manager.querycondition.member.MemberFilterCondition;
import com.laf.manager.querycondition.member.MemberListQueryCondition;
import com.laf.manager.utils.db.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, member.getName());
                ps.setString(2, member.getMobile());
                ps.setInt(3, member.getSex());
                ps.setLong(4, member.getBirthday());
                ps.setInt(5, member.getOccupation());
                ps.setString(6, member.getAddress());
                ps.setInt(7, member.getDegree_of_education());
                ps.setInt(8, member.getIncome_range());
                ps.setString(9, member.getInterest());
                ps.setString(10, member.getWechat_account());
                ps.setString(11, member.getLevel());
                ps.setInt(12, member.getLevel_id());
                ps.setString(13, member.getEmail());

                return ps;
            }
        }, holder);

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
                ps.setLong(3, new Date().getTime());
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
                "edit_flag=?,birthday_modified=?,cumulate_points=?,usable_points=?" +
                " where member_id=?";

        return jdbcTemplate.update(sql, member.getName(), member.getSex(), member.getBirthday(),
                member.getOccupation(), member.getAddress(), member.getDegree_of_education(), member.getIncome_range(),
                member.getInterest(), member.isEnable_public_wa(), member.getEdit_flag(), member.getBirthday_modified(),
                member.getCumulate_points(), member.getUsable_points(), member.getMember_id());
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

    public int updatePoints(int memberId, int cumulatePoints, int useablePoints) {
        final String sql = "update `T_MEMBER` set cumulate_points=?,usable_points=? where member_id=?";

        return jdbcTemplate.update(sql, cumulatePoints, useablePoints, memberId);
    }

    public int updateCumulateAmount(int memberId, BigDecimal amount) {
        final String sql = "update `T_MEMBER` set cumulate_amount=? where member_id=?";

        return jdbcTemplate.update(sql, amount, memberId);
    }

    public int updateLevel(int memberId, int levelId, String levelName) {
        final String sql = "update `T_MEMBER` set `level`=?,level_id=? where member_id=?";

        return jdbcTemplate.update(sql, levelName, levelId, memberId);
    }

    public List<Member> selectMemberList(MemberListQueryCondition condition) {

        if (condition.getMallId() <= 0) return ListUtils.EMPTY_LIST;

        String sql = "select m.member_id,m.name,m.mobile,m.sex,m.birthday,m.occupation,m.address," +
                "m.degree_of_education,m.income_range,m.interest,m.wechat_account,m.enable_public_wa," +
                "m.cumulate_points,m.usable_points,m.cumulate_amount,m.level,m.level_id,mm.open_id," +
                "m.member_card_no,mm.regist_date,mm.mall_id,m.email,m.edit_flag,m.birthday_modified " +
                "from `T_MEMBER` m inner join `T_MALL_MEMBER` mm on m.member_id=mm.member_id " +
                "where mm.mall_id=? order by mm.regist_date desc limit ?, ?";

        Object[] args = new Object[]{condition.getMallId(), condition.getOffset(), condition.getSize()};
        int[] argTypes = new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER};

        List<Member> list = jdbcTemplate.query(sql, args, argTypes, new ResultSetExtractor<List<Member>>() {
            @Override
            public List<Member> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Member> _list = new ArrayList<>();

                while (rs.next()) {
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
                    _list.add(member);
                }
                return _list;
            }
        });

        return list;
    }

    public int selectMembersCount(final MemberListQueryCondition condition) {
        String sql = "select count(1) c from `T_MEMBER` m inner join `T_MALL_MEMBER` mm on m.member_id=mm.member_id where mm.mall_id=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{condition.getMallId()});

        rowSet.first();
        return rowSet.getInt("c");
    }

    public List<Member> multipleSelectMembers(final MemberFilterCondition $$) {
        String sql = "select m.member_id,m.name,m.mobile,m.sex,m.birthday,m.occupation,m.address," +
                "m.degree_of_education,m.income_range,m.interest,m.wechat_account,m.enable_public_wa," +
                "m.cumulate_points,m.usable_points,m.cumulate_amount,m.level,m.level_id,mm.open_id,mm.status," +
                "m.member_card_no,mm.regist_date,mm.mall_id,m.email,m.edit_flag,m.birthday_modified,p.car_number " +
                "from `T_MEMBER` m inner join `T_MALL_MEMBER` mm on m.member_id=mm.member_id " +
                "left join `t_parking` p on m.member_id=p.member_id  " +
                "where ifnull(p.isdefault,1)=1 and mm.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getUsername())) {
            sql += " and m.name like '%' ? '%'";
            args.add($$.getUsername());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile = ?";
            args.add($$.getMobile());
        }

        if (!StringUtils.isEmpty($$.getCarNumber())) {
            sql += " and p.car_number like '%' ? '%'";
            args.add($$.getCarNumber());
        }

        if(!StringUtils.isEmpty($$.getBirthdayMonth())){
            if($$.getBirthdayMonth().indexOf("-") > -1){
                DateFormat formatter = new SimpleDateFormat("yyyy-MM");
                try {
                    Date date = formatter.parse($$.getBirthdayMonth());
                    sql += " and FROM_UNIXTIME(m.birthday/1000,'%Y-%m') = ?";
                    args.add($$.getBirthdayMonth());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                if($$.getBirthdayMonth().length()==1){
                    $$.setBirthdayMonth("0" + $$.getBirthdayMonth());
                    sql += " and FROM_UNIXTIME(m.birthday/1000,'%m') = ?";
                    args.add($$.getBirthdayMonth());
                }else if($$.getBirthdayMonth().length() == 4){
                    sql += " and FROM_UNIXTIME(m.birthday/1000,'%Y') = ?";
                    args.add($$.getBirthdayMonth());
                }else{
                    sql += " and FROM_UNIXTIME(m.birthday/1000,'%m') = ?";
                    args.add($$.getBirthdayMonth());
                }

            }
        }

        if ($$.getRegisterDateStart() > 0L && $$.getRegisterDateEnd() > 0L) {
            sql += " and mm.regist_date >= ? and mm.regist_date<=?";
            args.add($$.getRegisterDateStart());
            args.add($$.getRegisterDateEnd());
        }

        if ($$.getCumulateAmountStart().doubleValue() > 0 && $$.getCumulateAmountEnd().doubleValue() > 0) {
            sql += " and m.cumulate_amount>=? and m.cumulate_amount<=?";
            args.add($$.getCumulateAmountStart());
            args.add($$.getCumulateAmountEnd());
        }

        if ($$.getCumulatePointsStart() > 0 && $$.getCumulatePointsEnd() > 0) {
            sql += " and m.cumulate_points>=? and m.cumulate_points<=?";
            args.add($$.getCumulatePointsStart());
            args.add($$.getCumulatePointsEnd());
        }

        if ($$.getLevel() > 0) {
            sql += " and m.level_id=?";
            args.add($$.getLevel());
        }

        if ($$.getOccupation() >= 0) {
            sql += " and m.occupation=?";
            args.add($$.getOccupation());
        }

        if ($$.getBirthdayStart() > 0L && $$.getBirthdayEnd() > 0L) {
            sql += " and m.birthday>=? and m.birthday<=?";
            args.add($$.getBirthdayStart());
            args.add($$.getBirthdayEnd());
        }

        if ($$.getStatus() > -1) {
            sql += " and mm.status=?";
            args.add($$.getStatus());
        }

        sql += " order by mm.regist_date desc limit ?, ?";
        args.add($$.getOffset());
        args.add($$.getSize());

        return jdbcTemplate.query(sql, args.toArray(),
                (rs, rowNum) -> {
                    Member $ = new Member();
                    $.setMember_id(rs.getInt("member_id"));
                    $.setName(rs.getString("name"));
                    $.setMobile(rs.getString("mobile"));
                    $.setSex(rs.getInt("sex"));
                    $.setBirthday(new Date(rs.getLong("birthday")));
                    $.setOccupation(rs.getInt("occupation"));
                    $.setAddress(rs.getString("address"));
                    $.setDegree_of_education(rs.getInt("degree_of_education"));
                    $.setIncome_range(rs.getInt("income_range"));
                    $.setInterest(rs.getString("interest"));
                    $.setWechat_account(rs.getString("wechat_account"));
                    $.setEnable_public_wa(rs.getBoolean("enable_public_wa"));
                    $.setCumulate_points(rs.getInt("cumulate_points"));
                    $.setUsable_points(rs.getInt("usable_points"));
                    $.setMall_id(rs.getInt("mall_id"));
                    $.setCumulate_amount(rs.getBigDecimal("cumulate_amount"));
                    $.setLevel(rs.getString("level"));
                    $.setLevel_id(rs.getInt("level_id"));
                    $.setMember_card_no(rs.getString("member_card_no"));
                    $.setRegist_date(new Date(rs.getLong("regist_date")));
                    $.setOpen_id(rs.getString("open_id"));
                    $.setEmail(rs.getString("email"));
                    $.setEdit_flag(rs.getInt("edit_flag"));
                    $.setBirthday_modified(rs.getInt("birthday_modified"));
                    $.setStatus(rs.getInt("status"));
                    $.setCarNumber(rs.getString("car_number"));
                    return $;
                });
    }

    public int multipleSelectMembersCount(final MemberFilterCondition $$) {
        String sql = "select count(1) cnt from `T_MEMBER` m " +
                "inner join `T_MALL_MEMBER` mm on m.member_id=mm.member_id " +
                "left join `t_parking` p on m.member_id=p.member_id  " +
                "where ifnull(p.isdefault,1)=1 and mm.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getUsername())) {
            sql += " and m.name like '%' ? '%'";
            args.add($$.getUsername());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile = ?";
            args.add($$.getMobile());
        }

        if (!StringUtils.isEmpty($$.getCarNumber())) {
            sql += " and p.car_number like '%' ? '%'";
            args.add($$.getCarNumber());
        }

        if(!StringUtils.isEmpty($$.getBirthdayMonth())){
            if($$.getBirthdayMonth().indexOf("-") > -1){
                DateFormat formatter = new SimpleDateFormat("yyyy-MM");
                try {
                    Date date = formatter.parse($$.getBirthdayMonth());
                    sql += " and FROM_UNIXTIME(m.birthday/1000,'%Y-%m') = ?";
                    args.add($$.getBirthdayMonth());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                if($$.getBirthdayMonth().length()==1){
                    $$.setBirthdayMonth("0" + $$.getBirthdayMonth());
                    sql += " and FROM_UNIXTIME(m.birthday/1000,'%m') = ?";
                    args.add($$.getBirthdayMonth());
                }else if($$.getBirthdayMonth().length() == 4){
                    sql += " and FROM_UNIXTIME(m.birthday/1000,'%Y') = ?";
                    args.add($$.getBirthdayMonth());
                }else{
                    sql += " and FROM_UNIXTIME(m.birthday/1000,'%m') = ?";
                    args.add($$.getBirthdayMonth());
                }
            }
        }

        if ($$.getRegisterDateStart() > 0L && $$.getRegisterDateEnd() > 0L) {
            sql += " and mm.regist_date >= ? and mm.regist_date<=?";
            args.add($$.getRegisterDateStart());
            args.add($$.getRegisterDateEnd());
        }

        if ($$.getCumulateAmountStart().doubleValue() > 0 && $$.getCumulateAmountEnd().doubleValue() > 0) {
            sql += " and m.cumulate_amount>=? and m.cumulate_amount<=?";
            args.add($$.getCumulateAmountStart());
            args.add($$.getCumulateAmountEnd());
        }

        if ($$.getCumulatePointsStart() > 0 && $$.getCumulatePointsEnd() > 0) {
            sql += " and m.cumulate_points>=? and m.cumulate_points<=?";
            args.add($$.getCumulatePointsStart());
            args.add($$.getCumulatePointsEnd());
        }

        if ($$.getLevel() > 0) {
            sql += " and m.level_id=?";
            args.add($$.getLevel());
        }

        if ($$.getOccupation() >= 0) {
            sql += " and m.occupation=?";
            args.add($$.getOccupation());
        }

        if ($$.getBirthdayStart() > 0L && $$.getBirthdayEnd() > 0L) {
            sql += " and m.birthday>=? and m.birthday<=?";
            args.add($$.getBirthdayStart());
            args.add($$.getBirthdayEnd());
        }

        if ($$.getStatus() > -1) {
            sql += " and mm.status=?";
            args.add($$.getStatus());
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, args.toArray());
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public int updateStatus(int status, int memberId, int mallId) {
        final String sql = "update `T_MALL_MEMBER` set status=? " +
                "where member_id=? and mall_id=?";

        return jdbcTemplate.update(sql, ps -> {
            ps.setInt(1, status);
            ps.setInt(2, memberId);
            ps.setInt(3, mallId);
        });
    }

    public BigDecimal selectCumulateAmountSum(final MemberFilterCondition $$) {
        String sql = "SELECT sum(cumulate_amount) amount FROM T_MEMBER m INNER JOIN T_MALL_MEMBER mm ON m.member_id=mm.member_id WHERE mm.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getUsername())) {
            sql += " and m.name like '%' ? '%'";
            args.add($$.getUsername());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile = ?";
            args.add($$.getMobile());
        }

        if ($$.getRegisterDateStart() > 0L && $$.getRegisterDateEnd() > 0L) {
            sql += " and mm.regist_date >= ? and mm.regist_date<=?";
            args.add($$.getRegisterDateStart());
            args.add($$.getRegisterDateEnd());
        }

        if ($$.getCumulateAmountStart().doubleValue() > 0 && $$.getCumulateAmountEnd().doubleValue() > 0) {
            sql += " and m.cumulate_amount>=? and m.cumulate_amount<=?";
            args.add($$.getCumulateAmountStart());
            args.add($$.getCumulateAmountEnd());
        }

        if ($$.getCumulatePointsStart() > 0 && $$.getCumulatePointsEnd() > 0) {
            sql += " and m.cumulate_points>=? and m.cumulate_points<=?";
            args.add($$.getCumulatePointsStart());
            args.add($$.getCumulatePointsEnd());
        }

        if ($$.getLevel() > 0) {
            sql += " and m.level_id=?";
            args.add($$.getLevel());
        }

        if ($$.getOccupation() >= 0) {
            sql += " and m.occupation=?";
            args.add($$.getOccupation());
        }

        if ($$.getBirthdayStart() > 0L && $$.getBirthdayEnd() > 0L) {
            sql += " and m.birthday>=? and m.birthday<=?";
            args.add($$.getBirthdayStart());
            args.add($$.getBirthdayEnd());
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, args.toArray());
        BigDecimal result = new BigDecimal(0D);

        if (row.first()) {
            result = row.getBigDecimal("amount");
        }
log.info("[total cumulate_count==={}]", result);
        return result;
    }

    public List<Member> multipleSelectAllMembers(final MemberFilterCondition $$) {
        String sql = "select m.member_id,mm.mall_id from `T_MEMBER` m " +
                "inner join `T_MALL_MEMBER` mm on m.member_id=mm.member_id " +
                "where mm.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getUsername())) {
            sql += " and m.name like '%' ? '%'";
            args.add($$.getUsername());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile = ?";
            args.add($$.getMobile());
        }

        if ($$.getRegisterDateStart() > 0L && $$.getRegisterDateEnd() > 0L) {
            sql += " and mm.regist_date >= ? and mm.regist_date<=?";
            args.add($$.getRegisterDateStart());
            args.add($$.getRegisterDateEnd());
        }

        if ($$.getCumulateAmountStart().doubleValue() > 0 && $$.getCumulateAmountEnd().doubleValue() > 0) {
            sql += " and m.cumulate_amount>=? and m.cumulate_amount<=?";
            args.add($$.getCumulateAmountStart());
            args.add($$.getCumulateAmountEnd());
        }

        if ($$.getCumulatePointsStart() > 0 && $$.getCumulatePointsEnd() > 0) {
            sql += " and m.cumulate_points>=? and m.cumulate_points<=?";
            args.add($$.getCumulatePointsStart());
            args.add($$.getCumulatePointsEnd());
        }

        if ($$.getLevel() > 0) {
            sql += " and m.level_id=?";
            args.add($$.getLevel());
        }

        if ($$.getOccupation() >= 0) {
            sql += " and m.occupation=?";
            args.add($$.getOccupation());
        }

        if ($$.getBirthdayStart() > 0L && $$.getBirthdayEnd() > 0L) {
            sql += " and m.birthday>=? and m.birthday<=?";
            args.add($$.getBirthdayStart());
            args.add($$.getBirthdayEnd());
        }

        if ($$.getStatus() > -1) {
            sql += " and mm.status=?";
            args.add($$.getStatus());
        }

        return jdbcTemplate.query(sql, args.toArray(),
                (rs, rowNum) -> {
                    Member member = new Member();
                    member.setMember_id(rs.getInt("member_id"));
                    member.setMall_id(rs.getInt("mall_id"));

                    return member;
                });
    }

    public List<Integer> selectVisitTimesMonthly(int mallId) {
        final String sql = "SELECT times FROM\n" +
                "(SELECT count(1) `times`,(SELECT from_unixtime(simple_time/1000,'%m')) m  FROM `T_MEMBER_VISIT_LOG` WHERE mall_id=? GROUP BY simple_time) t1\n" +
                "RIGHT JOIN\n" +
                "(SELECT '01' AS MONTH\n" +
                "UNION SELECT '02' AS MONTH\n" +
                "UNION SELECT '03' AS MONTH\n" +
                "UNION SELECT '04' AS MONTH\n" +
                "UNION SELECT '05' AS MONTH\n" +
                "UNION SELECT '06' AS MONTH\n" +
                "UNION SELECT '07' AS MONTH\n" +
                "UNION SELECT '08' AS MONTH\n" +
                "UNION SELECT '09' AS MONTH\n" +
                "UNION SELECT '10' AS MONTH\n" +
                "UNION SELECT '11' AS MONTH\n" +
                "UNION SELECT '12' AS MONTH) t2\n" +
                "ON t1.m=t2.month\n" +
                "ORDER BY t2.MONTH";
        return jdbcTemplate.query(sql, new Object[]{mallId},
                (rs, rowNum) -> rs.getInt("times"));
    }

    public List<Integer> selectVisitCountMonthly(int mallId) {
        final String sql = "SELECT count FROM\n" +
                "  (SELECT count(1) `count`,(SELECT from_unixtime(t.simple_time/1000,'%m')) m FROM (SELECT member_id,simple_time FROM `T_MEMBER_VISIT_LOG` WHERE mall_id=? GROUP BY member_id,simple_time) t GROUP BY t.simple_time) t1\n" +
                "RIGHT JOIN\n" +
                "  (SELECT '01' AS MONTH\n" +
                "UNION SELECT '02' AS MONTH\n" +
                "UNION SELECT '03' AS MONTH\n" +
                "UNION SELECT '04' AS MONTH\n" +
                "UNION SELECT '05' AS MONTH\n" +
                "UNION SELECT '06' AS MONTH\n" +
                "UNION SELECT '07' AS MONTH\n" +
                "UNION SELECT '08' AS MONTH\n" +
                "UNION SELECT '09' AS MONTH\n" +
                "UNION SELECT '10' AS MONTH\n" +
                "UNION SELECT '11' AS MONTH\n" +
                "UNION SELECT '12' AS MONTH) t2\n" +
                "ON t1.m=t2.month\n" +
                "ORDER BY t2.MONTH";

        return jdbcTemplate.query(sql, new Object[]{mallId},
                (rs, rowNum) -> rs.getInt("count"));
    }

    public List<Integer> selectActiveMembersCountMonthly(int mallId) {
        final String sql = "SELECT cnt,m FROM\n" +
                "(SELECT (SELECT from_unixtime(simple_time/1000,'%m')) m,count(1) cnt FROM\n" +
                "(SELECT t.member_id,t.simple_time,t.cnt FROM\n" +
                "(SELECT member_id,simple_time,count(1) cnt FROM `T_MEMBER_VISIT_LOG` WHERE mall_id=? GROUP BY member_id,simple_time) t WHERE t.cnt>1) tt GROUP BY tt.simple_time) t1\n" +
                "RIGHT JOIN\n" +
                "(SELECT '01' AS MONTH\n" +
                "UNION SELECT '02' AS MONTH\n" +
                "UNION SELECT '03' AS MONTH\n" +
                "UNION SELECT '04' AS MONTH\n" +
                "UNION SELECT '05' AS MONTH\n" +
                "UNION SELECT '06' AS MONTH\n" +
                "UNION SELECT '07' AS MONTH\n" +
                "UNION SELECT '08' AS MONTH\n" +
                "UNION SELECT '09' AS MONTH\n" +
                "UNION SELECT '10' AS MONTH\n" +
                "UNION SELECT '11' AS MONTH\n" +
                "UNION SELECT '12' AS MONTH) t2\n" +
                "ON t1.m=t2.month\n" +
                "ORDER BY t2.MONTH";

        return jdbcTemplate.query(sql, new Object[]{mallId},
                (rs, rowNum) -> rs.getInt("cnt"));
    }

    public int selectMemberCountBySex(int sex) {
        final String sql = "select count(1) `count` from t_member where sex=?";

        return jdbcTemplate.queryForObject(sql, new Object[]{sex},
                (rs, rowNum) -> rs.getInt("count"));
    }

    public int selectMemberCountByAge(int min, int max) {
        final String sql = "select count(1) as `count` from " +
                "(select format(datediff(current_date,from_unixtime(birthday/1000,'%Y-%m-%d'))/365,0) as age from t_member) t " +
                "where t.age>? and t.age<?";

        return jdbcTemplate.queryForObject(sql, new Object[]{min, max},
                (rs, rowNum) -> rs.getInt("count"));
    }

    public List<Integer> selectMemberCountByBirthday() {
        final String sql = "select t1.count,t2.month from \n" +
                "  (select count(1) `count`,month from (select from_unixtime(birthday/1000,'%m') month from t_member) t GROUP BY month) t1 \n" +
                "  right join\n" +
                "  (SELECT '01' AS MONTH\n" +
                "  UNION SELECT '02' AS MONTH\n" +
                "  UNION SELECT '03' AS MONTH\n" +
                "  UNION SELECT '04' AS MONTH\n" +
                "  UNION SELECT '05' AS MONTH\n" +
                "  UNION SELECT '06' AS MONTH\n" +
                "  UNION SELECT '07' AS MONTH\n" +
                "  UNION SELECT '08' AS MONTH\n" +
                "  UNION SELECT '09' AS MONTH\n" +
                "  UNION SELECT '10' AS MONTH\n" +
                "  UNION SELECT '11' AS MONTH\n" +
                "  UNION SELECT '12' AS MONTH) t2\n" +
                "  on t1.month=t2.month";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getInt("count"));
    }

    public List<Integer> selectRegisterCountMonthly(int mallId) {
        final String sql = "select t1.count,t2.ym from\n" +
                "  (select count(1) `count`, ym from (select from_unixtime(regist_date/1000,'%Y-%m') ym from t_mall_member where mall_id=?) t where ym like '%2018%' group by ym) t1\n" +
                "  right join\n" +
                "  (SELECT '2018-01' AS ym\n" +
                "  UNION SELECT '2018-02' AS ym\n" +
                "  UNION SELECT '2018-03' AS ym\n" +
                "  UNION SELECT '2018-04' AS ym\n" +
                "  UNION SELECT '2018-05' AS ym\n" +
                "  UNION SELECT '2018-06' AS ym\n" +
                "  UNION SELECT '2018-07' AS ym\n" +
                "  UNION SELECT '2018-08' AS ym\n" +
                "  UNION SELECT '2018-09' AS ym\n" +
                "  UNION SELECT '2018-10' AS ym\n" +
                "  UNION SELECT '2018-11' AS ym\n" +
                "  UNION SELECT '2018-12' AS ym) t2\n" +
                "  on t1.ym=t2.ym";

        return jdbcTemplate.query(sql, new Object[]{mallId},
                (rs, rowNum) -> rs.getInt("count"));
    }

    public List<Integer> selectRegisterCountMonthly(String year, int mallId) {
        final String sql = "select t1.count,t2.month from\n" +
                "(select count(1) `count`, y,m from\n" +
                "(select member_id,(select from_unixtime(regist_date/1000,'%Y')) y,(select from_unixtime(regist_date/1000,'%m')) m from t_mall_member where mall_id=?) t where y=? group by y,m) t1\n" +
                "right join\n" +
                "(SELECT '01' AS MONTH\n" +
                "UNION SELECT '02' AS MONTH\n" +
                "UNION SELECT '03' AS MONTH\n" +
                "UNION SELECT '04' AS MONTH\n" +
                "UNION SELECT '05' AS MONTH\n" +
                "UNION SELECT '06' AS MONTH\n" +
                "UNION SELECT '07' AS MONTH\n" +
                "UNION SELECT '08' AS MONTH\n" +
                "UNION SELECT '09' AS MONTH\n" +
                "UNION SELECT '10' AS MONTH\n" +
                "UNION SELECT '11' AS MONTH\n" +
                "UNION SELECT '12' AS MONTH) t2\n" +
                "on t1.m=t2.month\n" +
                "ORDER BY t2.MONTH";

        return jdbcTemplate.query(sql, new Object[]{mallId,year},
                (rs, rowNum) -> rs.getInt("count"));
    }

    public int selectMemberCountByYear(String year, int mallId) {
        final String sql = "select count(1) `count` from " +
                "(select member_id, (select from_unixtime(regist_date/1000,'%Y')) y from t_mall_member where mall_id=?) t " +
                "where t.y=? group by t.y";

        return jdbcTemplate.queryForObject(sql, new Object[]{mallId, year},
                (rs, rowNum) -> rs.getInt("count"));
    }
}
