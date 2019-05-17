package com.juniors.miaosha.Status;

/**
 * 订单状态枚举类
 * @author Juniors
 */
public enum OrderStuts {

    NOPAY("新建未支付", 0), PAYDONE("已支付", 1),
    DELIY("已发货", 2), RECE("已收货", 3),
    REFUND("已退款", 4), DONE("已完成", 5);

    private String name;
    private int index;

    private OrderStuts(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;

    }
}
