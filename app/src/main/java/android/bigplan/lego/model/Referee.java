package android.bigplan.lego.model;

import android.text.TextUtils;
import android.view.View;

import java.io.Serializable;


public class Referee implements Serializable {

    private String MemberId;
    private String RealName;
    private String StoreName;
    private String Nickname;
    private String Mobile;
    private String Avatar;
	private String IsLazyStaff;
    

    public String getMemberId() {
        return MemberId;
    }
    public String getLevel(){
        String levelStr = "普通会员";
        if (IsLazyStaff.equals("1")){
            levelStr = "创业会员";
        }
        return levelStr;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public String getShowName(){
        if (!TextUtils.isEmpty(StoreName)){
            return StoreName;
        }
        if (!TextUtils.isEmpty(RealName)){
            return RealName;
        }
        if (!TextUtils.isEmpty(Nickname)){
            return Nickname;
        }
        return MemberId;
    }
    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getNickname() {
        return Nickname;
    }

    public String getIsLazyStaff(){
        return  IsLazyStaff;
    }
    public void setIsLazyStaff(String isLazyStaff){
        IsLazyStaff = isLazyStaff;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    
    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }



    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

}
