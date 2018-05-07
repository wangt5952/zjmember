package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Points;
import com.laf.mall.api.querycondition.points.PointsActionCondition;
import com.laf.mall.api.querycondition.points.PointsQueryCondition;
import com.laf.mall.api.utils.db.DbUtils;
import com.laf.manager.core.support.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
@Slf4j
public class PointsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbUtils dbUtils;


    public List<Points> selectPointsList(PointsQueryCondition condition, final Integer memberId) {
        String sql = "SELECT mplog_id,amount,points,shop_name,shopping_date,sources " +
                "FROM `T_MEMBER_POINTS_LOG` WHERE mall_id=? AND member_id=? " +
                "ORDER BY " + condition.getSort() + " " + condition.getDirection() + " limit ?, ?";

//       log.info("[{}]", sql);

        Object[] args = new Object[]{condition.getMallId(), memberId, condition.getOffset(), condition.getSize()};
        int[] argsType = new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER};
        List<Points> list = jdbcTemplate.query(sql, args, argsType, new PointsRowMapper());

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
     * 录入积分记录
     * @param points
     * @return
     */
    public int insertPoints(Points points) {
        String sql = "INSERT INTO `T_MEMBER_POINTS_LOG` " +
                "(mplog_id,member_id,member_name,member_mobile,amount,points,shop_id,shop_name,shopping_date,ticket_no," +
                "vc_id,vc_name,handle_date,sources,mall_id) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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

                    ps.setString(10, points.getTicket_no());
                    ps.setInt(11, points.getVc_id());
                    ps.setString(12, points.getVc_name());
                    ps.setLong(13, points.getHandle_date());
                    ps.setInt(14, points.getSources());
                    ps.setInt(15, points.getMall_id());

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
log.info("PointsRepository#insertPointsExcludeConsume()==condition.getHandleDate == {}", condition.getHandleDate());
        return jdbcTemplate.update(sql, condition.getMplogId(), condition.getMemberId(), condition.getMemberName(), condition.getMemberMobile(),
                condition.getPoints(), condition.getHandleDate(), condition.getSources(), condition.getMallId());

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
}
