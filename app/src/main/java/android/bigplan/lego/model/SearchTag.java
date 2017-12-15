package android.bigplan.lego.model;

import android.bigplan.lego.db.orm.annotation.Column;
import android.bigplan.lego.db.orm.annotation.Id;
import android.bigplan.lego.db.orm.annotation.Table;

import java.io.Serializable;

/**
 * Created by gg on 17-9-14.
 */

@Table(name = "SearchHistoryTag")
public class SearchTag implements Serializable {
    @Id
    @Column(name = "_id")
    private int _id;

    @Column(name = "KeyWords")
    private String KeyWords;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getKeyWords() {
        return KeyWords;
    }

    public void setKeyWords(String keyWords) {
        KeyWords = keyWords;
    }

    @Override
    public String toString() {
        return "SearchTag [_id=" + _id + ", KeyWords="
                + KeyWords + "]";
    }
}
