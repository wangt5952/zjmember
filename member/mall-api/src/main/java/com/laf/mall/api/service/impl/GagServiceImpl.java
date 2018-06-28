package com.laf.mall.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laf.mall.api.YmlConfig;
import com.laf.mall.api.dao.LevelDao;
import com.laf.mall.api.dao.MemberDao;
import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.points.PromotionQueryCondition;
import com.laf.mall.api.service.GagService;
import com.laf.mall.api.service.PointsService;
import com.laf.mall.api.service.SettingsService;
import com.laf.mall.api.service.ShopService;
import com.laf.mall.api.utils.HttpGetUtil;
import com.laf.mall.api.utils.SignUtils;
import com.laf.manager.core.exception.MallDBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

@Service
public class GagServiceImpl implements GagService {

    private static final int DEF_DIV_SCALE = 0;

    @Autowired
    private YmlConfig config;

    @Autowired
    MemberDao memberDao;

    @Autowired
    LevelDao levelDao;

    @Autowired
    ShopService shopService;

    @Autowired
    PointsService pointsService;

    @Autowired
    SettingsService settingsService;

    public GagBill getBill(String qrCode){
        //获取签名
        Map<String, String> hm = new HashMap<String, String>();
        hm.put("method", "gogo.bill.amount.query");
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(d);
        hm.put("timestamp", time);
        hm.put("messageFormat", "json");
        //String appKey = "8688458140778369";
        String appKey = config.getGouagou().get("appkey");
        hm.put("appKey",appKey);
        hm.put("v", "1.0");
        hm.put("signMethod", "MD5");
        qrCode = config.getGouagou().get("qrurl") + qrCode;
        hm.put("qrCode", qrCode);
        String appSecret = config.getGouagou().get("appSecret");
        String sign = SignUtils.getSign(appSecret, hm, "MD5");
        StringBuilder sb = new StringBuilder("method=gogo.bill.amount.query");
        sb.append("&timestamp=").append(time);
        sb.append("&messageFormat=json");
        sb.append("&appKey=").append(appKey);
        sb.append("&v=1.0");
        sb.append("&signMethod=MD5");
        sb.append("&qrCode=").append(qrCode);
        sb.append("&sign=").append(sign);
        StringBuilder url = new StringBuilder(config.getGouagou().get("url"));
        String result = HttpGetUtil.getHttp(url.append("?").append(sb).toString());
        GagBill bill = null;
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            bill = objectMapper.readValue(result, GagBill.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bill;
    }

    public int uploadBill(int memberId, String qrCode){
        GagBill bill = this.getBill(qrCode);
        Member member = memberDao.getMemberById(memberId);
        if (member == null) return 0;
        int result = 0;
        if (bill.getRescode().equals("OPEN_SUCCESS")) {
            String shopname =  bill.getMerchantName();
            Shop shop = shopService.getShopDetail(bill.getMerchantCode());
            if (shop == null)
                shop = shopService.getShopDetail(shopname);
            if (shop == null) return 0;

            Points pointslog = pointsService.getPointsDetailByTickNo(bill.getUuid());
            if(pointslog != null) return -1;//已经存在兑换记录

            PromotionQueryCondition condition = new PromotionQueryCondition();
            condition.setLevelId(Integer.valueOf(member.getLevel_id()));
            condition.setShopId(Integer.valueOf(shop.getShop_id()));
            condition.setBirthday(member.getBirthday());
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try{
                date = sdf.parse(sdf.format(date) +  " 00:00:00.0000");
            }catch (Exception ex){
                ex.printStackTrace();
            }
            condition.setToday(date.getTime());
            BigDecimal amount = pointsService.getPromotionAmount(condition);

            if (amount == null)
                amount = pointsService.getSimpleAmount(Integer.valueOf(member.getLevel_id()), Integer.valueOf(shop.getShop_id()));
            if (amount == null) return 0;
            System.out.println(amount);
            BigDecimal point = bill.getAmount().divide(amount, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
            System.out.println(point);
            if(point.equals(BigDecimal.ZERO)) return 0;

            //更新会员的累计消费,重新计算会员等级
            BigDecimal newCumulateAmount = bill.getAmount().add(member.getCumulate_amount());
            result = memberDao.updateCumulateAmount(memberId, newCumulateAmount);

            if (result <= 0) throw new MallDBException();

            int upgrade = 0; // 升级奖励

            int newCumulatePoints = member.getCumulate_points();
            int newUseablePoints = member.getUsable_points();

            List<Level> list = levelDao.getLevelWidthAmount(newCumulateAmount);
            Level level = list.get(0);
            if (level != null && level.getLevel_id() != condition.getLevelId()) {
                result = memberDao.updateLevel(memberId, level.getLevel_id(), level.getLevel_name());
                upgrade = settingsService.getPointsByLevel(level.getLevel_id());
            }

            if (result <= 0) throw new MallDBException();

            int pointsValue = point.intValue();
            if (pointsValue > 0) {
                // 乘以系数 四舍五入 取整
                float coefficient = settingsService.getPointsCoefficient(1);
                float coefficientPoints = pointsValue * coefficient;
                pointsValue = Math.round(coefficientPoints);

                newCumulatePoints += pointsValue;
                newUseablePoints += pointsValue;
                result = memberDao.updatePoints(memberId, newCumulatePoints, newUseablePoints);

                if (result <= 0) throw new MallDBException();

                Points points = new Points();
                points.setMember_id(member.getMember_id());
                points.setMember_mobile(member.getMobile());
                points.setMember_name(member.getName());
                points.setVc_id(member.getMember_id()); // TODO: 2017/11/12 获取操作人员的信息
                points.setVc_name(member.getName());
                points.setHandle_date(new Date());
                points.setAmount(bill.getAmount().negate());
                points.setPoints(pointsValue);
                points.setMall_id(1);
                points.setShop_id(condition.getShopId());
                points.setShop_name(shop.getShop_name());
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
                try{
                    points.setShopping_date(sdf2.parse(bill.getSaleTime()));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                points.setTicket_no(bill.getUuid());
                points.setSources(1);
                result = pointsService.savePoints(points);

                if (result <= 0) throw new MallDBException();
            }

            if (upgrade > 0) {
                newCumulatePoints += upgrade;
                newUseablePoints += upgrade;
                result = memberDao.updatePoints(memberId, newCumulatePoints, newUseablePoints);

                if (result <= 0) throw new MallDBException();

                Points points = new Points();
                points.setMember_id(member.getMember_id());
                points.setMember_mobile(member.getMobile());
                points.setMember_name(member.getName());
                points.setHandle_date(new Date());
                points.setAmount(new BigDecimal(0d));
                points.setPoints(upgrade);
                points.setMall_id(1);
                points.setSources(9);
                result = pointsService.savePoints(points);

                if (result <= 0) throw new MallDBException();
            }
            result = pointsValue;

        }
        return result;
    }

    public static void main(String[] args) {
        Points points = new Points();
        BigDecimal d = new BigDecimal("1.02");
        points.setAmount(d.negate());
        System.out.println(points.getAmount());
    }


}
