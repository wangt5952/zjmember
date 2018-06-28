package com.laf.mall.api.repository;

import com.laf.mall.api.dto.GiftCoupon;
import com.laf.mall.api.dto.GiftCouponInfo;
import com.laf.mall.api.dto.VerificationClerk;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.querycondition.giftcoupon.GiftCouponQueryCondition;
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

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class GiftCouponRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<GiftCouponInfo> selectGiftCouponInfoList(GiftCouponQueryCondition condition, long registTime) {
        String sql = "SELECT ci.coupon_id,ci.coupon_name,ci.picture,ci.price,ci.required_points,ci.receive_method,ci.mall_id" +
                " FROM `T_GIFT_COUPON_INFO` ci LEFT JOIN `T_GIFT_COUPON` c ON ci.coupon_id=c.coupon_id" +
                " WHERE ci.mall_id=? AND ci.status=1" +
                " AND (ci.circulation - (select count(1) from `T_GIFT_COUPON` where coupon_id=ci.coupon_id) > 0) AND (? >= ci.issue_start AND ? <= ci.issue_end)";
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
           // sb.append(" AND ? >= ci.reg_time_start AND ? <= ci.reg_time_end");
            sb.append(" AND ((? >= ci.reg_time_start AND ? <= ci.reg_time_end) OR reg_time_start=0)");
            args.add(registTime);
            args.add(registTime);
            argsType.add(Types.BIGINT);
            argsType.add(Types.BIGINT);
        }

//        if (condition.getReceiveMethod() == 2 || condition.getReceiveMethod() == 0) {
//            sb.append(" AND ci.receive_method=?");
//            args.add(condition.getReceiveMethod());
//            argsType.add(Types.INTEGER);
//        }

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

        final List<GiftCouponInfo> list = jdbcTemplate.query(sb.toString(), args.toArray(), arr,
                new ResultSetExtractor<List<GiftCouponInfo>>() {
                    @Override
                    public List<GiftCouponInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<GiftCouponInfo> list = new ArrayList<>();

                        while (rs.next()) {
                            GiftCouponInfo info = new GiftCouponInfo();
                            info.setCoupon_id(rs.getInt("coupon_id"));
                            info.setCoupon_name(rs.getString("coupon_name"));
                            info.setPicture(rs.getString("picture"));
                            info.setPrice(rs.getBigDecimal("price"));
                            info.setRequired_points(rs.getInt("required_points"));
                            info.setReceive_method(rs.getInt("receive_method"));
                            info.setMall_id(rs.getInt("mall_id"));
                            list.add(info);
                        }
                        return list;
                    }
                });

        return list;
    }


    public GiftCouponInfo selectGiftCouponInfoById(final Integer couponId, final long today) {
        {
            String sql = "SELECT ci.coupon_id,ci.coupon_name,ci.picture,ci.mall_id,ci.expiry_date_start,ci.expiry_date_end,ci.circulation,ci.daily_circulation," +
                    "ci.inventory,ci.receive_method,ci.required_points,ci.limited,ci.daily_limited,ci.price," +
                    "ci.keep_verification_of,ci.verification_of,ci.issue_start,ci.issue_end,a.content," +
                    "(select count(1) from `T_GIFT_COUPON` where coupon_id=?) as total," +
                    "(select count(1) from `T_GIFT_COUPON` where coupon_id=? AND receive_date>=?) as daily" +
                    " FROM `T_GIFT_COUPON_INFO` ci LEFT JOIN `T_ARTICLES` a ON ci.intro=a.article_id" +
                    " WHERE ci.coupon_id=?";

            log.info("[{}]" + sql);

            final GiftCouponInfo couponInfo = jdbcTemplate.query(sql, new Object[]{couponId, couponId, today, couponId},
                    new int[]{Types.INTEGER, Types.INTEGER, Types.BIGINT, Types.INTEGER},
                    new ResultSetExtractor<GiftCouponInfo>() {
                        @Override
                        public GiftCouponInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
                            GiftCouponInfo info = null;

                            if (rs.next()) {
                                info = new GiftCouponInfo();
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
                                info.setRequired_points(rs.getInt("required_points"));
                                info.setLimited(rs.getInt("limited"));
                                info.setDaily_limited(rs.getInt("daily_limited"));
                                info.setPrice(rs.getBigDecimal("price"));
                                info.setKeep_verification_of(rs.getInt("keep_verification_of"));
                                info.setVerification_of(rs.getInt("verification_of"));
                                info.setIssue_start(new Date(rs.getLong("issue_start")));
                                info.setIssue_end(new Date(rs.getLong("issue_end")));
                                info.setIntro(rs.getString("content"));
                                info.setReceivedTotal(rs.getInt("total"));
                                info.setReceivedDaily(rs.getInt("daily"));
                            }
                            return info;
                        }
                    });

            return couponInfo;
        }
    }

    /**
     * 获取会员-某一种券有几张未核销
     *
     * @param memberId
     * @param couponId
     * @return
     */
    public int selectNonVerificationCouponByMember(final Integer memberId, final Integer couponId) {
        String sql = "select count(1) as non from `T_GIFT_COUPON` where coupon_id=? and member_id=? and coupon_status!=2";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{couponId, memberId}, new int[]{Types.INTEGER, Types.INTEGER});
        rowSet.first();
        int count = rowSet.getInt("non");

        return count;
    }

    /**
     * 获取会员--某一种券的领取张数
     *
     * @param memberId
     * @param couponId
     * @return
     */
    public int selectReceivedByMember(final Integer memberId, final Integer couponId) {
        String sql = "select count(1) as total from `T_GIFT_COUPON` where coupon_id=? and member_id=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{couponId, memberId}, new int[]{Types.INTEGER, Types.INTEGER});
        rowSet.first();

        return rowSet.getInt("total");
    }

    /**
     * 获取会员--某一种券的每日领取张数
     *
     * @param memberId
     * @param couponId
     * @return
     */
    public int selectDailyReceivedByMember(final Integer memberId, final Integer couponId, final long currTime) {
        String sql = "select count(1) as daily from `T_GIFT_COUPON` where coupon_id=? and member_id=? and receive_date>?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{couponId, memberId, currTime}, new int[]{Types.INTEGER, Types.INTEGER, Types.BIGINT});
        rowSet.first();

        return rowSet.getInt("daily");
    }

    /**
     * 根据状态获取券的数量
     * @param couponId
     * @param couponStatus
     * @return
     */
    public int selectCouponCountByStatus(final int couponId, final int couponStatus) {
        String sql = "SELECT count(1) cnt FROM `T_GIFT_COUPON` where coupon_id=? and coupon_status=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{couponId, couponStatus}, new int[]{Types.INTEGER, Types.INTEGER});
        rowSet.first();

        return rowSet.getInt("cnt");
    }


    /**
     * 录入已领取的礼品券
     *
     * @param condition
     * @return
     */
    public int insertGiftCouponByMember(final CouponReceiveCondition condition) {
        String sql = "INSERT INTO `T_GIFT_COUPON` (coupon_id,member_id,mall_id,receive_date,coupon_status) " +
                "VALUES (?,?,?,?,?)";

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
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    /**
     * 获取我的礼品券列表
     *
     * @param condition
     * @return
     */
    public List<GiftCoupon> selectCouponListByMember(MyCouponListQueryCondition condition) {
        String sql = "select c.crl_id,c.coupon_id,c.member_id,c.mall_id,c.receive_date,ci.coupon_name,ci.picture,ci.expiry_date_start," +
                "ci.expiry_date_end,c.coupon_status" +
                " from `T_GIFT_COUPON` c left join `T_GIFT_COUPON_INFO` ci on c.coupon_id=ci.coupon_id" +
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

        List<GiftCoupon> list = jdbcTemplate.query(sb.toString(),
                new Object[]{condition.getMemberId(), condition.getMallId(), condition.getOffset(), condition.getSize()},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER},
                new ResultSetExtractor<List<GiftCoupon>>() {
                    @Override
                    public List<GiftCoupon> extractData(ResultSet rs) throws SQLException, DataAccessException {

                        List<GiftCoupon> list = new ArrayList<>();

                        while (rs.next()) {
                            GiftCoupon c = new GiftCoupon();
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
    public GiftCoupon selectCouponById(final Integer crlId) {
        final String sql = "select crl_id,c.coupon_id,member_id,c.mall_id,coupon_status,coupon_name,picture," +
                "expiry_date_start,expiry_date_end,verification_of,keep_verification_of,a.content" +
                " from `T_GIFT_COUPON` c inner join `T_GIFT_COUPON_INFO` ci on ci.coupon_id = c.coupon_id" +
                " LEFT JOIN `T_ARTICLES` a ON ci.intro=a.article_id where crl_id=?";
        log.info("[{}]", sql);
        GiftCoupon coupon = jdbcTemplate.query(sql, new Object[]{crlId}, new int[]{Types.INTEGER}, new ResultSetExtractor<GiftCoupon>() {
            @Override
            public GiftCoupon extractData(ResultSet rs) throws SQLException, DataAccessException {
                GiftCoupon coupon = null;

                if (rs.next()) {
                    coupon = new GiftCoupon();
                    coupon.setCrl_id(rs.getInt("crl_id"));
                    coupon.setCoupon_id(rs.getInt("coupon_id"));
                    coupon.setMember_id(rs.getInt("member_id"));
                    coupon.setMall_id(rs.getInt("mall_id"));
                    coupon.setCoupon_status(rs.getInt("coupon_status"));
                    coupon.setCoupon_name(rs.getString("coupon_name"));
                    coupon.setPicture(rs.getString("picture"));
                    coupon.setExpiry_date_start(new Date(rs.getLong("expiry_date_start")));
                    coupon.setExpiry_date_end(new Date(rs.getLong("expiry_date_end")));
                    coupon.setKeep_verification_of(rs.getInt("keep_verification_of"));
                    coupon.setVerification_of(rs.getInt("verification_of"));
                    coupon.setIntro(rs.getString("content"));
                }
                return coupon;
            }
        });
        return coupon;
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
        String sql = "update `T_GIFT_COUPON` set coupon_status=? where crl_id=?";

        return jdbcTemplate.update(sql, status, crlId);
    }

    /**
     * 核销后，新增核销日志
     * @param vc
     * @param coupon
     * @return
     */
    public int insertVerification(VerificationClerk vc, GiftCoupon coupon) {
        String sql = "insert into `T_GIFT_VERIFICATION_LOG` (coupon_id,member_id,vc_id,verification_date,shop_id,mall_id,crl_id) values (?,?,?,?,?,?,?)";

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
     * 根据状态获取券的数量
     * @param memberId
     * @param couponId
     * @param couponStatus
     * @return
     */
    public int selectCouponCountByStatus(final int memberId, final int couponId, final int couponStatus) {
        String sql = "SELECT count(1) cnt FROM `T_GIFT_COUPON` where member_id=? and coupon_id=? and coupon_status=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{memberId, couponId, couponStatus}, new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});
        rowSet.first();

        return rowSet.getInt("cnt");
    }

    public List<GiftCouponInfo> selectCouponInfoListByShopId(final CouponForShopQueryCondition condition) {
        final String sql = "SELECT ci.coupon_id,ci.coupon_name,ci.picture,ci.price,ci.required_points,ci.receive_method,cv.mall_id " +
                "FROM `T_COUPON_VERIFICATION_WIDE` cv INNER JOIN `T_GIFT_COUPON_INFO` ci ON cv.coupon_id=ci.coupon_id " +
                "WHERE shop_id=? AND cv.mall_id=? ORDER BY create_date DESC LIMIT ?,?";

        final List<GiftCouponInfo> list = jdbcTemplate.query(sql,
                new Object[]{condition.getShopId(), condition.getMallId(), condition.getOffset(), condition.getSize()},
                new ResultSetExtractor<List<GiftCouponInfo>>() {
                    @Override
                    public List<GiftCouponInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<GiftCouponInfo> _list = new ArrayList<>();

                        while (rs.next()) {
                            GiftCouponInfo info = new GiftCouponInfo();
                            info.setCoupon_id(rs.getInt("coupon_id"));
                            info.setCoupon_name(rs.getString("coupon_name"));
                            info.setPicture(rs.getString("picture"));
                            info.setPrice(rs.getBigDecimal("price"));
                            info.setRequired_points(rs.getInt("required_points"));
                            info.setReceive_method(rs.getInt("receive_method"));
                            info.setMall_id(rs.getInt("mall_id"));
                            _list.add(info);
                        }
                        return _list;
                    }
                });

        return list;
    }
}
