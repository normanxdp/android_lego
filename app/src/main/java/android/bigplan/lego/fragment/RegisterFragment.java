package android.bigplan.lego.fragment;

import android.app.Activity;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.LoginRegisterActivity;
import android.bigplan.lego.activity.MainTabActivity;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.model.User;
import android.bigplan.lego.task.thread.AbThreadFactory;
import android.bigplan.lego.util.AbAppUtil;
import android.bigplan.lego.util.AbJsonUtil;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gg on 16-3-26.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = RegisterFragment.class.getSimpleName();
    private LoginRegisterActivity loginRegisterActivity;

    private Button mBtnSendCode;
    private Button mBtnRegister;
    private CheckBox mBtnShowPwd;
    private Button mBtnPtotocol;

    private EditText mEtUserTele;
    private EditText mEtCode;
    private EditText mEtPwd;
    private EditText mGuid;

    private static final int TIMER = 3;
    private int smsStep = 60;
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container,
                false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        loginRegisterActivity = (LoginRegisterActivity) activity;
    }

    private void initView() {
        mEtUserTele = (EditText) getView().findViewById(R.id.et_telphone);
        mEtCode = (EditText) getView().findViewById(R.id.et_verification);
        mEtPwd = (EditText) getView().findViewById(R.id.et_pwd);
        mGuid = (EditText) getView().findViewById(R.id.et_guid);

        mBtnRegister = (Button) getView().findViewById(R.id.btn_register);
        mBtnSendCode = (Button) getView().findViewById(R.id.btn_send_code);
        mBtnShowPwd = (CheckBox) getView().findViewById(R.id.btn_show_pwd);

        mBtnPtotocol = (Button) getView().findViewById(R.id.btn_user_protocol);

        mBtnRegister.setOnClickListener(this);
        mBtnSendCode.setOnClickListener(this);
        mBtnPtotocol.setOnClickListener(this);

        mBtnShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    mEtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                registerUser();
                break;
            case R.id.btn_send_code:
                String strUserTele = mEtUserTele.getText().toString();
                if (TextUtils.isEmpty(strUserTele)) {
                    AbToastUtil.showToast(getContext(), R.string.error_phone);
                    mEtUserTele.setFocusable(true);
                    mEtUserTele.requestFocus();
                    return;
                }
                if (AbStrUtil.strLength(strUserTele) != 11 || !AbStrUtil.isNumberLetter(strUserTele)) {
                    AbToastUtil.showToast(getContext(), R.string.error_phone_input);
                    mEtUserTele.setFocusable(true);
                    mEtUserTele.requestFocus();
                    return;
                }
                getMobileVerifyCodeTask(strUserTele, AbConstant.REGISTER_USER);
                break;
            case R.id.btn_user_protocol:
                break;
        }
    }

    private void registerUser() {
        String strPwd = mEtPwd.getText().toString();
        String strSmsCode = mEtCode.getText().toString();
        String strTele = mEtUserTele.getText().toString();

        if (TextUtils.isEmpty(strTele)) {
            AbToastUtil.showToast(getContext(), R.string.error_phone);
            mEtUserTele.setFocusable(true);
            mEtUserTele.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(strTele) != 11 || !AbStrUtil.isNumberLetter(strTele)) {
            AbToastUtil.showToast(getContext(), R.string.error_phone_input);
            mEtUserTele.setFocusable(true);
            mEtUserTele.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(strPwd)) {
            AbToastUtil.showToast(getContext(), R.string.error_pwd);
            mEtPwd.setFocusable(true);
            mEtPwd.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(strPwd) < 6) {
            AbToastUtil.showToast(getContext(), R.string.error_pwd_length1);
            return;
        }
        if (AbStrUtil.strLength(strPwd) > 20) {
            AbToastUtil.showToast(getContext(), R.string.error_pwd_length2);
            mEtPwd.setFocusable(true);
            mEtPwd.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(strSmsCode)) {
            AbToastUtil.showToast(getContext(), R.string.sms_name);
            mEtCode.setFocusable(true);
            mEtCode.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(strSmsCode) < 4) {
            AbToastUtil.showToast(getContext(), R.string.sms_name_length1);
            mEtCode.setFocusable(true);
            mEtCode.requestFocus();
            return;
        }

        if (!AbStrUtil.isNumberLetter(strSmsCode)) {
            AbToastUtil.showToast(getContext(), R.string.sms_name_expr);
            return;
        }
        String guid = mGuid.getText().toString();
        if (!TextUtils.isEmpty(guid)){
            if (AbStrUtil.strLength(guid) > 11){
                AbToastUtil.showToast(getContext(), R.string.error_id_input);
                mGuid.setFocusable(true);
                mGuid.requestFocus();
                return;
            }else if (AbStrUtil.strLength(guid) < 11) {
                if (!AbStrUtil.isNumber(guid)){
                    AbToastUtil.showToast(getContext(), R.string.error_id_input);
                    mGuid.setFocusable(true);
                    mGuid.requestFocus();
                    return;
                }
            }else{
                if (!AbStrUtil.isNumberLetter(guid)){
                    AbToastUtil.showToast(getContext(), R.string.error_id_input);
                    mGuid.setFocusable(true);
                    mGuid.requestFocus();
                    return;
                }
            }
        }

        registerUserTask(strTele, strPwd, strSmsCode, guid);

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
    public void onDetach() {
        super.onDetach();
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        loginRegisterActivity = null;
    }

    public void registerUserTask(final String strTele, final String strPwd, final String strSmsCode, final String guid) {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", strTele);
            object.put("password", strPwd);
            object.put("vcode", strSmsCode);
            object.put("deviceno", AbAppUtil.getImeiNumber(getContext()));
            object.put("devicetype", "2");
            if (!TextUtils.isEmpty(guid)){
                object.put("guid", guid);
            }
        } catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Member/Register", object);
    }


    public void getMobileVerifyCodeTask(final String strUserTele, final String type) {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", strUserTele);
            object.put("codetype", type);
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Common/GetMobileVerifyCode", object);
    }

    protected  void onSuccessCallback(String url, String content, JSONObject param){
        try {
            if (url.equals("/Common/GetMobileVerifyCode")) {
                AbToastUtil.showToast(getContext(), R.string.send_verification_code_success);
                timer = new Timer(true);
                timer.schedule(task, 0, 1000); // 延时1000ms后执行，1000ms执行一次
            }else if (url.equals("/Member/Register")){
                AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
                String id = new JSONObject(abResult.getData().toString()).getString("Id");
                Logger.d(TAG, id);
                String strTele = param.getString("mobile");
                String strPwd = param.getString("password");
                AbSharedUtil.putString(getContext(), AbConstant.KEY_SP_TELPHONE, strTele);
                AbSharedUtil.putString(getContext(), AbConstant.KEY_SP_PASSWORD, strPwd);
                AbToastUtil.showToast(getContext(), R.string.register_success);
                if (loginRegisterActivity != null) {
                    loginRegisterActivity.onSwitchFragment(R.id.btn_login);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
