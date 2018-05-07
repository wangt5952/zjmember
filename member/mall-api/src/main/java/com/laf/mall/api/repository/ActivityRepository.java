package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Activity;
import com.laf.mall.api.dto.ActivitySignUpLog;
import com.laf.mall.api.dto.ReceiveCouponInfo;
import com.laf.mall.api.querycondition.activity.ActivityQueryCondition;
import com.laf.mall.api.utils.db.DbUtils;
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
public class ActivityRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DbUtils dbUtils;

    public Activity selectActivityById(final Integer activityId) {

        final String sql = "select a.activity_id,a.title,a.picture,a.activity_time_start,a.activity_time_end,a.address,a.is_sign_up," +
                "a.sign_up_end,a.sign_up_limited,a.sign_up_points,a.is_comment,a.is_sign_in,a.incentive_points,ar.content,a.qr_code,a.mall_id " +
                "from `T_ACTIVITY` a left join `T_ARTICLES` ar on a.intro=ar.article_id " +
                "where a.activity_id=?";

        final Activity activity = jdbcTemplate.query(sql, new Object[]{activityId}, new ResultSetExtractor<Activity>() {
            @Override
            public Activity extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    Activity _activity = new Activity();

                    _activity.setActivity_id(rs.getInt("activity_id"));
                    _activity.setTitle(rs.getString("title"));
                    _activity.setPicture(rs.getString("picture"));
                    _activity.setActivity_time_start(new Date(rs.getLong("activity_time_start")));
                    _activity.setActivity_time_end(new Date(rs.getLong("activity_time_end")));
                    _activity.setAddress(rs.getString("address"));
                    _activity.set_sign_up(rs.getBoolean("is_sign_up"));
                    _activity.setSign_up_limited(rs.getInt("sign_up_limited"));
                    _activity.setSign_up_end(new Date(rs.getLong("sign_up_end")));
                    _activity.setSign_up_points(rs.getInt("sign_up_points"));
                    _activity.set_comment(rs.getBoolean("is_comment"));
                    _activity.set_sign_in(rs.getBoolean("is_sign_in"));
                    _activity.setIncentive_points(rs.getInt("incentive_points"));
                    _activity.setIntro(rs.getString("content"));
                    _activity.setQr_code(rs.getString("qr_code"));
                    _activity.setMall_id(rs.getInt("mall_id"));

                    return _activity;
                }

                return null;
            }
        });

        return activity;
    }

    public Activity selectActivityByMall(final Integer mallId, final Integer activityId) {

        final String sql = "select a.activity_id,a.title,a.picture,a.activity_time_start,a.activity_time_end,a.address,a.is_sign_up," +
                "a.sign_up_end,a.sign_up_limited,a.sign_up_points,a.is_comment,a.is_sign_in,ar.content,a.qr_code,a.mall_id,a.incentive_points " +
                "from `T_ACTIVITY` a left join `T_ARTICLES` ar on a.intro=ar.article_id " +
                "where a.activity_id=? and a.mall_id=?";

        final Activity activity = jdbcTemplate.query(sql, new Object[]{activityId, mallId}, new ResultSetExtractor<Activity>() {
            @Override
            public Activity extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    Activity _activity = new Activity();

                    _activity.setActivity_id(rs.getInt("activity_id"));
                    _activity.setTitle(rs.getString("title"));
                    _activity.setPicture(rs.getString("picture"));
                    _activity.setActivity_time_start(new Date(rs.getLong("activity_time_start")));
                    _activity.setActivity_time_end(new Date(rs.getLong("activity_time_end")));
                    _activity.setAddress(rs.getString("address"));
                    _activity.set_sign_up(rs.getBoolean("is_sign_up"));
                    _activity.setSign_up_limited(rs.getInt("sign_up_limited"));
                    _activity.setSign_up_end(new Date(rs.getLong("sign_up_end")));
                    _activity.setSign_up_points(rs.getInt("sign_up_points"));
                    _activity.set_comment(rs.getBoolean("is_comment"));
                    _activity.set_sign_in(rs.getBoolean("is_sign_in"));
                    _activity.setIntro(rs.getString("content"));
                    _activity.setQr_code(rs.getString("qr_code"));
                    _activity.setMall_id(rs.getInt("mall_id"));
                    _activity.setIncentive_points(rs.getInt("incentive_points"));

                    return _activity;
                }

                return null;
            }
        });

        return activity;
    }

    public Activity selectActivityById(final Integer activityId, final Integer memberId) {

        final String sql = "select a.activity_id,a.title,a.picture,a.activity_time_start,a.activity_time_end,a.address,a.is_sign_up," +
                "a.sign_up_end,a.sign_up_limited,a.sign_up_points,a.is_comment,a.is_sign_in,ar.content,a.qr_code,a.mall_id," +
                "s.sign_up_time,s.sign_in_time,s.sign_type " +
                "from `T_ACTIVITY` a left join `T_ARTICLES` ar on a.intro=ar.article_id " +
                "left join (select sign_up_time,sign_in_time,sign_type,activity_id,member_id from `T_ACTIVITY_SIGN_UP_LOG` where activity_id=? and member_id=?) s " +
                "on a.activity_id=s.activity_id " +
                "where a.activity_id=?";

        final Activity activity = jdbcTemplate.query(sql, new Object[]{activityId, memberId, activityId}, new ResultSetExtractor<Activity>() {
            @Override
            public Activity extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    Activity _activity = new Activity();

                    _activity.setActivity_id(rs.getInt("activity_id"));
                    _activity.setTitle(rs.getString("title"));
                    _activity.setPicture(rs.getString("picture"));
                    _activity.setActivity_time_start(new Date(rs.getLong("activity_time_start")));
                    _activity.setActivity_time_end(new Date(rs.getLong("activity_time_end")));
                    _activity.setAddress(rs.getString("address"));
                    _activity.set_sign_up(rs.getBoolean("is_sign_up"));
                    _activity.setSign_up_limited(rs.getInt("sign_up_limited"));
                    _activity.setSign_up_end(new Date(rs.getLong("sign_up_end")));
                    _activity.setSign_up_points(rs.getInt("sign_up_points"));
                    _activity.set_comment(rs.getBoolean("is_comment"));
                    _activity.set_sign_in(rs.getBoolean("is_sign_in"));
                    _activity.setIntro(rs.getString("content"));
                    _activity.setQr_code(rs.getString("qr_code"));
                    _activity.setMall_id(rs.getInt("mall_id"));
                    _activity.setSign_up_time(new Date(rs.getLong("sign_up_time")));
                    _activity.setSign_in_time(new Date(rs.getLong("sign_in_time")));
                    _activity.setSign_type(rs.getInt("sign_type"));

                    return _activity;
                }

                return null;
            }
        });

        return activity;
    }

    public List<Activity> selectActivityList(final ActivityQueryCondition condition, int from) {
        final StringBuilder sb = new StringBuilder();
        Object[] args = null;

        if (from == 0) { //我的活动列表
            sb.append("select a.activity_id,a.title,a.picture,a.activity_time_start,a.activity_time_end,a.sign_up_end,a.status,");
            sb.append("s.sign_up_time,s.sign_in_time,s.sign_type");
            sb.append(" from `T_ACTIVITY` a inner join `T_ACTIVITY_SIGN_UP_LOG` s on a.activity_id=s.activity_id");
            sb.append(" where a.mall_id=? and s.member_id=?");
            sb.append(" order by a.").append(condition.getSort()).append(" ").append(condition.getDirection()).append(" limit ?,?");

            args = new Object[]{condition.getMallId(), condition.getMemberId(), condition.getOffset(), condition.getSize()};
        } else if (from == 1) { //活动列表
            sb.append("select a.activity_id,title,picture,activity_time_start,activity_time_end,sign_up_end,status,sign_type,is_sign_up,is_sign_in");
            sb.append(" from `T_ACTIVITY` a left join `T_ACTIVITY_SIGN_UP_LOG` s on a.activity_id=s.activity_id and s.member_id=?");
            sb.append(" where a.mall_id=? and status=1 and ? <= activity_time_end");
            sb.append(" order by ").append(condition.getSort()).append(" ").append(condition.getDirection()).append(" limit ?,?");

            args = new Object[]{condition.getMemberId(), condition.getMallId(), condition.getCurrTime(), condition.getOffset(), condition.getSize()};
        }

        final String sql = sb.toString();

        final List<Activity> list = jdbcTemplate.query(sql, args, new ResultSetExtractor<List<Activity>>() {
            @Override
            public List<Activity> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Activity> _list = new ArrayList<>();

                while (rs.next()) {
                    Activity activity = new Activity();

                    activity.setActivity_id(rs.getInt("activity_id"));
                    activity.setTitle(rs.getString("title"));
                    activity.setActivity_time_start(new Date(rs.getLong("activity_time_start")));
                    activity.setActivity_time_end(new Date(rs.getLong("activity_time_end")));
                    activity.setStatus(rs.getInt("status"));
                    activity.setPicture(rs.getString("picture"));

                    if (dbUtils.hasColumn(rs, "sign_up_time"))
                        activity.setSign_up_time(new Date(rs.getLong("sign_up_time")));

                    if (dbUtils.hasColumn(rs, "sign_in_time"))
                        activity.setSign_in_time(new Date(rs.getLong("sign_in_time")));

                    if (dbUtils.hasColumn(rs, "sign_type"))
                        activity.setSign_type(rs.getInt("sign_type"));

                    if (dbUtils.hasColumn(rs, "is_sign_up"))
                        activity.set_sign_up(rs.getBoolean("is_sign_up"));

                    if (dbUtils.hasColumn(rs, "is_sign_in"))
                        activity.set_sign_in(rs.getBoolean("is_sign_in"));

                    _list.add(activity);
                }

                return _list;
            }
        });

        return list;
    }

    public int insertSignUpLog(final ActivitySignUpLog signUpLog) {
        final String sql = "insert into `T_ACTIVITY_SIGN_UP_LOG` " +
                "(activity_id,member_id,sign_up_time,sign_in_time,mall_id,sign_type) values " +
                "(?,?,?,?,?,?)";

        return jdbcTemplate.update(sql, signUpLog.getActivity_id(), signUpLog.getMember_id(), signUpLog.getSign_up_time(),
                signUpLog.getSign_in_time(), signUpLog.getMall_id(), signUpLog.getSign_type());
    }

    public int updateSignUpLog(final ActivitySignUpLog signUpLog) {
        final String sql = "update `T_ACTIVITY_SIGN_UP_LOG` set " +
                "sign_in_time=?,sign_type=?" +
                " where activity_id=? and member_id=?";

        return jdbcTemplate.update(sql, signUpLog.getSign_in_time(),
                signUpLog.getSign_type(), signUpLog.getActivity_id(), signUpLog.getMember_id());
    }

    public int selectSignUpCount(final Integer activityId) {
        final String sql = "select count(1) cnt from `T_ACTIVITY_SIGN_UP_LOG` where activity_id=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{activityId});
        row.first();

        return row.getInt("cnt");
    }

    public int selectMemberSignType(final Integer activityId, final Integer memberId, final Integer signType) {
        final String sql = "select count(1) cnt from `T_ACTIVITY_SIGN_UP_LOG` where activity_id=? and member_id=? and sign_type=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{activityId, memberId, signType});
        row.first();

        return row.getInt("cnt");
    }

    public int selectMemberSignUp(final Integer activityId, final Integer memberId) {
        final String sql = "select count(1) cnt from `T_ACTIVITY_SIGN_UP_LOG` where activity_id=? and member_id=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{activityId, memberId});
        row.first();

        return row.getInt("cnt");
    }

    public List<ReceiveCouponInfo> selectIncentiveCouponInfoList(final Integer activityId) {
        final String sql = "select ci.coupon_id,coupon_type,ci.mall_id,receive_method,required_points from `T_COUPON_INFO` ci " +
                "inner join `T_ACTIVITY_INCENTIVE_COUPON` ai " +
                "on ci.coupon_id=ai.coupon_id where ai.activity_id=?";

        List<ReceiveCouponInfo> list = jdbcTemplate.query(sql, new Object[]{activityId}, new ResultSetExtractor<List<ReceiveCouponInfo>>() {
            @Override
            public List<ReceiveCouponInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<ReceiveCouponInfo> _list = new ArrayList<>();

                while (rs.next()) {
                    ReceiveCouponInfo info = new ReceiveCouponInfo();
                    info.setCoupon_id(rs.getInt("coupon_id"));
                    info.setCoupon_type(rs.getInt("coupon_type"));
                    info.setMall_id(rs.getInt("mall_id"));
                    info.setReceive_method(rs.getInt("receive_method"));
                    info.setRequired_points(rs.getInt("required_points"));
                    _list.add(info);
                }
                return _list;
            }
        });

        return list;


    }
}
