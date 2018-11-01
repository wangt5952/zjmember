package com.laf.manager.repository;


import com.laf.manager.dto.*;
import com.laf.manager.querycondition.coupon.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class CouponRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int updateCouponInventory(final Integer couponId, final Integer val) {
        return 0;
    }

    public int updateCouponReceiveLog(final CouponReceiveCondition condition) {
        return 0;
    }

    public int insertReceiveCoupon(final ReceiveCouponInfo coupon) {
        final String sql = "insert into `T_COUPON_INFO` (coupon_name,create_date,status,shops,expiry_date_start,expiry_date_end," +
                "circulation,daily_circulation,verification_type,mall_id,coupon_type,issue_start,issue_end,limited,daily_limited," +
                "price,cost_price,discounted_price,required_points,reg_time_start,reg_time_end,sort_id,receive_method," +
                "keep_verification_of,verification_of,intro,text1,picture,active_time,activable,activation_condition,activetion_site)" +
                " values (?,?,1,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, coupon.getCoupon_name());
                ps.setLong(2, new Date().getTime());
                ps.setString(3, coupon.getShops());
                ps.setLong(4, coupon.getExpiry_date_start());
                ps.setLong(5, coupon.getExpiry_date_end());
                ps.setInt(6, coupon.getCirculation());
                ps.setInt(7, coupon.getDaily_circulation());
                ps.setInt(8, coupon.getVerification_type());
                ps.setInt(9, coupon.getMall_id());
                ps.setInt(10, coupon.getCoupon_type());
                ps.setLong(11, coupon.getIssue_start());
                ps.setLong(12, coupon.getIssue_end());
                ps.setInt(13, coupon.getLimited());
                ps.setInt(14, coupon.getDaily_limited());
                ps.setBigDecimal(15, coupon.getPrice());
                ps.setBigDecimal(16, coupon.getCost_price());
                if (coupon.getDiscounted_price() == null) ps.setNull(17, Types.DECIMAL);
                else ps.setBigDecimal(17, coupon.getDiscounted_price());
                ps.setInt(18, coupon.getRequired_points());
                ps.setLong(19, coupon.getReg_time_start());
                ps.setLong(20, coupon.getReg_time_end());
                ps.setInt(21, coupon.getSort_id());
                ps.setInt(22, coupon.getReceive_method());
                ps.setInt(23, coupon.getKeep_verification_of());
                ps.setInt(24, coupon.getVerification_of());
                ps.setInt(25, coupon.getIntro_id());
                ps.setString(26, coupon.getText1());
                ps.setString(27, coupon.getPicture());
                ps.setLong(28, coupon.getActive_time());
                ps.setInt(29, coupon.getActivable());
                ps.setString(30, coupon.getActivation_condition());
                ps.setString(31, coupon.getActivetion_site());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int insertParkingCoupon(final ReceiveCouponInfo coupon) {
        final String sql = "insert into `T_PARKING_COUPON_INFO` (coupon_name,create_date,status,shops,expiry_date_start,expiry_date_end," +
                "circulation,daily_circulation,verification_type,mall_id,coupon_type,issue_start,issue_end,limited,daily_limited," +
                "price,cost_price,discounted_price,required_points,reg_time_start,reg_time_end,sort_id,receive_method," +
                "keep_verification_of,verification_of,intro,text1,picture,active_time,activable,activation_condition,activetion_site)" +
                " values (?,?,1,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, coupon.getCoupon_name());
                ps.setLong(2, new Date().getTime());
                ps.setString(3, coupon.getShops());
                ps.setLong(4, coupon.getExpiry_date_start());
                ps.setLong(5, coupon.getExpiry_date_end());
                ps.setInt(6, coupon.getCirculation());
                ps.setInt(7, coupon.getDaily_circulation());
                ps.setInt(8, coupon.getVerification_type());
                ps.setInt(9, coupon.getMall_id());
                ps.setInt(10, coupon.getCoupon_type());
                ps.setLong(11, coupon.getIssue_start());
                ps.setLong(12, coupon.getIssue_end());
                ps.setInt(13, coupon.getLimited());
                ps.setInt(14, coupon.getDaily_limited());
                ps.setBigDecimal(15, coupon.getPrice());
                ps.setBigDecimal(16, coupon.getCost_price());
                if (coupon.getDiscounted_price() == null) ps.setNull(17, Types.DECIMAL);
                else ps.setBigDecimal(17, coupon.getDiscounted_price());
                ps.setInt(18, coupon.getRequired_points());
                ps.setLong(19, coupon.getReg_time_start());
                ps.setLong(20, coupon.getReg_time_end());
                ps.setInt(21, coupon.getSort_id());
                ps.setInt(22, coupon.getReceive_method());
                ps.setInt(23, coupon.getKeep_verification_of());
                ps.setInt(24, coupon.getVerification_of());
                ps.setInt(25, coupon.getIntro_id());
                ps.setString(26, coupon.getText1());
                ps.setString(27, coupon.getPicture());
                ps.setLong(28, coupon.getActive_time());
                ps.setInt(29, coupon.getActivable());
                ps.setString(30, coupon.getActivation_condition());
                ps.setString(31, coupon.getActivetion_site());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int updateReceiveCoupon(final ReceiveCouponInfo coupon) {
        final String sql = "update `T_COUPON_INFO` set coupon_name=?,shops=?,expiry_date_start=?,expiry_date_end=?," +
                "circulation=?,daily_circulation=?,verification_type=?,issue_start=?,issue_end=?,limited=?,daily_limited=?," +
                "price=?,cost_price=?,discounted_price=?,required_points=?,reg_time_start=?,reg_time_end=?,sort_id=?,receive_method=?,coupon_type=?," +
                "keep_verification_of=?,verification_of=?,intro=?,text1=?,picture=?,active_time=?,activable=?,activation_condition=?,activetion_site=? " +
                "where coupon_id=?";

        return jdbcTemplate.update(sql, coupon.getCoupon_name(), coupon.getShops(), coupon.getExpiry_date_start(),
                coupon.getExpiry_date_end(), coupon.getCirculation(), coupon.getDaily_circulation(), coupon.getVerification_type(),
                coupon.getIssue_start(), coupon.getIssue_end(), coupon.getLimited(), coupon.getDaily_limited(), coupon.getPrice(),
                coupon.getCost_price(), coupon.getDiscounted_price(), coupon.getRequired_points(), coupon.getReg_time_start(),
                coupon.getReg_time_end(), coupon.getSort_id(), coupon.getReceive_method(), coupon.getCoupon_type(), coupon.getKeep_verification_of(),
                coupon.getVerification_of(), coupon.getIntro_id(), coupon.getText1(), coupon.getPicture(), coupon.getActive_time(),
                coupon.getActivable(), coupon.getActivation_condition(), coupon.getActivetion_site(), coupon.getCoupon_id());
    }

    public int updateParkingCoupon(final ReceiveCouponInfo coupon) {
        final String sql = "update `T_PARKING_COUPON_INFO` set coupon_name=?,shops=?,expiry_date_start=?,expiry_date_end=?," +
                "circulation=?,daily_circulation=?,verification_type=?,issue_start=?,issue_end=?,limited=?,daily_limited=?," +
                "price=?,cost_price=?,discounted_price=?,required_points=?,reg_time_start=?,reg_time_end=?,sort_id=?,receive_method=?,coupon_type=?," +
                "keep_verification_of=?,verification_of=?,intro=?,text1=?,picture=?,active_time=?,activable=?,activation_condition=?,activetion_site=? " +
                "where coupon_id=?";

        return jdbcTemplate.update(sql, coupon.getCoupon_name(), coupon.getShops(), coupon.getExpiry_date_start(),
                coupon.getExpiry_date_end(), coupon.getCirculation(), coupon.getDaily_circulation(), coupon.getVerification_type(),
                coupon.getIssue_start(), coupon.getIssue_end(), coupon.getLimited(), coupon.getDaily_limited(), coupon.getPrice(),
                coupon.getCost_price(), coupon.getDiscounted_price(), coupon.getRequired_points(), coupon.getReg_time_start(),
                coupon.getReg_time_end(), coupon.getSort_id(), coupon.getReceive_method(), coupon.getCoupon_type(), coupon.getKeep_verification_of(),
                coupon.getVerification_of(), coupon.getIntro_id(), coupon.getText1(), coupon.getPicture(), coupon.getActive_time(),
                coupon.getActivable(), coupon.getActivation_condition(), coupon.getActivetion_site(), coupon.getCoupon_id());
    }

    public int deleteCoupon(final Integer couponId) {
        final String sql = "delete from `T_COUPON_INFO` where coupon_id=?";

        return jdbcTemplate.update(sql, couponId);
    }

    public int updateCouponState(final Integer couponId,final Integer state) {
        final String sql = "update `T_COUPON_INFO` set status=?  where coupon_id=?";

        return jdbcTemplate.update(sql, state, couponId);
    }

    /**
     * 查询优惠券信息列表(领取型，激活型，关联型，推送型)
     * @param condition
     * @return
     */
    public List<ReceiveCouponInfo> selectCouponInfoList(CouponQueryCondition condition) {

//        String sql = "SELECT ci.coupon_id,coupon_name,picture,issue_start,issue_end,circulation,daily_circulation," +
//                "coupon_type,receive_method,create_date,sort_id,status,ci.text1,ci.mall_id,verification_of,qr_code," +
//                "(select count(1) from `T_COUPON` where coupon_id=ci.coupon_id) as total," +
//                "(select count(1) from `T_COUPON` where coupon_id=ci.coupon_id AND receive_date <= ? and receive_date >= ?) as daily," +
//                "(select count(1) from `T_COUPON` where coupon_id=ci.coupon_id AND coupon_status=2) as verification," +
//                "(select count(1) from `T_COUPON` where coupon_id=ci.coupon_id AND coupon_status=1) as activate" +
//                " FROM `T_COUPON_INFO` ci left join `T_COUPON` c on ci.coupon_id=c.coupon_id where 1=1";

        String sql = "SELECT ci.coupon_id,coupon_name,picture,issue_start,issue_end,circulation,daily_circulation," +
                "coupon_type,receive_method,create_date,sort_id,status,ci.text1,ci.mall_id,verification_of,qr_code," +
                "count(1) as total," +
                "(select count(1) from `T_COUPON` where 1=1 AND receive_date <= ? and receive_date >= ?) as daily," +
                "(select count(1) from `T_COUPON` where 1=1 AND coupon_status=2) as verification," +
                "(select count(1) from `T_COUPON` where 1=1 AND coupon_status=1) as activate" +
                " FROM `T_COUPON_INFO` ci left join `T_COUPON` c on ci.coupon_id=c.coupon_id where 1=1";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        args.add(condition.getCurrDateEnd());
        args.add(condition.getCurrDateStart());
        argsType.add(Types.BIGINT);
        argsType.add(Types.BIGINT);

        if (condition.getMallId() > 0) {
            sb.append(" and ci.mall_id=?");
            args.add(condition.getMallId());
            argsType.add(Types.INTEGER);
        }


        if (!StringUtils.isEmpty(condition.getKeywords())) {
            sb.append(" and text1 like '%' ? '%'");
            args.add(condition.getKeywords());
            argsType.add(Types.VARCHAR);
        }

        if (condition.getCouponType() != null && condition.getCouponType() != -1) {
            sb.append(" and coupon_type=?");
            args.add(condition.getCouponType());
            argsType.add(Types.INTEGER);
        }

        sb.append(" GROUP BY ci.coupon_id");
        sb.append(" ORDER BY ");
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
                            info.setIssue_start(new Date(rs.getLong("issue_start")));
                            info.setIssue_end(new Date(rs.getLong("issue_end")));
                            info.setCirculation(rs.getInt("circulation"));
                            info.setDaily_circulation(rs.getInt("daily_circulation"));
                            info.setCoupon_type(rs.getInt("coupon_type"));
                            info.setReceive_method(rs.getInt("receive_method"));
                            info.setCreate_date(new Date(rs.getLong("create_date")));
                            info.setSort_id(rs.getInt("sort_id"));
                            info.setStatus(rs.getInt("status"));
                            info.setMall_id(rs.getInt("mall_id"));
                            info.setVerification_of(rs.getInt("verification_of"));
                            info.setQr_code(rs.getString("qr_code"));
                            info.setReceivedTotal(rs.getInt("total"));
                            info.setReceivedDaily(rs.getInt("daily"));
                            info.setVerification(rs.getInt("verification"));
                            info.setActivate(rs.getInt("activate"));

                            list.add(info);
                        }
                        return list;
                    }
                });

        return list;
    }

    /**
     * 查询停车券信息列表
     * @param condition
     * @return
     */
    public List<ReceiveCouponInfo> selectParkingCouponInfoList(CouponQueryCondition condition) {

        String sql = "SELECT ci.coupon_id,coupon_name,picture,issue_start,issue_end,circulation,daily_circulation," +
                "coupon_type,receive_method,create_date,sort_id,status,ci.text1,ci.mall_id,verification_of,qr_code," +
                "(select count(1) from `T_COUPON` where coupon_id=ci.coupon_id) as total," +
                "(select count(1) from `T_COUPON` where coupon_id=ci.coupon_id AND receive_date <= ? and receive_date >= ?) as daily," +
                "(select count(1) from `T_COUPON` where coupon_id=ci.coupon_id AND coupon_status=2) as verification," +
                "(select count(1) from `T_COUPON` where coupon_id=ci.coupon_id AND coupon_status=1) as activate" +
                " FROM `T_PARKING_COUPON_INFO` ci left join `T_PARKING_COUPON` c on ci.coupon_id=c.coupon_id where 1=1";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        args.add(condition.getCurrDateEnd());
        args.add(condition.getCurrDateStart());
        argsType.add(Types.BIGINT);
        argsType.add(Types.BIGINT);

        if (condition.getMallId() > 0) {
            sb.append(" and ci.mall_id=?");
            args.add(condition.getMallId());
            argsType.add(Types.INTEGER);
        }


        if (!StringUtils.isEmpty(condition.getKeywords())) {
            sb.append(" and text1 like '%' ? '%'");
            args.add(condition.getKeywords());
            argsType.add(Types.VARCHAR);
        }

        if (condition.getCouponType() != null && condition.getCouponType() != -1) {
            sb.append(" and coupon_type=?");
            args.add(condition.getCouponType());
            argsType.add(Types.INTEGER);
        }

        sb.append(" GROUP BY ci.coupon_id");
        sb.append(" ORDER BY ");
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
                            info.setIssue_start(new Date(rs.getLong("issue_start")));
                            info.setIssue_end(new Date(rs.getLong("issue_end")));
                            info.setCirculation(rs.getInt("circulation"));
                            info.setDaily_circulation(rs.getInt("daily_circulation"));
                            info.setCoupon_type(rs.getInt("coupon_type"));
                            info.setReceive_method(rs.getInt("receive_method"));
                            info.setCreate_date(new Date(rs.getLong("create_date")));
                            info.setSort_id(rs.getInt("sort_id"));
                            info.setStatus(rs.getInt("status"));
                            info.setMall_id(rs.getInt("mall_id"));
                            info.setVerification_of(rs.getInt("verification_of"));
                            info.setQr_code(rs.getString("qr_code"));
                            info.setReceivedTotal(rs.getInt("total"));
                            info.setReceivedDaily(rs.getInt("daily"));
                            info.setVerification(rs.getInt("verification"));
                            info.setActivate(rs.getInt("activate"));

                            list.add(info);
                        }
                        return list;
                    }
                });

        return list;
    }

    /**
     * 查询优惠券信息详情(4种类型)
     * @param couponId
     * @return
     */
    public ReceiveCouponInfo selectCouponInfoById(final Integer couponId) {
        String sql = "SELECT coupon_id,coupon_name,shops,intro,picture,verification_type,ci.mall_id,coupon_type,issue_start,issue_end,expiry_date_start,expiry_date_end," +
                "circulation,daily_circulation,receive_method,required_points,limited,daily_limited,price,cost_price,discounted_price,verification_of," +
                "reg_time_start,reg_time_end,sort_id,status,keep_verification_of,group_id,content,use_condition,active_time,activable,activation_condition,activetion_site,qrcode_param" +
                " FROM `T_COUPON_INFO` ci LEFT JOIN `T_ARTICLES` r ON intro=article_id" +
                " WHERE coupon_id=?";

        final ReceiveCouponInfo couponInfo = jdbcTemplate.query(sql, new Object[]{couponId},
                new int[]{Types.INTEGER},
                new ResultSetExtractor<ReceiveCouponInfo>() {
                    @Override
                    public ReceiveCouponInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
                        ReceiveCouponInfo info = null;

                        if (rs.next()) {
                            info = new ReceiveCouponInfo();
                            info.setCoupon_id(rs.getInt("coupon_id"));
                            info.setPicture(rs.getString("picture"));
                            info.setCoupon_name(rs.getString("coupon_name"));
                            info.setVerification_type(rs.getInt("verification_type"));
                            info.setMall_id(rs.getInt("mall_id"));
                            info.setCoupon_type(rs.getInt("coupon_type"));
                            info.setIssue_start(new Date(rs.getLong("issue_start")));
                            info.setIssue_end(new Date(rs.getLong("issue_end")));
                            info.setExpiry_date_start(new Date(rs.getLong("expiry_date_start")));
                            info.setExpiry_date_end(new Date(rs.getLong("expiry_date_end")));
                            info.setCirculation(rs.getInt("circulation"));
                            info.setDaily_circulation(rs.getInt("daily_circulation"));
                            info.setReceive_method(rs.getInt("receive_method"));
                            info.setRequired_points(rs.getInt("required_points"));
                            info.setLimited(rs.getInt("limited"));
                            info.setDaily_limited(rs.getInt("daily_limited"));
                            info.setPrice(rs.getBigDecimal("price"));
                            info.setCost_price(rs.getBigDecimal("cost_price"));
                            info.setShops(rs.getString("shops"));
                            info.setDiscounted_price(rs.getBigDecimal("discounted_price"));
                            info.setVerification_of(rs.getInt("verification_of"));
                            info.setKeep_verification_of(rs.getInt("keep_verification_of"));
                            info.setReg_time_start(new Date(rs.getLong("reg_time_start")));
                            info.setReg_time_end(new Date(rs.getLong("reg_time_end")));
                            info.setSort_id(rs.getInt("sort_id"));
                            info.setStatus(rs.getInt("status"));
                            info.setGroup_id(rs.getInt("group_id"));
                            info.setIntro(rs.getString("content"));
                            info.setIntro_id(rs.getInt("intro"));
                            info.setUse_condition(rs.getBigDecimal("use_condition"));
                            info.setActive_time(new Date(rs.getLong("active_time")));
                            info.setActivable(rs.getInt("activable"));
                            info.setActivation_condition(rs.getString("activation_condition"));
                            info.setActivetion_site(rs.getString("activetion_site"));
                            info.setQrcode_param(rs.getString("qrcode_param"));
                        }
                        return info;
                    }
                });

        return couponInfo;
    }

    /**
     * 查询停车券信息详情
     * @param couponId
     * @return
     */
    public ReceiveCouponInfo selectParkingCouponInfoById(final Integer couponId) {
        String sql = "SELECT coupon_id,coupon_name,shops,intro,picture,verification_type,ci.mall_id,coupon_type,issue_start,issue_end,expiry_date_start,expiry_date_end," +
                "circulation,daily_circulation,receive_method,required_points,limited,daily_limited,price,cost_price,discounted_price,verification_of," +
                "reg_time_start,reg_time_end,sort_id,status,keep_verification_of,group_id,content,use_condition,active_time,activable,activation_condition,activetion_site,qrcode_param" +
                " FROM `T_PARKING_COUPON_INFO` ci LEFT JOIN `T_ARTICLES` r ON intro=article_id" +
                " WHERE coupon_id=?";

        final ReceiveCouponInfo couponInfo = jdbcTemplate.query(sql, new Object[]{couponId},
                new int[]{Types.INTEGER},
                new ResultSetExtractor<ReceiveCouponInfo>() {
                    @Override
                    public ReceiveCouponInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
                        ReceiveCouponInfo info = null;

                        if (rs.next()) {
                            info = new ReceiveCouponInfo();
                            info.setCoupon_id(rs.getInt("coupon_id"));
                            info.setPicture(rs.getString("picture"));
                            info.setCoupon_name(rs.getString("coupon_name"));
                            info.setVerification_type(rs.getInt("verification_type"));
                            info.setMall_id(rs.getInt("mall_id"));
                            info.setCoupon_type(rs.getInt("coupon_type"));
                            info.setIssue_start(new Date(rs.getLong("issue_start")));
                            info.setIssue_end(new Date(rs.getLong("issue_end")));
                            info.setExpiry_date_start(new Date(rs.getLong("expiry_date_start")));
                            info.setExpiry_date_end(new Date(rs.getLong("expiry_date_end")));
                            info.setCirculation(rs.getInt("circulation"));
                            info.setDaily_circulation(rs.getInt("daily_circulation"));
                            info.setReceive_method(rs.getInt("receive_method"));
                            info.setRequired_points(rs.getInt("required_points"));
                            info.setLimited(rs.getInt("limited"));
                            info.setDaily_limited(rs.getInt("daily_limited"));
                            info.setPrice(rs.getBigDecimal("price"));
                            info.setCost_price(rs.getBigDecimal("cost_price"));
                            info.setShops(rs.getString("shops"));
                            info.setDiscounted_price(rs.getBigDecimal("discounted_price"));
                            info.setVerification_of(rs.getInt("verification_of"));
                            info.setKeep_verification_of(rs.getInt("keep_verification_of"));
                            info.setReg_time_start(new Date(rs.getLong("reg_time_start")));
                            info.setReg_time_end(new Date(rs.getLong("reg_time_end")));
                            info.setSort_id(rs.getInt("sort_id"));
                            info.setStatus(rs.getInt("status"));
                            info.setGroup_id(rs.getInt("group_id"));
                            info.setIntro(rs.getString("content"));
                            info.setIntro_id(rs.getInt("intro"));
                            info.setUse_condition(rs.getBigDecimal("use_condition"));
                            info.setActive_time(new Date(rs.getLong("active_time")));
                            info.setActivable(rs.getInt("activable"));
                            info.setActivation_condition(rs.getString("activation_condition"));
                            info.setActivetion_site(rs.getString("activetion_site"));
                            info.setQrcode_param(rs.getString("qrcode_param"));
                        }
                        return info;
                    }
                });

        return couponInfo;
    }

    public List<VerificationWide> selectVerificationWideByCouponId(final int couponId, final int verificationType) {
        String sql = "";

        if (verificationType == 2) {
            sql = "select cvw_id,coupon_id,shop_id from `T_COUPON_VERIFICATION_WIDE` where coupon_id=?";

            List<VerificationWide> list = jdbcTemplate.query(sql, new Object[]{couponId}, new ResultSetExtractor<List<VerificationWide>>() {
                @Override
                public List<VerificationWide> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<VerificationWide> _list = new ArrayList<>();

                    while (rs.next()) {
                        VerificationWide vw = new VerificationWide();
                        vw.setCvw_id(rs.getInt("cvw_id"));
                        vw.setCoupon_id(rs.getInt("coupon_id"));
                        vw.setShop_id(rs.getInt("shop_id"));
                        _list.add(vw);
                    }
                    return _list;
                }
            });

            return list;

        } else if (verificationType == 3) {
            sql = "select cvw_id,coupon_id,vc_id from `T_COUPON_VERIFICATION_WIDE` where coupon_id=?";

            List<VerificationWide> list = jdbcTemplate.query(sql, new Object[]{couponId}, new ResultSetExtractor<List<VerificationWide>>() {
                @Override
                public List<VerificationWide> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<VerificationWide> _list = new ArrayList<>();

                    while (rs.next()) {
                        VerificationWide vw = new VerificationWide();
                        vw.setCvw_id(rs.getInt("cvw_id"));
                        vw.setCoupon_id(rs.getInt("coupon_id"));
                        vw.setShop_id(rs.getInt("vc_id"));
                        _list.add(vw);
                    }
                    return _list;
                }
            });

            return list;
        } else if (verificationType == 4) {
            //停车场
        }

        return ListUtils.EMPTY_LIST;
    }

    public int deleteVerificationWide(final int cvwId) {
        String sql = "delete from `T_COUPON_VERIFICATION_WIDE` where cvw_id=?";

        int result = jdbcTemplate.update(sql, cvwId);

        return result;
    }

    public int insertVerificationWide(VerificationWide vw) {
        String sql = "insert into `T_COUPON_VERIFICATION_WIDE` (coupon_id,shop_id,mall_id) values (?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, vw.getCoupon_id());
                ps.setInt(2, vw.getShop_id());
                ps.setInt(3, vw.getMall_id());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int deleteVerificationWideByCoupon(final Integer couponId) {
        String sql = "delete from `T_COUPON_VERIFICATION_WIDE` where coupon_id=?";

        int result = jdbcTemplate.update(sql, couponId);

        return result;
    }

    public int selectCouponsCount(final CouponQueryCondition condition) {
        final String sql = "select count(1) cnt from `T_COUPON_INFO` where 1=1";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        if (condition.getMallId() > 0) {
            sb.append(" and mall_id=?");
            args.add(condition.getMallId());
            argsType.add(Types.INTEGER);
        }


        if (!StringUtils.isEmpty(condition.getKeywords())) {
            sb.append(" and text1 like '%' ? '%'");
            args.add(condition.getKeywords());
            argsType.add(Types.VARCHAR);
        }

        if (condition.getCouponType() != null && condition.getCouponType() != -1) {
            sb.append(" and coupon_type=?");
            args.add(condition.getCouponType());
            argsType.add(Types.INTEGER);
        }

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sb.toString(), args.toArray(), arr);
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public int selectParkingCouponsCount(final CouponQueryCondition condition) {
        final String sql = "select count(1) cnt from `T_PARKING_COUPON_INFO` where 1=1";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        if (condition.getMallId() > 0) {
            sb.append(" and mall_id=?");
            args.add(condition.getMallId());
            argsType.add(Types.INTEGER);
        }


        if (!StringUtils.isEmpty(condition.getKeywords())) {
            sb.append(" and text1 like '%' ? '%'");
            args.add(condition.getKeywords());
            argsType.add(Types.VARCHAR);
        }

        if (condition.getCouponType() != null && condition.getCouponType() != -1) {
            sb.append(" and coupon_type=?");
            args.add(condition.getCouponType());
            argsType.add(Types.INTEGER);
        }

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sb.toString(), args.toArray(), arr);
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    /**
     * 获取领取记录
     *
     * @param condition
     * @return
     */
    //一张优惠券的信息，优惠券有效期，会员信息，核销日志,核销人员信息,核销商户信息
    //券类型,积分
    public List<Coupon> selectReceivedCouponList(ReceivedQueryCondition condition) {

        if (condition.getMallId() <= 0) return ListUtils.EMPTY_LIST;

        String sql = "select c.crl_id,coupon_name,coupon_type,required_points,coupon_status,receive_date,name,mobile,expiry_date_start,expiry_date_end,vlog_id" +
                " from `T_COUPON` c inner join `T_MEMBER` m on c.member_id = m.member_id" +
                " inner join `T_COUPON_INFO` ci on ci.coupon_id=c.coupon_id" +
                " left join `T_VERIFICATION_LOG` vl on c.crl_id=vl.crl_id" +
                " where c.mall_id=?";
//                " left join `T_VERIFICATION_CLERK` vc on vc.vc_id=vl.vc_id";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argType = new ArrayList<>();

        args.add(condition.getMallId());
        argType.add(Types.INTEGER);

        if (condition.getCouponStatus() != null && condition.getCouponStatus() > 0) {
            sb.append(" and coupon_status=?");
            args.add(condition.getCouponStatus());
            argType.add(Types.INTEGER);
        }

        sb.append(" order by " + condition.getSort() + " " + condition.getDirection());
        sb.append(" limit ?,?");
        args.add(condition.getOffset());
        args.add(condition.getSize());
        argType.add(Types.INTEGER);
        argType.add(Types.INTEGER);

        int[] arr = new int[argType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argType.get(i);
        }

        log.info("[{}]", sb.toString());

        List<Coupon> list = jdbcTemplate.query(sb.toString(), args.toArray(), arr, new ResultSetExtractor<List<Coupon>>() {
            @Override
            public List<Coupon> extractData(ResultSet rs) throws SQLException, DataAccessException {

                List<Coupon> _list = new ArrayList<>();

                while (rs.next()) {
                    Coupon coupon = new Coupon();
                    coupon.setCrl_id(rs.getInt("crl_id"));
                    coupon.setCoupon_name(rs.getString("coupon_name"));
                    coupon.setCoupon_type(rs.getInt("coupon_type"));
                    coupon.setRequired_points(rs.getInt("required_points"));
                    coupon.setReceive_date(new Date(rs.getLong("receive_date")));
                    coupon.setCoupon_status(rs.getInt("coupon_status"));
                    coupon.setMember_name(rs.getString("name"));
                    coupon.setMember_mobile(rs.getString("mobile"));
                    coupon.setExpiry_date_start(new Date(rs.getLong("expiry_date_start")));
                    coupon.setExpiry_date_end(new Date(rs.getLong("expiry_date_end")));
                    coupon.setVlog_id(rs.getInt("vlog_id"));
                    _list.add(coupon);

                    if (coupon.getVlog_id() != 0)
                        log.info("核销日志!=0[优惠券{}核销状态{}核销日志{}]", coupon.getCrl_id(), coupon.getCoupon_status(), coupon.getVlog_id());
                    if (coupon.getVlog_id() == 0)
                        log.info("核销日志==0[优惠券{}核销状态{}核销日志{}]", coupon.getCrl_id(), coupon.getCoupon_status(), coupon.getVlog_id());
                }
                return _list;
            }
        });
        return list;
    }

    public List<VerificationLog> selectVerificationLogList(LogQueryCondition condition) {
        String sql = "select vlog_id,verification_date,coupon_name,m.name,m.mobile" +
                " from `T_VERIFICATION_LOG` vl inner join `T_COUPON_INFO` ci on vl.coupon_id=ci.coupon_id" +
                " inner join `T_MEMBER` m on vl.member_id = m.member_id where vl.mall_id=? order by verification_date desc limit ?,?";

        List<VerificationLog> list = jdbcTemplate.query(sql, new Object[]{condition.getMallId(), condition.getOffset(), condition.getSize()},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER},
                new ResultSetExtractor<List<VerificationLog>>() {
                    @Override
                    public List<VerificationLog> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<VerificationLog> _list = new ArrayList<>();

                        while (rs.next()) {
                            VerificationLog log = new VerificationLog();
                            log.setVlog_id(rs.getInt("vlog_id"));
                            log.setVerification_date(new Date(rs.getLong("verification_date")));
                            log.setCoupon_name(rs.getString("coupon_name"));
                            log.setMember_name(rs.getString("name"));
                            log.setMember_mobile(rs.getString("mobile"));

                            _list.add(log);
                        }
                        return _list;
                    }
                });
        return list;
    }

    public int selectReceivedCouponCount(ReceivedQueryCondition condition) {
        String sql = "select count(1) cnt from `T_COUPON` where mall_id=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{condition.getMallId()});
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public int selectVerificationLogCount(LogQueryCondition condition) {
        String sql = "select count(1) c from `T_VERIFICATION_LOG` where mall_id=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{condition.getMallId()});
        int result = 0;

        if (row.first()) {
            result = row.getInt("c");
        }

        return result;
    }

    public List<Coupon> multipleSelectCouponLogs(CouponLogFilterCondition $$) {
        String sql = "select ci.coupon_id,ci.coupon_name,ci.coupon_type,c.crl_id,c.receive_date,c.coupon_status," +
                "ci.cost_price, ci.required_points, m.member_id,m.`name`,m.mobile,ci.mall_id," +
                "vc_id,verification_date,vc_name,phone,shop_id,shop_name " +
                "from t_coupon c inner join t_coupon_info ci on c.coupon_id=ci.coupon_id " +
                "left join t_member m on c.member_id=m.member_id left join " +
                "(select v.vc_id,v.verification_date,v.coupon_id,v.crl_id,vc.vc_name,vc.phone,s.shop_id,s.shop_name " +
                "from t_verification_log v inner join t_verification_clerk vc on v.vc_id=vc.vc_id " +
                "inner join t_shop s on vc.shop_id=s.shop_id) vv on c.crl_id=vv.crl_id where c.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getCouponName())) {
            sql += " and ci.coupon_name like '%' ? '%'";
            args.add($$.getCouponName());
        }

        if (!StringUtils.isEmpty($$.getMemberName())) {
            sql += " and m.`name` like '%' ? '%'";
            args.add($$.getMemberName());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile=?";
            args.add($$.getMobile());
        }

        if ($$.getReceiveDateStart() > 0D && $$.getReceiveDateEnd() > 0D) {
            sql += " and c.receive_date>=? and c.receive_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if ($$.getCouponType() > -1) {
            sql += " and ci.coupon_type=?";
            args.add($$.getCouponType());
        }

        if ($$.getCouponStatus() > -1) {
            sql += " and c.coupon_status=?";
            args.add($$.getCouponStatus());
        }

        if ($$.getVerificationDateStart() > 0D && $$.getVerificationDateEnd() > 0D) {
            sql += " and verification_date>=? and verification_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if (!StringUtils.isEmpty($$.getVcName())) {
            sql += " and vc_name like '%' ? '%'";
            args.add($$.getVcName());
        }

        if ($$.getShopIdByVc() > 0) {
            sql += " and shop_id=?";
            args.add($$.getShopIdByVc());
        }

        sql += " order by " + $$.getOrder() + " desc limit ?,?";
        args.add($$.getOffset());
        args.add($$.getSize());

        return jdbcTemplate.query(sql, args.toArray(),
                (rs, rowNum) -> {
                    Coupon coupon = new Coupon();
                    coupon.setCrl_id(rs.getInt("c.crl_id"));
                    coupon.setCoupon_id(rs.getInt("ci.coupon_id"));
                    coupon.setCoupon_name(rs.getString("ci.coupon_name"));
                    coupon.setCoupon_type(rs.getInt("ci.coupon_type"));
                    coupon.setReceive_date(new Date(rs.getLong("receive_date")));
                    coupon.setCoupon_status(rs.getInt("c.coupon_status"));
                    coupon.setCost_price(rs.getBigDecimal("cost_price"));
                    coupon.setRequired_points(rs.getInt("ci.required_points"));
                    coupon.setMember_id(rs.getInt("m.member_id"));
                    coupon.setMember_name(rs.getString("m.name"));
                    coupon.setMember_mobile(rs.getString("m.mobile"));
                    coupon.setVc_name(rs.getString("vc_name"));
                    coupon.setVerification_date(new Date(rs.getLong("verification_date")));
                    coupon.setVc_mobile(rs.getString("phone"));
                    coupon.setShop_id(rs.getInt("shop_id"));
                    coupon.setShop_name(rs.getString("shop_name"));
                    coupon.setMall_id(rs.getInt("ci.mall_id"));
                    return coupon;
                });
    }

    public List<ParkingCouponInfo> multipleSelectParkingCouponLogs(CouponLogFilterCondition $$) {
        String sql = "select ci.coupon_id,ci.coupon_name,ci.coupon_type,c.crl_id,c.receive_date,c.coupon_status," +
                "ci.cost_price, ci.required_points, m.member_id,m.`name`,m.mobile,ci.mall_id," +
                "car_number,use_date,p.price,in_date,out_date,ticket_no " +
                "from t_parking_coupon c inner join t_parking_coupon_info ci on c.coupon_id=ci.coupon_id " +
                "left join t_member m on c.member_id=m.member_id left join " +
                "t_parking_log p on p.crl_id=c.crl_id where c.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getCouponName())) {
            sql += " and ci.coupon_name like '%' ? '%'";
            args.add($$.getCouponName());
        }

        if (!StringUtils.isEmpty($$.getMemberName())) {
            sql += " and m.`name` like '%' ? '%'";
            args.add($$.getMemberName());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile=?";
            args.add($$.getMobile());
        }

        if ($$.getReceiveDateStart() > 0D && $$.getReceiveDateEnd() > 0D) {
            sql += " and c.receive_date>=? and c.receive_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if ($$.getCouponType() > -1) {
            sql += " and ci.coupon_type=?";
            args.add($$.getCouponType());
        }

        if ($$.getCouponStatus() > -1) {
            sql += " and c.coupon_status=?";
            args.add($$.getCouponStatus());
        }

        if ($$.getVerificationDateStart() > 0D && $$.getVerificationDateEnd() > 0D) {
            sql += " and use_date>=? and use_date<=?";
            args.add($$.getVerificationDateStart());
            args.add($$.getVerificationDateEnd());
        }

        if (!StringUtils.isEmpty($$.getVcName())) {
            sql += " and vc_name like '%' ? '%'";
            args.add($$.getVcName());
        }

        if ($$.getShopIdByVc() > 0) {
            sql += " and shop_id=?";
            args.add($$.getShopIdByVc());
        }

        sql += " order by " + $$.getOrder() + " desc limit ?,?";
        args.add($$.getOffset());
        args.add($$.getSize());

        return jdbcTemplate.query(sql, args.toArray(),
                (rs, rowNum) -> {
                    ParkingCouponInfo coupon = new ParkingCouponInfo();
                    coupon.setCrl_id(rs.getInt("c.crl_id"));
                    coupon.setCoupon_id(rs.getInt("ci.coupon_id"));
                    coupon.setCoupon_name(rs.getString("ci.coupon_name"));
                    coupon.setCoupon_type(rs.getInt("ci.coupon_type"));
                    coupon.setReceive_date(new Date(rs.getLong("receive_date")));
                    coupon.setCoupon_status(rs.getInt("c.coupon_status"));
                    coupon.setCost_price(rs.getBigDecimal("cost_price"));
                    coupon.setRequired_points(rs.getInt("ci.required_points"));
                    coupon.setMember_id(rs.getInt("m.member_id"));
                    coupon.setMember_name(rs.getString("m.name"));
                    coupon.setMember_mobile(rs.getString("m.mobile"));
                   // coupon.setVc_name(rs.getString("vc_name"));
                   // coupon.setVerification_date(new Date(rs.getLong("verification_date")));
                   // coupon.setVc_mobile(rs.getString("phone"));
                  //  coupon.setShop_id(rs.getInt("shop_id"));
                   // coupon.setShop_name(rs.getString("shop_name"));
                    coupon.setMall_id(rs.getInt("ci.mall_id"));
                    coupon.setCar_number(rs.getString("car_number"));
                    if(rs.getLong("use_date") > 0){
                        coupon.setUse_date(new Date(rs.getLong("use_date")));
                    }else{
                        coupon.setUse_date(null);
                    }
                    if(rs.getLong("in_date") > 0){
                        coupon.setIn_date(new Date(rs.getLong("in_date")));
                    }else{
                        coupon.setIn_date(null);
                    }
                    if(rs.getLong("out_date") > 0){
                        coupon.setOut_date(new Date(rs.getLong("out_date")));
                    }else{
                        coupon.setOut_date(null);
                    }

                    coupon.setPrice(rs.getBigDecimal("price"));
                    coupon.setTicket_no(rs.getString("ticket_no"));
                    return coupon;
                });
    }

    public List<ParkingCouponInfo> multipleSelectParkingCouponPayLogs(CouponLogFilterCondition $$) {
        String sql = "select m.name,m.mobile,car_number,use_date,p.price,in_date,out_date,ticket_no," +
                "coupon_name,ci.price,ci.required_points " +
                "from t_parking_log p " +
                "left join t_member m on p.member_id=m.member_id " +
                "left join t_parking_coupon c on p.crl_id=c.crl_id " +
                "left join t_parking_coupon_info ci on c.coupon_id=ci.coupon_id where 1=1";

        List<Object> args = new ArrayList<>();
        //args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getCouponName())) {
            sql += " and ci.coupon_name like '%' ? '%'";
            args.add($$.getCouponName());
        }

        if (!StringUtils.isEmpty($$.getMemberName())) {
            sql += " and m.`name` like '%' ? '%'";
            args.add($$.getMemberName());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile=?";
            args.add($$.getMobile());
        }

        if ($$.getReceiveDateStart() > 0D && $$.getReceiveDateEnd() > 0D) {
            sql += " and c.receive_date>=? and c.receive_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if ($$.getCouponType() > -1) {
            sql += " and ci.coupon_type=?";
            args.add($$.getCouponType());
        }

        if ($$.getCouponStatus() > -1) {
            sql += " and c.coupon_status=?";
            args.add($$.getCouponStatus());
        }

        if ($$.getVerificationDateStart() > 0D && $$.getVerificationDateEnd() > 0D) {
            sql += " and use_date>=? and use_date<=?";
            args.add($$.getVerificationDateStart());
            args.add($$.getVerificationDateEnd());
        }

        if (!StringUtils.isEmpty($$.getVcName())) {
            sql += " and vc_name like '%' ? '%'";
            args.add($$.getVcName());
        }

        if ($$.getShopIdByVc() > 0) {
            sql += " and shop_id=?";
            args.add($$.getShopIdByVc());
        }

        sql += " order by use_date desc limit ?,?";
        args.add($$.getOffset());
        args.add($$.getSize());

        return jdbcTemplate.query(sql, args.toArray(),
                (rs, rowNum) -> {
                    ParkingCouponInfo coupon = new ParkingCouponInfo();
                    coupon.setCoupon_name(rs.getString("ci.coupon_name"));
                    //coupon.setReceive_date(new Date(rs.getLong("receive_date")));
                    //coupon.setCoupon_status(rs.getInt("c.coupon_status"));
                    coupon.setCost_price(rs.getBigDecimal("ci.price"));//优惠券市场价
                    coupon.setRequired_points(rs.getInt("ci.required_points"));
                    coupon.setMember_name(rs.getString("m.name"));
                    coupon.setMember_mobile(rs.getString("m.mobile"));
                    coupon.setCar_number(rs.getString("car_number"));
                    if(rs.getLong("use_date") > 0){
                        coupon.setUse_date(new Date(rs.getLong("use_date")));
                    }else{
                        coupon.setUse_date(null);
                    }
                    if(rs.getLong("in_date") > 0){
                        coupon.setIn_date(new Date(rs.getLong("in_date")));
                    }else{
                        coupon.setIn_date(null);
                    }
                    if(rs.getLong("out_date") > 0){
                        coupon.setOut_date(new Date(rs.getLong("out_date")));
                    }else{
                        coupon.setOut_date(null);
                    }

                    coupon.setPrice(rs.getBigDecimal("price"));
                    coupon.setTicket_no(rs.getString("ticket_no"));
                    return coupon;
                });
    }

    public int multipleSelectCouponLogsCount(CouponLogFilterCondition $$) {
        String sql = "select count(1) cnt " +
                "from t_coupon c inner join t_coupon_info ci on c.coupon_id=ci.coupon_id " +
                "left join t_member m on c.member_id=m.member_id left join " +
                "(select v.crl_id,v.verification_date,vc.vc_name,v.shop_id " +
                "from t_verification_log v inner join t_verification_clerk vc on v.vc_id=vc.vc_id " +
                "inner join t_shop s on vc.shop_id=s.shop_id) vv on c.crl_id=vv.crl_id where c.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getCouponName())) {
            sql += " and ci.coupon_name like '%' ? '%'";
            args.add($$.getCouponName());
        }

        if (!StringUtils.isEmpty($$.getMemberName())) {
            sql += " and m.`name` like '%' ? '%'";
            args.add($$.getMemberName());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile=?";
            args.add($$.getMobile());
        }

        if ($$.getReceiveDateStart() > 0D && $$.getReceiveDateEnd() > 0D) {
            sql += " and c.receive_date>=? and c.receive_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if ($$.getCouponType() > -1) {
            sql += " and ci.coupon_type=?";
            args.add($$.getCouponType());
        }

        if ($$.getCouponStatus() > -1) {
            sql += " and c.coupon_status=?";
            args.add($$.getCouponStatus());
        }

        if ($$.getVerificationDateStart() > 0D && $$.getVerificationDateEnd() > 0D) {
            sql += " and vv.verification_date>=? and vv.verification_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if (!StringUtils.isEmpty($$.getVcName())) {
            sql += " and vv.vc_name like '%' ? '%'";
            args.add($$.getVcName());
        }

        if ($$.getShopIdByVc() > 0) {
            sql += " and vv.shop_id=?";
            args.add($$.getShopIdByVc());
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, args.toArray());
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public int multipleSelectParkingCouponLogsCount(CouponLogFilterCondition $$) {
        String sql = "select count(1) cnt " +
                "from t_parking_coupon c inner join t_parking_coupon_info ci on c.coupon_id=ci.coupon_id " +
                "left join t_member m on c.member_id=m.member_id left join " +
                "t_parking_log p on p.crl_id=c.crl_id where c.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getCouponName())) {
            sql += " and ci.coupon_name like '%' ? '%'";
            args.add($$.getCouponName());
        }

        if (!StringUtils.isEmpty($$.getMemberName())) {
            sql += " and m.`name` like '%' ? '%'";
            args.add($$.getMemberName());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile=?";
            args.add($$.getMobile());
        }

        if ($$.getReceiveDateStart() > 0D && $$.getReceiveDateEnd() > 0D) {
            sql += " and c.receive_date>=? and c.receive_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if ($$.getCouponType() > -1) {
            sql += " and ci.coupon_type=?";
            args.add($$.getCouponType());
        }

        if ($$.getCouponStatus() > -1) {
            sql += " and c.coupon_status=?";
            args.add($$.getCouponStatus());
        }

        if ($$.getVerificationDateStart() > 0D && $$.getVerificationDateEnd() > 0D) {
            sql += " and use_date>=? and use_date<=?";
            args.add($$.getVerificationDateStart());
            args.add($$.getVerificationDateEnd());
        }

        if (!StringUtils.isEmpty($$.getVcName())) {
            sql += " and vv.vc_name like '%' ? '%'";
            args.add($$.getVcName());
        }

        if ($$.getShopIdByVc() > 0) {
            sql += " and vv.shop_id=?";
            args.add($$.getShopIdByVc());
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, args.toArray());
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public int multipleSelectParkingCouponPayLogsCount(CouponLogFilterCondition $$) {
        String sql = "select count(1) cnt " +
                "from t_parking_log p " +
                "left join t_member m on p.member_id=m.member_id " +
                "left join t_parking_coupon c on p.crl_id=c.crl_id " +
                "left join t_parking_coupon_info ci on c.coupon_id=ci.coupon_id where 1=1";

        List<Object> args = new ArrayList<>();
        //args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getCouponName())) {
            sql += " and ci.coupon_name like '%' ? '%'";
            args.add($$.getCouponName());
        }

        if (!StringUtils.isEmpty($$.getMemberName())) {
            sql += " and m.`name` like '%' ? '%'";
            args.add($$.getMemberName());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile=?";
            args.add($$.getMobile());
        }

        if ($$.getReceiveDateStart() > 0D && $$.getReceiveDateEnd() > 0D) {
            sql += " and c.receive_date>=? and c.receive_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if ($$.getCouponType() > -1) {
            sql += " and ci.coupon_type=?";
            args.add($$.getCouponType());
        }

        if ($$.getCouponStatus() > -1) {
            sql += " and c.coupon_status=?";
            args.add($$.getCouponStatus());
        }

        if ($$.getVerificationDateStart() > 0D && $$.getVerificationDateEnd() > 0D) {
            sql += " and use_date>=? and use_date<=?";
            args.add($$.getVerificationDateStart());
            args.add($$.getVerificationDateEnd());
        }

        if (!StringUtils.isEmpty($$.getVcName())) {
            sql += " and vv.vc_name like '%' ? '%'";
            args.add($$.getVcName());
        }

        if ($$.getShopIdByVc() > 0) {
            sql += " and vv.shop_id=?";
            args.add($$.getShopIdByVc());
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, args.toArray());
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public BigDecimal selectCouponLogsSum(CouponLogFilterCondition $$) {
        String sql = "select sum(ci.cost_price) cp " +
                "from t_coupon c inner join t_coupon_info ci on c.coupon_id=ci.coupon_id " +
                "left join t_member m on c.member_id=m.member_id left join " +
                "(select v.crl_id,v.verification_date,vc.vc_name,v.shop_id " +
                "from t_verification_log v inner join t_verification_clerk vc on v.vc_id=vc.vc_id " +
                "inner join t_shop s on vc.shop_id=s.shop_id) vv on c.crl_id=vv.crl_id where c.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getCouponName())) {
            sql += " and ci.coupon_name like '%' ? '%'";
            args.add($$.getCouponName());
        }

        if (!StringUtils.isEmpty($$.getMemberName())) {
            sql += " and m.`name` like '%' ? '%'";
            args.add($$.getMemberName());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile=?";
            args.add($$.getMobile());
        }

        if ($$.getReceiveDateStart() > 0D && $$.getReceiveDateEnd() > 0D) {
            sql += " and c.receive_date>=? and c.receive_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if ($$.getCouponType() > -1) {
            sql += " and ci.coupon_type=?";
            args.add($$.getCouponType());
        }

        if ($$.getCouponStatus() > -1) {
            sql += " and c.coupon_status=?";
            args.add($$.getCouponStatus());
        }

        if ($$.getVerificationDateStart() > 0D && $$.getVerificationDateEnd() > 0D) {
            sql += " and vv.verification_date>=? and vv.verification_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if (!StringUtils.isEmpty($$.getVcName())) {
            sql += " and vv.vc_name like '%' ? '%'";
            args.add($$.getVcName());
        }

        if ($$.getShopIdByVc() > 0) {
            sql += " and vv.shop_id=?";
            args.add($$.getShopIdByVc());
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, args.toArray());
        BigDecimal result = new BigDecimal(0D);

        if (row.first()) {
            result = row.getBigDecimal("cp");
        }

        return result;
    }

    public BigDecimal selectParkingCouponLogsSum(CouponLogFilterCondition $$) {
        String sql = "select sum(ci.cost_price) cp " +
                "from t_parking_coupon c inner join t_parking_coupon_info ci on c.coupon_id=ci.coupon_id " +
                "left join t_member m on c.member_id=m.member_id left join " +
                "t_parking_log p on p.crl_id=c.crl_id where c.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getCouponName())) {
            sql += " and ci.coupon_name like '%' ? '%'";
            args.add($$.getCouponName());
        }

        if (!StringUtils.isEmpty($$.getMemberName())) {
            sql += " and m.`name` like '%' ? '%'";
            args.add($$.getMemberName());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and m.mobile=?";
            args.add($$.getMobile());
        }

        if ($$.getReceiveDateStart() > 0D && $$.getReceiveDateEnd() > 0D) {
            sql += " and c.receive_date>=? and c.receive_date<=?";
            args.add($$.getReceiveDateStart());
            args.add($$.getReceiveDateEnd());
        }

        if ($$.getCouponType() > -1) {
            sql += " and ci.coupon_type=?";
            args.add($$.getCouponType());
        }

        if ($$.getCouponStatus() > -1) {
            sql += " and c.coupon_status=?";
            args.add($$.getCouponStatus());
        }

        if ($$.getVerificationDateStart() > 0D && $$.getVerificationDateEnd() > 0D) {
            sql += " and use_date>=? and use_date<=?";
            args.add($$.getVerificationDateStart());
            args.add($$.getVerificationDateEnd());
        }

        if (!StringUtils.isEmpty($$.getVcName())) {
            sql += " and vv.vc_name like '%' ? '%'";
            args.add($$.getVcName());
        }

        if ($$.getShopIdByVc() > 0) {
            sql += " and vv.shop_id=?";
            args.add($$.getShopIdByVc());
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, args.toArray());
        BigDecimal result = new BigDecimal(0D);

        if (row.first()) {
            result = row.getBigDecimal("cp");
        }

        return result;
    }

    public int insertCouponsForPush2Members(final List<Member> members, final int coupon_id) {
        final String sql = "insert into `T_COUPON` (coupon_id,member_id,receive_date,coupon_status,mall_id) " +
                "values(?,?,?,1,?)";


        int result = jdbcTemplate.execute(new ConnectionCallback<int[]>() {
            @Override
            public int[] doInConnection(Connection conn) throws SQLException, DataAccessException {
                conn.setAutoCommit(false);
                PreparedStatement ps = conn.prepareStatement(sql);

                for (int i = 0; i < members.size(); i++) {
                    ps.setInt(1, coupon_id);
                    ps.setInt(2, members.get(i).getMember_id());
                    ps.setLong(3, (new Date()).getTime());
                    ps.setInt(4, members.get(i).getMall_id());
                    ps.addBatch();
                }

                int[] _result = ps.executeBatch();
                conn.commit();
                ps.clearBatch();
                ps.close();
                conn.close();

                return _result;
            }
        }).length;

        return result > 0 ? 1 : 0;
    }

    public List<ReceiveCouponInfo> selectCouponInfoListByType(CouponQueryCondition condition) {
        String sql = "SELECT coupon_id,coupon_name FROM `T_COUPON_INFO` " +
                "WHERE ?>=expiry_date_start and ?<=expiry_date_end AND mall_id=? AND coupon_type=?";

        List<ReceiveCouponInfo> list = jdbcTemplate.query(sql,
                new Object[]{condition.getCurr(), condition.getCurr(), condition.getMallId(), condition.getCouponType()},
                new int[]{Types.BIGINT, Types.BIGINT, Types.INTEGER, Types.INTEGER},
                new ResultSetExtractor<List<ReceiveCouponInfo>>() {
                    @Override
                    public List<ReceiveCouponInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<ReceiveCouponInfo> _list = new ArrayList<>();

                        while (rs.next()) {
                            ReceiveCouponInfo info = new ReceiveCouponInfo();
                            info.setCoupon_id(rs.getInt("coupon_id"));
                            info.setCoupon_name(rs.getString("coupon_name"));

                            _list.add(info);
                        }

                        return _list;
                    }
                });

        return list;
    }

    public List<Integer> selectCouponCountMonthly(String year, int mallId) {
        final String sql = "select count,t2.month from\n" +
                "(select count(1) `count`,t.y,t.m from\n" +
                "(select crl_id,(select from_unixtime(receive_date/1000,'%Y')) y,(select from_unixtime(receive_date/1000,'%m')) m from t_coupon where mall_id=?) t\n" +
                "where t.y=? group by t.y,t.m) t1\n" +
                "right JOIN\n" +
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

    public List<Integer> selectVerifCountMonthly(String year, int mallId) {
        final String sql = "select t1.count,t2.month from\n" +
                "(select count(1) `count`, y,m from\n" +
                "(select vlog_id,(select from_unixtime(verification_date/1000,'%Y')) y,(select from_unixtime(verification_date/1000,'%m')) m from T_VERIFICATION_LOG where mall_id=?) t where y=? group by y,m) t1\n" +
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

    public int updateCouponInfoQRCode(final int couponId, final String url, final String qrCodeParam) {
        final String sql = "update `T_COUPON_INFO` set `qr_code`=?,`qrcode_param`=? where `coupon_id`=?";
        int result = jdbcTemplate.update(sql, url, qrCodeParam, couponId);
//        int result = jdbcTemplate.update(sql);
        return result;
    }

    public int updateParkingCouponInfoQRCode(final int couponId, final String url, final String qrCodeParam) {
        final String sql = "update `T_PARKING_COUPON_INFO` set `qr_code`=?,`qrcode_param`=? where `coupon_id`=?";
        int result = jdbcTemplate.update(sql, url, qrCodeParam, couponId);
//        int result = jdbcTemplate.update(sql);
        return result;
    }

    public int insertParkingInfo(String content, int mallId) {
        final String sql = "insert into `t_parking_info` (content,mall_id) values (?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, content);
                ps.setInt(2, mallId);

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
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
}
