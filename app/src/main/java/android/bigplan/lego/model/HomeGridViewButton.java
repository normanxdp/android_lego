package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 16-4-12.
 */
public class HomeGridViewButton implements Serializable {
    private String title = null;
    private String iconLink = null;
    private int resDrawable;


    public HomeGridViewButton() {
        title = null;
        iconLink = null;
        resDrawable = 0;
    }

    public HomeGridViewButton(String title, String link, int res) {
        this.title = title;
        this.iconLink = link;
        this.resDrawable = res;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getIconLink(){return  iconLink;}
    public void setIconLink(String link){iconLink = link;}

    public int getResDrawable() {
        return resDrawable;
    }

    public void setResDrawable(int resDrawable) {
        this.resDrawable = resDrawable;
    }
}
