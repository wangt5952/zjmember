package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Ticket;
import com.laf.mall.api.querycondition.ticket.TicketNoPassSaveCondition;
import com.laf.mall.api.querycondition.ticket.TicketNoQueryConditon;
import com.laf.mall.api.querycondition.ticket.TicketQueryCondition;
import com.laf.mall.api.utils.file.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class TicketRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    FileProperties fileProperties;

    public int insertTicket(final Ticket ticket) {
        final String sql = "insert into `T_TICKET` (member_id,member_name,member_mobile,upload_date," +
                "file_url,mall_id,ticket_no,shop_id,shopping_date,amounts) values (?,?,?,?,?,?,?,?,?,?)";
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
                ps.setString(7, ticket.getTicket_no());
                ps.setInt(8, ticket.getShop_id());
                ps.setLong(9, ticket.getShopping_date());
                ps.setBigDecimal(10, ticket.getAmounts());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public int deleteTicketBy(final Integer ticketId) {

        final String sql = "delete from `T_TICKET` where ticket_id=?";

        return jdbcTemplate.update(sql, ticketId);
    }

    public List<Ticket> selectTicketListByMemberId(final TicketQueryCondition condition) {
        final String sql = "select ticket_id,handle_status,responses,upload_date,file_url " +
                "from `T_TICKET` where member_id=? and mall_id=? order by " + condition.getOrderBy()+
                " limit ?,?";

        final List<Ticket> list = jdbcTemplate.query(sql, new Object[]{condition.getMemberId(),condition.getMallId(),condition.getOffset(),condition.getSize()}, new ResultSetExtractor<List<Ticket>>() {
            @Override
            public List<Ticket> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Ticket> _list = new ArrayList<>();

                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setTicket_id(rs.getInt("ticket_id"));
                    ticket.setHandle_status(rs.getInt("handle_status"));
                    ticket.setResponses(rs.getString("responses"));
                    ticket.setUpload_date(new Date(rs.getLong("upload_date")));
                    ticket.setFile_url(fileProperties.getDomain() + rs.getString("file_url"));

                    _list.add(ticket);
                }

                return _list;
            }
        });

        return list;
    }

    public int updateTicketForNoPass(TicketNoPassSaveCondition condition) {
        String sql = "update `T_TICKET` set vc_id=?,vc_name=?,vc_phone=?,handle_date=?,responses=?,handle_status=? where ticket_id=?";

        int result = jdbcTemplate.update(sql, condition.getVcId(), condition.getVcName(), condition.getVcPhone(), condition.getHandleDate(),
                condition.getResponses(), condition.getHandleStatus(), condition.getTicketId());

        return result;
    }

    public int selectTicketByTicketNoCount(TicketNoQueryConditon condition) {
        final String sql = "select count(1) cnt from `T_TICKET` where ticket_no=?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{condition.getTicketNo()});

        int result = 0;
        if (rowSet.first()) {
            result = rowSet.getInt("cnt");
        }

        return result;
    }
}
