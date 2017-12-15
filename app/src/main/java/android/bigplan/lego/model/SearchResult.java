package android.bigplan.lego.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by gg on 17-9-16.
 */

public class SearchResult implements Serializable {

    private String MemberId;
    private String RealName;
    private String StoreName;
    private String Introduction;
    private String Star;
    private String Mobile;
    private String Longitude;
    private String Latitude;
    private String Avatar;
    private String Type;
    private String Distance;
    private String Address;
    private String MobileReserve;

    public String getMemberId() {
        return MemberId;
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
        return RealName;
    }
    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public String getMobileReserve(){
        if (!TextUtils.isEmpty(MobileReserve)){
            return  MobileReserve;
        }
        return  Mobile;
    }

    public void setMobileReserve(String mobile){
        MobileReserve = mobile;
    }

    public String getAddress(){
        return  Address;
    }
    public void setAddress(String address){
        Address = address;
    }

    public void setIntroduction(String introduction) {
        Introduction = introduction;
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

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
    public void setDistance(String distance){
        this.Distance = distance;
    }
    public String getDistance(){
        return this.Distance;
    }
}
