package com.laf.manager.repository;

import com.laf.manager.dto.Assessment;
import com.laf.manager.querycondition.assessment.AssessmentQueryCondition;
import javafx.beans.property.adapter.ReadOnlyJavaBeanBooleanProperty;
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
public class AssessmentRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Assessment> selectAssessmentList(AssessmentQueryCondition condition) {

        String sql = "SELECT assessment_id,member_id,member_name,member_mobile,action_time,shop_id,shop_name,status,reply,comments " +
                "FROM `T_ASSESSMENT` WHERE mall_id=? ORDER BY action_time desc limit ?,?";

        List<Assessment> list = jdbcTemplate.query(sql, new Object[]{condition.getMallId(), condition.getOffset(), condition.getSize()},
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
                            assessment.setComments(rs.getString("comments"));
                            _list.add(assessment);
                        }

                        return _list;
                    }
                });

        return list;
    }

    public Assessment selectAssessmentById(final int assessment_id) {

        String sql = "SELECT assessment_id,member_id,member_name,member_mobile,action_time,shop_id,shop_name,status,reply,comments " +
                "FROM `T_ASSESSMENT` WHERE assessment_id=?";

        Assessment assessment = jdbcTemplate.query(sql, new Object[]{assessment_id}, new ResultSetExtractor<Assessment>() {
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
                }
                return _assessment;
            }
        });

        return assessment;
    }

    public int updateReply(Assessment assessment) {
        String sql = "UPDATE `T_ASSESSMENT` SET reply=?,status=? WHERE assessment_id=?";

        int result = jdbcTemplate.update(sql, assessment.getReply(), assessment.getStatus(), assessment.getAssessment_id());

        return result;
    }

    public int deleteAssessmentById(final int assessment_id) {
        String sql = "DELETE FROM `T_ASSESSMENT` WHERE assessment_id=?";

        return jdbcTemplate.update(sql, assessment_id);
    }

    public int selectAssessmentCount(AssessmentQueryCondition condition) {

        String sql = "SELECT count(1) cnt FROM `T_ASSESSMENT` WHERE mall_id=?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, new Object[]{condition.getMallId()});
        int result = 0;

        if (row.first()) {
            result = row.getInt("cnt");
        }

        return result;
    }

    public List<Integer> selectAssessmentCountMonthly(String year, int mallId) {
        final String sql = "select t1.count,t2.month from\n" +
                "(select count(1) `count`, y,m from\n" +
                "(select assessment_id,(select from_unixtime(action_time/1000,'%Y')) y,(select from_unixtime(action_time/1000,'%m')) m from t_assessment where mall_id=?) t where y=? group by y,m) t1\n" +
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
}
