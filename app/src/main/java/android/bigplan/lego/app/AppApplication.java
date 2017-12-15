package android.bigplan.lego.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bigplan.lego.BuildConfig;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.WelcomeActivity;
import android.bigplan.lego.apppermission.PermissionsActivity;
import android.bigplan.lego.apppermission.PermissionsArray;
import android.bigplan.lego.apppermission.PermissionsChecker;
import android.bigplan.lego.db.dao.UserDao;
import android.bigplan.lego.db.storage.AbSqliteStorage;
import android.bigplan.lego.db.storage.AbStorageQuery;
import android.bigplan.lego.global.AbAppConfig;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.model.ProvinceCity;
import android.bigplan.lego.model.ReceiveAddress;
import android.bigplan.lego.model.User;
import android.bigplan.lego.task.thread.AbThreadFactory;
import android.bigplan.lego.util.AbAppUtil;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.ExampleUtil;
import android.bigplan.lego.util.Logger;
import android.bigplan.lego.db.storage.AbSqliteStorageListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.nearby.NearbySearch;
import com.google.gson.reflect.TypeToken;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.bigplan.lego.global.AbConstant.ACTION_LOCALTION;

public class AppApplication extends Application {
    private static final String TAG = AppApplication.class.getSimpleName();
    public static int mWinWidth = 0;
    public static int  mWinHeight  = 0;
    private User user;
    public boolean isUserLogin; //判断是否已登录

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private AMapLocation mCurLocation = null;
    AMapLocationListener locationListener = null;
    private double mLongitude = 0.0;
    private double mLatitude = 0.0;
    public NearbySearch mNearbySearch;

    public List<ReceiveAddress> mReceiveAddress = null;

    public static String mAppName; // 应用名称

    public static int mVersionCode; // 版本号

    public static String mVersionName; // 版本名称

    public static String mSdcardDataDir; // SD卡路径

    public static String mApkDownloadUrl = null;

    private List<Activity> activities; // 用来保存普通的页面

    private static AppApplication instance;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private NotificationManager mNotificationManager;
    private boolean bFirstLocaltion = true;

    public boolean userPasswordRemember = false;

    public boolean isFirstStart = true;

    public SharedPreferences mSharedPreferences = null;

    // 定义数据库操作实现类
    private UserDao userDao = null;
    public static List<ProvinceCity> mProvinceList = new ArrayList<>();
    public static List<ProvinceCity> mCityList = new ArrayList<>();
    public static List<ProvinceCity> mTotalProvinceCityList = new ArrayList<>();

    // 数据库操作类
    private AbSqliteStorage mAbSqliteStorage = null;

    public static boolean isPublishSelect = false;

    public final String PREF_USERNAME = "username";
    public  static   String sendpath,receivepath;// path/xx.amr就是完整路径
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    @Override
    public void onCreate() {
        long s = System.currentTimeMillis();
        Log.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();
        instance = this;
        ZXingLibrary.initDisplayOpinion(this);
        mNearbySearch = NearbySearch.getInstance(this);
        mPermissionsChecker = new PermissionsChecker(this);
        //initDatabase();
        userDao = new UserDao(this);
        mAbSqliteStorage = AbSqliteStorage.getInstance(this);

        activities = new ArrayList<Activity>();
        initUserParams();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWinWidth = wm.getDefaultDisplay().getWidth();
        mWinHeight = wm.getDefaultDisplay().getHeight();

        // 极光推送
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        String registerID = JPushInterface.getRegistrationID(this);
        Logger.d(TAG, "init jpush" + registerID);

        // handleCrash();

        initProvinceCityData();
        initLocalVersion();
        initLocation();
        Logger.d(TAG, "初始化共计用时："+(System.currentTimeMillis()-s));
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                sendpath = Environment.getExternalStorageDirectory()
                        .getCanonicalPath().toString()
//						+ "/MessageMediaReceive";
                        + "/lego/media/send";
                File files = new File(sendpath);
                if (!files.exists()) {
                    boolean mkdir = files.mkdirs();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                receivepath = Environment.getExternalStorageDirectory()
                        .getCanonicalPath().toString()
//						+ "/MessageMediaReceive";
                        + "/lazy/media/accept";
                File files = new File(receivepath);
                if (!files.exists()) {
                    boolean mkdir = files.mkdirs();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    int count = 0;


    public boolean lacksPermissions(){
        String release = Build.VERSION.RELEASE;
        String substring = release.substring(0, 1);

        if (substring!=null&&Integer.valueOf(substring)>5) {
            // 缺少权限时, 进入权限配置页面
            return mPermissionsChecker.lacksPermissions(PermissionsArray.PERMISSIONS);
        }
        return  false;
    }

    public double getLatitude(){
        if (mLatitude < 3.0 || mLatitude > 54.0){
            if (mCurLocation != null){
                return mCurLocation.getLatitude();
            }
            Log.d("AbLogUtil", "DEFAULT_DOUBLE_LATITUDE");
            return AbConstant.DEFAULT_DOUBLE_LATITUDE;
        }
        return  mLatitude;

    }
    public double getLongitude(){
        if (mLongitude < 70.0 || mLongitude > 136.0){
            if (mCurLocation != null){
                return mCurLocation.getLongitude();
            }
            Log.d("AbLogUtil", "DEFAULT_DOUBLE_LONGITUDE");
            return  AbConstant.DEFAULT_DOUBLE_LONGITUDE;
        }
        return  mLongitude;
    }

    public boolean isInitLocaltion(){
        if (mCurLocation != null){
            return  true;
        }
        return false;
    }
    public void setLocation(AMapLocation localtion){
        mCurLocation = localtion;
    }

    public void initLocation(){
        if (!lacksPermissions()){
            //初始化client
            locationClient = new AMapLocationClient(this.getApplicationContext());
            locationOption = getDefaultOption();
            //设置定位参数
            locationClient.setLocationOption(locationOption);

            /**
             * 定位监听
             */
            locationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation location) {
                    if (null != location) {
                        mLongitude = location.getLongitude();
                        mLatitude = location.getLatitude();
                        mCurLocation = location;
                        Log.d("AbLogUtil", "Appapplication lat: " + mLatitude + "  lng: " + mLongitude);
                        if (mLatitude > 3.0 && mLatitude < 54.0 && mLongitude > 70.0 && mLongitude < 136.0){
                            if (bFirstLocaltion){
                                Intent intent = new Intent();
                                intent.setAction(ACTION_LOCALTION);
                                intent.putExtra("lego_localtion_get", "1");
                                sendBroadcast(intent);
                                bFirstLocaltion = false;
                            }
                        }


                    }
                }
            };

            locationClient.setLocationListener(locationListener);
            startLocation();
        }else{
            Log.d("AbLogUtil","Appapplication lacksPermissions");
        }
    }


    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }

    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置是否单次定位
        locationOption.setOnceLocation(false);
    }

    private void startLocation(){
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        return mOption;
    }

    /**
     * 处理全局的崩溃信息
     */
    private void handleCrash() {
        AndroidCrash androidCrash = AndroidCrash.getInstance();
        androidCrash.init(new CrashHandler() {
            @Override
            public void handleCarsh(Thread thread, Throwable ex) {
                try {
                    Logger.e(TAG, "发生奔溃");

                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            Toast.makeText(instance, "程序发生异常即将重启", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }.start();

                    StringBuilder errMsg = new StringBuilder();
                    StackTraceElement stackTraceElement = ex.getStackTrace()[0];
                    errMsg.append("Crash: " + ex.toString());
                    errMsg.append("\nAt: " + stackTraceElement.getFileName().substring(0, stackTraceElement.getFileName().lastIndexOf(".") + 1) + stackTraceElement.getMethodName() + " : " + stackTraceElement.getLineNumber());
                    Logger.e(TAG, errMsg);

                    AbHttpUtil mAbHttpUtil = AbHttpUtil.getInstance(instance);
                    AbRequest request = new AbRequest();
                    JSONObject object = new JSONObject();
                    //	object.put("allyid", getUser().getAllyId());
                    object.put("memberid", getUser().getMemberId());
                    object.put("deviceno", AbAppUtil.getImeiNumber(instance));
                    object.put("info", errMsg);

                    AbRequestParams params = request.getRequestParams(object.toString());

                    mAbHttpUtil.post(AbConstant.CRASH_URL + "/Common/SubmitCrash", params, new AbStringHttpResponseListener() {
                        @Override
                        public void onSuccess(int statusCode, String content) {
                            //Empty
                        }

                        @Override
                        public void onStart() {
                            //Empty
                        }

                        @Override
                        public void onFinish() {
                            count = 3;
                        }

                        @Override
                        public void onFailure(int statusCode, String content,
                                              Throwable error) {
                            //Empty
                        }
                    });

                    //等待崩溃信息提交完成
                    while (count < 3) {
                        SystemClock.sleep(1000);
                        ++count;
                    }

                    Intent intent = new Intent(instance, WelcomeActivity.class);
                    PendingIntent restartIntent = PendingIntent.getActivity(instance.getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
                    //退出程序
//                    AlarmManager mgr = (AlarmManager) instance.getSystemService(Context.ALARM_SERVICE);
//                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 0, restartIntent); // 0秒钟后重启应用
//                    Logger.e(TAG, "崩溃处理完成");
//
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                    System.exit(1);
                } catch (Exception e) {
                    Logger.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * TODO 耗时操作放入线程中去执行，加快启动速度
     */
    private void initProvinceCityData() {
        AbThreadFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                Logger.d(TAG, "start initProvinceCityData");
              //  getCityList();
                Logger.d(TAG, "end initProvinceCityData");
            }
        });
    }

    public static String getCityCode(String cityName) {
        for (int i = 0; i < AppApplication.mTotalProvinceCityList.size(); i++) {
            ProvinceCity item = AppApplication.mTotalProvinceCityList.get(i);
            if (item.getName().equalsIgnoreCase(cityName)) {
                return item.getCode();
            }
        }
        return null;
    }

    private void bubbleSort(List<ProvinceCity> cityTotalList) {
        int temp; // 记录临时中间值
        int size = cityTotalList.size(); // 数组大小
        for (int i = 0; i < cityTotalList.size() - 1; i++) {
            for (int j = i + 1; j < cityTotalList.size(); j++) {
                ProvinceCity firstItem = cityTotalList.get(i);
                ProvinceCity secondItem = cityTotalList.get(j);

                //   Logger.d(TAG, "first1 Char: " + firstItem.getFirstLetter());
                //   Logger.d(TAG, "second1 Char: " + secondItem.getFirstLetter());

                if (firstItem.getFirstLetter().equalsIgnoreCase("")) {
                    cityTotalList.remove(i);
                    i--;
                    break;
                }

                if (secondItem.getFirstLetter().equalsIgnoreCase("")) {
                    cityTotalList.remove(j);
                    j--;
                    continue;
                }

                char[] firstChar = firstItem.getFirstLetter().toCharArray();
                char[] secondChar = secondItem.getFirstLetter().toCharArray();

                //   Logger.d(TAG, "first Char: " + firstChar[0]);
                //   Logger.d(TAG, "second Char: " + secondChar[0]);
                if (firstChar[0] > secondChar[0]) {
                    ProvinceCity tmp = firstItem;
                    cityTotalList.remove(i);
                    cityTotalList.add(i, secondItem);
                    cityTotalList.remove(j);
                    cityTotalList.add(j, tmp);
                }
            }
        }
    }

    /**
     * 更新sqlite数据库的用户数据
     *
     * @param mUser
     */
    public void saveUser2Sqlite(User mUser) {
        userDao.startWritableDatabase(false);
        userDao.deleteAll();
        userDao.addUser(mUser);
        userDao.closeDatabase();
    }

    /**
     * 上次登录参数
     */
    private void initUserParams() {
        isUserLogin = false;
        String telephone = AbSharedUtil.getString(this, AbConstant.KEY_SP_TELPHONE);

        Logger.d(TAG, "telephone: " + telephone);
        AbStorageQuery mAbStorageQuery = new AbStorageQuery();
        mAbStorageQuery.setWhereClause("Mobile=?", new String[]{telephone});
        mAbSqliteStorage.findData(mAbStorageQuery, userDao, new AbSqliteStorageListener.AbDataSelectListener() {
            @Override
            public void onFailure(int errorCode, String errorMessage) {
                AbToastUtil.showToast(getApplicationContext(), errorMessage);
            }

            @Override
            public void onSuccess(List<?> paramList) {
                if (paramList != null && paramList.size() > 0) {
                    User u = (User) paramList.get(0);
                    user = u;
                    Logger.d(TAG, "success initUserParams  ....." + user.getMobile());
                }
            }

        });

    }

    public void updateUserParams(User u) {
        // 无sql存储的更新
        mAbSqliteStorage.updateData(u, userDao, new AbSqliteStorageListener.AbDataUpdateListener() {

            @Override
            public void onFailure(int errorCode, String errorMessage) {
                AbToastUtil.showToast(getApplicationContext(), errorMessage);
            }

            @Override
            public void onSuccess(int rows) {

            }

        });

        isFirstStart = false;
    }

    /**
     * 清空上次登录参数
     */
    public void clearLoginParams() {
        AbSharedUtil.remove(this, AbConstant.KEY_SP_TELPHONE);
        AbSharedUtil.remove(this, AbConstant.KEY_SP_PASSWORD);

        AbSharedUtil.remove(this, AbConstant.KEY_QUICK_USER_NICKNAME);
        AbSharedUtil.remove(this, AbConstant.KEY_QUICK_USER_TYPE);
        AbSharedUtil.remove(this, AbConstant.KEY_QUICK_USERNAME);
        AbSharedUtil.remove(this, AbConstant.KEY_QUICK_USER_AVATAR);
        this.user = null;
        isUserLogin = false;
    }

    public void initEnv() {
        mAppName = "B+";
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + AbAppConfig.DOWNLOAD_FILE_DIR);
            if (!file.exists()) {
                if (file.mkdirs()) {
                    mSdcardDataDir = file.getAbsolutePath();
                }
            } else {
                mSdcardDataDir = file.getAbsolutePath();
            }
        }
    }

    public void exitApp(Context context) {
    }

    public static AppApplication getInstance() {
        return instance;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void exit() {
        for (Activity a : activities) {
            if (a != null) {
                a.finish();
            }
        }
    }

    public void initLocalVersion() {
        PackageInfo pinfo;
        try {
            pinfo = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            mVersionCode = pinfo.versionCode;
            mVersionName = pinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 获取当前用户信息
     */
    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void closeSelectActivity() {

    }

    public void setPushTag(String tag) {
        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            tagSet.add(sTagItme);
        }
        // 调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }

    public void setPushAlias(String alias) {
        // 调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            // ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            // ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    private static final int MSG_SET_ALIAS = 1001;

    private static final int MSG_SET_TAGS = 1002;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
   /* private void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.drawable.ic_launcher_common;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
        
    }*/
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }


}
