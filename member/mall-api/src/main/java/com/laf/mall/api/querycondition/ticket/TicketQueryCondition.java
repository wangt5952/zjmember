package com.laf.mall.api.querycondition.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketQueryCondition {

    private int memberId;

    private int mallId;

    private int page = 1;

    private int size = 5;

    private int offset = 0;

    private String orderBy = "upload_date DESC";

    public TicketQueryCondition(int memberId, int mallId) {
        this.memberId = memberId;
        this.mallId = mallId;
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}
