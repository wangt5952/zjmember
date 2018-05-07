package com.laf.manager.querycondition.points;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointsLogFilterCondition {

    private String username = "";

    private String mobile = "";

    private long shoppingDateStart;

    private long shoppingDateEnd;

    private int shop = -1;

    private int sources = -1;

    private BigDecimal amountsStart;

    private BigDecimal amountsEnd;

    private int pointsStart;

    private int pointsEnd;

    private int mallId = 0;

    private int size = 10;

    private int page = 1;

    public BigDecimal getAmountsStart() {
        return amountsStart == null ? new BigDecimal(0D) : amountsStart;
    }

    public BigDecimal getAmountsEnd() {
        return amountsEnd == null ? new BigDecimal(0D) : amountsEnd;
    }

    public long getShoppingDateEnd() {
        return shoppingDateEnd + getTime();
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    private long getTime() {
        long time = (((23 * 60 + 59) * 60) + 59) * 1000;

        return time;
    }
}
