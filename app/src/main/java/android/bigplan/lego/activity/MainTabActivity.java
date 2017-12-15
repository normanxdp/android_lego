package android.bigplan.lego.activity;

import android.app.Dialog;
import android.app.TabActivity;
import android.bigplan.lego.R;

import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.apppermission.PermissionsActivity;
import android.bigplan.lego.apppermission.PermissionsArray;
import android.bigplan.lego.apppermission.PermissionsChecker;
;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.model.Skill;
import android.bigplan.lego.model.User;
import android.bigplan.lego.task.thread.AbThreadFactory;
import android.bigplan.lego.util.AbAppUtil;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.DialogFactory;
import android.bigplan.lego.util.Logger;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MainTabActivity extends TabActivity implements View.OnClickListener {

    protected static final String TAG = MainTabActivity.class.getSimpleName();

    private TabHost tabHost;

    private RadioButton tab_first, tab_second, tab_four, tab_five;

    public TextView tv_first_msg;

    private String currentTab = "first";

    public static TabActivity tabActivity;

    private View menuHome;  //first

    private View menuNearBy; //second

    private View menuTakeOrder;  //three

    private View menuOrder; //fourth

    private View menuMy; //fifth

    private TextView tvTakeOrder;

    private Context mContext;

    private String mNumber, mAvatar, mNickName, mType;

    private boolean mbFirst = true;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main_tab);
        AppApplication.getInstance().addActivity(this);
        initTab();
        init();
        tabActivity = this;
        currentTab = getIntent().getStringExtra("action");
        changeTabSelect(currentTab);

        getHomePageConfig();

        JPushInterface.resumePush(getApplicationContext());
        String release = Build.VERSION.RELEASE;

        String substring = release.substring(0, 1);
        mPermissionsChecker = new PermissionsChecker(mContext);
        boolean isLacksPermissions = false;
        if(substring!=null&&Integer.valueOf(substring)>5) {
            // 缺少权限时, 进入权限配置页面
            if (mPermissionsChecker.lacksPermissions(PermissionsArray.PERMISSIONS)) {
                isLacksPermissions = true;
                startPermissionsActivity();
            }
        }
        if (isLacksPermissions == false){
            setPushInfo();
        }

        String userTele = AbSharedUtil.getString(mContext, AbConstant.KEY_SP_TELPHONE);
        String userPwd = AbSharedUtil.getString(mContext, AbConstant.KEY_SP_PASSWORD);
        if (!AppApplication.getInstance().isUserLogin)
        {
            if (userTele!=null && !userTele.equals("") && userPwd!=null && !userPwd.equals("")){
                loginTask(userTele, userPwd);
                return;
            }
            mNumber = AbSharedUtil.getString(mContext, AbConstant.KEY_QUICK_USERNAME);
            mNickName = AbSharedUtil.getString(mContext, AbConstant.KEY_QUICK_USER_NICKNAME);
            mAvatar = AbSharedUtil.getString(mContext, AbConstant.KEY_QUICK_USER_AVATAR);
            mType = AbSharedUtil.getString(mContext, AbConstant.KEY_QUICK_USER_TYPE);

            if (!TextUtils.isEmpty(mNumber) && !TextUtils.isEmpty(mNickName) && !TextUtils.isEmpty(mAvatar)){
                if (mType.equals("1") || mType.equals("2")) {
                    loginSanFangTask();
                }
            }
        }
    }

    private static final int REQUEST_CODE = 0; // 请求码
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PermissionsArray.PERMISSIONS);
    }
    protected void setPushInfo(){
        // 设置推送消息的Alias(别名)
        Logger.d(TAG, "IMEI: " + AbAppUtil.getImeiNumber(this));
        AppApplication.getInstance().setPushAlias(AbAppUtil.getImeiNumber(this));
        // 设置推送消息的TAG(标签)
        AppApplication.getInstance().setPushTag("lego");
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Log.d("AbLogUtil","MainTAb lacksPermissions");
            AppApplication.getInstance().initLocation();
            setPushInfo();
        }
    }
    private void init() {
		menuHome = findViewById(R.id.fl_menu_first);
		menuNearBy = findViewById(R.id.fl_menu_second);
		menuTakeOrder = findViewById(R.id.fl_menu_take_order);
		menuOrder = findViewById(R.id.fl_menu_fourth);
		menuMy = findViewById(R.id.fl_menu_fifth);

		tab_first = (RadioButton) findViewById(R.id.tab_first);
		tab_second = (RadioButton)findViewById(R.id.tab_second);
        tab_four = (RadioButton)findViewById(R.id.tab_fourth);
        tab_five = (RadioButton)findViewById(R.id.tab_fifth);
		tvTakeOrder = (TextView) findViewById(R.id.tv_take_order);

        menuTakeOrder.setOnClickListener(this);
        tab_first.setOnClickListener(this);
        tab_second.setOnClickListener(this);
        tab_five.setOnClickListener(this);
        tab_four.setOnClickListener(this);

        tv_first_msg = (TextView)findViewById(R.id.tv_first_msg);

        initMenu();
        //ShowUnreadMsgCountTotal();
    }

    private void initMenu() {

        menuHome.setVisibility(View.VISIBLE);
        tab_first.setText(R.string.label_tab_tv_first);

        menuNearBy.setVisibility(View.VISIBLE);
        tab_second.setText(R.string.label_tab_tv_search);

        /*
        menuTakeOrder.setVisibility(View.VISIBLE);
        tvTakeOrder.setText(R.string.label_tab_tv_third);

        menuOrder.setVisibility(View.VISIBLE);
        tab_four.setText(R.string.label_tab_tv_fouth);
        */

        menuMy.setVisibility(View.VISIBLE);
        tab_five.setText(R.string.label_tab_tv_fifth);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    
    @Override
    protected void onDestroy() {
        AbAppUtil.commonHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private  void bandMobile(){
        Intent intent = new Intent(mContext, BandMobileCodeActivity.class);
        intent.putExtra(BandMobileCodeActivity.APPLY_STAFF, "1");
        mContext.startActivity(intent);
    }

    public void onClick(View v) {
        Logger.d(TAG, "onclick");
        Intent intent;
        switch (v.getId()) {
            case R.id.tab_first:
                tabHost.setCurrentTabByTag("first");
                setRadioStats(tab_first);
                break;
            case R.id.tab_second:
                tabHost.setCurrentTabByTag("second");
                setRadioStats(tab_second);
                break;
            case R.id.tab_fourth:
                if (AppApplication.getInstance().isUserLogin) {
                    tabHost.setCurrentTabByTag("four");
                    setRadioStats(tab_four);
                } else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tab_fifth:
                tabHost.setCurrentTabByTag("five");
                setRadioStats(tab_five);
                break;
            case R.id.fl_menu_take_order:
                if (AppApplication.getInstance().isUserLogin) {


                } else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void changeTabSelect(String action) {
        if ("first".equals(action)) {
            tabHost.setCurrentTabByTag("first");
            setRadioStats(tab_first);
        }
        if ("second".equals(action)) {
            tabHost.setCurrentTabByTag("second");
            setRadioStats(tab_second);
        }
        if ("four".equals(action)) {
            tabHost.setCurrentTabByTag("four");
            setRadioStats(tab_four);
        }
        if ("five".equals(action)) {
            tabHost.setCurrentTabByTag("five");
            setRadioStats(tab_five);
        }

    }

    private void setRadioStats(RadioButton current) {
        tab_first.setChecked(false);
        tab_second.setChecked(false);
        tab_four.setChecked(false);
        tab_five.setChecked(false);
        current.setChecked(true);
    }

    private void initTab() {
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("first").setIndicator("first")
                .setContent(new Intent(this, TabHomeActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("second").setIndicator("second")
                .setContent(new Intent(this, TabNearbyActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("five").setIndicator("five")
                .setContent(new Intent(this, TabMyActivity.class)));
    }

    private void getHomePageConfig() {
        // 获取Http工具类
        AbHttpUtil mAbHttpUtil = AbHttpUtil.getInstance(this);
        AbRequest request = new AbRequest();
        JSONObject object = new JSONObject();

        AbRequestParams params = request.getRequestParams(object.toString());
        mAbHttpUtil.post(AbConstant.REQUEST_URL + "/Home/GetHomePage", params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                if (content != null) {
                    try {
                        AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
                        if (abResult.getCode() == AbResult.RESULT_OK) {
                            JSONObject jsonObject = new JSONObject(content).getJSONObject("data");

                        } else {
                            String msg = abResult.getMsg();
                            AbToastUtil.showToast(mContext, msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 开始执行前
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(int statusCode, String content,
                                  Throwable error) {
                AbToastUtil.showToast(mContext, error.getMessage());
            }
            public void onFinish() {
            }
        });
    }

    public void loginSanFangTask() {

        AbHttpUtil mAbHttpUtil = AbHttpUtil.getInstance(mContext);
        AbRequest request = new AbRequest();
        JSONObject object = new JSONObject();
        try {

            object.put("type", mType);
            object.put("number",mNumber);//唯一值相当QQ号码
            object.put("avatar", mAvatar);
            object.put("nickname", mNickName);
            object.put("deviceno", AbAppUtil.getImeiNumber(mContext));
            object.put("devicetype", "2");

        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }

        AbRequestParams params = request.getRequestParams(object.toString());
        mAbHttpUtil.post(AbConstant.REQUEST_URL + "/Member/QuickLogin", params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                if (content != null) {
                    try {
                        AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
                        if (abResult.getCode() == AbResult.RESULT_OK) {
                            AbSharedUtil.putString(mContext, AbConstant.KEY_QUICK_USERNAME, mNumber);
                            AbSharedUtil.putString(mContext, AbConstant.KEY_QUICK_USER_TYPE, mType);
                            AbSharedUtil.putString(mContext, AbConstant.KEY_QUICK_USER_AVATAR, mAvatar);
                            AbSharedUtil.putString(mContext, AbConstant.KEY_QUICK_USER_NICKNAME, mNickName);

                            JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
                            User user = AppApplication.getInstance().getUser();
                            user.setMemberId(jsonObject.getString("MemberId"));
                            user.setPassId(jsonObject.getString("PassId"));
                            user.setQQ(jsonObject.getString("QQ"));
                            user.setMobile(jsonObject.getString("Mobile"));
                            user.setAvatar(jsonObject.getString("Avatar"));
                            user.setSex(jsonObject.getString("Sex"));
                            user.setRealName(jsonObject.getString("RealName"));
                            user.setNickname(jsonObject.getString("Nickname"));
                            user.setStoreName(jsonObject.getString("StoreName"));
                            user.setFirstLetter(jsonObject.getString("FirstLetter"));
                            user.setWeChatId(jsonObject.getString("WeChatId"));
                            user.setIntroduce(jsonObject.getString("Introduce"));
                            user.setIntroduction(jsonObject.getString("Introduction"));
                            user.setState(jsonObject.getString("State"));
                            user.setCreateTime(jsonObject.getString("CreateTime"));
                            user.setSinopecAccountCount(jsonObject.getString("SinopecAccountCount"));
                            user.setPetroChinaAccountCount(jsonObject.getString("PetroChinaAccountCount"));
                            user.setOilMoney(jsonObject.getString("OilMoney"));
                            user.setIsLazyStaff(jsonObject.getString("IsLazyStaff"));
                            user.setPayPwd(jsonObject.getString("PayPwd"));
                            AppApplication.getInstance().setUser(user);
                            AppApplication.getInstance().saveUser2Sqlite(user);
                            AppApplication.getInstance().isUserLogin = true;
							String mobile = jsonObject.getString("Mobile");
							if (TextUtils.isEmpty(mobile)){
								bandMobile();
							}

                        } else {
                            String msg = abResult.getMsg();
                            AbToastUtil.showToast(mContext, msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 开始执行前
            @Override
            public void onStart() {
                showRequestDialog("正在登录...");
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                AbToastUtil.showToast(mContext, error.getMessage());
                currentTab = "five";
                changeTabSelect(currentTab);

                AppApplication.getInstance().isUserLogin = false;
            }

            // 完成后调用，失败，成功，都要调用
            public void onFinish() {
                closeRequestDialog();
            }
        });
    }

    private void loginTask(final String telphone, final String password) {
        // 获取Http工具类
        AbHttpUtil mAbHttpUtil = AbHttpUtil.getInstance(mContext);
        AbRequest request = new AbRequest();
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", telphone);
            object.put("pwd", password);
            object.put("deviceno", AbAppUtil.getImeiNumber(mContext));
            object.put("devicetype", "2");

        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }

        AbRequestParams params = request.getRequestParams(object.toString());
        mAbHttpUtil.post(AbConstant.REQUEST_URL + "/Member/Login", params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                if (content != null) {
                    try {
                        AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
                        if (abResult.getCode() == AbResult.RESULT_OK) {
                            AbSharedUtil.putString(mContext, AbConstant.KEY_SP_TELPHONE, telphone);
                            AbSharedUtil.putString(mContext, AbConstant.KEY_SP_PASSWORD, password);

                            JSONObject jsonObject = new JSONObject(content).getJSONObject("data");

                            User user = AppApplication.getInstance().getUser();
                            user.setMemberId(jsonObject.getString("MemberId"));
                            user.setPassId(jsonObject.getString("PassId"));
                            user.setMobile(jsonObject.getString("Mobile"));
                            user.setAvatar(jsonObject.getString("Avatar"));
                            user.setSex(jsonObject.getString("Sex"));
                            user.setRealName(jsonObject.getString("RealName"));

                            user.setNickname(jsonObject.getString("Nickname"));
                            user.setStoreName(jsonObject.getString("StoreName"));
                            user.setIntroduction(jsonObject.getString("Introduction"));
                            user.setFirstLetter(jsonObject.getString("FirstLetter"));
                            user.setQQ(jsonObject.getString("QQ"));
                            user.setWeChatId(jsonObject.getString("WeChatId"));
                            user.setYMobile(jsonObject.getString("YMobile"));
                            user.setYunAccount(jsonObject.getString("YunAccount"));
                            user.setIntroduce(jsonObject.getString("Introduce"));
                            user.setState(jsonObject.getString("State"));
                            user.setCreateTime(jsonObject.getString("CreateTime"));
                            user.setIsLazyStaff(jsonObject.getString("IsLazyStaff"));
                            user.setSinopecAccountCount(jsonObject.getString("SinopecAccountCount"));
                            user.setPetroChinaAccountCount(jsonObject.getString("PetroChinaAccountCount"));
                            user.setOilMoney(jsonObject.getString("OilMoney"));
                            user.setPayPwd(jsonObject.getString("PayPwd"));
                            AppApplication.getInstance().saveUser2Sqlite(user);
                            AppApplication.getInstance().isUserLogin = true;

                        } else {
                            String msg = abResult.getMsg();
                            AbToastUtil.showToast(mContext, msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 开始执行前
            @Override
            public void onStart() {
                showRequestDialog("正在登录...");
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                AbToastUtil.showToast(mContext, error.getMessage());
                currentTab = "five";
                changeTabSelect(currentTab);

                AppApplication.getInstance().isUserLogin = false;
            }

            // 完成后调用，失败，成功，都要调用
            public void onFinish() {
                closeRequestDialog();
            }
        });
    }
    
    public Dialog mDialog = null;

    public void showRequestDialog(String msg) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(this, msg);
        mDialog.show();
    }

    public void showRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(this, getString(R.string.loading));
        mDialog.show();
    }

    public void closeRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}

