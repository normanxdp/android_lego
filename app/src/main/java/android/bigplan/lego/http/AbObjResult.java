package android.bigplan.lego.http;


import android.bigplan.lego.util.AbJsonUtil;

public class AbObjResult<T>{
    
    /** 返回码：成功. */
    public static final int RESULT_OK = 0;
    
    /** 返回码：失败. */
    public static final int RESULT_ERROR = 1;
    
    /** 返回码：0 成功. 1 失败 */
    private int code;
    
    /** 结果 message. */
    private String msg;
    
    /** 数据. */
    private T data;
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    /**
     * 
     * 描述：转换成json.
     * 
     * @return
     */
    public String toJson() {
        return AbJsonUtil.toJson(this);
    }
    
}
