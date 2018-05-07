package com.laf.mall.api.repository;

import com.laf.mall.api.dto.ReceiveCouponInfo;
import com.laf.mall.api.dto.Shop;
import com.laf.mall.api.querycondition.coupon.CouponQueryCondition;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CouponRepositoryTest {

    @Autowired
    CouponRepository repository;

    @Autowired
    JdbcTemplate template;

    @Autowired
    DateTimeUtils timeUtils;

    @Test
    public void selectCouponInfoList() throws Exception {
        CouponQueryCondition condition = new CouponQueryCondition();
        condition.setMallId(10);
//        condition.setReceiveMethod(0);
        List<ReceiveCouponInfo> list = repository.selectCouponInfoList(condition, 0);
        Assert.assertNotEquals(0, list.size());
        log.info("size is {}", list.size());
        for (ReceiveCouponInfo info : list) {
            log.info("{}", info.getCoupon_name());
        }
    }

    @Test
    public void selectCouponListByMember() throws Exception {
    }

    @Test
    public void selectCouponById() throws Exception {
    }

    @Test
    public void updateCouponReceiveLog() throws Exception {
    }

    @Test
    public void insertCouponInfoList() throws Exception {
        List<ReceiveCouponInfo> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ReceiveCouponInfo info = new ReceiveCouponInfo();
            info.setCoupon_name("券" + (i + 1));

            LocalDate date = LocalDate.of(2017, Month.of(10), 1); //10月1日创建
            long createTime = timeUtils.getMilliWithoutTime(date);
            info.setCreate_date(new Date(createTime));

            info.setStatus(1);
//有效期 10月10日 -- 11月10日
            long ExpiryStartTime = timeUtils.getMilliWithoutTime(LocalDate.of(2017, Month.of(11), 2));
            info.setExpiry_date_start(new Date(ExpiryStartTime));
            long ExpiryEndTime = timeUtils.getMilliWithoutTime(LocalDate.of(2017, Month.of(11), 30));
            info.setExpiry_date_end(new Date(ExpiryEndTime));

            info.setCirculation(1000);
            info.setDaily_circulation(100);
            info.setVerification_type(2);
            info.setMall_id(10);
            info.setCoupon_type(0);
//发行期 10月5日 -- 10月31日
            long issue_start = timeUtils.getMilliWithoutTime(LocalDate.of(2017, Month.of(10), 31));
            info.setIssue_start(new Date(issue_start));
            long issue_end = timeUtils.getMilliWithoutTime(LocalDate.of(2017, Month.of(11), 28));
            info.setIssue_end(new Date(issue_end));

            info.setLimited(10);
            info.setDaily_limited(4);
            info.setPrice(new BigDecimal(50.0 + (i + 5)));
            info.setRequired_points(50 + (i + 5));

            long reg_time_start = timeUtils.getMilliWithoutTime(LocalDate.of(1900, Month.of(1), 1));
            info.setReg_time_start(new Date(reg_time_start));
            long reg_time_end = timeUtils.getMilliWithoutTime(LocalDate.of(2100, Month.of(1), 1));
            info.setReg_time_end(new Date(reg_time_end));

            info.setSort_id(i + 1);
            info.setReceive_method(0);
            info.setKeep_verification_of(2);
            info.setGroup_id(i % 2 == 0 ? 0 : 1);
            info.setUse_condition(info.getPrice());
            info.setText1(info.getCoupon_name());
            info.setInventory(1020);
            list.add(info);
        }

        int result = insertList(list);
        Assert.assertTrue(result != 0);
        log.info("rows ====== {}", result);
    }

    int insertList(List<ReceiveCouponInfo> info) {
        String sql = "insert into `t_coupon_info` (coupon_name, create_date, status,expiry_date_start,expiry_date_end," +
                "circulation,daily_circulation,verification_type,mall_id,coupon_type,issue_start,issue_end,limited,daily_limited," +
                "price,required_points,reg_time_start,reg_time_end,sort_id,receive_method,keep_verification_of,group_id,use_condition,text1)" +
                " values ";
        StringBuilder sb = new StringBuilder(sql);
        for (int i = 0; i < info.size(); i++) {
            sb.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?),");
        }
        String newSql = sb.substring(0, sb.length() - 1);

        log.info("newSql ? {}", newSql);

        int result = template.update(newSql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int index = 1;
                for (int i = 0; i < info.size(); i++) {
                    ReceiveCouponInfo couponInfo = info.get(i);
                    ps.setString(index++, couponInfo.getCoupon_name());
                    ps.setLong(index++, couponInfo.getCreate_date().getTime());
                    ps.setInt(index++, couponInfo.getStatus());
                    ps.setLong(index++, couponInfo.getExpiry_date_start().getTime());
                    ps.setLong(index++, couponInfo.getExpiry_date_end().getTime());
                    ps.setInt(index++, couponInfo.getCirculation());
                    ps.setInt(index++, couponInfo.getDaily_circulation());
                    ps.setInt(index++, couponInfo.getVerification_type());
                    ps.setInt(index++, couponInfo.getMall_id());
                    ps.setInt(index++, couponInfo.getCoupon_type());
                    ps.setLong(index++, couponInfo.getIssue_start().getTime());
                    ps.setLong(index++, couponInfo.getIssue_end().getTime());
                    ps.setInt(index++, couponInfo.getLimited());
                    ps.setInt(index++, couponInfo.getDaily_limited());
                    ps.setBigDecimal(index++, couponInfo.getPrice());
                    ps.setInt(index++, couponInfo.getRequired_points());
                    ps.setLong(index++, couponInfo.getReg_time_start().getTime());
                    ps.setLong(index++, couponInfo.getReg_time_end().getTime());
                    ps.setInt(index++, couponInfo.getSort_id());
                    ps.setInt(index++, couponInfo.getReceive_method());
                    ps.setInt(index++, couponInfo.getKeep_verification_of());
                    ps.setInt(index++, couponInfo.getGroup_id());
                    ps.setBigDecimal(index++, couponInfo.getUse_condition());
                    ps.setString(index++, couponInfo.getText1());
                }
            }
        });

        return result;
    }
}