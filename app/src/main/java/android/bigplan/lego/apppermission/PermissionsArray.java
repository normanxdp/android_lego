package android.bigplan.lego.apppermission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * 检查权限的工具类
 * <p/>
 * Created by wangchenlong on 16/1/26.
 */
public class PermissionsArray {
    public  static final String[] PERMISSIONS = new String[]{

//            Manifest.permission.ACCESS_COARSE_LOCATION,//网络有关
//            Manifest.permission.ACCESS_WIFI_STATE,//网络有关
//            Manifest.permission.CHANGE_WIFI_STATE,//网络有关
//            Manifest.permission.CHANGE_NETWORK_STATE,//网络有关
//            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
//            Manifest.permission.ACCESS_NETWORK_STATE,
//            Manifest.permission.WRITE_SETTINGS,//WRITE_SETTINGS允许程序读取或写入系统设置

            Manifest.permission.INTERNET,//网络
            Manifest.permission.READ_PHONE_STATE,//网络
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//读写sd卡
            Manifest.permission.READ_EXTERNAL_STORAGE,///读写sd卡
            Manifest.permission.READ_CONTACTS,//允许程序读取用户联系人数据
            Manifest.permission.CALL_PHONE,//电话
            Manifest.permission.CAMERA,//相机
           // Manifest.permission.FLASHLIGHT,//允许访问闪光灯
            Manifest.permission.RECORD_AUDIO,//允许访问闪光灯
            Manifest.permission.ACCESS_FINE_LOCATION,//允许访问闪光灯
            Manifest.permission.ACCESS_COARSE_LOCATION//允许访问闪光灯

    };
}
