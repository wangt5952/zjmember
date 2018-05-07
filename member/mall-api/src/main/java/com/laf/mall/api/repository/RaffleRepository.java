package com.laf.mall.api.repository;

import com.laf.mall.api.dto.RaffleInfo;
import com.laf.mall.api.dto.RaffleLog;
import com.laf.mall.api.dto.RaffleReward;
import com.laf.mall.api.querycondition.raffle.MyRewardQueryCondition;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import com.laf.mall.api.vo.MyRewardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class RaffleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DateTimeUtils dateTimeUtils;

    public RaffleInfo selectRaffleInfoByActivityCode(final String activityCode, final Integer mallId) {

        String sql = "SELECT raffle_id,title,raffle_time_start,raffle_time_end,required_points,activity_code,picture,content " +
                "FROM `T_RAFFLE_INFO` ri LEFT JOIN `T_ARTICLES` r ON intro=article_id " +
                "WHERE ri.mall_id=? AND activity_code=? AND raffle_time_start<=? AND raffle_time_end>=?";

        Long curr = new Date().getTime();

        List<RaffleInfo> list = jdbcTemplate.query(sql, new Object[]{mallId, activityCode, curr, curr}, new ResultSetExtractor<List<RaffleInfo>>() {
            @Override
            public List<RaffleInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<RaffleInfo> _list = new ArrayList<>();

                while (rs.next()) {
                    RaffleInfo _info = new RaffleInfo();
                    _info.setRaffle_id(rs.getInt("raffle_id"));
                    _info.setTitle(rs.getString("title"));
                    _info.setRaffle_time_start(new Date(rs.getLong("raffle_time_start")));
                    _info.setRaffle_time_end(new Date(rs.getLong("raffle_time_end")));
                    _info.setRequired_points(rs.getInt("required_points"));
                    _info.setActivity_code(rs.getString("activity_code"));
                    _info.setPicture(rs.getString("picture"));
                    _info.setIntro(rs.getString("content"));
                    List<RaffleReward> list = selectRaffleRewardList(_info.getRaffle_id());
                    _info.setRewards(list);
                    _list.add(_info);
                }
                return _list;
            }
        });

        return list.isEmpty() ? null : list.get(0);
    }

    public int insertRaffleLog(final RaffleLog log) {
        String sql = " INSERT INTO `T_RAFFLE_LOG` (raffle_id,member_id,member_name,member_mobile,trr_id,action_time) VALUES (?,?,?,?,?,?)";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, log.getRaffle_id());
                ps.setInt(2, log.getMember_id());
                ps.setString(3, log.getMember_name());
                ps.setString(4, log.getMember_mobile());
                ps.setInt(5, log.getTrr_id());
                ps.setLong(6, log.getAction_time());
                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public List<MyRewardInfo> selectMyRewardList(MyRewardQueryCondition condition) {
        String sql = "SELECT action_time,reward_type,reward_value,coupon_name,price,reward_picture FROM `T_RAFFLE_LOG` rl " +
                "INNER JOIN `T_RAFFLE_REWARD` rr ON rl.trr_id=rr.trr_id " +
                "LEFT JOIN `T_COUPON_INFO` ci ON rr.reward_value=ci.coupon_id " +
                "WHERE rl.member_id=?";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        args.add(condition.getMemberId());
        argsType.add(Types.INTEGER);

        sb.append(" ORDER BY action_time DESC LIMIT ?,?");
        args.add(condition.getOffset());
        args.add(condition.getSize());
        argsType.add(Types.INTEGER);
        argsType.add(Types.INTEGER);

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }

        List<MyRewardInfo> list = jdbcTemplate.query(sb.toString(), args.toArray(), arr, new ResultSetExtractor<List<MyRewardInfo>>() {
            @Override
            public List<MyRewardInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<MyRewardInfo> _list = new ArrayList<>();

                while (rs.next()) {
                    MyRewardInfo info = new MyRewardInfo();
                    info.setAction_time(new Date(rs.getLong("action_time")));
                    info.setReward_type(rs.getInt("reward_type"));
                    info.setReward_value(rs.getInt("reward_value"));
                    info.setCoupon_name(rs.getString("coupon_name"));
                    info.setPrice(rs.getBigDecimal("price"));
                    info.setReward_picture(rs.getString("reward_picture"));

                    _list.add(info);
                }
                return _list;
            }
        });

        return list;
    }

    public List<RaffleReward> selectRaffleRewardList(final int raffleId) {

        String sql = "SELECT trr_id,raffle_id,reward_name,reward_type,reward_picture,inventory,hit_rate,reward_value,sort_id " +
                "FROM `T_RAFFLE_REWARD` WHERE raffle_id=? ORDER BY sort_id ASC";

        List<RaffleReward> list = jdbcTemplate.query(sql, new Object[]{raffleId}, new ResultSetExtractor<List<RaffleReward>>() {
            @Override
            public List<RaffleReward> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<RaffleReward> _list = new ArrayList<>();

                while (rs.next()) {
                    RaffleReward reward = new RaffleReward();
                    reward.setTrr_id(rs.getInt("trr_id"));
                    reward.setRaffle_id(rs.getInt("raffle_id"));
                    reward.setReward_name(rs.getString("reward_name"));
                    reward.setReward_type(rs.getInt("reward_type"));
                    reward.setReward_picture(rs.getString("reward_picture"));
                    reward.setInventory(rs.getInt("inventory"));
                    reward.setHit_rate(rs.getDouble("hit_rate"));
                    reward.setReward_value(rs.getInt("reward_value"));
                    reward.setSort_id(rs.getInt("sort_id"));
                    _list.add(reward);
                }
                return _list;
            }
        });
        return list;
    }

    public int selectRaffleLogCount(final int memberId, final int raffleId, final boolean daily) {
        final String sql = "SELECT count(1) cnt FROM `T_RAFFLE_LOG` WHERE raffle_id=? AND member_id=?";

        StringBuilder sb = new StringBuilder(sql);
        if (daily) {
            long currS = dateTimeUtils.getMilliByToDay();
            LocalDate date = Instant.ofEpochMilli(currS).atZone(ZoneId.systemDefault()).toLocalDate();
            long currE = dateTimeUtils.getMilliByDateTime(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 23, 59, 59, 0);
            sb.append(" AND action_time>=").append(currS).append(" AND action_time<=").append(currE);
        }

        int result = 0;
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sb.toString(), new Object[]{raffleId, memberId});
        if (rowSet.first()) {
            result = rowSet.getInt("cnt");
        }
        return result;
    }

    public RaffleReward selectRewardById(final int trrId) {
        final String sql ="SELECT trr_id,raffle_id,reward_name,reward_type,reward_picture,inventory,hit_rate,reward_value,sort_id " +
                "FROM `T_RAFFLE_REWARD` WHERE trr_id=?";

        RaffleReward reward = jdbcTemplate.query(sql, new Object[]{trrId}, new ResultSetExtractor<RaffleReward>() {
            @Override
            public RaffleReward extractData(ResultSet rs) throws SQLException, DataAccessException {
                RaffleReward _reward = null;

                if (rs.next()) {
                    _reward = new RaffleReward();
                    _reward.setTrr_id(rs.getInt("trr_id"));
                    _reward.setRaffle_id(rs.getInt("raffle_id"));
                    _reward.setReward_name(rs.getString("reward_name"));
                    _reward.setReward_type(rs.getInt("reward_type"));
                    _reward.setReward_picture(rs.getString("reward_picture"));
                    _reward.setInventory(rs.getInt("inventory"));
                    _reward.setHit_rate(rs.getInt("hit_rate"));
                    _reward.setReward_value(rs.getInt("reward_value"));
                    _reward.setSort_id(rs.getInt("sort_id"));
                }
                return _reward;
            }
        });
        return reward;
    }

    public int updateRewardInventory(final int trrId, final int inventory) {
        final String sql = "UPDATE `T_RAFFLE_REWARD` SET inventory=? WHERE trr_id=?";

        return jdbcTemplate.update(sql, inventory, trrId);
    }
}
