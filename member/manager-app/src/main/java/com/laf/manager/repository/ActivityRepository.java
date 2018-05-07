package com.laf.manager.repository;

import com.laf.manager.dto.Activity;
import com.laf.manager.dto.ActivitySignUpLog;
import com.laf.manager.querycondition.activity.ActivityLogQueryCondition;
import com.laf.manager.querycondition.activity.ActivityQueryCondition;
import com.laf.manager.utils.db.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class ActivityRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DbUtils dbUtils;

    public Activity selectActivityById(final Integer activityId) {

        final String sql = "select a.activity_id,a.title,a.picture,a.activity_time_start,a.address,a.is_sign_up,a.intro,a.activity_time_end," +
                "a.sign_up_end,a.sign_up_limited,a.sign_up_points,a.is_comment,a.is_sign_in,is_incentive,status,incentive_points,qr_code,ar.content,a.mall_id " +
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
                    _activity.setIs_sign_up(rs.getBoolean("is_sign_up"));
                    _activity.setSign_up_limited(rs.getInt("sign_up_limited"));
                    _activity.setSign_up_end(new Date(rs.getLong("sign_up_end")));
                    _activity.setSign_up_points(rs.getInt("sign_up_points"));
                    _activity.set_comment(rs.getBoolean("is_comment"));
                    _activity.setIs_sign_in(rs.getBoolean("is_sign_in"));
                    _activity.set_incentive(rs.getBoolean("is_incentive"));
                    _activity.setIncentive_points(rs.getInt("incentive_points"));
                    _activity.setStatus(rs.getInt("status"));
                    _activity.setIntro_id(rs.getInt("intro"));
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

//    public Activity selectActivityById(final Integer activityId, final Integer memberId) {
//
//        final String sql = "select a.activity_id,a.title,a.picture,a.activity_time_start,a.activity_time_end,a.address,a.is_sign_up," +
//                "a.sign_up_end,a.sign_up_limited,a.sign_up_points,a.is_comment,a.is_sign_in,ar.content,a.qr_code,a.mall_id," +
//                "s.sign_up_time,s.sign_in_time,s.sign_type " +
//                "from `T_ACTIVITY` a left join `T_ARTICLES` ar on a.intro=ar.article_id " +
//                "left join (select sign_up_time,sign_in_time,sign_type,activity_id,member_id from `T_ACTIVITY_SIGN_UP_LOG` where activity_id=? and member_id=?) s " +
//                "on a.activity_id=s.activity_id " +
//                "where a.activity_id=?";
//
//        final Activity activity = jdbcTemplate.query(sql, new Object[]{activityId,memberId,activityId}, new ResultSetExtractor<Activity>() {
//            @Override
//            public Activity extractData(ResultSet rs) throws SQLException, DataAccessException {
//                if (rs.next()) {
//                    Activity _activity = new Activity();
//
//                    _activity.setActivity_id(rs.getInt("activity_id"));
//                    _activity.setTitle(rs.getString("title"));
//                    _activity.setPicture(rs.getString("picture"));
//                    _activity.setActivity_time_start(new Date(rs.getLong("activity_time_start")));
//                    _activity.setActivity_time_end(new Date(rs.getLong("activity_time_end")));
//                    _activity.setAddress(rs.getString("address"));
//                    _activity.set_sign_up(rs.getBoolean("is_sign_up"));
//                    _activity.setSign_up_limited(rs.getInt("sign_up_limited"));
//                    _activity.setSign_up_end(new Date(rs.getLong("sign_up_end")));
//                    _activity.setSign_up_points(rs.getInt("sign_up_points"));
//                    _activity.set_comment(rs.getBoolean("is_comment"));
//                    _activity.set_sign_in(rs.getBoolean("is_sign_in"));
//                    _activity.setIntro(rs.getString("content"));
//                    _activity.setQr_code(rs.getString("qr_code"));
//                    _activity.setMall_id(rs.getInt("mall_id"));
//                    _activity.setSign_up_time(new Date(rs.getLong("sign_up_time")));
//                    _activity.setSign_in_time(new Date(rs.getLong("sign_in_time")));
//                    _activity.setSign_type(rs.getInt("sign_type"));
//
//                    return _activity;
//                }
//
//                return null;
//            }
//        });
//
//        return activity;
//    }

    public List<Activity> selectActivityList(final ActivityQueryCondition condition) {

        final String sql = "SELECT a.activity_id,title,activity_time_start,activity_time_end,is_sign_up,is_sign_in," +
                "sign_up_points,status,incentive_points,qr_code,a.mall_id,signup.cnt as ucnt,signin.cnt as icnt" +
                " FROM `T_ACTIVITY` a";

        //1.不报名不签到 报名签到人数为0
        //2.不报名签到 报名人数为0，签到人数有值
        //3.报名不签到 报名人数有值，签到人数为0
        //4.报名签到活动 signup+signin=总参与人数
        final StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        if (condition.getSignUpCountMin() != 0 && condition.getSignUpCountMax() != 0) {
            sb.append(" INNER JOIN (SELECT activity_id,count(1) cnt FROM `T_ACTIVITY_SIGN_UP_LOG` WHERE sign_type = 1 GROUP BY activity_id) signup " +
                    "ON a.activity_id=signup.activity_id AND signup.cnt<=? and signup.cnt>=?");
            args.add(condition.getSignUpCountMax());
            args.add(condition.getSignUpCountMin());
            argsType.add(Types.INTEGER);
            argsType.add(Types.INTEGER);
        } else {
            sb.append(" LEFT JOIN (SELECT activity_id,count(1) cnt FROM `T_ACTIVITY_SIGN_UP_LOG` WHERE sign_type = 1 GROUP BY activity_id) signup " +
                    "ON a.activity_id=signup.activity_id");
        }

        if (condition.getSignInCountMin() != 0 && condition.getSignInCountMax() != 0) {
            sb.append(" INNER JOIN (SELECT activity_id,count(1) cnt FROM `T_ACTIVITY_SIGN_UP_LOG` WHERE sign_type = 2 GROUP BY activity_id) signin " +
                    "ON a.activity_id=signin.activity_id AND signin.cnt<=? and signin.cnt>=?");
            args.add(condition.getSignInCountMax());
            args.add(condition.getSignInCountMin());
            argsType.add(Types.INTEGER);
            argsType.add(Types.INTEGER);
        } else {
            sb.append(" LEFT JOIN (SELECT activity_id,count(1) cnt FROM `T_ACTIVITY_SIGN_UP_LOG` WHERE sign_type = 2 GROUP BY activity_id) signin " +
                    "ON a.activity_id=signin.activity_id");
        }

        sb.append(" WHERE a.mall_id=?");
        args.add(condition.getMallId());
        argsType.add(Types.INTEGER);

        if (!StringUtils.isEmpty(condition.getTitle())) {
            sb.append(" AND title like '%' ? '%'");
            args.add(condition.getTitle());
            argsType.add(Types.VARCHAR);
        }

        switch (condition.getProcess()) {
            case 0:
                sb.append(" AND ? < activity_time_start");
                args.add(condition.getCurrTime());
                argsType.add(Types.BIGINT);
                break;

            case 1:
                sb.append(" AND ? >= activity_time_start and ? <= activity_time_end");
                args.add(condition.getCurrTime());
                args.add(condition.getCurrTime());
                argsType.add(Types.BIGINT);
                argsType.add(Types.BIGINT);
                break;

            case 2:
                sb.append(" AND ? > activity_time_end");
                args.add(condition.getCurrTime());
                argsType.add(Types.BIGINT);
        }

        switch (condition.getSignInOrSignUp()) {
            case 0:
                sb.append(" AND is_sign_up = 1");
                break;

            case 1:
                sb.append(" AND is_sign_in = 1");
                break;
        }

        if (condition.getIsIncentive() != -1) {
            sb.append(" AND is_incentive=?");
            args.add(condition.getIsIncentive());
            argsType.add(Types.TINYINT);
        }

        sb.append(" ORDER BY ");
        sb.append(condition.getSort() + " " + condition.getDirection() + " limit ?,?");
        args.add(condition.getOffset());
        args.add(condition.getSize());
        argsType.add(Types.INTEGER);
        argsType.add(Types.INTEGER);

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }
//        log.info("[{}]", sb.toString());
//        log.info("ActivityRepository#sort is {}", condition.getSort());
        final List<Activity> list = jdbcTemplate.query(sb.toString(), args.toArray(), arr, new ResultSetExtractor<List<Activity>>() {
            @Override
            public List<Activity> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Activity> _list = new ArrayList<>();

                while (rs.next()) {
                    Activity activity = new Activity();
                    activity.setActivity_id(rs.getInt("activity_id"));
                    activity.setTitle(rs.getString("title"));
                    activity.setActivity_time_start(new Date(rs.getLong("activity_time_start")));
                    activity.setActivity_time_end(new Date(rs.getLong("activity_time_end")));
                    activity.setIs_sign_up(rs.getBoolean("is_sign_up"));
                    activity.setIs_sign_in(rs.getBoolean("is_sign_in"));
                    activity.setSign_up_points(rs.getInt("sign_up_points"));
                    activity.setStatus(rs.getInt("status"));
                    activity.setIncentive_points(rs.getInt("incentive_points"));
                    activity.setQr_code(rs.getString("qr_code"));
                    activity.setMall_id(rs.getInt("mall_id"));
                    activity.setSignUpCount(rs.getInt("ucnt"));
                    activity.setSignInCount(rs.getInt("icnt"));

                    _list.add(activity);
                }

                return _list;
            }
        });

        return list;
    }

//    public int insertSignUpLog(final ActivitySignUpLog signUpLog) {
//        final String sql = "insert into `T_ACTIVITY_SIGN_UP_LOG` " +
//                "(activity_id,member_id,sign_up_time,sign_in_time,mall_id,sign_type) values " +
//                "(?,?,?,?,?,?)";
//
//        return jdbcTemplate.update(sql, signUpLog.getActivity_id(), signUpLog.getMember_id(), signUpLog.getSign_up_time(),
//                signUpLog.getSign_in_time(), signUpLog.getMall_id(), signUpLog.getSign_type());
//    }
//
//    public int updateSignUpLog(final ActivitySignUpLog signUpLog) {
//        final String sql = "update `T_ACTIVITY_SIGN_UP_LOG` set sign_up_time=?," +
//                "sign_in_time=?,sign_type=?" +
//                " where activity_id=? and member_id=?";
//
//        return jdbcTemplate.update(sql, signUpLog.getSign_up_time(), signUpLog.getSign_in_time(),
//                signUpLog.getSign_type(), signUpLog.getActivity_id(), signUpLog.getMember_id());
//    }

    public int selectSignUpCount(final Integer activityId) {
        final String sql = "select count(1) cnt from `T_ACTIVITY_SIGN_UP_LOG` where activity_id=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{activityId});
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public int insertActivity(Activity activity) {
        final String sql = "insert into `T_ACTIVITY` (title,picture,activity_time_start,address,is_sign_up,sign_up_end," +
                "sign_up_limited,sign_up_points,is_comment,is_sign_in,is_incentive,status,incentive_points,mall_id,qr_code,activity_time_end,intro) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, activity.getTitle());
                ps.setString(2, activity.getPicture());
                ps.setLong(3, activity.getActivity_time_start());
                ps.setString(4, activity.getAddress());
                ps.setBoolean(5, activity.isIs_sign_up());
                ps.setLong(6, activity.getSign_up_end());
                ps.setInt(7, activity.getSign_up_limited());
                ps.setInt(8, activity.getSign_up_points());
                ps.setBoolean(9, activity.is_comment());
                ps.setBoolean(10, activity.isIs_sign_in());
                ps.setBoolean(11, activity.is_incentive());
                ps.setInt(12, activity.getStatus());
                ps.setInt(13, activity.getIncentive_points());
                ps.setInt(14, activity.getMall_id());
                ps.setString(15, activity.getQr_code());
                ps.setLong(16, activity.getActivity_time_end());
                ps.setInt(17, activity.getIntro_id());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int updateActivity(Activity activity) {
        final String sql = "update `T_ACTIVITY` set title=?,picture=?,activity_time_start=?,address=?,is_sign_up=?,sign_up_end=?," +
                "sign_up_limited=?,sign_up_points=?,is_comment=?,is_sign_in=?,is_incentive=?,status=?,incentive_points=?," +
                "mall_id=?,qr_code=?,activity_time_end=?,intro=?" +
                " where activity_id=?";

        int result = jdbcTemplate.update(sql, activity.getTitle(), activity.getPicture(), activity.getActivity_time_start(), activity.getAddress(), activity.isIs_sign_up(),
                activity.getSign_up_end(), activity.getSign_up_limited(), activity.getSign_up_points(), activity.is_comment(), activity.isIs_sign_in(), activity.is_incentive(),
                activity.getStatus(), activity.getIncentive_points(), activity.getMall_id(), activity.getQr_code(), activity.getActivity_time_end(), activity.getIntro_id(), activity.getActivity_id());

        return result;
    }

    public int updateActivityQRCode(final int activityId, final String url) {
        final String sql = "update `T_ACTIVITY` set qr_code=? where activity_id=?";
        int result = jdbcTemplate.update(sql, url, activityId);

        return result;
    }

    public int deleteAtivity(final Integer activityId) {
        final String sql = "delete from `T_ACTIVITY` where activity_id=?";

        return jdbcTemplate.update(sql, activityId);
    }

    public int selectActivityCount(final ActivityQueryCondition condition) {
//        final String sql = "select count(1) cnt from `T_ACTIVITY` a where a.mall_id=?";
        final String sql = "select count(1) cnt from `T_ACTIVITY` a";

        final StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        if (condition.getSignUpCountMin() != 0 && condition.getSignUpCountMax() != 0) {
            sb.append(" INNER JOIN (SELECT activity_id,count(1) cnt FROM `T_ACTIVITY_SIGN_UP_LOG` WHERE sign_type = 1 GROUP BY activity_id) signup " +
                    "ON a.activity_id=signup.activity_id AND signup.cnt<=? and signup.cnt>=?");
            args.add(condition.getSignUpCountMax());
            args.add(condition.getSignUpCountMin());
            argsType.add(Types.INTEGER);
            argsType.add(Types.INTEGER);
        } else {
            sb.append(" LEFT JOIN (SELECT activity_id,count(1) cnt FROM `T_ACTIVITY_SIGN_UP_LOG` WHERE sign_type = 1 GROUP BY activity_id) signup " +
                    "ON a.activity_id=signup.activity_id");
        }

        if (condition.getSignInCountMin() != 0 && condition.getSignInCountMax() != 0) {
            sb.append(" INNER JOIN (SELECT activity_id,count(1) cnt FROM `T_ACTIVITY_SIGN_UP_LOG` WHERE sign_type = 2 GROUP BY activity_id) signin " +
                    "ON a.activity_id=signin.activity_id AND signin.cnt<=? and signin.cnt>=?");
            args.add(condition.getSignInCountMax());
            args.add(condition.getSignInCountMin());
            argsType.add(Types.INTEGER);
            argsType.add(Types.INTEGER);
        } else {
            sb.append(" LEFT JOIN (SELECT activity_id,count(1) cnt FROM `T_ACTIVITY_SIGN_UP_LOG` WHERE sign_type = 2 GROUP BY activity_id) signin " +
                    "ON a.activity_id=signin.activity_id");
        }

        sb.append(" WHERE a.mall_id=?");
        args.add(condition.getMallId());
        argsType.add(Types.INTEGER);

        if (!StringUtils.isEmpty(condition.getTitle())) {
            sb.append(" and title like '%' ? '%'");
            args.add(condition.getTitle());
            argsType.add(Types.VARCHAR);
        }

        switch (condition.getProcess()) {
            case 0:
                sb.append(" and ? < activity_time_start");
                args.add(condition.getCurrTime());
                argsType.add(Types.INTEGER);
                break;

            case 1:
                sb.append(" and ? >= activity_time_start and ? <= activity_time_end");
                args.add(condition.getCurrTime());
                args.add(condition.getCurrTime());
                argsType.add(Types.INTEGER);
                argsType.add(Types.INTEGER);
                break;

            case 2:
                sb.append(" and ? > activity_time_end");
                args.add(condition.getCurrTime());
                argsType.add(Types.INTEGER);
        }

        switch (condition.getSignInOrSignUp()) {
            case 0:
                sb.append(" and is_sign_up = 1");
                break;

            case 1:
                sb.append(" and is_sign_in = 1");
                break;
        }

        if (condition.getIsIncentive() != -1) {
            sb.append(" and is_incentive=?");
            args.add(condition.getIsIncentive());
            argsType.add(Types.INTEGER);
        }

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }

        log.info("[{}]", sb.toString());
        SqlRowSet row = jdbcTemplate.queryForRowSet(sb.toString(), args.toArray(), arr);
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public List<Integer> selectActivityCountMonthly(String year, int mallId) {
        final String sql = "select count,t2.MONTH from\n" +
                "(select count(1) `count`,t.y,t.m from\n" +
                "(select activity_id,(select from_unixtime(activity_time_start/1000,'%Y')) y,(select from_unixtime(activity_time_start/1000,'%m')) m from t_activity where mall_id=?) t where t.y=? group by t.y,t.m) t1\n" +
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

        return jdbcTemplate.query(sql, new Object[]{mallId, year},
                (rs, rowNum) -> rs.getInt("count"));
    }

    public List<ActivitySignUpLog> selectActivityLogList(ActivityLogQueryCondition condition) {
//return null;
        String sql = "SELECT name, mobile, sign_type, title, activity_time_start, activity_time_end, is_sign_in,is_sign_up " +
                "FROM `T_ACTIVITY_SIGN_UP_LOG` ul INNER JOIN `T_MEMBER`m ON ul.member_id=m.member_id " +
                "INNER JOIN `T_ACTIVITY` a on ul.activity_id=a.activity_id WHERE ul.mall_id=?";

        final StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        args.add(condition.getMallId());
        argsType.add(Types.INTEGER);

        if (!StringUtils.isEmpty(condition.getName())) {
            sb.append(" AND name like '%' ? '%'");
            args.add(condition.getName());
            argsType.add(Types.VARCHAR);
        }

        if (!StringUtils.isEmpty(condition.getMobile())) {
            sb.append(" AND mobile like '%' ? '%'");
            args.add(condition.getMobile());
            argsType.add(Types.VARCHAR);
        }


        if (!StringUtils.isEmpty(condition.getTitle())) {
            sb.append(" AND title like '%' ? '%'");
            args.add(condition.getTitle());
            argsType.add(Types.VARCHAR);
        }

        switch (condition.getProcess()) {
            case 0:
                sb.append(" AND ? < activity_time_start");
                args.add(condition.getCurr());
                argsType.add(Types.BIGINT);

                break;
            case 1:
                sb.append(" AND ? >= activity_time_start AND ? <= activity_time_end");
                args.add(condition.getCurr());
                argsType.add(Types.BIGINT);
                args.add(condition.getCurr());
                argsType.add(Types.BIGINT);

                break;
            case 2:
                sb.append(" AND ? > activity_time_end");
                args.add(condition.getCurr());
                argsType.add(Types.BIGINT);

                break;
        }

        if (condition.getSignType() > 0) {
            sb.append(" AND sign_type=?");
            args.add(condition.getSignType());
            argsType.add(Types.INTEGER);
        }

        sb.append(" ORDER BY activity_time_end ASC limit ?,?");
        args.add(condition.getOffset());
        args.add(condition.getSize());
        argsType.add(Types.INTEGER);
        argsType.add(Types.INTEGER);

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }

        List<ActivitySignUpLog> list = jdbcTemplate.query(sb.toString(), args.toArray(), arr, new ResultSetExtractor<List<ActivitySignUpLog>>() {
            @Override
            public List<ActivitySignUpLog> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<ActivitySignUpLog> _list = new ArrayList<>();

                while (rs.next()) {
                    ActivitySignUpLog log = new ActivitySignUpLog();
                    log.setName(rs.getString("name"));
                    log.setMobile(rs.getString("mobile"));
                    log.setSign_type(rs.getInt("sign_type"));
                    log.setTitle(rs.getString("title"));
                    log.setActivity_time_start(new Date(rs.getLong("activity_time_start")));
                    log.setActivity_time_end(new Date(rs.getLong("activity_time_end")));
                    log.set_sign_in(rs.getBoolean("is_sign_in"));
                    log.set_sign_up(rs.getBoolean("is_sign_up"));

                    _list.add(log);
                }
                return _list;
            }
        });

        return list;
    }
//public List<ActivitySignUpLog> selectActivityLogList() {
//        return null;
//}


    public int selectActivityLogCount(ActivityLogQueryCondition condition) {
//        return 0;

        String sql = "SELECT count(1) cnt FROM `T_ACTIVITY_SIGN_UP_LOG` ul INNER JOIN `T_MEMBER`m ON ul.member_id=m.member_id " +
                "INNER JOIN `T_ACTIVITY` a ON ul.activity_id=a.activity_id WHERE ul.mall_id=?";

        final StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        args.add(condition.getMallId());
        argsType.add(Types.INTEGER);

        if (!StringUtils.isEmpty(condition.getName())) {
            sb.append(" AND name like '%' ? '%'");
            args.add(condition.getName());
            argsType.add(Types.VARCHAR);
        }

        if (!StringUtils.isEmpty(condition.getMobile())) {
            sb.append(" AND mobile like '%' ? '%'");
            args.add(condition.getMobile());
            argsType.add(Types.VARCHAR);
        }


        if (!StringUtils.isEmpty(condition.getTitle())) {
            sb.append(" AND title like '%' ? '%'");
            args.add(condition.getTitle());
            argsType.add(Types.VARCHAR);
        }

        switch (condition.getProcess()) {
            case 0:
                sb.append(" AND ? < activity_time_start");
                args.add(condition.getCurr());
                argsType.add(Types.BIGINT);

                break;
            case 1:
                sb.append(" AND ? >= activity_time_start AND ? <= activity_time_end");
                args.add(condition.getCurr());
                argsType.add(Types.BIGINT);
                args.add(condition.getCurr());
                argsType.add(Types.BIGINT);

                break;
            case 2:
                sb.append(" AND ? > activity_time_end");
                args.add(condition.getCurr());
                argsType.add(Types.BIGINT);

                break;
        }

        if (condition.getSignType() != -1) {
            sb.append(" AND sign_type=?");
            args.add(condition.getSignType());
            argsType.add(Types.INTEGER);
        }

        sb.append(" ORDER BY activity_time_end ASC limit ?,?");
        args.add(condition.getOffset());
        args.add(condition.getSize());
        argsType.add(Types.INTEGER);
        argsType.add(Types.INTEGER);

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sb.toString(), args.toArray(), arr);

        int result = -1;
        if (rowSet.first()) {
            result = rowSet.getInt("cnt");
        }
        return result;
    }

}

