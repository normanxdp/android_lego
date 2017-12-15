package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 16-3-29.
 *
 * 首页下方的标签页的配置
 */
public class TabButtonConfig implements Serializable {
    String FontColor; //没按下按钮的字体颜色，没有本地默认
    String FontPressColor; //按下按钮的字体颜色，没有本地默认
    String IconLink; //没按下图片的地址，没有本地默认
    String IconPressLink; //按下图片地址，没有本地默认
    String Name; //显示的按钮名字，没有本地默认

    public String getFontColor() {
        return FontColor;
    }

    public void setFontColor(String fontColor) {
        FontColor = fontColor;
    }

    public String getFontPressColor() {
        return FontPressColor;
    }

    public void setFontPressColor(String fontPressColor) {
        FontPressColor = fontPressColor;
    }

    public String getIconLink() {
        return IconLink;
    }

    public void setIconLink(String iconLink) {
        IconLink = iconLink;
    }

    public String getIconPressLink() {
        return IconPressLink;
    }

    public void setIconPressLink(String iconPressLink) {
        IconPressLink = iconPressLink;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
