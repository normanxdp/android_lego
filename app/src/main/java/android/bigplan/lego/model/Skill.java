package android.bigplan.lego.model;

import android.bigplan.lego.db.orm.annotation.Column;
import android.bigplan.lego.db.orm.annotation.Table;

import java.io.Serializable;

/**
 * Created by gg on 16-4-27.
 */

@Table(name = "Skill")
public class Skill implements Serializable {
    // ID @Id主键,int类型,数据库建表时此字段会设为自增长
    @android.bigplan.lego.db.orm.annotation.Id
    @Column(name = "_id")
    private int _id;

    @Column(name = "Id")
    private int Id;

    @Column(name = "IsPublic")
    private String IsPublic;

    @Column(name = "Name")
    private String Name;

    @Column(name = "Explain")
    private String Explain;

    public String getSkillId() {
        return SkillId;
    }

    public void setSkillId(String skillId) {
        SkillId = skillId;
    }

    private String SkillId;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIsPublic() {
        return IsPublic;
    }

    public void setIsPublic(String isPublic) {
        IsPublic = isPublic;
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

    @Override
    public String toString() {
        return "Skill [_id=" + _id +
                ", Id=" + Id +
                ", IsPublic=" + IsPublic +
                ", Name=" + Name +
                ", Explain=" + Explain +
                "]";
    }
}
