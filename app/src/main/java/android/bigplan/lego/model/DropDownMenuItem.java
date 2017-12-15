package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 16-4-24.
 */
public class DropDownMenuItem implements Serializable {

    private int resID;
    private String title;
    private int totalSum;

    public DropDownMenuItem(int res, String str) {
        resID = res;
        title = str;
    }

    public DropDownMenuItem(int res, String str, int distance) {
        resID = res;
        title = str;
        totalSum = distance;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }
}
