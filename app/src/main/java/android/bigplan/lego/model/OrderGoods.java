package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * 订单的超市-商品
 * Created by FingerArt on 16/6/17.
 */
public class OrderGoods implements Serializable {
    private String name;
    private int amount = 1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderGoods{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
