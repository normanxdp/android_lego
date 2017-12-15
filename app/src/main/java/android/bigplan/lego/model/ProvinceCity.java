package android.bigplan.lego.model;

import android.bigplan.lego.db.orm.annotation.Column;
import android.bigplan.lego.db.orm.annotation.Id;
import android.bigplan.lego.db.orm.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gg on 16-4-14.
 */

@Table(name = "ProvinceCity")
public class ProvinceCity implements Serializable {

    // ID @Id主键,int类型,数据库建表时此字段会设为自增长
    @Id
    @Column(name = "_id")
    private int _id;

    @Column(name = "Code")
    private String Code;

    @Column(name = "Name")
    private String Name;

    @Column(name = "ParentCode")
    private String ParentCode;

    @Column(name = "FirstLetter")
    private String FirstLetter;

    private List<ProvinceCity> childAreaList;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getParentCode() {
        return ParentCode;
    }

    public void setParentCode(String parentCode) {
        ParentCode = parentCode;
    }

    public String getFirstLetter() {
        return FirstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        FirstLetter = firstLetter;
    }

    public List<ProvinceCity> getChildAreaList() {
        return childAreaList;
    }

    public void setChildAreaList(List<ProvinceCity> childAreaList) {
        this.childAreaList = childAreaList;
    }

    @Override
    public String toString() {
        return "ProvinceCity [_id=" + _id +
                ", Code=" + Code +
                ", Name=" + Name +
                ", ParentCode=" + ParentCode +
                ", FirstLetter=" + FirstLetter +
                "]";
    }
}
