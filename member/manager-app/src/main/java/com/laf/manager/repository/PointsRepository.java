package com.laf.manager.repository;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.laf.manager.core.support.SnowFlake;
import com.laf.manager.core.support.tuple.Tuple2;
import com.laf.manager.dto.Points;
import com.laf.manager.querycondition.points.PointsActionCondition;
import com.laf.manager.querycondition.points.PointsLogFilterCondition;
import com.laf.manager.querycondition.points.PointsQueryCondition;
import com.laf.manager.utils.db.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class PointsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbUtils dbUtils;

    public List<Points> selectPointsList(PointsQueryCondition condition) {
        String sql = "SELECT mplog_id,member_id,member_name,member_mobile,amount,points,shop_id,shop_name,shopping_date,ticket_no," +
                "vc_id,vc_name,handle_date,sources,mall_id " +
                "FROM `T_MEMBER_POINTS_LOG` WHERE 1=1";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argType = new ArrayList<>();

        if (condition.getMallId() != null && condition.getMallId() > 0) {
            sb.append(" and mall_id=?");
            args.add(condition.getMallId());
            argType.add(Types.INTEGER);
        }

        if (condition.getShopId() != null && condition.getShopId() > 0) {
            sb.append(" and shop_id=?");
            args.add(condition.getShopId());
            argType.add(Types.INTEGER);
        }

        if (condition.getShoppingDateFrom() > 0 && condition.getShoppingDateTo() > 0) {
            sb.append(" and shopping_date >= ? and shopping_date <= ?");
            args.add(condition.getShoppingDateFrom());
            args.add(condition.getShoppingDateTo());
            argType.add(Types.BIGINT);
            argType.add(Types.BIGINT);
        }

        if (!StringUtils.isEmpty(condition.getMobile())) {
            sb.append(" and member_mobile=?");
            args.add(condition.getMobile());
            argType.add(Types.VARCHAR);
        }
        sb.append(" ORDER BY " + condition.getSort() + " " + condition.getDirection() + " limit ?, ?");
//       log.info("[{}]", sql);
        args.add(condition.getOffset());
        args.add(condition.getSize());
        argType.add(Types.INTEGER);
        argType.add(Types.INTEGER);

        int[] arr = new int[argType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argType.get(i);
        }

        List<Points> list = jdbcTemplate.query(sb.toString(), args.toArray(), arr, new ResultSetExtractor<List<Points>>() {
            @Override
            public List<Points> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Points> _list = new ArrayList<>();

                while (rs.next()) {
                    //mplog_id,member_id,member_name,member_mobile,amount,points,shop_id,shop_name,shopping_date,ticket_no," +
//                    "vc_id,vc_name,handle_date,sources,mall_id
                    Points points = new Points();
                    points.setMplog_id(rs.getString("mplog_id"));
                    points.setMember_id(rs.getInt("member_id"));
                    points.setMember_name(rs.getString("member_name"));
                    points.setMember_mobile(rs.getString("member_mobile"));
                    points.setAmount(rs.getBigDecimal("amount"));
                    points.setPoints(rs.getInt("points"));
                    points.setShop_id(rs.getInt("shop_id"));
                    points.setShop_name(rs.getString("shop_name"));
                    points.setShopping_date(new Date(rs.getLong("shopping_date")));
                    points.setTicket_no(rs.getString("ticket_no"));
                    points.setVc_id(rs.getInt("vc_id"));
                    points.setVc_name(rs.getString("vc_name"));
                    points.setHandle_date(new Date(rs.getLong("handle_date")));
                    points.setSources(rs.getInt("sources"));
                    points.setMall_id(rs.getInt("mall_id"));
                    _list.add(points);
                }
                return _list;
            }
        });

        return list;
    }

    public Points selectPointsDetail(final String mplog_id) {
        String sql = "SELECT mplog_id,amount,points,shop_name,shopping_date,sources " +
                "FROM `T_MEMBER_POINTS_LOG` WHERE mplog_id=?";

//        log.info("[{}]", sql);

        Points points = null;
        try {
            points = jdbcTemplate.queryForObject(sql, new Object[]{mplog_id}, new int[]{Types.VARCHAR}, new PointsRowMapper());
        } catch (Exception e) {
        }

        return points;
    }

    /**
     * 插入小票上传的积分收入记录
     *
     * @param points
     * @return
     */
    public int insertPoints(Points points) {
        String sql = "INSERT INTO `T_MEMBER_POINTS_LOG` " +
                "(mplog_id,member_id,member_name,member_mobile,amount,points,shop_id,shop_name,shopping_date,ticket_no," +
                "vc_id,vc_name,handle_date,sources,`desc`,mall_id) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        log.debug("[{}]", sql);

        SnowFlake snowFlake = new SnowFlake();
        points.setMplog_id(snowFlake.nextId2String());

        int result = 0;
        try {
            result = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, points.getMplog_id());
                    ps.setInt(2, points.getMember_id());
                    ps.setString(3, points.getMember_name());
                    ps.setString(4, points.getMember_mobile());
                    ps.setBigDecimal(5, points.getAmount());
                    ps.setInt(6, points.getPoints());
                    ps.setInt(7, points.getShop_id());
                    ps.setString(8, points.getShop_name());
                    ps.setLong(9, points.getShopping_date());
                    if (StringUtils.isEmpty(points.getTicket_no())) ps.setNull(10, Types.VARCHAR);
                    else ps.setString(10, points.getTicket_no());
                    if (points.getVc_id() <= 0) ps.setNull(11, Types.INTEGER);
                    else ps.setInt(11, points.getVc_id());
                    if (StringUtils.isEmpty(points.getVc_name())) ps.setNull(12, Types.VARCHAR);
                    else ps.setString(12, points.getVc_name());
                    ps.setLong(13, points.getHandle_date());
                    ps.setInt(14, points.getSources());
                    if (StringUtils.isEmpty(points.getDesc())) ps.setNull(15, Types.VARCHAR);
                    else ps.setString(15, points.getDesc());
                    ps.setInt(16, points.getMall_id());

                    return ps;
                }
            });
        } catch (DataAccessException e) {
        }

        return result;
    }

    public int insertPointsExcludeConsume(PointsActionCondition condition) {
        final String sql = "insert into `T_MEMBER_POINTS_LOG` " +
                "(mplog_id,member_id,member_name,member_mobile,points,handle_date,sources,mall_id) " +
                "values(?,?,?,?,?,?,?,?)";

        return jdbcTemplate.update(sql, condition.getMplogId(), condition.getMemberId(), condition.getMemberName(), condition.getMemberMobile(),
                condition.getPoints(), condition.getHandleDate(), condition.getSources(), condition.getMallId());

    }

    public int insertPointsExcludeConsume(Points points) {
        final String sql = "insert into `T_MEMBER_POINTS_LOG` " +
                "(mplog_id,member_id,member_name,member_mobile,points,handle_date,sources,mall_id,amount) " +
                "values(?,?,?,?,?,?,?,?,?)";

        return jdbcTemplate.update(sql, points.getMplog_id(), points.getMember_id(), points.getMember_name(), points.getMember_mobile(),
                points.getPoints(), points.getHandle_date(), points.getSources(), points.getMall_id(), points.getAmount());

    }

    public int selectLogsCount(final Integer mallId) {
        String sql = "SELECT count(1) cnt " +
                "FROM `T_MEMBER_POINTS_LOG` WHERE mall_id=? and sources=0";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{mallId});
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    class PointsRowMapper implements RowMapper<Points> {

        @Override
        public Points mapRow(ResultSet rs, int rowNum) throws SQLException {
            Points points = new Points();
            log.info("[{}]", rs);

            if (dbUtils.hasColumn(rs, "mplog_id")) points.setMplog_id(rs.getString("mplog_id"));
            if (dbUtils.hasColumn(rs, "amount")) points.setAmount(rs.getBigDecimal("amount"));
            if (dbUtils.hasColumn(rs, "points")) points.setPoints(rs.getInt("points"));
            if (dbUtils.hasColumn(rs, "shop_name")) points.setShop_name(rs.getString("shop_name"));
            if (dbUtils.hasColumn(rs, "shopping_date")) points.setShopping_date(new Date(rs.getLong("shopping_date")));
            if (dbUtils.hasColumn(rs, "sources")) points.setSources(rs.getInt("sources"));

            return points;
        }
    }

    public List<Points> multipleSelectPoints(final PointsLogFilterCondition $$) {
        String sql = "SELECT mplog_id,member_id,member_name,member_mobile,amount,l.points,l.shop_id,l.shop_name,shopping_date,ticket_no," +
                "vc_id,vc_name,handle_date,sources,l.mall_id,s.industry_name " +
                "FROM `T_MEMBER_POINTS_LOG` l left join `T_SHOP` s on l.shop_id=s.shop_id " +
                "WHERE l.mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getUsername())) {
            sql += " and member_name like '%' ? '%'";
            args.add($$.getUsername());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and member_mobile = ?";
            args.add($$.getMobile());
        }

        if ($$.getShoppingDateStart() > 0L && $$.getShoppingDateEnd() > 0L) {
            sql += " and shopping_date >= ? and shopping_date<=?";
            args.add($$.getShoppingDateStart());
            args.add($$.getShoppingDateEnd());
        }

        if ($$.getAmountsStart().doubleValue() > 0 && $$.getAmountsEnd().doubleValue() > 0) {
            sql += " and amount>=? and amount<=?";
            args.add($$.getAmountsStart());
            args.add($$.getAmountsEnd());
        }

        if ($$.getPointsStart() > 0 && $$.getPointsEnd() > 0) {
            sql += " and l.points>=? and l.points<=?";
            args.add($$.getPointsStart());
            args.add($$.getPointsEnd());
        }

        if ($$.getShop() > 0) {
            sql += " and l.shop_id=?";
            args.add($$.getShop());
        }

        if ($$.getSources() > 0) {
            sql += " and sources=?";
            args.add($$.getSources());
        }

        sql += " order by handle_date desc limit ?, ?";
        args.add($$.getOffset());
        args.add($$.getSize());

        return jdbcTemplate.query(sql, args.toArray(),
                (rs, rowNum) -> {
                    Points $ = new Points();
                    $.setMplog_id(rs.getString("mplog_id"));
                    $.setMember_id(rs.getInt("member_id"));
                    $.setMember_name(rs.getString("member_name"));
                    $.setMember_mobile(rs.getString("member_mobile"));
                    $.setAmount(rs.getBigDecimal("amount"));
                    $.setPoints(rs.getInt("points"));
                    $.setShop_id(rs.getInt("shop_id"));
                    $.setShop_name(rs.getString("shop_name"));
                    $.setShop_industry(rs.getString("industry_name"));
                    $.setShopping_date(new Date(rs.getLong("shopping_date")));
                    $.setTicket_no(rs.getString("ticket_no"));
                    $.setVc_id(rs.getInt("vc_id"));
                    $.setVc_name(rs.getString("vc_name"));
                    $.setHandle_date(new Date(rs.getLong("handle_date")));
                    $.setSources(rs.getInt("sources"));
                    $.setMall_id(rs.getInt("mall_id"));
                    return $;
                });
    }

    public int multipleSelectPointsCount(final PointsLogFilterCondition $$) {
        String sql = "select count(1) cnt from `T_MEMBER_POINTS_LOG` " +
                "where mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getUsername())) {
            sql += " and member_name like '%' ? '%'";
            args.add($$.getUsername());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and member_mobile = ?";
            args.add($$.getMobile());
        }

        if ($$.getShoppingDateStart() > 0L && $$.getShoppingDateEnd() > 0L) {
            sql += " and shopping_date >= ? and shopping_date<=?";
            args.add($$.getShoppingDateStart());
            args.add($$.getShoppingDateEnd());
        }

        if ($$.getAmountsStart().doubleValue() > 0 && $$.getAmountsEnd().doubleValue() > 0) {
            sql += " and amount>=? and amount<=?";
            args.add($$.getAmountsStart());
            args.add($$.getAmountsEnd());
        }

        if ($$.getPointsStart() > 0 && $$.getPointsEnd() > 0) {
            sql += " and points>=? and points<=?";
            args.add($$.getPointsStart());
            args.add($$.getPointsEnd());
        }

        if ($$.getShop() > 0) {
            sql += " and shop_id=?";
            args.add($$.getShop());
        }

        if ($$.getSources() > 0) {
            sql += " and sources=?";
            args.add($$.getSources());
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, args.toArray());
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public int selectPointsSum(final PointsLogFilterCondition $$) {
        String sql = "select sum(points) p from `T_MEMBER_POINTS_LOG` " +
                "where mall_id=?";

        List<Object> args = new ArrayList<>();
        args.add($$.getMallId());

        if (!StringUtils.isEmpty($$.getUsername())) {
            sql += " and member_name like '%' ? '%'";
            args.add($$.getUsername());
        }

        if (!StringUtils.isEmpty($$.getMobile())) {
            sql += " and member_mobile = ?";
            args.add($$.getMobile());
        }

        if ($$.getShoppingDateStart() > 0L && $$.getShoppingDateEnd() > 0L) {
            sql += " and shopping_date >= ? and shopping_date<=?";
            args.add($$.getShoppingDateStart());
            args.add($$.getShoppingDateEnd());
        }

        if ($$.getAmountsStart().doubleValue() > 0 && $$.getAmountsEnd().doubleValue() > 0) {
            sql += " and amount>=? and amount<=?";
            args.add($$.getAmountsStart());
            args.add($$.getAmountsEnd());
        }

        if ($$.getPointsStart() > 0 && $$.getPointsEnd() > 0) {
            sql += " and points>=? and points<=?";
            args.add($$.getPointsStart());
            args.add($$.getPointsEnd());
        }

        if ($$.getShop() > 0) {
            sql += " and shop_id=?";
            args.add($$.getShop());
        }

        if ($$.getSources() > 0) {
            sql += " and sources=?";
            args.add($$.getSources());
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, args.toArray());
        int result = 0;

        if (row.first()) {
            result = row.getInt("p");
        }

        return result;
    }
}
