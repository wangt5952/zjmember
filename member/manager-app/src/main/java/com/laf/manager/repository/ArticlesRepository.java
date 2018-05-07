package com.laf.manager.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class ArticlesRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int insertArticle(String content, int mallId) {
        final String sql = "insert into `T_ARTICLES` (content,`reads`,mall_id) values (?,0,?)";

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

    public int updateArticle(int articleId, String content) {
        final String sql = "update `T_ARTICLES` set content=? where article_id=?";

        return jdbcTemplate.update(sql, content, articleId);
    }

    public int deleteArticle(final int articleId) {

        if (articleId <= 0) return 0;

        String sql = "delete from `T_ARTICLES` where article_id=?";

        int result = jdbcTemplate.update(sql, articleId);

        return result;
    }
}
