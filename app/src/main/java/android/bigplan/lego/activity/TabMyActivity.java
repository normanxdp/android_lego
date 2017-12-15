package android.bigplan.lego.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.msg.SecondMsgListActivity;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.http.entity.mine.content.StringBody;
import android.bigplan.lego.model.SecondMsg;
import android.bigplan.lego.model.User;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import cn.jpush.android.api.JPushInterface;

public class TabMyActivity extends BaseTabActivity implements View.OnClickListener,  SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = TabMyActivity.class.getSimpleName();
    public static final String MESSAGE_CHANGEAVR_ACTION = "android.bigplan.lego.MESSAGE_ACTION";

    private boolean isLogined;
    private String mUserID;

    private TextView mBtnLoginNow;
    private LinearLayout mLLStaff;
    private LinearLayout mLLMyWallet;
    private LinearLayout mllFavorite;
    private LinearLayout mLLShopWallet;
    private LinearLayout mLLFeedback;
    private LinearLayout mLLWalletLog;
    private LinearLayout mbeComeStaff;
    private LinearLayout mLLSetting;
    private String mStrCodeUrl = null;
    private LinearLayout mLLYlMember = null;
    private LinearLayout mReferee = null;


    private TextView mTvUserID;
    private TextView mTvUserLevel;
    private ImageView mIvAvatar;
    private ImageView mIvScanCode = null;
    private AbImageLoader mAbImageLoader = null;
    private SwipeRefreshLayout mSwipeRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mAbImageLoader = AbImageLoader.getInstance(this);
        registerMessageReceiver();
        getInfo();
        initView();
    }

    private void getInfo(){
        String memberId = AppApplication.getInstance().getUser().getMemberId();
        if (TextUtils.isEmpty(memberId) || memberId.equals("0")){
            return;
        }
        JSONObject object = new JSONObject();

        try {
            object .put("memberid", memberId);
            object.put("tomemberid",  memberId);

        } catch (Exception e) {
            e.printStackTrace();

        }
        post("/Member/GetMemberBase", object);
    }


    public void onRefresh(){
        getInfo();
        mSwipeRefresh.setRefreshing(false);
    }

    public void getQRcode(int isClick) {
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("objid",  AppApplication.getInstance().getUser().getMemberId());
            String isLayStaff = AppApplication.getInstance().getUser().getIsLazyStaff();
            if (!TextUtils.isEmpty(isLayStaff) && isLayStaff.equals("0")){
                object.put("type", "1");
            }else{
                object.put("type", "3");
            }

            object.put("isClick", isClick);
        } catch (Exception e) {
            e.printStackTrace();

        }
        post("/Qr/GetEncoded", object);
    }

    private MessageReceiver mMessageReceiver;
    public static final String KEY_CHANGE_AVATAR = "change_avatar";
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_CHANGEAVR_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    protected String getToolBarTitle() {
        return getString(R.string.title_login);
    }

    @Override
    protected void back() {
        onBackPressed();
    }

    public class MessageReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_CHANGEAVR_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_CHANGE_AVATAR);
                if (!TextUtils.isEmpty(messge)) {
                    mAbImageLoader.display(mIvAvatar, R.drawable.default_head, messge);
                }
            }
        }
    }

    protected void onResume() {
        super.onResume();
        initData(false);
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }

    private void initData(boolean isRefresh) {
        isLogined = AppApplication.getInstance().isUserLogin;

        if (isLogined) {
            getQRcode(0);
            if (isRefresh == true){
                mUserID = AppApplication.getInstance().getUser().getShowName();
            }else{
                String showName = getIntent().getStringExtra(MyDetailActivity.USER_NAME);
                if (TextUtils.isEmpty(showName)){
                    mUserID = AppApplication.getInstance().getUser().getShowName();
                }else{
                    mUserID = showName;
                }
            }

            mIvScanCode.setVisibility(View.VISIBLE);
        }else{
            mLLShopWallet.setVisibility(View.GONE);
            mIvScanCode.setVisibility(View.GONE);
        }

        if (isLogined) {
            mAbImageLoader.display(mIvAvatar, R.drawable.default_head, AppApplication.getInstance().getUser().getAvatar());
            mTvUserID.setText(mUserID);
            mBtnLoginNow.setVisibility(View.INVISIBLE);

            if ("0".equals(AppApplication.getInstance().getUser().getIsLazyStaff())) {
                mLLShopWallet.setVisibility(View.GONE);
            }else{
                mLLShopWallet.setVisibility(View.VISIBLE);
            }

            String levelStr = "普通会员";
            if ("1".equals(AppApplication.getInstance().getUser().getIsLazyStaff())) {
                levelStr = "创业会员";
                mbeComeStaff.setVisibility(View.GONE);
            }else{
                mbeComeStaff.setVisibility(View.VISIBLE);
            }
            mTvUserLevel.setText(levelStr);
            mTvUserLevel.setVisibility(View.VISIBLE);


        } else {
            mBtnLoginNow.setVisibility(View.VISIBLE);
            mTvUserLevel.setVisibility(View.GONE);
            mbeComeStaff.setVisibility(View.VISIBLE);
            mTvUserID.setText(R.string.label_no_login);
        }
    }

    private void initView() {
        hideToolBarBack();
        mIvScanCode = (ImageView) findViewById(R.id.iv_erweima_person);
        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
        mBtnLoginNow = (TextView) findViewById(R.id.btn_login);
        mBtnLoginNow.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mLLStaff = (LinearLayout) findViewById(R.id.ll_lazy_staff);
        mLLMyWallet = (LinearLayout) findViewById(R.id.ll_my_wallet);
        mllFavorite = (LinearLayout) findViewById(R.id.ll_my_favorite);
        mLLShopWallet = (LinearLayout) findViewById(R.id.ll_shop_wallet);
        LinearLayout oilCard = (LinearLayout) findViewById(R.id.ll_my_payoilcard);
        oilCard.setOnClickListener(this);
        mLLFeedback = (LinearLayout) findViewById(R.id.ll_feedback);
        mbeComeStaff = (LinearLayout) findViewById(R.id.ll_become_staff);
        mReferee = (LinearLayout) findViewById(R.id.ll_referee);

        //   mLLSetting = (LinearLayout) findViewById(R.id.ll_setting);
        mLLWalletLog = (LinearLayout) findViewById(R.id.ll_my_paylog);
        mLLYlMember = (LinearLayout) findViewById(R.id.ll_yl_member);
        mTvUserID = (TextView) findViewById(R.id.tv_userid);
        mTvUserLevel = (TextView) findViewById(R.id.tv_user_level);
        mLLSetting = (LinearLayout) findViewById(R.id.ll_setting);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefresh.offsetTopAndBottom(600);
        mSwipeRefresh.setOnRefreshListener(this);

        mIvAvatar.setOnClickListener(this);
        mllFavorite.setOnClickListener(this);
        mBtnLoginNow.setOnClickListener(this);
        mLLStaff.setOnClickListener(this);
        mLLMyWallet.setOnClickListener(this);
        mLLShopWallet.setOnClickListener(this);
        mLLFeedback.setOnClickListener(this);
        mIvScanCode.setOnClickListener(this);
        mLLWalletLog.setOnClickListener(this);
        mbeComeStaff.setOnClickListener(this);
        mLLYlMember.setOnClickListener(this);
        mLLSetting.setOnClickListener(this);
        mReferee.setOnClickListener(this);
    }

    private  void bandMobile(){
        Intent intent = new Intent(mContext, BandMobileCodeActivity.class);
        intent.putExtra(BandMobileCodeActivity.APPLY_STAFF, "1");
        mContext.startActivity(intent);
    }

    private void checkLogin(){
        Intent intent = null;
        if (isLogined) {
            intent = new Intent(mContext, MyDetailActivity.class);
            intent.putExtra(MyDetailActivity.USER_NAME, mTvUserID.getText());
            startActivity(intent);
        }else{
            intent = new Intent(mContext, LoginRegisterActivity.class);
            startActivity(intent);
        }
    }

    protected void onSuccessCallback(String url, String content, JSONObject param) {
        try {
            JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
            if (url.equals("/Qr/GetEncoded")){
                mStrCodeUrl = jsonObject.getString("link");
                int isClick = param.getInt("isClick");
                if (isClick == 1){
                    showErWeiMa();
                }
            }else if (url.equals("/Member/GetMemberBase")){
                User user = AppApplication.getInstance().getUser();
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
                user.setIsLazyStaff(jsonObject.getString("IsLazyStaff"));
                user.setPayPwd(jsonObject.getString("PayPwd"));
                user.setYMobile(jsonObject.getString("YMobile"));
                user.setYunAccount(jsonObject.getString("YunAccount"));
                AppApplication.getInstance().setUser(user);
                AppApplication.getInstance().saveUser2Sqlite(user);
                initData(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showErWeiMa() {
        if (TextUtils.isEmpty(mStrCodeUrl)){
            getQRcode(1);
        }else{
            final Dialog alertDialog = new Dialog(this, R.style.selectorDialog);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(R.layout.item_erweima);
            ImageView iv = (ImageView) alertDialog.findViewById(R.id.erweima_iv);
            mAbImageLoader.display(iv, mStrCodeUrl);
            View fl = alertDialog.findViewById(R.id.erweima_ly);
            fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }

    }

    public void onClick(View v) {
        int resID = v.getId();
        Intent intent = null;

        switch (resID) {
            case R.id.iv_avatar:
            case R.id.btn_login:
            case R.id.ll_lazy_staff:
                checkLogin();
                break;
            case R.id.ll_yl_member:
                if (isLogined) {
                    intent = new Intent(mContext, YunLianMemberActivity.class);
                    intent.putExtra("lego_member_avatar", AppApplication.getInstance().getUser().getAvatar());
                    intent.putExtra("lego_member_yid", AppApplication.getInstance().getUser().getYunAccount());
                    intent.putExtra("lego_member_ymobile", AppApplication.getInstance().getUser().getYMobile());
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_become_staff:
                if (isLogined) {
                    intent = new Intent(mContext, BecomeStaffActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_referee:
                if (isLogined) {
                    intent = new Intent(mContext, MyRefereeActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_shop_wallet:
                if (isLogined) {
                    intent = new Intent(mContext, ShopWalletActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_my_payoilcard:
                if (isLogined) {
                    if ("0".equals(AppApplication.getInstance().getUser().getIsLazyStaff())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TabMyActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("油卡充值仅限于创业会员，是否马上升级创业会员？");
                        builder.setPositiveButton(" 升级 ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent2 = new Intent(mContext, BecomeStaffActivity.class);
                                startActivity(intent2);
                            }
                        });
                        builder.setNegativeButton(" 取消 ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                        builder.show();
                    }else{
                        intent = new Intent(mContext, MyOilCardActivity.class);
                        startActivity(intent);
                    }

                }else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_my_paylog:
                if (isLogined) {
                    intent = new Intent(mContext, MyWalletLogActivity.class);
                    intent.putExtra("type", "-1");
                    startActivity(intent);
                }else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.ll_my_favorite:
                if (isLogined) {
                    intent = new Intent(mContext,MyFavoriteActivity.class);
                    startActivity(intent);
                }else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                    startActivity(intent);
                }


                break;
            case R.id.iv_erweima_person:
                showErWeiMa();
                break;
            case R.id.ll_my_wallet:
                if (isLogined) {
                    intent = new Intent(mContext, MyWalletActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_feedback:
                intent = new Intent(mContext, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_setting:
                intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
