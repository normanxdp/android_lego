package android.bigplan.lego.model;

import java.io.Serializable;
import java.util.List;

public class VoiceMsgEvent implements Serializable{

    private int duration;// 总的大小
    private int currentPosition;//  当前位置

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
