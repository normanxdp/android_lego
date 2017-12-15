package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 16-3-29.
 *
 * 系统配置
 */
public class SystemConfig implements Serializable {

    String HomeBkColor; //首页背景颜色
    String SkillMax; //员工最大的技能个数

    public String getHomeBkColor() {
        return HomeBkColor;
    }

    public void setHomeBkColor(String homeBkColor) {
        HomeBkColor = homeBkColor;
    }

    public String getSkillMax() {
        return SkillMax;
    }

    public void setSkillMax(String skillMax) {
        SkillMax = skillMax;
    }
}
