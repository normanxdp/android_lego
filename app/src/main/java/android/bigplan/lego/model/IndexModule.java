package android.bigplan.lego.model;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.bigplan.lego.R;
import android.bigplan.lego.activity.WebActivity;
import android.bigplan.lego.db.orm.annotation.Column;
import android.bigplan.lego.db.orm.annotation.Id;
import android.bigplan.lego.db.orm.annotation.Table;
import android.graphics.drawable.Drawable;

/**
 * 首页模块
 *
 * @author FingerArt FingerArt.me
 * @date 2015年11月25日 下午12:07:21
 */
@SuppressLint("UseSparseArrays")
@Table(name = "indexModule")
public class IndexModule implements Serializable, Comparable<IndexModule> {

    @Id
    @Column(name = "_id")
    private int _id;

    /**
     * 背景色
     */
    @Column(name = "color")
    private String Color;

    @Column(name = "fontColor")
    private String FontColor;

    /**
     * 模块id
     */
    @Column(name = "moduleId")
    private int ModuleId;

    /**
     * 模块名
     */
    @Column(name = "name")
    private String Name;

    /**
     * 子标题
     */
    @Column(name = "subTitle")
    private String SubTitle;

    /**
     * 标题
     */
    @Column(name = "title")
    private String Title;

    /**
     * 网页链接
     */
    @Column(name = "link")
    private String Link;


    @Column(name = "iconLink")
    private String IconLink;


    /**
     * 排序
     */
    @Column(name = "sortIndex")
    private int SortIndex;


    /**
     * 子模块
     */
    private List<IndexModule> childModule = new ArrayList<IndexModule>();

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public void setFontColor(String color) {
        FontColor = color;
    }

    public String getFontColor() {
        return FontColor;
    }

    public int getModuleId() {
        return ModuleId;
    }

    public void setModuleId(int moduleId) {
        ModuleId = moduleId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public void setIconLink(String link) {
        IconLink = link;
    }

    public String getIconLink() {
        return IconLink;
    }

    public int getSortIndex() {
        return SortIndex;
    }

    public void setSortIndex(int sortIndex) {
        SortIndex = sortIndex;
    }



    public int getIcon() {

        return R.drawable.ic_launcher;
    }

    @SuppressLint("NewApi")
    @Override
    public int compareTo(IndexModule another) {
        return Integer.compare(getSortIndex(), another.getSortIndex());
    }

    @Override
    public String toString() {
        return "IndexModule [_id=" + _id + ", Color="
                + Color + ", ModuleId=" + ModuleId + ", Name=" + Name
                + ", SubTitle=" + SubTitle + ", Title=" + Title + ", Link="
                + Link + "]";
    }

}
