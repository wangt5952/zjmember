package com.laf.manager.repository;

import com.laf.manager.dto.Ticket;
import com.laf.manager.querycondition.ticket.TicketFilterCondition;
import com.laf.manager.querycondition.ticket.TicketNoPassSaveCondition;
import com.laf.manager.querycondition.ticket.TicketQueryCondition;
import com.laf.manager.utils.datetime.DateTimeUtils;
import com.laf.manager.utils.file.FileProperties;
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

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class TicketRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertTicket(final Ticket ticket) {
        final String sql = "insert into `T_TICKET` (member_id,member_name,member_mobile,upload_date," +
                "file_url,mall_id) values (?,?,?,?,?,?)";

        log.info("[sql = {}]", sql);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, ticket.getMember_id());
                ps.setString(2, ticket.getMember_name());
                ps.setString(3, ticket.getMember_mobile());
                ps.setLong(4, ticket.getUpload_date());
                ps.setString(5, ticket.getFile_url());
                ps.setInt(6, ticket.getMall_id());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int insertMultipleTicket(final Ticket ticket) {

        final String sql = "insert into `T_TICKET` (member_id,member_name,member_mobile,vc_id,vc_name,handle_date," +
                "amounts,handle_status,responses,ticket_no,shopping_date,shop_id,mall_id" +
                ") values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        log.info("[sql = {}]", sql);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, ticket.getMember_id());
                ps.setString(2, ticket.getMember_name());
                ps.setString(3, ticket.getMember_mobile());
                ps.setInt(4, ticket.getVc_id());
                ps.setString(5, ticket.getVc_name());
                ps.setLong(6, ticket.getHandle_date());
                ps.setBigDecimal(7, ticket.getAmounts());
                ps.setInt(8, ticket.getHandle_status());
                ps.setString(9, ticket.getResponses());
                ps.setString(10, ticket.getTicket_no());
                ps.setLong(11, ticket.getShopping_date());
                ps.setInt(12, ticket.getShop_id());
                ps.setInt(13, ticket.getMall_id());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int deleteTicketBy(final Integer ticketId) {

        if (ticketId == null || ticketId == 0) return 0;

        final String sql = "delete from `T_TICKET` where ticket_id=?";

        return jdbcTemplate.update(sql, ticketId);
    }

    public List<Ticket> selectTicketList(final TicketQueryCondition condition) {

        if (condition.getMallId() <= 0) return ListUtils.EMPTY_LIST;

        final String sql = "select ticket_id,member_id,member_name,member_mobile,vc_id,vc_name,vc_phone,handle_date,handle_status,responses,upload_date,file_url,mall_id " +
                "from `T_TICKET` where mall_id=?";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argType = new ArrayList<>();

        args.add(condition.getMallId());
        argType.add(Types.INTEGER);

        if (condition.getMemberId() > 0) {
            sb.append(" and member_id=?");

            args.add(condition.getMemberId());
            argType.add(Types.INTEGER);
        }
        sb.append(" order by " + condition.getOrderBy() + " limit ?,?");

        args.add(condition.getOffset());
        args.add(condition.getSize());
        argType.add(Types.INTEGER);
        argType.add(Types.INTEGER);

        int[] arr = new int[argType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argType.get(i);
        }

        final List<Ticket> list = jdbcTemplate.query(sb.toString(), args.toArray(), arr, new ResultSetExtractor<List<Ticket>>() {
            @Override
            public List<Ticket> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Ticket> _list = new ArrayList<>();

                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setTicket_id(rs.getInt("ticket_id"));
                    ticket.setMember_id(rs.getInt("member_id"));
                    ticket.setMember_name(rs.getString("member_name"));
                    ticket.setMember_mobile(rs.getString("member_mobile"));
                    ticket.setVc_id(rs.getInt("vc_id"));
                    ticket.setVc_name(rs.getString("vc_name"));
                    ticket.setVc_phone(rs.getString("vc_phone"));
                    ticket.setHandle_date(new Date(rs.getLong("handle_date")));
                    ticket.setHandle_status(rs.getInt("handle_status"));
                    ticket.setResponses(rs.getString("responses"));
                    ticket.setUpload_date(new Date(rs.getLong("upload_date")));
                    ticket.setMall_id(rs.getInt("mall_id"));
                    _list.add(ticket);
                }

                return _list;
            }
        });

        return list;
    }

    public int updateTicket(Ticket ticket) {
        String sql = "update `T_TICKET` set vc_id=?,vc_name=?,vc_phone=?,handle_date=?,responses=?,handle_status=?,ticket_no=?,shop_id=?,shop_name=?,shopping_date=?,amounts=?" +
                " where ticket_id=?";

        int result = jdbcTemplate.update(sql, ticket.getVc_id(), ticket.getVc_name(), ticket.getVc_phone(), ticket.getHandle_date(), ticket.getResponses(),
                ticket.getHandle_status(), ticket.getTicket_no(), ticket.getShop_id(), ticket.getShop_name(), ticket.getShopping_date(), ticket.getAmounts(), ticket.getTicket_id());

        return result;
    }

    public int updateTicketForNoPass(TicketNoPassSaveCondition condition) {
        String sql = "update `T_TICKET` set vc_id=?,vc_name=?,vc_phone=?,handle_date=?,responses=?,handle_status=? where ticket_id=?";

        int result = jdbcTemplate.update(sql, condition.getVcId(), condition.getVcName(), condition.getVcPhone(), condition.getHandleDate(),
                condition.getResponses(), condition.getHandleStatus(), condition.getTicketId());

        return result;
    }

    public Ticket selectTicketById(final Integer ticketId) {

        final String sql = "select t.ticket_id,t.member_id,t.member_name,t.member_mobile,t.vc_id," +
                "t.vc_name,t.vc_phone,t.handle_date,t.handle_status,t.responses,t.upload_date,t.file_url," +
                "t.ticket_no,log.points,t.mall_id,t.shop_id,t.shop_name,t.shopping_date,t.amounts " +
                "from `T_TICKET` t left join `T_MEMBER_POINTS_LOG` log on t.ticket_no=log.ticket_no" +
                " where t.ticket_id=?";

        return jdbcTemplate.queryForObject(sql, new Object[]{ticketId},
                (rs, rowNum) -> {
                    Ticket $$ = new Ticket();
                    $$.setTicket_id(rs.getInt("ticket_id"));
                    $$.setMember_id(rs.getInt("member_id"));
                    $$.setMember_name(rs.getString("member_name"));
                    $$.setMember_mobile(rs.getString("member_mobile"));
                    $$.setVc_id(rs.getInt("vc_id"));
                    $$.setVc_name(rs.getString("vc_name"));
                    $$.setVc_phone(rs.getString("vc_phone"));
                    $$.setHandle_date(new Date(rs.getLong("handle_date")));
                    $$.setHandle_status(rs.getInt("handle_status"));
                    $$.setResponses(rs.getString("responses"));
                    $$.setUpload_date(new Date(rs.getLong("upload_date")));
//                    $$.setFile_url(fileProperties.getDomain() + rs.getString("file_url"));
                    $$.setFile_url(rs.getString("file_url"));
                    $$.setTicket_no(rs.getString("ticket_no"));
                    $$.setPoints(rs.getInt("points"));
                    $$.setMall_id(rs.getInt("mall_id"));
                    $$.setShop_id(rs.getInt("shop_id"));
                    $$.setShop_name(rs.getString("shop_name"));
                    $$.setShopping_date(new Date(rs.getLong("shopping_date")));
                    $$.setAmounts(rs.getBigDecimal("amounts"));
                    return $$;
                });
    }

    public int selectTicketsCount(final Integer mallId) {
        final String sql = "select count(1) cnt from `T_TICKET` where mall_id=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{mallId});
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public List<Ticket> multipleSelectTickets(final TicketFilterCondition $$) {
        String sql = "select ticket_id,member_id,member_name,member_mobile,upload_date,vc_name," +
                "vc_phone,vc_id,handle_date,handle_status,t.shop_id,s.shop_name,shopping_date,amounts," +
                "ticket_no,t.mall_id " +
                "from `T_TICKET` t left join `T_SHOP` s on t.shop_id=s.shop_id where t.mall_id=?";

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

        if (!StringUtils.isEmpty($$.getTicketNo())) {
            sql += " and ticket_no like '%' ? '%'";
            args.add($$.getTicketNo());
        }

        if ($$.getCreateDateStart() > 0L && $$.getCreateDateEnd() > 0L) {
            sql += " and upload_date >= ? and upload_date<=?";
            args.add($$.getCreateDateStart());
            args.add($$.getCreateDateEnd());
        }

        if ($$.getAmountStart().doubleValue() > 0 && $$.getAmountEnd().doubleValue() > 0) {
            sql += " and amounts >=? and amounts<=?";
            args.add($$.getAmountStart());
            args.add($$.getAmountEnd());
        }

        if ($$.getShop() > 0) {
            sql += " and t.shop_id=?";
            args.add($$.getShop());
        }

        if ($$.getStatus() >= 0) {
            sql += " and handle_status=?";
            args.add($$.getStatus());
        }

        if ($$.getActionDateStart() > 0L && $$.getActionDateEnd() > 0L) {
            sql += " and shopping_date>=? and shopping_date<=?";
            args.add($$.getActionDateStart());
            args.add($$.getActionDateEnd());
        }

        if ($$.getHandleDateStart() > 0L && $$.getHandleDateEnd() > 0L) {
            sql += " and handle_date>=? and handle_date<=?";
            args.add($$.getHandleDateStart());
            args.add($$.getHandleDateEnd());
        }

        sql += " order by upload_date desc limit ?,?";
        args.add($$.getOffset());
        args.add($$.getSize());

        return jdbcTemplate.query(sql, args.toArray(),
                (rs, rowNum) -> {
                    Ticket $ = new Ticket();
                    $.setTicket_id(rs.getInt("ticket_id"));
                    $.setMember_id(rs.getInt("member_id"));
                    $.setMember_name(rs.getString("member_name"));
                    $.setMember_mobile(rs.getString("member_mobile"));
                    $.setUpload_date(new Date(rs.getLong("upload_date")));
                    $.setVc_name(rs.getString("vc_name"));
                    $.setVc_phone(rs.getString("vc_phone"));
                    $.setVc_id(rs.getInt("vc_id"));
                    $.setHandle_date(new Date(rs.getLong("handle_date")));
                    $.setHandle_status(rs.getInt("handle_status"));
                    $.setShop_id(rs.getInt("shop_id"));
                    $.setShop_name(rs.getString("shop_name"));
                    $.setShopping_date(new Date(rs.getLong("shopping_date")));
                    $.setAmounts(rs.getBigDecimal("amounts"));
                    $.setTicket_no(rs.getString("ticket_no"));
                    $.setMall_id(rs.getInt("mall_id"));
                    return $;
                });
    }

    public int multipleSelectTicketsCount(final TicketFilterCondition $$) {
        String sql = "select count(1) cnt from `T_TICKET` where mall_id=?";

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

        if (!StringUtils.isEmpty($$.getTicketNo())) {
            sql += " and ticket_no like '%' ? '%'";
            args.add($$.getTicketNo());
        }

        if ($$.getCreateDateStart() > 0L && $$.getCreateDateEnd() > 0L) {
            sql += " and upload_date >=? and upload_date<=?";
            args.add($$.getCreateDateStart());
            args.add($$.getCreateDateEnd());
        }

        if ($$.getAmountStart().doubleValue() > 0 && $$.getAmountEnd().doubleValue() > 0) {
            sql += " and amounts >=? and amounts<=?";
            args.add($$.getAmountStart());
            args.add($$.getAmountEnd());
        }

        if ($$.getShop() > 0) {
            sql += " and shop_id=?";
            args.add($$.getShop());
        }

        if ($$.getStatus() >= 0) {
            sql += " and handle_status=?";
            args.add($$.getStatus());
        }

        if ($$.getActionDateStart() > 0L && $$.getActionDateEnd() > 0L) {
            sql += " and shopping_date>=? and shopping_date<=?";
            args.add($$.getActionDateStart());
            args.add($$.getActionDateEnd());
        }

        if ($$.getHandleDateStart() > 0L && $$.getHandleDateEnd() > 0L) {
            sql += " and handle_date>=? and handle_date<=?";
            args.add($$.getHandleDateStart());
            args.add($$.getHandleDateEnd());
        }

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, args.toArray());
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public int selectTicketByTicketNoCount(final String ticketNo) {
        final String sql = "select count(1) cnt from `T_TICKET` where ticket_no=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{ticketNo});

        int result = 0;
        if (rowSet.first()) {
            result = rowSet.getInt("cnt");
        }

        return result;
    }
}
