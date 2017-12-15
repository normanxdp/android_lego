package android.bigplan.lego.model;

import java.io.Serializable;

/**
 * Created by gg on 16-4-27.
 */

//use in Skill Certification  and   Package Describe
public class UploadPicture implements Serializable {

    private boolean isShowDelete;
    private String picFilePath;
    private String picUrl;

    public UploadPicture() {
        picFilePath = null;
        picUrl = null;
        isShowDelete = false;
    }

    public UploadPicture(String filePath, String url, boolean showDelete) {
        picFilePath = filePath;
        picUrl = url;
        isShowDelete = showDelete;
    }

    public String getPicFilePath() {
        return picFilePath;
    }

    public void setPicFilePath(String picFilePath) {
        this.picFilePath = picFilePath;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public boolean isShowDelete() {
        return isShowDelete;
    }

    public void setShowDelete(boolean showDelete) {
        isShowDelete = showDelete;
    }
}
