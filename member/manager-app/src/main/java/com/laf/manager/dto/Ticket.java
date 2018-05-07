package com.laf.manager.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ticket {
    public interface TicketAppView {};
    public interface TicketAllView extends TicketAppView {};

    @JsonView(TicketAppView.class)
    private int ticket_id;

    private int member_id;

    private String member_name;

    private String member_mobile;

    @JsonView(TicketAppView.class)
    private Date upload_date;

    /**
     * 工作人员(审核员)姓名
     */
    private String vc_name;

    /**
     * 工作人员(审核员)电话(手机)
     */
    private String vc_phone;

    /**
     * 处理时间
     */
    private Date handle_date;

    /**
     * 处理的回复
     */
    @JsonView(TicketAppView.class)
    private String responses;

    @JsonView(TicketAppView.class)
    private int handle_status;

    /**
     * 票据编号
     */
    private String ticket_no;

    /**
     * 消费商户id
     */
    private int shop_id;

    private String shop_name;

    /**
     * 消费时间
     */
    private Date shopping_date;

    /**
     * 消费金额
     */
    private BigDecimal amounts;

    private int mall_id;

    /**
     * 相关工作人员id
     */
    private int vc_id;

    private int points;

    /**
     * 小票图片地址
     */
    @JsonView(TicketAppView.class)
    private String file_url;

    public long getUpload_date() {
        if (this.upload_date == null) return 0L;

        return this.upload_date.getTime();
    }

    public long getHandle_date() {
        if (this.handle_date == null) return 0L;

        return this.handle_date.getTime();
    }

    public long getShopping_date() {
        if (this.shopping_date == null) return 0L;

        return this.shopping_date.getTime();
    }
}
