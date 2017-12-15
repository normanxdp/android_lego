package android.bigplan.lego.model;

import android.bigplan.lego.db.orm.annotation.Column;
import android.bigplan.lego.db.orm.annotation.Id;
import android.bigplan.lego.db.orm.annotation.Table;
import android.text.TextUtils;

import java.util.List;

@Table(name = "user")
public class User{
    // ID @Id主键,int类型,数据库建表时此字段会设为自增长
    @Id
    @Column(name = "_id")
    private int _id;

    //用户id
    @Column(name = "MemberId")
    private String MemberId;
    
    // 密码
    @Column(name = "PassId")
    private String PassId;

    // 手机号码
    @Column(name = "Mobile")
    private String Mobile;

    //头像
    @Column(name = "Avatar")
    private String Avatar;

    //性别
    @Column(name = "Sex")
    private String Sex;
    
    // 真实名字
    @Column(name = "RealName")
    public String RealName;

    // 昵称
    @Column(name = "Nickname")
    public String Nickname;

    // 商店名称
    @Column(name = "StoreName")
    public String StoreName;


    // 首字母
    @Column(name = "FirstLetter")
    public String FirstLetter;

    // QQ号
    @Column(name = "QQ")
    public String QQ;

    // 微信号
    @Column(name = "WeChatId")
    public String WeChatId;


    @Column(name = "Introduce")
    public String Introduce;

    @Column(name = "Introduction")
    public String Introduction;

    // 状态
    @Column(name = "State")
    private String State;

    // 创建时间
    @Column(name = "CreateTime")
    private String CreateTime;

    // 是否懒人员工
    @Column(name = "IsLazyStaff")
    private String IsLazyStaff;


    // 支付密码
    @Column(name = "PayPwd")
    private String PayPwd;

    // 评分
    @Column(name = "Score")
    private String Score;

    // 评分
    @Column(name = "BankCount")
    private String BankCount;

    @Column(name = "PetroChinaAccountCount")
    private String PetroChinaAccountCount;

    @Column(name = "SinopecAccountCount")
    private String SinopecAccountCount;

    @Column(name = "OilMoney")
    private String OilMoney;

    @Column(name = "YMobile")
    private String YMobile;

    @Column(name = "YunAccount")
    private String YunAccount;

    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }

    public String getYunAccount(){
        return  YunAccount;
    }
    public void setYunAccount(String account){
        this.YunAccount = account;
    }
    public void setYMobile(String mobile){
        YMobile = mobile;
    }
    public String getYMobile(){
        return YMobile;
    }

    public String getBankCount() {
        if (TextUtils.isEmpty(BankCount)){
            return "0";
        }
        return BankCount;
    }

    public void setBankCount(String bankCount) {

        BankCount = bankCount;
    }

    public void setSinopecAccountCount(String count){
        SinopecAccountCount = count;
    }

    public void setOilMoney(String money){
        OilMoney = money;
    }

    public String getOilMoney(){
        return OilMoney;
    }

    public  void setPetroChinaAccountCount(String count){
        PetroChinaAccountCount = count;
    }

    public  String getPetroChinaAccountCount(){
        return  PetroChinaAccountCount;
    }
    public String getSinopecAccountCount(){
        return  SinopecAccountCount;
    }
    public String getMemberId() {
        if (TextUtils.isEmpty(MemberId)){
            return "0";
        }
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getPassId() {
        return PassId;
    }
    public void setPassId(String passId) {
        PassId = passId;
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

    public String getSex() {
        return Sex;
    }
    public void setSex(String sex) {
        Sex = sex;
    }

    public String getRealName() {
        return RealName;
    }
    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getNickname() {
        return Nickname;
    }
    public void setNickname(String name) {
        Nickname = name;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }
    public String getStoreName() {
        return StoreName;
    }


    public String getFirstLetter() {
        return FirstLetter;
    }
    public void setFirstLetter(String firstLetter) {
        FirstLetter = firstLetter;
    }

    public String getQQ() {
        return QQ;
    }
    public void setQQ(String qq) {
        QQ = qq;
    }

    public String getWeChatId() {
        return WeChatId;
    }
    public void setWeChatId(String weChatId) {
        WeChatId = weChatId;
    }


    public String getIntroduction() {
        return Introduction;
    }
    public void setIntroduction(String introduce) {
        Introduction = Introduction;
    }

    public String getIntroduce() {
        return Introduce;
    }
    public void setIntroduce(String introduce) {
        Introduce = introduce;
    }

    public String getState() {
        return State;
    }
    public void setState(String state) {
        State = state;
    }

    public String getCreateTime() {
        return CreateTime;
    }
    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getIsLazyStaff() {
        return IsLazyStaff;
    }
    public void setIsLazyStaff(String isLazyStaff) {
        IsLazyStaff = isLazyStaff;
    }

    public String getPayPwd() {
        return PayPwd;
    }
    public void setPayPwd(String payPwd) {
        PayPwd = payPwd;
    }


    public String getShowName(){
        String strName = "";
        if (IsLazyStaff.equals("1")){
            strName = getStoreName();
            if (!TextUtils.isEmpty(strName)){
                return strName;
            }
            strName = getRealName();

            if (!TextUtils.isEmpty(strName)){
                return strName;
            }

            return getNickname();
        }

        strName = getNickname();
        if (!TextUtils.isEmpty(strName)){
            return strName;
        }
        strName = getRealName();
        if (!TextUtils.isEmpty(strName)){
            return strName;
        }
        return getMemberId();
    }

    public void setShowName(String name){
        if (IsLazyStaff.equals("1")){
            setStoreName(name);
        }else{
            setNickname(name);
        }
    }

    @Override
    public String toString() {
        return "User [_id=" + _id +
                ", MemberId=" + MemberId +
                ", PassId=" + PassId +
                ", Mobile=" + Mobile +
                ", Avatar=" + Avatar +
                ", Sex=" + Sex +
                ", RealName=" + RealName +
                ", Nickname=" + Nickname +
                ", StoreName=" + StoreName +
                ", FirstLetter=" + FirstLetter +
                ", QQ=" + QQ +
                ", WeChatId=" + WeChatId +
                ", Introduce=" + Introduce +
                ", State=" + State +
                ", CreateTime=" + CreateTime +
                ", IsLazyStaff=" + IsLazyStaff +
                ", PayPwd=" + PayPwd +
                ", Score=" + Score +
                ", BankCount=" + BankCount +
                ", PetroChinaAccountCount=" + PetroChinaAccountCount +
                ", SinopecAccountCount=" + SinopecAccountCount +
                ", OilMoney=" + OilMoney +
                ", YMobile=" + YMobile +
                ", YunAccount=" + YunAccount +
                "]";
    }
}
