package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 16-5-11.
 */
public class ApplyCash implements Serializable {
    private String Id;
    private String Money;
    private String State;
    private String StateName;
	private String CreateTime;
    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

	public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }


    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
