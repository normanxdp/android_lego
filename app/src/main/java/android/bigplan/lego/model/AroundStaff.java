package android.bigplan.lego.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gg on 16-4-12.
 */
public class AroundStaff implements Serializable {

    private String StaffId;
    private String MemberId;
    private String RealName;
    private String Star;
    private String Mobile;
    private String Avatar;
    private String IsPublic;
    private String Id;
    private String Name;
    private String Explain;
    private String Longitude;
    private String Latitude;
    private int Distance;
    private List<Skill> Skill;

    //下面是员工详细信息的字段
    private String CityCode;
    private String IdCard;
    private String UpperPic;
    private String HandPic;
    private String IdCardFront;
    private String IdCardBack;
    private String Score;
    private String Sex;
    private String FirstLetter;
    private String WeChatId;
    private String MScore;
    private String MStar;

    public AroundStaff() {
        Skill = new ArrayList<>();
    }

    public String getStaffId() {
        if (TextUtils.isEmpty(MemberId)){
            return  StaffId;
        }
        return MemberId;
    }

    public void setStaffId(String memberId) {
        StaffId = memberId;

    }

    public String getMemberId() {
        if (TextUtils.isEmpty(MemberId)){
            return  StaffId;
        }
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

    public String getIsPublic() {
        return IsPublic;
    }

    public void setIsPublic(String isPublic) {
        IsPublic = isPublic;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getExplain() {
        return Explain;
    }

    public void setExplain(String explain) {
        Explain = explain;
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

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int distance) {
        Distance = distance;
    }

    public List<Skill> getSkillList() {
        return Skill;
    }

    public void setSkillList(List<Skill> skillList) {
        this.Skill = skillList;
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

    public String getIdCardFront() {
        return IdCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        IdCardFront = idCardFront;
    }

    public String getIdCardBack() {
        return IdCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        IdCardBack = idCardBack;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getFirstLetter() {
        return FirstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        FirstLetter = firstLetter;
    }

    public String getWeChatId() {
        return WeChatId;
    }

    public void setWeChatId(String weChatId) {
        WeChatId = weChatId;
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
}
