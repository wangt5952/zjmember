package com.laf.manager.repository;

import com.laf.manager.core.support.tuple.Tuple3;
import com.laf.manager.dto.RaffleInfo;
import com.laf.manager.querycondition.raffle.RaffleQueryCondition;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RaffleRepositoryJava {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> selectRaffleList(RaffleQueryCondition condition) {

//        String sql = "SELECT raffle_id,title,raffle_time_start,raffle_time_end,required_points,activity_code " +
//                "FROM `T_RAFFLE_INFO` WHERE mall_id=?";

        final String sql = "SELECT t.raffle_id,t.title,raffle_time_start,t.raffle_time_end,t.required_points,t.activity_code," +
                "t2.times,t3.cnt FROM t_raffle_info t RIGHT JOIN" +
                "  (SELECT count(1) AS times,raffle_id FROM t_raffle_log GROUP BY raffle_id) t2 ON t.raffle_id=t2.raffle_id RIGHT JOIN" +
                "  (SELECT count(1) AS cnt,raffle_id FROM (SELECT member_id,raffle_id FROM t_raffle_log GROUP BY member_id,raffle_id) tt GROUP BY raffle_id) t3\n" +
                "  ON t.raffle_id=t3.raffle_id WHERE t.mall_id=?";

//        Tuple3<String, Object[], int[]> t3 = addFilter(sql, condition);

//        List<RaffleInfo> list = jdbcTemplate.query(t3.$1, t3.$2, t3.$3, new ResultSetExtractor<List<RaffleInfo>>() {
//            @Override
//            public List<RaffleInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
//                List<RaffleInfo> _list = new ArrayList<>();
//
//                while (rs.next()) {
//                    val raffle = new RaffleInfo(
//                            rs.getInt("raffle_id"),
//                            rs.getString("title"),
//                            rs.getLong("raffle_time_start"),
//                            rs.getLong("raffle_time_end"),
//                            rs.getInt("required_points"),
//                            rs.getString("activity_code")
//                    );
//                    _list.add(raffle);
//                }
//                return _list;
//            }
//        });

        return jdbcTemplate.query(sql, new Object[]{condition.getMallId()},
                (rs, __) -> {
                    Map<String, Object> map = new HashMap<>();

                    map.put("raffle_id", rs.getInt("raffle_id"));
                    map.put("title", rs.getString("title"));
                    map.put("raffle_time_start", rs.getLong("raffle_time_start"));
                    map.put("raffle_time_end", rs.getLong("raffle_time_end"));
                    map.put("required_points", rs.getInt("required_points"));
                    map.put("activity_code", rs.getString("activity_code"));
                    map.put("times", rs.getInt("times"));
                    map.put("cnt", rs.getInt("cnt"));

                    return map;
                });
//        return list;
    }

    public int selectRaffleCount(RaffleQueryCondition condition) {
        String sql = "SELECT count(1) cnt FROM `T_RAFFLE_INFO` WHERE mall_id=?";

        Tuple3<String, Object[], int[]> t3 = addFilter(sql, condition);

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(t3.$1, t3.$2, t3.$3);
        int result = 0;

        if (rowSet.first()) {
            result = rowSet.getInt("cnt");
        }
        return result;
    }

    private Tuple3 addFilter(String sql, RaffleQueryCondition condition) {

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        args.add(condition.getMallId());
        argsType.add(Types.INTEGER);

        sb.append(" ORDER BY raffle_time_end ASC LIMIT ?,?");
        args.add(condition.getOffset());
        args.add(condition.getSize());
        argsType.add(Types.INTEGER);
        argsType.add(Types.INTEGER);

        int arr[] = new int[argsType.size()];
        for (int i = 0; i < argsType.size(); i++) {
            arr[i] = argsType.get(i);
        }

        Tuple3<String, Object[], int[]> t3 = new Tuple3<>(sb.toString(), args.toArray(), arr);
        return t3;
    }
}
