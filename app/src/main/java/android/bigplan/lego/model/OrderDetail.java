package android.bigplan.lego.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gg on 16-5-12.
 */
public class OrderDetail implements Serializable {
    private String DemId;
    private String DemTitle;
    private String Type;
    private String MemberId;
    private String StaffId;
    private String State;
    private String StartAddr;
    private String Hour;
    private String EndAddr;
    private String Name;
    private String Mobile;
    private String Explain;
    private String Graphic;
    private String TagName;
    private String Valuation;
    private String RealVal;
    private String CreateTime;
    private int LeftTime;

    private String MyAddr;
    private String MyMobile;
    private String MyName;

    public String getStyle() {
        return Style;
    }

    public void setStyle(String style) {
        Style = style;
    }

    private String Style;

    private String TimeAbolish;
    private String TimeComplete;
    private String IsHandled;
    private String StateExplain;
    private Object StaffInfo;


    public Object getStaffInfo() {
        return StaffInfo;
    }

    public void setStaffInfo(List<?> staffInfo) {
        StaffInfo = staffInfo;
    }


    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDemId() {
        return DemId;
    }

    public void setDemId(String demId) {
        DemId = demId;
    }

    public String getDemTitle() {
        return DemTitle;
    }

    public void setDemTitle(String demTitle) {
        DemTitle = demTitle;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getStaffId() {
        return StaffId;
    }

    public void setStaffId(String staffId) {
        StaffId = staffId;
    }

    public String getStartAddr() {
        return StartAddr;
    }

    public void setStartAddr(String startAddr) {
        StartAddr = startAddr;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public String getEndAddr() {
        return EndAddr;
    }

    public void setEndAddr(String endAddr) {
        EndAddr = endAddr;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getExplain() {
        return Explain;
    }

    public void setExplain(String explain) {
        Explain = explain;
    }

    public String getGraphic() {
        return Graphic;
    }

    public void setGraphic(String graphic) {
        Graphic = graphic;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String tagName) {
        TagName = tagName;
    }

    public String getValuation() {
        return Valuation;
    }

    public void setValuation(String valuation) {
        Valuation = valuation;
    }

    public String getRealVal() {
        return RealVal;
    }

    public void setRealVal(String realVal) {
        RealVal = realVal;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getLeftTime() {
        return LeftTime;
    }

    public void setLeftTime(int leftTime) {
        LeftTime = leftTime;
    }

    public String getMyAddr() {
        return MyAddr;
    }

    public void setMyAddr(String myAddr) {
        MyAddr = myAddr;
    }

    public String getMyMobile() {
        return MyMobile;
    }

    public void setMyName(String myAddr) {
        MyName = myAddr;
    }

    public String getMyName() {
        return MyName;
    }


    public void setMyMobile(String myMobile) {
        MyMobile = myMobile;
    }

    public String getTimeAbolish() {
        return TimeAbolish;
    }

    public void setTimeAbolish(String timeAbolish) {
        TimeAbolish = timeAbolish;
    }

    public String getTimeComplete() {
        return TimeComplete;
    }

    public void setTimeComplete(String timeComplete) {
        TimeComplete = timeComplete;
    }

    public String getIsHandled() {
        return IsHandled;
    }

    public void setIsHandled(String isHandled) {
        IsHandled = isHandled;
    }

    public String getStateExplain() {
        return StateExplain;
    }

    public void setStateExplain(String stateExplain) {
        StateExplain = stateExplain;
    }
}
