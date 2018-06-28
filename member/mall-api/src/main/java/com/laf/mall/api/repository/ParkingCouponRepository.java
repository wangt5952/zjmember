package com.laf.mall.api.repository;

import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class ParkingCouponRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Parking> selectParkingByMemberId(final Integer memberId) {
        final String sql = "select parking_id, member_id, car_number, isdefault from T_PARKING where member_id=? order by isdefault desc ";
        final List<Parking> list = jdbcTemplate.query(sql, new Object[]{memberId}, new int[]{Types.INTEGER},
                new ResultSetExtractor<List<Parking>>() {
                    @Override
                    public List<Parking> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<Parking> list = new ArrayList<>();

                        while (rs.next()) {
                            Parking info = new Parking();
                            info.setParking_id(rs.getInt("parking_id"));
                            info.setMember_id(rs.getInt("member_id"));
                            info.setCar_number(rs.getString("car_number"));
                            info.setIsdefault(rs.getInt("isdefault"));
                            list.add(info);
                        }
                        return list;
                    }
                });

        return list;
    }

    public List<Parking> selectParkingByMemberId(final Integer memberId, String car_number) {
        final String sql = "select parking_id, member_id, car_number, isdefault from T_PARKING where member_id=? and car_number=? order by isdefault desc ";
        final List<Parking> list = jdbcTemplate.query(sql, new Object[]{memberId,car_number}, new int[]{Types.INTEGER,Types.VARCHAR},
                new ResultSetExtractor<List<Parking>>() {
                    @Override
                    public List<Parking> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<Parking> list = new ArrayList<>();

                        while (rs.next()) {
                            Parking info = new Parking();
                            info.setParking_id(rs.getInt("parking_id"));
                            info.setMember_id(rs.getInt("member_id"));
                            info.setCar_number(rs.getString("car_number"));
                            info.setIsdefault(rs.getInt("isdefault"));
                            list.add(info);
                        }
                        return list;
                    }
                });

        return list;
    }

    public int insertParking(final Parking parking) {
        String sql = "INSERT INTO `T_PARKING` (member_id, car_number, isdefault) " +
                "VALUES (?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, parking.getMember_id());
                ps.setString(2, parking.getCar_number());
                ps.setInt(3, parking.getIsdefault());
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public int updateParking(final int memberId) {
        String sql = "update `T_PARKING` set isdefault=0 where member_id=? and isdefault!=0";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, memberId);
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public int updateParking(final int memberId, String carNumber) {
        String sql = "update `T_PARKING` set isdefault=1 where member_id=? and car_number=?";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, memberId);
                ps.setString(2,carNumber);
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public List<ReceiveCouponInfo> selectCouponInfoList(CouponQueryCondition condition, long registTime) {
        String sql = "SELECT ci.coupon_id,ci.coupon_name,ci.picture,ci.price,ci.required_points,ci.coupon_type,ci.receive_method," +
                "ci.active_time,ci.activable,ci.activation_condition,ci.activetion_site,ci.mall_id" +
                " FROM `T_PARKING_COUPON_INFO` ci LEFT JOIN `T_PARKING_COUPON` c ON ci.coupon_id=c.coupon_id" +
                " WHERE ci.mall_id=? AND ci.status=1 AND ci.coupon_type in (0,1)" +
                " AND (ci.circulation - (select count(1) from `T_COUPON` where coupon_id=ci.coupon_id) > 0) AND (? >= ci.issue_start AND ? <= ci.issue_end)";
//总发行量-总领取数
        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        if (condition.getMallId() == null || condition.getMallId() <= 0) return ListUtils.EMPTY_LIST;

        args.add(condition.getMallId());
        argsType.add(Types.INTEGER);
        args.add(condition.getCurrentTime());
        argsType.add(Types.BIGINT);
        args.add(condition.getCurrentTime());
        argsType.add(Types.BIGINT);

        if (registTime > 0) {
            sb.append(" AND ((? >= ci.reg_time_start AND ? <= ci.reg_time_end) OR reg_time_start=0)");
            args.add(registTime);
            args.add(registTime);
            argsType.add(Types.BIGINT);
            argsType.add(Types.BIGINT);
        }

        if (condition.getReceiveMethod() == 2 || condition.getReceiveMethod() == 0) {
            sb.append(" AND ci.receive_method=?");
            args.add(condition.getReceiveMethod());
            argsType.add(Types.INTEGER);
        }

        if (!StringUtils.isEmpty(condition.getKeywords())) {
            sb.append(" AND ci.text1 like '%' ? '%'");
            args.add(condition.getKeywords());
            argsType.add(Types.VARCHAR);
        }

        sb.append(" GROUP BY ci.coupon_id ORDER BY ");
        sb.append(condition.getSort());
        sb.append(" " + condition.getDirection());

        sb.append(" LIMIT ?,?");
        args.add(condition.getOffset());
        argsType.add(Types.INTEGER);
        args.add(condition.getSize());
        argsType.add(Types.INTEGER);

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }
        log.info("[{}]", sb.toString());

        final List<ReceiveCouponInfo> list = jdbcTemplate.query(sb.toString(), args.toArray(), arr,
                new ResultSetExtractor<List<ReceiveCouponInfo>>() {
                    @Override
                    public List<ReceiveCouponInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<ReceiveCouponInfo> list = new ArrayList<>();

                        while (rs.next()) {
                            ReceiveCouponInfo info = new ReceiveCouponInfo();
                            info.setCoupon_id(rs.getInt("coupon_id"));
                            info.setCoupon_name(rs.getString("coupon_name"));
                            info.setPicture(rs.getString("picture"));
                            info.setPrice(rs.getBigDecimal("price"));
                            info.setRequired_points(rs.getInt("required_points"));
                            info.setCoupon_type(rs.getInt("coupon_type"));
                            info.setReceive_method(rs.getInt("receive_method"));
                            info.setActive_time(new Date(rs.getLong("active_time")));
                            info.setActivable(rs.getInt("activable"));
                            info.setActivation_condition(rs.getString("activation_condition"));
                            info.setActivetion_site(rs.getString("activetion_site"));
                            info.setMall_id(rs.getInt("mall_id"));
                            list.add(info);
                        }
                        return list;
                    }
                });

        return list;
    }

    public ReceiveCouponInfo selectCouponInfoById(final Integer couponId, final long today) {
        String sql = "SELECT ci.coupon_id,ci.coupon_name,ci.picture,ci.mall_id,ci.expiry_date_start,ci.expiry_date_end,ci.circulation,ci.daily_circulation," +
                "ci.inventory,ci.receive_method,ci.coupon_type,ci.required_points,ci.limited,ci.daily_limited,ci.price," +
                "ci.keep_verification_of,ci.verification_of,ci.group_id,ci.issue_start,ci.issue_end,ci.active_time,ci.activable,ci.activation_condition," +
                "ci.activetion_site,a.content," +
                "(select count(1) from `T_PARKING_COUPON` where coupon_id=?) as total," +
                "(select count(1) from `T_PARKING_COUPON` where coupon_id=? AND receive_date>=?) as daily" +
                " FROM `T_PARKING_COUPON_INFO` ci LEFT JOIN `T_ARTICLES` a ON ci.intro=a.article_id" +
                " WHERE ci.coupon_id=?";

        log.info("[{}]" + sql);

        final ReceiveCouponInfo couponInfo = jdbcTemplate.query(sql, new Object[]{couponId, couponId, today, couponId},
                new int[]{Types.INTEGER, Types.INTEGER, Types.BIGINT, Types.INTEGER},
                new ResultSetExtractor<ReceiveCouponInfo>() {
                    @Override
                    public ReceiveCouponInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
                        ReceiveCouponInfo info = null;

                        if (rs.next()) {
                            info = new ReceiveCouponInfo();
                            info.setCoupon_id(rs.getInt("coupon_id"));
                            info.setCoupon_name(rs.getString("coupon_name"));
                            info.setPicture(rs.getString("picture"));
                            info.setMall_id(rs.getInt("mall_id"));
                            info.setExpiry_date_start(new Date(rs.getLong("expiry_date_start")));
                            info.setExpiry_date_end(new Date(rs.getLong("expiry_date_end")));
                            info.setCirculation(rs.getInt("circulation"));
                            info.setDaily_circulation(rs.getInt("daily_circulation"));
                            info.setInventory(rs.getInt("inventory"));
                            info.setReceive_method(rs.getInt("receive_method"));
                            info.setCoupon_type(rs.getInt("coupon_type"));
                            info.setRequired_points(rs.getInt("required_points"));
                            info.setLimited(rs.getInt("limited"));
                            info.setDaily_limited(rs.getInt("daily_limited"));
                            info.setPrice(rs.getBigDecimal("price"));
                            info.setKeep_verification_of(rs.getInt("keep_verification_of"));
                            info.setVerification_of(rs.getInt("verification_of"));
                            info.setGroup_id(rs.getInt("group_id"));
                            info.setIssue_start(new Date(rs.getLong("issue_start")));
                            info.setIssue_end(new Date(rs.getLong("issue_end")));
                            info.setIntro(rs.getString("content"));
                            info.setReceivedTotal(rs.getInt("total"));
                            info.setReceivedDaily(rs.getInt("daily"));
                            info.setActive_time(new Date(rs.getLong("active_time")));
                            info.setActivable(rs.getInt("activable"));
                            info.setActivation_condition(rs.getString("activation_condition"));
                            info.setActivetion_site(rs.getString("activetion_site"));
                        }
                        return info;
                    }
                });

        return couponInfo;
    }

//    public int updateCouponInventory(final Integer couponId, final Integer val) {
//        return 0;
//    }

    /**
     * 录入已领取的停车券以及相关信息
     * @param condition
     * @return
     */
    public int insertReceivedCouponByMember(final CouponReceiveCondition condition) {
        String sql = "INSERT INTO `T_PARKING_COUPON` (coupon_id,member_id,mall_id,receive_date,coupon_status,sources,source_id) " +
                "VALUES (?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, condition.getCouponId());
                ps.setInt(2, condition.getMemberId());
                ps.setInt(3, condition.getMallId());
                ps.setLong(4, condition.getCurrTime());
                ps.setInt(5, condition.getCouponStatus());
                ps.setInt(6, condition.getSources());
                ps.setInt(7, condition.getSourceId());
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    /**
     * 获取会员的券列表
     *
     * @param condition
     * @return
     */
    public List<Coupon> selectCouponListByMember(MyCouponListQueryCondition condition) {
        String sql = "select c.crl_id,c.coupon_id,c.member_id,c.mall_id,c.receive_date,ci.coupon_name,ci.picture,ci.expiry_date_start," +
                "ci.expiry_date_end,c.coupon_status,ci.coupon_type,ci.active_time,ci.activable,ci.activation_condition,ci.activetion_site,ci.price,ci.cost_price" +
                " from `T_PARKING_COUPON` c left join `T_PARKING_COUPON_INFO` ci on c.coupon_id=ci.coupon_id" +
                " where c.member_id=? and c.mall_id=?";

        if ((condition.getMemberId() == null || condition.getMemberId() <= 0)
                || (condition.getMallId() == null || condition.getMallId() <= 0)) {
            return ListUtils.EMPTY_LIST;
        }

        StringBuilder sb = new StringBuilder(sql);
        if (condition.getCouponStatus() == 2) {
            sb.append(" and c.coupon_status=2");
        } else {
            sb.append(" and c.coupon_status!=2");
        }

        sb.append(" order by ci.expiry_date_end asc limit ?,?");

        List<Coupon> list = jdbcTemplate.query(sb.toString(),
                new Object[]{condition.getMemberId(), condition.getMallId(), condition.getOffset(), condition.getSize()},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER},
                new ResultSetExtractor<List<Coupon>>() {
                    @Override
                    public List<Coupon> extractData(ResultSet rs) throws SQLException, DataAccessException {

                        List<Coupon> list = new ArrayList<>();

                        while (rs.next()) {
                            Coupon c = new Coupon();
                            c.setCrl_id(rs.getInt("crl_id"));
                            c.setCoupon_id(rs.getInt("coupon_id"));
                            c.setMember_id(rs.getInt("member_id"));
                            c.setMall_id(rs.getInt("mall_id"));
                            c.setReceive_date(new Date(rs.getLong("receive_date")));
                            c.setCoupon_name(rs.getString("coupon_name"));
                            c.setPicture(rs.getString("picture"));
                            c.setExpiry_date_start(new Date(rs.getLong("expiry_date_start")));
                            c.setExpiry_date_end(new Date(rs.getLong("expiry_date_end")));
                            c.setCoupon_status(rs.getInt("coupon_status"));
                            c.setCoupon_type(rs.getInt("coupon_type"));
                            c.setActive_time(new Date(rs.getLong("active_time")));
                            c.setActivable(rs.getInt("activable"));
                            c.setActivation_condition(rs.getString("activation_condition"));
                            c.setActivetion_site(rs.getString("activetion_site"));
                            c.setPrice(rs.getBigDecimal("price"));
                            list.add(c);
                        }
                        return list;
                    }
                });
        return list;
    }

    /**
     * 获取我的券详情
     *
     * @param crlId
     * @return
     */
    public Coupon selectCouponById(final Integer crlId) {
        final String sql = "select crl_id,c.coupon_id,member_id,c.mall_id,coupon_status,coupon_name,picture,expiry_date_start,expiry_date_end,coupon_type," +
                "active_time,activable,activation_condition,activetion_site,a.content,verification_of,keep_verification_of" +
                " from `T_PARKING_COUPON` c inner join `T_PARKING_COUPON_INFO` ci on ci.coupon_id = c.coupon_id" +
                " LEFT JOIN `T_ARTICLES` a ON ci.intro=a.article_id where crl_id=?";
        log.info("[{}]", sql);
        Coupon coupon = jdbcTemplate.query(sql, new Object[]{crlId}, new int[]{Types.INTEGER}, new ResultSetExtractor<Coupon>() {
            @Override
            public Coupon extractData(ResultSet rs) throws SQLException, DataAccessException {
                Coupon coupon = null;

                if (rs.next()) {
                    coupon = new Coupon();
                    coupon.setCrl_id(rs.getInt("crl_id"));
                    coupon.setCoupon_id(rs.getInt("coupon_id"));
                    coupon.setMember_id(rs.getInt("member_id"));
                    coupon.setMall_id(rs.getInt("mall_id"));
                    coupon.setCoupon_status(rs.getInt("coupon_status"));
                    coupon.setCoupon_name(rs.getString("coupon_name"));
                    coupon.setPicture(rs.getString("picture"));
                    coupon.setExpiry_date_start(new Date(rs.getLong("expiry_date_start")));
                    coupon.setExpiry_date_end(new Date(rs.getLong("expiry_date_end")));
                    coupon.setCoupon_type(rs.getInt("coupon_type"));
                    coupon.setActive_time(new Date(rs.getLong("active_time")));
                    coupon.setActivable(rs.getInt("activable"));
                    coupon.setActivation_condition(rs.getString("activation_condition"));
                    coupon.setActivetion_site(rs.getString("activetion_site"));
                    coupon.setIntro(rs.getString("content"));
                    coupon.setKeep_verification_of(rs.getInt("keep_verification_of"));
                    coupon.setVerification_of(rs.getInt("verification_of"));
                }
                return coupon;
            }
        });
        return coupon;
    }

    /**
     * 判断是否已领取券组里其他任意一种券
     *
     * @param couponId
     * @param memberId
     * @param groupId
     * @return
     */
    public int selectReceivedCountInCouponGroup(final Integer couponId, final Integer memberId, final Integer groupId) {
        String sql = "SELECT count(1) AS cnt FROM `T_PARKING_COUPON` WHERE coupon_id in (SELECT coupon_id FROM `T_PARKING_COUPON_INFO`" +
                "WHERE group_id = ? AND coupon_id != ?) AND member_id=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{groupId, couponId, memberId}, new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});
        rowSet.first();

        return rowSet.getInt("cnt");
    }

    /**
     * 获取会员--某一种优惠券的领取张数
     *
     * @param memberId
     * @param couponId
     * @return
     */
    public int selectReceivedByMember(final Integer memberId, final Integer couponId) {
        String sql = "select count(1) as total from `T_PARKING_COUPON` where coupon_id=? and member_id=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{couponId, memberId}, new int[]{Types.INTEGER, Types.INTEGER});
        rowSet.first();

        return rowSet.getInt("total");
    }

    /**
     * 获取会员--某一种优惠券的每日领取张数
     *
     * @param memberId
     * @param couponId
     * @return
     */
    public int selectDailyReceivedByMember(final Integer memberId, final Integer couponId, final long currTime) {
        String sql = "select count(1) as daily from `T_PARKING_COUPON` where coupon_id=? and member_id=? and receive_date>?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{couponId, memberId, currTime}, new int[]{Types.INTEGER, Types.INTEGER, Types.BIGINT});
        rowSet.first();

        return rowSet.getInt("daily");
    }

    /**
     * 获取会员-某一种优惠券有几张未核销(领取型or激活型)
     *
     * @param memberId
     * @param couponId
     * @return
     */
    public int selectNonVerificationCouponByMember(final Integer memberId, final Integer couponId) {
        String sql = "select count(1) as non from `T_PARKING_COUPON` where coupon_id=? and member_id=? and (coupon_status=0 or coupon_status=1)";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{couponId, memberId}, new int[]{Types.INTEGER, Types.INTEGER});
        rowSet.first();
        int count = rowSet.getInt("non");

        return count;
    }

    /**
     * 检查商户是否在某一种券的核销范围内
     * @param vc
     * @param coupon_id
     * @return
     */
    public int selectVerificationWideByShop(VerificationClerk vc, final int coupon_id) {
        String sql = "select count(1) as c from `T_COUPON_VERIFICATION_WIDE` where coupon_id=? and shop_id=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{coupon_id, vc.getShop_id()}, new int[]{Types.INTEGER, Types.INTEGER});
        rowSet.first();

        return rowSet.getInt("c");
    }

    /**
     * 更新某一张券的状态
     * @param status
     * @param crlId
     * @return
     */
    public int updateCouponStatus(final int status, final int crlId) {
        String sql = "update `T_PARKING_COUPON` set coupon_status=? where crl_id=?";

        return jdbcTemplate.update(sql, status, crlId);
    }

    /**
     * 核销后，新增核销日志
     * @param vc
     * @param coupon
     * @return
     */
    public int insertVerification(VerificationClerk vc, Coupon coupon) {
        String sql = "insert into `T_VERIFICATION_LOG` (coupon_id,member_id,vc_id,verification_date,shop_id,mall_id,crl_id) values (?,?,?,?,?,?,?)";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, coupon.getCoupon_id());
                ps.setInt(2, coupon.getMember_id());
                ps.setInt(3, vc.getVc_id());
                ps.setLong(4, new Date().getTime());
                ps.setInt(5, vc.getShop_id());
                ps.setInt(6, coupon.getMall_id());
                ps.setInt(7, coupon.getCrl_id());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    /**
     * 获得赠送规则的优惠券信息
     * @param mallId
     * @return
     */
    public String selectCouponRule(int mallId) {
        final String sql = "select coupon from `T_SETTINGS` WHERE mall_id=?";

        final String rule = jdbcTemplate.query(sql, new Object[] {mallId}, new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {

                if (rs.next()) {

                    String _rule = rs.getString("coupon");

                    return _rule;

                }

                return null;
            }
        });

        return rule;
    }

    /**
     * 根据状态获取会员的券的数量
     * @param memberId
     * @param couponId
     * @param couponStatus
     * @return
     */
    public int selectCouponCountByStatus(final int memberId, final int couponId, final int couponStatus) {
        String sql = "SELECT count(1) cnt FROM `T_PARKING_COUPON` where member_id=? and coupon_id=? and coupon_status=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{memberId, couponId, couponStatus}, new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});
        rowSet.first();

        return rowSet.getInt("cnt");
    }

    /**
     * 根据状态获取券的数量
     * @param couponId
     * @param couponStatus
     * @return
     */
    public int selectCouponCountByStatus(final int couponId, final int couponStatus) {
        String sql = "SELECT count(1) cnt FROM `T_PARKING_COUPON` where coupon_id=? and coupon_status=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{couponId, couponStatus}, new int[]{Types.INTEGER, Types.INTEGER});
        rowSet.first();

        return rowSet.getInt("cnt");
    }

    public int insertCouponActiveLog(Activate activate) {
        final String sql = "insert into `T_COUPON_ACTIVE_LOG` (coupon_id,member_id,vc_id,active_time,mall_id,receive_date,cri_id) values (?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, activate.getCoupon_id());
                ps.setInt(2, activate.getMember_id());
                ps.setInt(3, activate.getVc_id());
                ps.setLong(4,activate.getActive_time());
                ps.setInt(5, activate.getMall_id());
                ps.setLong(6, activate.getReceive_date());
                ps.setInt(7, activate.getCri_id());
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().intValue();

    }

    public List<ReceiveCouponInfo> selectRelateCouponInfoList(final int mallId) {
        String sql = "select coupon_id,coupon_name,picture,expiry_date_start,expiry_date_end,price FROM `T_PARKING_COUPON_INFO` " +
                "WHERE ?>=expiry_date_start AND ?<=expiry_date_end AND coupon_type=3 AND mall_id=?";

        DateTimeUtils utils = new DateTimeUtils();
        long today = utils.getMilliByToDay();

        List<ReceiveCouponInfo> list = jdbcTemplate.query(sql, new Object[]{today, today, mallId}, new ResultSetExtractor<List<ReceiveCouponInfo>>() {
            @Override
            public List<ReceiveCouponInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<ReceiveCouponInfo> _list = new ArrayList<>();

                while (rs.next()) {
                    ReceiveCouponInfo info = new ReceiveCouponInfo();
                    info.setCoupon_id(rs.getInt("coupon_id"));
                    info.setCoupon_name(rs.getString("coupon_name"));
                    info.setPicture(rs.getString("picture"));
                    info.setExpiry_date_start(new Date(rs.getLong("expiry_date_start")));
                    info.setExpiry_date_end(new Date(rs.getLong("expiry_date_end")));
                    info.setPrice(rs.getBigDecimal("price"));
                    _list.add(info);
                }
                return _list;
            }
        });
        return list;
    }

    public List<ReceiveCouponInfo> selectCouponInfoListByShopId(final CouponForShopQueryCondition condition) {
        final String sql = "SELECT ci.coupon_id,ci.coupon_name,ci.picture,ci.price,ci.required_points,ci.coupon_type,ci.receive_method," +
                "ci.active_time,ci.activable,ci.activation_condition,ci.activetion_site " +
                "FROM `T_COUPON_VERIFICATION_WIDE` cv INNER JOIN `T_PARKING_COUPON_INFO` ci ON cv.coupon_id=ci.coupon_id " +
                "WHERE shop_id=? AND cv.mall_id=? ORDER BY create_date DESC LIMIT ?,?";

        final List<ReceiveCouponInfo> list = jdbcTemplate.query(sql,
                new Object[]{condition.getShopId(), condition.getMallId(), condition.getOffset(), condition.getSize()},
                new ResultSetExtractor<List<ReceiveCouponInfo>>() {
                    @Override
                    public List<ReceiveCouponInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<ReceiveCouponInfo> _list = new ArrayList<>();

                        while (rs.next()) {
                            ReceiveCouponInfo info = new ReceiveCouponInfo();
                            info.setCoupon_id(rs.getInt("coupon_id"));
                            info.setCoupon_name(rs.getString("coupon_name"));
                            info.setPicture(rs.getString("picture"));
                            info.setPrice(rs.getBigDecimal("price"));
                            info.setRequired_points(rs.getInt("required_points"));
                            info.setCoupon_type(rs.getInt("coupon_type"));
                            info.setReceive_method(rs.getInt("receive_method"));
                            info.setActive_time(new Date(rs.getLong("active_time")));
                            info.setActivable(rs.getInt("activable"));
                            info.setActivation_condition(rs.getString("activation_condition"));
                            info.setActivetion_site(rs.getString("activetion_site"));
                            _list.add(info);
                        }
                        return _list;
                    }
                });

        return list;
    }

    public ParkingInfo selectParkingInfo() {
        String sql = "SELECT content FROM `t_parking_info` order by pinfo_id desc limit 1";

        final ParkingInfo info = jdbcTemplate.query(sql, new ResultSetExtractor<ParkingInfo>() {
            @Override
            public ParkingInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
                ParkingInfo info = null;

                if (rs.next()) {
                    info = new ParkingInfo();
                    info.setIntro(rs.getString("content"));
                }
                return info;
            }
        });
        return info;
    }

    /**
     * 核销后，新增核销日志
     * @param vc
     * @param coupon
     * @return
     */
    public int insertParkingLog(int crlId, int memberId, String carNumber, BigDecimal price, Date inDate,String ticketNo) {
        String sql = "insert into `t_parking_log` (crl_id,member_id,car_number,use_date,price,in_date,out_date,ticket_no) values (?,?,?,?,?,?,?,?)";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, crlId);
                ps.setInt(2, memberId);
                ps.setString(3, carNumber);
                ps.setLong(4, new Date().getTime());
                ps.setBigDecimal(5, price);
                ps.setLong(6, inDate.getTime());
                ps.setLong(7, new Date().getTime());
                ps.setString(8, ticketNo);

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }
}
