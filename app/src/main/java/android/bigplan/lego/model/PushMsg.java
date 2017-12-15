package android.bigplan.lego.model;

import java.io.Serializable;

import android.bigplan.lego.db.orm.annotation.Column;
import android.bigplan.lego.db.orm.annotation.Id;
import android.bigplan.lego.db.orm.annotation.Table;

@Table(name = "PushMsg")
public class PushMsg implements Serializable{

    // ID @Id主键,int类型,数据库建表时此字段会设为自增长
    @Id
    @Column(name = "_id")
    private int _id;

    @Column(name = "Id")
    private String Id;

    @Column(name = "MemberId")
    private String MemberId;
    /**
     * 发布主体的ID
     */
    @Column(name = "InitiatorId")
    private int InitiatorId;

    @Column(name = "Time")
    private String Time;

    @Column(name = "TopTime")
    private String TopTime;

    @Column(name = "ObjId")
    private String ObjId;

    @Column(name = "Type")
    private String Type;

    @Column(name = "Title")
    private String Title;

    @Column(name = "SubTitle")
    private String SubTitle;

    @Column(name = "Pic")
    private String Pic;

    @Column(name = "Amount")
    private int Amount;

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        this.MemberId = memberId;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getObjId() {
        return ObjId;
    }

    public void setObjId(String objId) {
        ObjId = objId;
    }

    public String getTopTime() {
        return TopTime;
    }

    public void setTopTime(String topTime) {
        TopTime = topTime;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubtitle() {
        return SubTitle;
    }

    public void setSubtitle(String subtitle) {
        SubTitle = subtitle;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getInitiatorId() {
        return InitiatorId;
    }

    public void setInitiatorId(int initiatorId) {
        InitiatorId = initiatorId;
    }



}
