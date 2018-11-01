package com.laf.manager.repository;

import com.laf.manager.dto.Shop;
import com.laf.manager.querycondition.shop.ShopQueryCondition;
import com.laf.manager.utils.db.DbUtils;
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
import org.springframework.jdbc.support.rowset.SqlRowSet;
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

    public List<Shop> selectShopsByPlantMap(final Integer mapId) {
        final String sql = "select shop_id,shop_name,mall_id " +
                "from `T_SHOP` where plane_map=? order by shop_name";

        return jdbcTemplate.query(sql, ps -> ps.setInt(1, mapId),
                (rs, rowNum) -> new Shop(rs.getInt("shop_id"), rs.getString("shop_name"), rs.getInt("mall_id")));
    }

    public Shop selectShopById(final Integer shopId) {

        if (shopId == null || shopId <= 0) return null;

        String sql = "SELECT s.shop_id, s.shop_name,s.logo,s.pictures, s.phone,s.mobile,s.linkman, s.pictures, s.logo, s.berth_number," +
                " s.map_name, s.plane_map, s.industry, s.industry_name, s.brand, s.business_hours, s.status, s.sort, s.mall_id, s.intro, a.content, a.article_id" +
                " FROM `T_SHOP` s" +
                " LEFT JOIN `T_ARTICLES` a ON s.intro=a.article_id" +
                " WHERE s.shop_id=?";

        Shop shop = null;

        try {
            shop = jdbcTemplate.queryForObject(sql, new Object[]{shopId}, new ShopRowMapper());
        } catch (Exception ex) {

        }

        return shop;
    }

    public List<Shop> selectShopList(ShopQueryCondition sqc) {
        final String sql = "SELECT s.shop_id, s.shop_name, s.brand, s.berth_number, s.sort, s.status, s.linkman, s.phone, i.industry_name, pm.map_name" +
                " FROM `T_SHOP` s" +
                " LEFT JOIN `T_INDUSTRIES` i ON s.industry=i.industry_id" +
                " LEFT JOIN `T_PLANE_MAP` pm ON s.plane_map=pm.map_id" +
                " WHERE 1=1";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        if (sqc.getMall_id() != null && sqc.getMall_id() > 0) {
            sb.append(" AND s.mall_id=?");

            args.add(sqc.getMall_id());
            argsType.add(Types.INTEGER);
        }
        if (!StringUtils.isEmpty(sqc.getKeywords())) {
            sb.append(" AND s.text1 like '%' ? '%'");

            args.add(sqc.getKeywords().trim());
            argsType.add(Types.VARCHAR);
        }
        if (sqc.getMap_id() != null && sqc.getMap_id() > 0) {
            sb.append(" AND s.plane_map=?");

            args.add(sqc.getMap_id());
            argsType.add(Types.INTEGER);
        }
        if (sqc.getIndustry_id() != null && sqc.getIndustry_id() > 0) {
            sb.append(" AND s.industry=?");

            args.add(sqc.getIndustry_id());
            argsType.add(Types.INTEGER);
        }

        sb.append(" ORDER BY s.sort ASC");

        sb.append(" LIMIT ?, ?");
        args.add(sqc.getOffset());
        args.add(sqc.getSize());
        argsType.add(Types.INTEGER);
        argsType.add(Types.INTEGER);

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }

        log.debug("args: {}", args.toString());
        log.debug("sql: {}", sb.toString());

        List<Shop> list = jdbcTemplate.query(sb.toString(), args.toArray(), arr, new ShopRowMapper());
        return list;
    }

    public int selectCountShops(ShopQueryCondition sqc) {
        String sql = "select count(1) as count from `T_SHOP` s where 1=1";

        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> args = new ArrayList<>();
        ArrayList<Integer> argsType = new ArrayList<>();

        if (sqc.getMall_id() != null && sqc.getMall_id() > 0) {
            sb.append(" AND s.mall_id=?");

            args.add(sqc.getMall_id());
            argsType.add(Types.INTEGER);
        }
        if (!StringUtils.isEmpty(sqc.getKeywords())) {
            sb.append(" AND s.text1 like '%' ? '%'");

            args.add(sqc.getKeywords().trim());
            argsType.add(Types.VARCHAR);
        }
        if (sqc.getMap_id() != null && sqc.getMap_id() > 0) {
            sb.append(" AND s.plane_map=?");

            args.add(sqc.getMap_id());
            argsType.add(Types.INTEGER);
        }
        if (sqc.getIndustry_id() != null && sqc.getIndustry_id() > 0) {
            sb.append(" AND s.industry=?");

            args.add(sqc.getIndustry_id());
            argsType.add(Types.INTEGER);
        }

        int[] arr = new int[argsType.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = argsType.get(i);
        }

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sb.toString(), args.toArray(), arr);
        rowSet.first();

        return rowSet.getInt("count");
    }

    public int insertShop(Shop shop) {
        String sql = "insert into `T_SHOP` (shop_name,phone,mobile,linkman,pictures,logo,berth_number," +
                "map_name,plane_map,industry,industry_name,brand,business_hours,status,sort,intro,mall_id,text1,acronym)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, shop.getShop_name());
                ps.setString(2, shop.getPhone());
                ps.setString(3, shop.getMobile());
                ps.setString(4, shop.getLinkman());
                ps.setString(5, shop.getPictures());
                ps.setString(6, shop.getLogo());
                ps.setString(7, shop.getBerth_number());
                ps.setString(8, shop.getMap_name());
                ps.setInt(9, shop.getPlane_map());
                ps.setInt(10, shop.getIndustry());
                ps.setString(11, shop.getIndustry_name());
                ps.setString(12, shop.getBrand());
                ps.setString(13, shop.getBusiness_hours());
                ps.setInt(14, shop.getStatus());
                ps.setInt(15, shop.getSort());
                ps.setInt(16, shop.getArticle_id());
                ps.setInt(17, shop.getMall_id());
                ps.setString(18, shop.getText1());
                ps.setString(19, shop.getAcronym());
                return ps;
            }
        }, holder);

        int newShopId = holder.getKey().intValue();

        return newShopId;
    }

    public int updateShop(Shop shop) {
        String sql = "update `T_SHOP` set shop_name=?,phone=?,mobile=?,linkman=?,pictures=?,logo=?,berth_number=?," +
                "map_name=?,plane_map=?,industry=?,industry_name=?,brand=?,business_hours=?,status=?,sort=?,acronym=?,text1=? " +
                "where shop_id=?";

        int result = jdbcTemplate.update(sql, shop.getShop_name(), shop.getPhone(), shop.getMobile(), shop.getLinkman(), shop.getPictures(),
                shop.getLogo(), shop.getBerth_number(), shop.getMap_name(), shop.getPlane_map(), shop.getIndustry(), shop.getIndustry_name(),
                shop.getBrand(), shop.getBusiness_hours(), shop.getStatus(), shop.getSort(),shop.getAcronym(),shop.getText1(), shop.getShop_id());

        return result;
    }

    public int deleteShop(final Integer shopId) {

        if (shopId == null || shopId <= 0) return 0;

        String sql = "delete from `T_SHOP` where shop_id=?";

        int result = jdbcTemplate.update(sql, shopId);

        return result;
    }

    class ShopRowMapper implements RowMapper<Shop> {
        @Override
        public Shop mapRow(ResultSet rs, int rowNum) throws SQLException {
            Shop shop = new Shop();

            if (dbUtils.hasColumn(rs, "shop_id")) shop.setShop_id(rs.getInt("shop_id"));

            if (dbUtils.hasColumn(rs, "shop_name")) shop.setShop_name(rs.getString("shop_name"));

            if (dbUtils.hasColumn(rs, "phone")) shop.setPhone(rs.getString("phone"));

            if (dbUtils.hasColumn(rs, "pictures")) shop.setPictures(rs.getString("pictures"));

            if (dbUtils.hasColumn(rs, "status")) shop.setStatus(rs.getInt("status"));

            if (dbUtils.hasColumn(rs, "map_name")) shop.setMap_name(rs.getString("map_name"));

            if (dbUtils.hasColumn(rs, "industry_name")) shop.setIndustry_name(rs.getString("industry_name"));

            if (dbUtils.hasColumn(rs, "plane_map")) shop.setPlane_map(rs.getInt("plane_map"));

            if (dbUtils.hasColumn(rs, "industry")) shop.setIndustry(rs.getInt("industry"));

            if (dbUtils.hasColumn(rs, "content")) shop.setIntro(rs.getString("content"));

            if (dbUtils.hasColumn(rs, "berth_number")) shop.setBerth_number(rs.getString("berth_number"));

            if (dbUtils.hasColumn(rs, "logo")) shop.setLogo(rs.getString("logo"));

            if (dbUtils.hasColumn(rs, "mall_id")) shop.setMall_id(rs.getInt("mall_id"));

            if (dbUtils.hasColumn(rs, "sort")) shop.setSort(rs.getInt("sort"));

            if (dbUtils.hasColumn(rs, "brand")) shop.setBrand(rs.getString("brand"));

            if (dbUtils.hasColumn(rs, "linkman")) shop.setLinkman(rs.getString("linkman"));

            if (dbUtils.hasColumn(rs, "mobile")) shop.setMobile(rs.getString("mobile"));

            if (dbUtils.hasColumn(rs, "business_hours")) shop.setBusiness_hours(rs.getString("business_hours"));

            if (dbUtils.hasColumn(rs, "intro")) shop.setArticle_id(rs.getInt("intro"));

            return shop;
        }
    }

}
