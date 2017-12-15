package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 16-3-29.
 *
 * 首页服务模块配置
 */
public class ModuleConfig implements Serializable {
    String ModuleId; //模块Id
    String Link; //点击跳转的位置，没有的话，说明是原生程序
    String IconLink; //图片下载地址，没有本地默认
    String Title; //显示的title
    String FontColor; //字体颜色，没有本地默认
    String SortIndex; //排序的位置

    public String getModuleId() {
        return ModuleId;
    }

    public void setModuleId(String moduleId) {
        ModuleId = moduleId;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getIconLink() {
        return IconLink;
    }

    public void setIconLink(String iconLink) {
        IconLink = iconLink;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFontColor() {
        return FontColor;
    }

    public void setFontColor(String fontColor) {
        FontColor = fontColor;
    }

    public String getSortIndex() {
        return SortIndex;
    }

    public void setSortIndex(String sortIndex) {
        SortIndex = sortIndex;
    }
}
