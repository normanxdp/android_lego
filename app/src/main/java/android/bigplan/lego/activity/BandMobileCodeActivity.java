package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.bigplan.lego.view.MyAlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

public class BandMobileCodeActivity extends BaseTActivity implements View.OnClickListener {
    private final static String TAG = BandMobileCodeActivity.class.getSimpleName();
    private EditText mEtTele;
    private EditText mEtCode;
    private Button mBtnConfirm;
    private Button mBtnSendCode;
    private String mType;
    public  final  static String APPLY_STAFF = "BandMobileCodeActivity_Apply";


    private static final int TIMER = 3;
    private int smsStep = 60;
    private Timer timer;
    private int confirmId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_mobilecode);
        mType =  getIntent().getStringExtra(BandMobileCodeActivity.APPLY_STAFF);

        AppApplication.getInstance().addActivity(this);
        initView();


    }

    protected String getToolBarTitle() {
        return getString(R.string.str_band_mobile);
    }

    private void logout(){
        JPushInterface.stopPush(getApplicationContext());

        AppApplication.getInstance().clearLoginParams();
        Intent intent = new Intent(mContext, LoginRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    //退出账户
    private void exitUser() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Member/Logout", object);
    }

    protected void back() {
        String title  = "提示";
        String subTitle = "是否退出绑定？";
        final MyAlertDialog dialog1 = new MyAlertDialog( this)
                .builder()
                .setTitle(title)
                .setMsg(subTitle)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog1.setPositiveButton("确定", new View.OnClickListener() {
            public void onClick(View v) {
                exitUser();
            }
        });
        dialog1.show();
    }

    public void onBackPressed() {
        back();
    }
    private void initView() {
        mEtTele = (EditText) findViewById(R.id.et_telphone);
        mEtCode = (EditText) findViewById(R.id.et_verification);

        mBtnSendCode = (Button) findViewById(R.id.btn_send_code);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);

        mBtnSendCode.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_code:
                String strUserTele = mEtTele.getText().toString();
                if (TextUtils.isEmpty(strUserTele)) {
                    AbToastUtil.showToast(mContext, R.string.error_phone);
                    mEtTele.setFocusable(true);
                    mEtTele.requestFocus();
                    return;
                }
                if (AbStrUtil.strLength(strUserTele) != 11 || !AbStrUtil.isNumberLetter(strUserTele)) {
                    AbToastUtil.showToast(mContext, R.string.error_phone_input);
                    mEtTele.setFocusable(true);
                    mEtTele.requestFocus();
                    return;
                }
                getMobileVerifyCodeTask(strUserTele, AbConstant.BOUND_MOBILE);
                break;
            case R.id.btn_confirm:
                bandMobile();
                break;
        }
    }


    private void bandMobile() {
        String strSmsCode = mEtCode.getText().toString();
        String strTele = mEtTele.getText().toString();

        if (TextUtils.isEmpty(strTele)) {
            AbToastUtil.showToast(mContext, R.string.error_phone);
            mEtTele.setFocusable(true);
            mEtTele.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(strTele) != 11 || !AbStrUtil.isNumberLetter(strTele)) {
            AbToastUtil.showToast(mContext, R.string.error_phone_input);
            mEtTele.setFocusable(true);
            mEtTele.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(strSmsCode)) {
            AbToastUtil.showToast(mContext, R.string.sms_name);
            mEtCode.setFocusable(true);
            mEtCode.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(strSmsCode) < 4) {
            AbToastUtil.showToast(mContext, R.string.sms_name_length1);
            mEtCode.setFocusable(true);
            mEtCode.requestFocus();
            return;
        }

        if (!AbStrUtil.isNumberLetter(strSmsCode)) {
            AbToastUtil.showToast(mContext, R.string.sms_name_expr);
            return;
        }

        boundMobileTask(strTele, strSmsCode);

    }

    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = TIMER;
            mHandler.sendMessage(message);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case TIMER:
                    smsStep--;
                    if (smsStep == 0) {
                        mBtnSendCode.setText("获取验证码");
                        timer.cancel();
                        smsStep = 60;
                        timer = null;
                        mBtnSendCode.setClickable(true);
                    } else {
                        mBtnSendCode.setClickable(false);
                        mBtnSendCode.setText(smsStep + "秒后重新发");
                    }

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    protected void onSuccessCallback(String url, String content, JSONObject param){
        try {
            if (url.equals("/Member/Logout")){
                logout();
            }else if (url.equals("/Member/BoundMobile")) {
                JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
                String success = jsonObject.getString("Success");
                String mobile = param.getString("mobile");
                AbSharedUtil.putString(mContext, AbConstant.KEY_SP_TELPHONE, mobile);
                AppApplication.getInstance().getUser().setMobile(mobile);
                if (success.equals("1")){
                    String strTele = mEtTele.getText().toString();

                    Intent intent = new Intent(mContext, BandMobilePwdActivity.class);
                    intent.putExtra(BandMobilePwdActivity.TELEPHONE, strTele);
                    mContext.startActivity(intent);
                }else{
                    confirmId = jsonObject.getInt("ConfirmId");
                    String title = jsonObject.getString("Title");
                    String subTitle = jsonObject.getString("SubTitile");
                    final MyAlertDialog dialog1 = new MyAlertDialog( this)
                            .builder()
                            .setTitle(title)
                            .setMsg(subTitle)
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    dialog1.setPositiveButton("确定", new View.OnClickListener() {
                        public void onClick(View v) {
                            JSONObject object = new JSONObject();
                            try {
                                object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
                                object.put("confirmid", confirmId);
                            } catch (Exception ex) {
                                AbLogUtil.e(ex.getMessage());
                            }
                            post("/Member/ConfirmBoundMobile", object);
                        }
                    });
                    dialog1.show();

                }

            } else if (url.equals("/Common/GetMobileVerifyCode")) {
                AbToastUtil.showToast(mContext, R.string.send_verification_code_success);
                timer = new Timer(true);
                timer.schedule(task, 0, 1000); // 延时1000ms后执行，1000ms执行一次
            }else if (url.equals("/Member/ConfirmBoundMobile")){
                JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
                String success = jsonObject.getString("Success");
                if (success == "0"){
                    String message = jsonObject.getString("message");
                    AbToastUtil.showToast(mContext, message);

                }else{
                    String memberId = jsonObject.getString("MemberId");
                    String PassId = jsonObject.getString("PassId");
                    if (!TextUtils.isEmpty(memberId)){
                        AppApplication.getInstance().getUser().setMemberId(memberId);
                    }
                    if (!TextUtils.isEmpty(PassId)){
                        AppApplication.getInstance().getUser().setPassId(PassId);
                    }

                    Intent intent = new Intent(mContext, MainTabActivity.class);
                    mContext.startActivity(intent);
                    finish();
                }
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
    }

    public void boundMobileTask(final String strTele, final String strSmsCode) {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("mobile", strTele);
            object.put("vcode", strSmsCode);
            object.put("type", mType);

        } catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Member/BoundMobile", object);
    }

    public void getMobileVerifyCodeTask(final String strTelephone, final String type) {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", strTelephone);
            object.put("codetype", type);
        } catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Common/GetMobileVerifyCode", object);
    }
}
