package android.bigplan.lego.model;

import java.io.Serializable;
import java.util.List;


public class MyShop implements Serializable {


    private String Avatar;
    private String CityCode;
    private String HandPic;
    private String IdCard;
    private String IdCardBackPic;
    private String IdCardFrontPic;
    private String Latitude;
    private String Longitude;
    private String MScore;
    private String MStar;
    private String MemberId;
    private String Mobile;
    private String RealName;
    private String Score;
    private String Star;
    private String State;
    private String UpperPic;
    private List<Goods> Goods;

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getHandPic() {
        return HandPic;
    }

    public void setHandPic(String handPic) {
        HandPic = handPic;
    }

    public String getIdCard() {
        return IdCard;
    }

    public void setIdCard(String idCard) {
        IdCard = idCard;
    }

    public String getIdCardBackPic() {
        return IdCardBackPic;
    }

    public void setIdCardBackPic(String idCardBackPic) {
        IdCardBackPic = idCardBackPic;
    }

    public String getIdCardFrontPic() {
        return IdCardFrontPic;
    }

    public void setIdCardFrontPic(String idCardFrontPic) {
        IdCardFrontPic = idCardFrontPic;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getMScore() {
        return MScore;
    }

    public void setMScore(String MScore) {
        this.MScore = MScore;
    }

    public String getMStar() {
        return MStar;
    }

    public void setMStar(String MStar) {
        this.MStar = MStar;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getStar() {
        return Star;
    }

    public void setStar(String star) {
        Star = star;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getUpperPic() {
        return UpperPic;
    }

    public void setUpperPic(String upperPic) {
        UpperPic = upperPic;
    }

    public List<android.bigplan.lego.model.Goods> getGoods() {
        return Goods;
    }

    public void setGoods(List<android.bigplan.lego.model.Goods> goods) {
        Goods = goods;
    }
}
