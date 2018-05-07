package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Assessment;
import com.laf.mall.api.querycondition.assessment.AssessmentListCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class AssessmentRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int insertAssessment(Assessment assessment) {
        final String sql = "INSERT INTO `T_ASSESSMENT` (assessment_id,member_id,member_name,member_mobile,action_time,shop_id,shop_name,status,reply,comments,mall_id) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, assessment.getAssessment_id());
                ps.setInt(2, assessment.getMember_id());
                ps.setString(3, assessment.getMember_name());
                ps.setString(4, assessment.getMember_mobile());
                ps.setLong(5, assessment.getAction_time());
                ps.setInt(6, assessment.getShop_id());
                ps.setString(7, assessment.getShop_name());
                ps.setInt(8, assessment.getStatus());
                ps.setString(9, assessment.getReply());
                ps.setString(10, assessment.getComments());
                ps.setInt(11, assessment.getMall_id());

                return ps;
            }
        }, holder);

        return holder.getKey().intValue();
    }

    public List<Assessment> selectAssessmentList(final AssessmentListCondition condition, final Integer memberId) {
        String sql = "SELECT assessment_id,member_id,member_name,member_mobile,action_time,shop_id,shop_name,status,reply,mall_id " +
                "FROM `T_ASSESSMENT` WHERE member_id=? and mall_id=? ORDER BY action_time DESC LIMIT ?,?";

        List<Assessment> list = jdbcTemplate.query(sql, new Object[]{memberId, condition.getMallId(), condition.getOffset(), condition.getSize()},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER},
                new ResultSetExtractor<List<Assessment>>() {
                    @Override
                    public List<Assessment> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<Assessment> _list = new ArrayList<>();

                        while (rs.next()) {
                            Assessment assessment = new Assessment();
                            assessment.setAssessment_id(rs.getInt("assessment_id"));
                            assessment.setMember_id(rs.getInt("member_id"));
                            assessment.setMember_name(rs.getString("member_name"));
                            assessment.setMember_mobile(rs.getString("member_mobile"));
                            assessment.setAction_time(new Date(rs.getLong("action_time")));
                            assessment.setShop_id(rs.getInt("shop_id"));
                            assessment.setShop_name(rs.getString("shop_name"));
                            assessment.setStatus(rs.getInt("status"));
                            assessment.setReply(rs.getString("reply"));
                            assessment.setMall_id(rs.getInt("mall_id"));

                            _list.add(assessment);
                        }

                        return _list;
                    }
                });

        return list;
    }

    public Assessment selectAssessmentById(final Integer assessmentId) {
        String sql = "SELECT assessment_id,member_id,member_name,member_mobile,action_time,shop_id,shop_name,status,reply,comments,mall_id " +
                "FROM `T_ASSESSMENT` WHERE assessment_id=?";

        Assessment assessment = jdbcTemplate.query(sql, new Object[]{assessmentId}, new ResultSetExtractor<Assessment>() {
            @Override
            public Assessment extractData(ResultSet rs) throws SQLException, DataAccessException {
                Assessment _assessment = null;

                if (rs.next()) {
                    _assessment = new Assessment();
                    _assessment.setAssessment_id(rs.getInt("assessment_id"));
                    _assessment.setMember_id(rs.getInt("member_id"));
                    _assessment.setMember_name(rs.getString("member_name"));
                    _assessment.setMember_mobile(rs.getString("member_mobile"));
                    _assessment.setAction_time(new Date(rs.getLong("action_time")));
                    _assessment.setShop_id(rs.getInt("shop_id"));
                    _assessment.setShop_name(rs.getString("shop_name"));
                    _assessment.setStatus(rs.getInt("status"));
                    _assessment.setReply(rs.getString("reply"));
                    _assessment.setComments(rs.getString("comments"));
                    _assessment.setMall_id(rs.getInt("mall_id"));
                }

                return _assessment;
            }
        });

        return assessment;
    }
}
