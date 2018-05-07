package com.laf.manager.repository

import com.laf.manager.dto.RaffleInfo
import com.laf.manager.dto.RaffleLog
import com.laf.manager.querycondition.raffle.RaffleFilterCondition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Date
import java.sql.Statement
import java.sql.Types

@Repository
class RaffleRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun insertRaffleInfo(raffleInfo: RaffleInfo): Int {
        val sql = "insert into `T_RAFFLE_INFO`(title,raffle_time_start,raffle_time_end,required_points," +
                "intro,mall_id,`times`,activity_code) " +
                "values(?,?,?,?,?,?,?,?)"

        val holder = GeneratedKeyHolder()

        jdbcTemplate.update({ conn ->
            val ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, raffleInfo.title)
            ps.setLong(2, raffleInfo.raffle_time_start)
            ps.setLong(3, raffleInfo.raffle_time_end)
            ps.setInt(4, raffleInfo.required_points)
            ps.setInt(5, raffleInfo.intro_id)
            ps.setInt(6, raffleInfo.mall_id)
            ps.setInt(7, raffleInfo.times)
            ps.setString(8, raffleInfo.activity_code)

            ps
        }, holder)

        return holder.key.toInt()
    }

    fun selectRaffleByCode(activity_code: String): List<RaffleInfo> {
        val sql = "select raffle_id, title,raffle_time_start,raffle_time_end,required_points," +
                "r.intro,a.content,r.mall_id,`times`,activity_code " +
                "from `T_RAFFLE_INFO` r inner join `T_ARTICLES` a on r.intro=a.article_id " +
                "where activity_code=?"

        return jdbcTemplate.query(sql, arrayOf(activity_code), {
            rs, _ ->
            val raffle = RaffleInfo(
                    rs.getInt("raffle_id"),
                    rs.getString("title"),
                    rs.getLong("raffle_time_start"),
                    rs.getLong("raffle_time_end"),
                    rs.getInt("required_points"),
                    rs.getInt("intro"),
                    rs.getString("content"),
                    rs.getInt("mall_id"),
                    rs.getInt("times"),
                    rs.getString("activity_code")
            )

            raffle
        })
    }

    fun selectRaffleById(raffleId: Int): List<RaffleInfo> {
        val sql = "select raffle_id, title,raffle_time_start,raffle_time_end,required_points," +
                "r.intro,a.content,r.mall_id,`times`,activity_code " +
                "from `T_RAFFLE_INFO` r inner join `T_ARTICLES` a on r.intro=a.article_id " +
                "where raffle_id=?"

        return jdbcTemplate.query(sql, arrayOf(raffleId), {
            rs, _ ->
            val raffle = RaffleInfo(
                    rs.getInt("raffle_id"),
                    rs.getString("title"),
                    rs.getLong("raffle_time_start"),
                    rs.getLong("raffle_time_end"),
                    rs.getInt("required_points"),
                    rs.getInt("intro"),
                    rs.getString("content"),
                    rs.getInt("mall_id"),
                    rs.getInt("times"),
                    rs.getString("activity_code")
            )

            raffle
        })
    }

    fun updateRaffleInfo(raffleInfo: RaffleInfo): Int {
        val sql = "update `T_RAFFLE_INFO` set title=?,raffle_time_start=?,raffle_time_end=?," +
                "required_points=?,times=?,activity_code=? where raffle_id=?"

        return jdbcTemplate.update(sql, raffleInfo.title, raffleInfo.raffle_time_start, raffleInfo.raffle_time_end,
                raffleInfo.required_points, raffleInfo.times,raffleInfo.activity_code, raffleInfo.raffle_id)
    }

    fun multipleSelectRaffleLogs(it: RaffleFilterCondition): List<RaffleLog> {
        var sql = "SELECT i.title,l.member_name,l.member_mobile,i.required_points,l.trr_id,l.action_time,r.reward_name,r.reward_type " +
                "FROM `T_RAFFLE_LOG` l INNER JOIN t_raffle_info i ON l.raffle_id=i.raffle_id LEFT JOIN t_raffle_reward r ON l.trr_id=r.trr_id " +
                "WHERE 1=1"

        val args = arrayListOf<Any>()
//        var argTypes = arrayListOf<Int>()
//        argTypes.add(Types.INTEGER)

        if (it.title.isNotEmpty()) {
            sql += " AND i.title LIKE '%' ? '%'"
            args.add(it.title)
        }

        if (it.username.isNotEmpty()) {
            sql += " AND l.member_name LIKE '%' ? '%'"
            args.add(it.username)
        }

        if (it.mobile.isNotEmpty()) {
            sql += " AND l.member_mobile=?"
            args.add(it.mobile)
        }

        if (it.actionTimeStart > 0 && it.actionTimeEnd > 0) {
            sql += " AND l.action_time>=? AND l.action_time<=?"
            args.add(it.actionTimeStart)
            args.add(it.actionTimeEnd)
        }

        if (it.isWin != -1) {
            if (it.isWin == 1) {
                sql += " AND l.trr_id>0"
            } else if (it.isWin == 0) {
                sql += " AND l.trr_id=0"
            }
        }

        sql += " ORDER BY l.action_time DESC LIMIT ?,?"
        args.add(it.offset)
        args.add(it.size)
//        argTypes.add(Types.INTEGER)
//        argTypes.add(Types.INTEGER)

        val arr = args.toArray()

        return jdbcTemplate.query(sql, arr, {
            rs, _ ->
            val log = RaffleLog(
                    rs.getString("title"),
                    rs.getString("member_name"),
                    rs.getString("member_mobile"),
                    Date(rs.getLong("action_time")),
                    rs.getInt("required_points"),
                    rs.getInt("trr_id"),
                    rs.getString("reward_name"),
                    rs.getInt("reward_type")
            )
            log
        })
    }

    fun multipleSelectRaffleLogsCount(it: RaffleFilterCondition): Int {
        var sql = "SELECT count(1) cnt " +
                "FROM `T_RAFFLE_LOG` l INNER JOIN t_raffle_info i ON l.raffle_id=i.raffle_id LEFT JOIN t_raffle_reward r ON l.trr_id=r.trr_id " +
                "WHERE 1=1"

        val args = arrayListOf<Any>()
        var argTypes = arrayListOf<Int>()

        if (it.title.isNotEmpty()) {
            sql += " AND i.title LIKE '%' ? '%'"
            args.add(it.title)
            argTypes.add(Types.VARCHAR)
        }

        if (it.username.isNotEmpty()) {
            sql += " AND l.member_name LIKE '%' ? '%'"
            args.add(it.username)
            argTypes.add(Types.VARCHAR)
        }

        if (it.mobile.isNotEmpty()) {
            sql += " AND l.member_mobile=?"
            args.add(it.mobile)
            argTypes.add(Types.VARCHAR)
        }

        if (it.actionTimeStart > 0 && it.actionTimeEnd > 0) {
            sql += " AND l.action_time>=? AND l.action_time<=?"
            args.add(it.actionTimeStart)
            args.add(it.actionTimeEnd)
            argTypes.add(Types.BIGINT)
            argTypes.add(Types.BIGINT)
        }

        if (it.isWin != -1) {
            if (it.isWin == 1) {
                sql += " AND l.trr_id>0"
            } else if (it.isWin == 0) {
                sql += " AND l.trr_id=0"
            }
        }

        val arr = args.toArray()
        val artype = argTypes.toIntArray()
//        val artype = arrayOfNulls<Int>(arr.size)
//        argTypes.toArray(artype)

        val rowSet = jdbcTemplate.queryForRowSet(sql, arr, artype)

        var count = 0
        if (rowSet.first()) {
            count = rowSet.getInt("cnt")
        }
        return count
    }

    /*
    * public List<Member> multipleSelectMembers(final MemberFilterCondition $$) {
        String sql = "select m.member_id,m.name,m.mobile,m.sex,m.birthday,m.occupation,m.address," +
                "m.degree_of_education,m.income_range,m.interest,m.wechat_account,m.enable_public_wa," +
                "m.cumulate_points,m.usable_points,m.cumulate_amount,m.level,m.level_id,mm.open_id,mm.status," +
                "m.member_card_no,mm.regist_date,mm.mall_id,m.email,m.edit_flag,m.birthday_modified " +
                "from `T_MEMBER` m inner join `T_MALL_MEMBER` mm on m.member_id=mm.member_id " +
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
                    return $;
                });
    }
    * */

}

