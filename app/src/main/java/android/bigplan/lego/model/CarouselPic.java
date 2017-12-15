package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 16-4-11.
 */
public class CarouselPic implements Serializable {
    private String Id;
    private String Title;
    private String Pic;
    private String Link;
    private String Hits;
    private String IsHidden;
    private String AdType;
    private String CreateTime;
    private String ObjId;
    private String ObjType;
    private String Param;
    private String ObjTitle;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getHits() {
        return Hits;
    }

    public void setHits(String hits) {
        Hits = hits;
    }

    public String getIsHidden() {
        return IsHidden;
    }

    public void setIsHidden(String isHidden) {
        IsHidden = isHidden;
    }

    public String getAdType() {
        return AdType;
    }

    public void setAdType(String adType) {
        AdType = adType;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getObjId() {
        return ObjId;
    }

    public void setObjId(String objId) {
        ObjId = objId;
    }

    public String getObjType() {
        return ObjType;
    }

    public void setObjType(String objType) {
        ObjType = objType;
    }

    public String getParam() {
        return Param;
    }

    public void setParam(String param) {
        Param = param;
    }

    public String getObjTitle() {
        return ObjTitle;
    }

    public void setObjTitle(String objTitle) {
        ObjTitle = objTitle;
    }
}
