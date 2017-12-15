package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 17-9-18.
 */

public class StoreDetail implements Serializable {
    private int MemberId;

    private String Longitude;
    private String Latitude;
    private String CityCode;
    private String IdCard;
    private String UpperPic;
    private String HandPic;
    private String IdCardFrontPic;
    private String IdCardBackPic;
    private int Score;
    private String Star;
    private String Mobile;
    private String Avatar;
    private String RealName;
    private String StoreName;
    private String MScore;
    private String MStar;
    private String MobileReserve;
    private String Distance;

    public String getAddress() {
        return Address;
    }

    public void setMobileReserve(String mobile) {
        MobileReserve = mobile;
    }

    public String getMobileReserve() {
        return MobileReserve;
    }

    public void setAddress(String address) {
        Address = address;
    }
    public void setDistance(String distance){
        Distance = distance;
    }

    public String getDistance(){
        return Distance;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public void setIntroduction(String introduction) {
        Introduction = introduction;
    }
    public String getIsFavoraite(){
        return IsFavoraite;
    }
    public void setIsFavoraite(String isFavoraite){
        IsFavoraite = isFavoraite;
    }

    private String IsFavoraite;
    private String Address;
    private String Introduction;
    private int State;

    public int getMemberId() {
        return MemberId;
    }

    public void setMemberId(int memberId) {
        MemberId = memberId;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getIdCard() {
        return IdCard;
    }

    public void setIdCard(String idCard) {
        IdCard = idCard;
    }

    public String getUpperPic() {
        return UpperPic;
    }

    public void setUpperPic(String upperPic) {
        UpperPic = upperPic;
    }

    public String getHandPic() {
        return HandPic;
    }

    public void setHandPic(String handPic) {
        HandPic = handPic;
    }

    public String getIdCardFrontPic() {
        return IdCardFrontPic;
    }

    public void setIdCardFrontPic(String idCardFrontPic) {
        IdCardFrontPic = idCardFrontPic;
    }

    public String getIdCardBackPic() {
        return IdCardBackPic;
    }

    public void setIdCardBackPic(String idCardBackPic) {
        IdCardBackPic = idCardBackPic;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public String getStar() {
        return Star;
    }

    public void setStar(String star) {
        Star = star;
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

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
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

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }
}
