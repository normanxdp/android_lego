package android.bigplan.lego.model;

/**
 * Created by gg on 16-2-19.
 */
import java.io.Serializable;

public class GameTeam implements Serializable {

    private int GameTeamId;// 战队ID

    private String GameTeamName;// 战队名称

    private String LogoSmall;// LOGO

    private String MemberAmount;// 成员数量

    private String ActivityAmount;// 上线人数数量

    private String Motto; //格言

    private String CurrMemberId; //当前成员ID  以分号分隔 一个战队最多6名成员

    private String CaptainId; //队长编号

    private String CaptainName; //队长昵称

    public int getGameTeamId() {
        return GameTeamId;
    }

    public void setGameTeamId(int gameTeamId) {
        GameTeamId = gameTeamId;
    }

    public String getGameTeamName() {
        return GameTeamName;
    }

    public void setGameTeamName(String gameTeamName) {
        GameTeamName = gameTeamName;
    }

    public String getLogoSmall() {
        return LogoSmall;
    }

    public void setLogoSmall(String logoSmall) {
        LogoSmall = logoSmall;
    }

    public String getMemberAmount() {
        return MemberAmount;
    }

    public void setMemberAmount(String memberAmount) {
        MemberAmount = memberAmount;
    }

    public String getActivityAmount() {
        return ActivityAmount;
    }

    public void setActivityAmount(String activityAmount) {
        ActivityAmount = activityAmount;
    }

    public String getMotto() {
        return Motto;
    }

    public void setMotto(String motto) {
        Motto = motto;
    }

    public String getCaptainName() {
        return CaptainName;
    }

    public void setCaptainName(String captainName) {
        CaptainName = captainName;
    }

    public String getCaptainId() {

        return CaptainId;
    }

    public void setCaptainId(String captainId) {
        CaptainId = captainId;
    }

    public String getCurrMemberId() {

        return CurrMemberId;
    }

    public void setCurrMemberId(String currMemberId) {
        CurrMemberId = currMemberId;
    }
}
