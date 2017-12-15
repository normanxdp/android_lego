package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ForgetPwdActivity extends BaseTActivity implements View.OnClickListener {
    private final static String TAG = FeedbackActivity.class.getSimpleName();
    private EditText mEtTele;
    private EditText mEtCode;
    private EditText mEtNewPwd;
    private Button mBtnConfirm;
    private Button mBtnSendCode;
    private CheckBox mIvShowPwd;

    private boolean mIsShowPwd = false;

    private static final int TIMER = 3;
    private int smsStep = 60;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        AppApplication.getInstance().addActivity(this);
        initView();
    }

    protected String getToolBarTitle() {
        return getString(R.string.title_forget_pwd);
    }

    private void initView() {
        mEtTele = (EditText) findViewById(R.id.et_telphone);
        mEtCode = (EditText) findViewById(R.id.et_verification);
        mEtNewPwd = (EditText) findViewById(R.id.et_pwd);

        mBtnSendCode = (Button) findViewById(R.id.btn_send_code);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mIvShowPwd = (CheckBox) findViewById(R.id.btn_show_pwd);

        mBtnSendCode.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        mIvShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEtNewPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                    mEtNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEtNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
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
                getMobileVerifyCodeTask(strUserTele, AbConstant.FORGET_PASSWORD);
                break;
            case R.id.btn_confirm:
                forgetPwd();
                break;
        }
    }


    private void forgetPwd() {
        String strPwd = mEtNewPwd.getText().toString();
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

        if (AbStrUtil.strLength(strPwd) < 6 || AbStrUtil.strLength(strPwd) < 6) {
            AbToastUtil.showToast(mContext, R.string.error_pwd_length1);
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

        forgetPwdTask(strTele, strPwd, strSmsCode);

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
            if (url.equals("/Member/ResetPwdByForget")) {
                String strTele = mEtTele.getText().toString();
                String strPwd = mEtNewPwd.getText().toString();
                AbToastUtil.showToast(mContext, R.string.reset_pwd_success);
                AbSharedUtil.putString(mContext, AbConstant.KEY_SP_TELPHONE, strTele);
                AbSharedUtil.putString(mContext, AbConstant.KEY_SP_PASSWORD, "");
                Intent msgIntent = new Intent(LoginRegisterActivity.MESSAGE_RECEIVED_ACTION);
                msgIntent.putExtra(LoginRegisterActivity.KEY_PWD_MODIFY, strPwd);
                mContext.sendBroadcast(msgIntent);
                finish();
            } else if (url.equals("/Common/GetMobileVerifyCode")) {
                AbToastUtil.showToast(mContext, R.string.send_verification_code_success);
                timer = new Timer(true);
                timer.schedule(task, 0, 1000); // 延时1000ms后执行，1000ms执行一次
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
    }

    public void forgetPwdTask(final String strTele, final String strPwd, final String strSmsCode) {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", strTele);
            object.put("newpwd", strPwd);
            object.put("vcode", strSmsCode);
        } catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Member/ResetPwdByForget", object);
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
