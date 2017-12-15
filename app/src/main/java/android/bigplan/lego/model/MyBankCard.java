package android.bigplan.lego.model;

import java.io.Serializable;

public class MyBankCard implements Serializable {   
    private String Id;
    private String MemberId;
    private String RealName;
    private String BankType;
    private String BankCard;
	private String Name;
	private String CreateTime;
	private String IsOilMode;
	
	
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
	
    public String getCreateTime() {
        return CreateTime;
    }
    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

	public String getIsOilMode() {
        return IsOilMode;
    }
    public void setIsOilMode(String isOilMode) {
        IsOilMode = isOilMode;
    }
	
    public String getBankCard(){
        return BankCard;
    }
    public void setBankCard(String bankCard){
        BankCard = bankCard;
    }


    public String getRealName() {
        return RealName;
    }
    public void setRealName(String name){
        RealName = name;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }


    public void setBankType(String bankType) {
        BankType = bankType;
    }
    public String getBankType(){
        return BankType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
