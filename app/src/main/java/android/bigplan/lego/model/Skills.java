package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 16-4-27.
 */


public class Skills implements Serializable {


    private String Money;
    private String Name;

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
