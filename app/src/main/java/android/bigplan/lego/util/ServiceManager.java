package android.bigplan.lego.util;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

//--服务管理类

public class ServiceManager{
    // 判断某个服务是否启动
    public static boolean isServiceStart(Context context, String serviceName) {
        
        ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager.getRunningServices(30);
        
        for (int i = 0; i < mServiceList.size(); i++) {
            if (serviceName.equals(mServiceList.get(i).service.getClassName())) {
                return true;
            }
        }
        
        return false;
    }
    
    // 启动SerialPortService
    public static void startSerialService(Context context, String serviceName) {
        if (!isServiceStart(context, serviceName)) {
            context.startService(new Intent(serviceName));
        }
    }
    
    // 停止SerialPortService
    public static void stopSerialService(Context context, String serviceName) {
        if (isServiceStart(context, serviceName)) {
            context.stopService(new Intent(serviceName));
        }
    }
}
