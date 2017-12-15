package android.bigplan.lego.model;

import java.io.Serializable;

import android.bigplan.lego.db.orm.annotation.Column;
import android.bigplan.lego.db.orm.annotation.Id;
import android.bigplan.lego.db.orm.annotation.Table;

/**
 * Created by gg on 16-2-19.
 */
@Table(name = "friend")
public class Friend implements Serializable {

    @Id
    @Column(name = "_id")
    private int _id;

    @Column(name = "userId")
    private String userId;

    //头像
    @Column(name = "avatar")
    private String avatar;

    // 朋友昵称
    @Column(name = "nickName")
    private String nickName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }




}
