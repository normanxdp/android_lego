package android.bigplan.lego.model;

import java.io.Serializable;
import java.util.List;

public class Demand implements Serializable{
    
    private int DemId;// 商业需求标识编号
    
    private String DemTitle;// 需求标题
    
    private String CreateTime;// 需求发起时间
    
    private String AllyId;
    
    private int MemberId;
    
    private int SecId;
    
    private int AuditState;
    
    private int IsRelease;
    
    private int IsAbolish;
    
    private String Explain;
    
    private int AllyRole;
    
    private String Avatar;
    
    private String RealName;
    
    private String CompanyDuty;
    
    private String CompanyName;
    
    private int KpiJoin;
    
    private int KpiActive;
    

    /** 需求@的会员列表 */
    private List<Friend> AssignList;
    
    /** 需求标签 */
    private List<DemandTag> TagList;
    
    /** 最佳商友 */
    private List<Friend> BestAssignList;
    
    /** 状态的文字描述 */
    private String StateExplain;
    
    /** 商业总结文字 */
    private String Summary;
    
    /** 商业估值 */
    private int Valuation;
    
    /**完成状态，0代表未完成，1代表已完成*/
    private int IsComplete;
    
    /**图文混排内容*/
    private String Specify;
    
    
    
    public String getSpecify() {
		return Specify;
	}

	public void setSpecify(String specify) {
		Specify = specify;
	}

	public int getDemId() {
        return DemId;
    }
    
    public void setDemId(int demId) {
        DemId = demId;
    }
    
    public String getDemTitle() {
        return DemTitle;
    }
    
    public void setDemTitle(String demTitle) {
        DemTitle = demTitle;
    }
    
    public String getCreateTime() {
        return CreateTime;
    }
    
    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
    
    public String getAllyId() {
        return AllyId;
    }
    
    public void setAllyId(String allyId) {
        AllyId = allyId;
    }
    
    public int getMemberId() {
        return MemberId;
    }
    
    public void setMemberId(int memberId) {
        MemberId = memberId;
    }
    
    public int getSecId() {
        return SecId;
    }
    
    public void setSecId(int secId) {
        SecId = secId;
    }
    
    public int getAuditState() {
        return AuditState;
    }
    
    public void setAuditState(int auditState) {
        AuditState = auditState;
    }
    
    public int getIsRelease() {
        return IsRelease;
    }
    
    public void setIsRelease(int isRelease) {
        IsRelease = isRelease;
    }
    
    public int getIsAbolish() {
        return IsAbolish;
    }
    
    public void setIsAbolish(int isAbolish) {
        IsAbolish = isAbolish;
    }
    
    public String getExplain() {
        return Explain;
    }
    
    public void setExplain(String explain) {
        Explain = explain;
    }
    
    public int getAllyRole() {
        return AllyRole;
    }
    
    public void setAllyRole(int allyRole) {
        AllyRole = allyRole;
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
    
    public String getCompanyDuty() {
        return CompanyDuty;
    }
    
    public void setCompanyDuty(String companyDuty) {
        CompanyDuty = companyDuty;
    }
    
    public String getCompanyName() {
        return CompanyName;
    }
    
    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
    
    public int getKpiJoin() {
        return KpiJoin;
    }
    
    public void setKpiJoin(int kpiJoin) {
        KpiJoin = kpiJoin;
    }
    
    public int getKpiActive() {
        return KpiActive;
    }
    
    public void setKpiActive(int kpiActive) {
        KpiActive = kpiActive;
    }

    public int getValuation() {
        return Valuation;
    }

    public void setValuation(int valuation) {
        Valuation = valuation;
    }



    public List<Friend> getAssignList() {
        return AssignList;
    }
    
    public void setAssignList(List<Friend> assignList) {
        AssignList = assignList;
    }
    
    public List<DemandTag> getTagList() {
        return TagList;
    }
    
    public void setTagList(List<DemandTag> tagList) {
        TagList = tagList;
    }
    
    public List<Friend> getBestAssignList() {
        return BestAssignList;
    }
    
    public void setBestAssignList(List<Friend> bestAssignList) {
        BestAssignList = bestAssignList;
    }
    
    public String getStateExplain() {
        return StateExplain;
    }
    
    public void setStateExplain(String stateExplain) {
        StateExplain = stateExplain;
    }
    
    
    
    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public class Assign{
        private String CreateTime;// 的时间
        
        private String ReactState;// 被@会员对该需求的外理状态，0代表未处理，1代表同意帮助，2代表帮助不了
        
        private String IsSecAssign;// 是谁操作的@会员，0代表会员自己@，1代表秘书协助会员@
        
        private String Avatar;// 被@会员的头像
        
        private String RealName;// 被@会员名称
        
        public String getCreateTime() {
            return CreateTime;
        }
        
        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }
        
        public String getReactState() {
            return ReactState;
        }
        
        public void setReactState(String reactState) {
            ReactState = reactState;
        }
        
        public String getIsSecAssign() {
            return IsSecAssign;
        }
        
        public void setIsSecAssign(String isSecAssign) {
            IsSecAssign = isSecAssign;
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
        
    }
    
    public class DemandTag implements Serializable{
        private String TagName;
        
        public String getTagName() {
            return TagName;
        }
        
        public void setTagName(String tagName) {
            TagName = tagName;
        }
        
    }

    public int getIsComplete() {
        return IsComplete;
    }

    public void setIsComplete(int isComplete) {
        IsComplete = isComplete;
    }
    
    
    
}
