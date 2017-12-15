package android.bigplan.lego.http;

import java.io.Serializable;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbMd5;
import android.bigplan.lego.util.Des3Util;

public class AbRequest implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    /** 移动设备操作系统类型,0代表IOS,1代表Android */
    private String ostype = AbConstant.REQUEST_OSTYPE;

    private String version = AppApplication.mVersionName;
    
    /** 调用时间，请转化为时间戳 */
    private String time = String.valueOf(System.currentTimeMillis());
    
    /** 调用接口的APP类型,0代表会员APP,1代表秘书APP */
    private String apptype = AbConstant.REQUEST_APPTYPE;
    
    /** 具体业务参数，通过JSON对象保存参数值，传递时转化为JSON字符串 */
    private String data;
    
    /** 参数生成的验证码，生成规则 */
    private String hash;
    
    public String getOstype() {
        return ostype;
    }
    
    public void setOstype(String ostype) {
        this.ostype = ostype;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }

    static public String getOriginalData(String data){
        String result = "";
        try {
            result = Des3Util.deCode(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getData() {
        String result = "";
        try {
            
            result = Des3Util.enCode(data);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    public String getHash() {
        try {
            // 第2步：time参数、加密后的data参数、加密KYE连接成字符串
            String temp = time + getData() + AbConstant.CRYPT_KEY;
          //  temp = temp.replace("\n","");
            // 第3步:对第2步中生成的字符串进行MD5操作，生成的字符串就是最终的hash码
            return AbMd5.EncodeMD5(temp);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
    
    public AbRequestParams getRequestParams(String jsonData) {
        AbRequestParams params = new AbRequestParams();
        this.data = jsonData;
        try {
            AbLogUtil.d("request:" + jsonData);
            params.put("ostype", this.getOstype());
            params.put("ver", this.getVersion());
            params.put("time", this.getTime());
            params.put("data", this.getData());
            params.put("hash", this.getHash());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return params;
    }
    
}
