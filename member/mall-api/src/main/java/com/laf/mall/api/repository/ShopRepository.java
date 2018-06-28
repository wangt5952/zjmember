package com.laf.mall.api.repository;

import com.laf.mall.api.dto.Shop;
import com.laf.mall.api.querycondition.ShopQueryCondition;
import com.laf.mall.api.utils.db.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ShopRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbUtils dbUtils;

    public List<Shop> selectAllShopsByMall(final Integer mallId) {
        final String sql = "select shop_id,shop_name,mall_id from `T_SHOP` where mall_id=?";

        final List<Shop> list = jdbcTemplate.query(sql, new Object[]{mallId}, new ResultSetExtractor<List<Shop>>() {
            @Override
            public List<Shop> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Shop> _list = new ArrayList<>();

                while (rs.next()) {
                    Shop shop = new Shop();
                    shop.setShop_id(rs.getInt("shop_id"));
                    shop.setShop_name(rs.getString("shop_name"));
                    shop.setMall_id(rs.getInt("mall_id"));

                    _list.add(shop);
                }

                return _list;
            }
        });

        return list;
    }

    public List<Shop> selectShopListByIndustry(final Integer industryId, final Integer mallId) {
        final String sql = "select shop_id,shop_name,industry,mall_id from `T_SHOP` where industry=? and mall_id=?";

        final List<Shop> list = jdbcTemplate.query(sql, new Object[]{industryId, mallId}, new ResultSetExtractor<List<Shop>>() {
            @Override
            public List<Shop> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Shop> _list = new ArrayList<>();

                while (rs.next()) {
                    Shop shop = new Shop();
                    shop.setShop_id(rs.getInt("shop_id"));
                    shop.setShop_name(rs.getString("shop_name"));
                    shop.setIndustry(rs.getInt("industry"));
                    shop.setMall_id(rs.getInt("mall_id"));

                    _list.add(shop);
                }

                return _list;
            }
        });

        return list;
    }

    public Shop selectShopById(final Integer shopId) {
        Shop shop = null;
        String sql = "SELECT s.shop_id, s.shop_name, s.phone,s.mobile, s.pictures, s.logo, s.berth_number, s.points," +
                " p.map_picture, i.industry_name, a.content" +
                " FROM `T_SHOP` s" +
                " LEFT JOIN `T_PLANE_MAP` p ON s.plane_map=p.map_id" +
                " LEFT JOIN `T_INDUSTRIES` i ON s.industry=i.industry_id" +
                " LEFT JOIN `T_ARTICLES` a ON s.intro=a.article_id" +
                " WHERE s.shop_id=?";

        log.debug("[{}]", sql);
        try {
            shop = jdbcTemplate.queryForObject(sql, new Object[]{shopId}, new ShopRowMapper());
        } catch (Exception e) {
        }

        return shop;
    }

    public Shop selectShopByname(final String shopname) {
        Shop shop = null;
        String sql = "SELECT s.shop_id, s.shop_name, s.phone,s.mobile, s.pictures, s.logo, s.berth_number, s.points," +
                " p.map_picture, i.industry_name, a.content" +
                " FROM `T_SHOP` s" +
                " LEFT JOIN `T_PLANE_MAP` p ON s.plane_map=p.map_id" +
                " LEFT JOIN `T_INDUSTRIES` i ON s.industry=i.industry_id" +
                " LEFT JOIN `T_ARTICLES` a ON s.intro=a.article_id" +
                " WHERE s.shop_name=?";

        log.debug("[{}]", sql);
        try {
            shop = jdbcTemplate.queryForObject(sql, new Object[]{shopname}, new ShopRowMapper());
        } catch (Exception e) {
        }

        return shop;
    }

    public List<Shop> selectShopList(ShopQueryCondition sqc) {
        final String sql = "SELECT s.shop_id, s.shop_name, s.logo, s.points, s.berth_number, s.sort, i.industry_name" +
                " FROM `T_SHOP` s" +
                " LEFT JOIN `T_INDUSTRIES` i ON s.industry=i.industry_id" +
                " WHERE s.mall_id=?";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        if (sqc.getMall_id() == null || sqc.getMall_id() <= 0) return ListUtils.EMPTY_LIST;

        args.add(sqc.getMall_id());
        argsType.add(Types.INTEGER);

        if (!StringUtils.isEmpty(sqc.getKeywords())) {
            sb.append(" AND s.text1 like '%' ? '%'");

            args.add(sqc.getKeywords().trim());
            argsType.add(Types.VARCHAR);
        }
        if (sqc.getMap_id() > 0) {
            sb.append(" AND s.plane_map=?");

            args.add(sqc.getMap_id());
            argsType.add(Types.INTEGER);
        }
        if (sqc.getIndustry_id() > 0) {
            sb.append(" AND s.industry=?");

            args.add(sqc.getIndustry_id());
            argsType.add(Types.INTEGER);
        }

        sb.append(" ORDER BY"); // 排序默认按排序号排序
        sb.append(" " + sqc.getSort());
        sb.append(" " + sqc.getDirection());

        sb.append(" LIMIT ?, ?");
        args.add(sqc.getOffset());
        args.add(sqc.getSize());
        argsType.add(Types.INTEGER);
        argsType.add(Types.INTEGER);

        log.debug("offset is {}", sqc.getOffset());
        log.debug("size is {}", sqc.getSize());

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }

        log.debug("args: {}", args.toString());
        log.debug("sql: {}", sb.toString());

        List<Shop> list = jdbcTemplate.query(sb.toString(), args.toArray(), arr, new ShopRowMapper());
        return list;
    }

    public Shop saveOne(final Shop shop) {
        String sql = "insert into `T_SHOP` (shop_name, plane_map, industry, mall_id, sort) values (?, ?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, shop.getShop_name());
                ps.setInt(2, shop.getPlane_map());
                ps.setInt(3, shop.getIndustry());
                ps.setInt(4, shop.getMall_id());
                ps.setInt(5, shop.getSort());

                return ps;
            }
        }, holder);

        int newShopId = holder.getKey().intValue();
        shop.setShop_id(newShopId);

        return shop;
    }

    public int saveList(final List<Shop> list) {
        String sql = "insert into `T_SHOP` (shop_name, plane_map, industry, mall_id, sort) values";

        StringBuilder sb = new StringBuilder(sql);
        for (Shop s : list) {
            sb.append(" (?, ?, ?, ?, ?),");
        }
        String str = sb.substring(0, sb.length() - 1);

        KeyHolder holder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);

                int index = 1;
                for (Shop s : list) {
                    log.info("head==index:" + index);
                    ps.setString(index++, s.getShop_name());
                    ps.setInt(index++, s.getPlane_map());
                    ps.setInt(index++, s.getIndustry());
                    ps.setInt(index++, s.getMall_id());
                    ps.setInt(index++, s.getSort());
                    log.info("head==index:" + (index - 1));
                }
                return ps;
            }
        }, holder);

        return rows;
    }

    class ShopRowMapper implements RowMapper<Shop> {
        @Override
        public Shop mapRow(ResultSet rs, int rowNum) throws SQLException {
            Shop shop = new Shop();

            if (dbUtils.hasColumn(rs, "shop_id")) shop.setShop_id(rs.getInt("shop_id"));

            if (dbUtils.hasColumn(rs, "shop_name")) shop.setShop_name(rs.getString("shop_name"));

            if (dbUtils.hasColumn(rs, "phone")) shop.setPhone(rs.getString("phone"));

            if (dbUtils.hasColumn(rs, "pictures")) shop.setPictures(rs.getString("pictures"));

            if (dbUtils.hasColumn(rs, "points")) shop.setPoints(rs.getInt("points"));

            if (dbUtils.hasColumn(rs, "map_picture")) shop.setMap_picture(rs.getString("map_picture"));

            if (dbUtils.hasColumn(rs, "industry_name")) shop.setIndustry_name(rs.getString("industry_name"));

            if (dbUtils.hasColumn(rs, "content")) shop.setIntro(rs.getString("content"));

            if (dbUtils.hasColumn(rs, "berth_number")) shop.setBerth_number(rs.getString("berth_number"));

            if (dbUtils.hasColumn(rs, "logo")) shop.setLogo(rs.getString("logo"));

            if (dbUtils.hasColumn(rs, "mall_id")) shop.setMall_id(rs.getInt("mall_id"));

            if (dbUtils.hasColumn(rs, "sort")) shop.setSort(rs.getInt("sort"));

            return shop;
        }
    }

}
