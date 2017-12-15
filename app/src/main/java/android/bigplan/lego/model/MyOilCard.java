package android.bigplan.lego.model;

import java.io.Serializable;

public class MyOilCard implements Serializable {
    private String Id;
    private String MemberId;
    private String RealName;
    private String CardType;
    private String OilCard;
	private String Name;
	private String CreateTime;
	
	
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
	
    public String getCreateTime() {
        return CreateTime;
    }

    public String getOilCard(){
        return OilCard;
    }
    public String getCardType(){
        return  CardType;
    }
    public void setOilCard(String oilCard){
        OilCard = oilCard;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getRealName() {
        return RealName;
    }
    public void setRealName(String name){
        RealName = name;
    }
    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
