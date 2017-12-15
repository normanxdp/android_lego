package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 16-5-11.
 */
public class Order implements Serializable {

    private String MemberId;
    private String DemId;
    private String StaffId;
    private String DemTitle;
    private String MyName;
    private String Valuation;
    private String CreateTime;
    private String Style;
    private String Tip;
    private String Explain;

    public String getTip(){
        return  Tip;
    }
    public void setTip(String tip){
        Tip = tip;
    }
    public String getStyle() {
        return Style;
    }
    public void setStyle(String style) {
        Style = style;
    }

    private int State;
    private int LeftTime;
    private String StartAddr;
    private String EndAddr;
    private int Type;
    private String TypeExpla;

    public void setStaffId(String staffId){
        StaffId = staffId;
    }
    public String getStaffId(){
        return  StaffId;
    }
    public void setMyName(String explain) {
        MyName = explain;
    }

    public String getMyName() {
        return MyName;
    }

    public String MyMobile;

    public void setMyMobile(String explain) {
        MyMobile = explain;
    }

    public String getMyMobile() {
        return MyMobile;
    }


    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
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

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getExplain() {
        return Explain;
    }

    public void setExplain(String explain) {
        Explain = explain;
    }

    public String getValuation() {
        return Valuation;
    }

    public void setValuation(String valuation) {
        Valuation = valuation;
    }

    public int getLeftTime() {
        return LeftTime;
    }

    public void setLeftTime(int leftTime) {
        LeftTime = leftTime;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getStartAddr() {
        return StartAddr;
    }

    public void setStartAddr(String startAddr) {
        StartAddr = startAddr;
    }

    public String getEndAddr() {
        return EndAddr;
    }

    public void setEndAddr(String endAddr) {
        EndAddr = endAddr;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getTypeExpla() {
        return TypeExpla;
    }

    public void setTypeExpla(String typeExpla) {
        TypeExpla = typeExpla;
    }
}
