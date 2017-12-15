package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONObject;

public class ResetPwdActivity extends BaseTActivity implements View.OnClickListener {
    private static final String TAG = ResetPwdActivity.class.getSimpleName();

    private EditText mEtOldPwd;
    private EditText mEtNewPwd;
    private EditText mEtConfirmNewPwd;
    private Button mBtnReset;

    private ImageView mIvOldPwd;
    private ImageView mIvNewPwd;
    private ImageView mIvConfirmNewPwd;

    private boolean mIsShowOldPwd;
    private boolean mIsShowNewPwd;
    private boolean mIsShowConfirmNewPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);

        AppApplication.getInstance().addActivity(this);

        mIsShowOldPwd = false;
        mIsShowNewPwd = false;
        mIsShowConfirmNewPwd = false;
        initView();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.title_reset_pwd);
    }

    private void initView() {
        mEtOldPwd = (EditText) findViewById(R.id.et_old_pwd);
        mEtNewPwd = (EditText) findViewById(R.id.et_new_pwd);
        mEtConfirmNewPwd = (EditText) findViewById(R.id.et_confirm_new_pwd);

        mBtnReset = (Button) findViewById(R.id.btn_reset_pwd);

        mIvOldPwd = (ImageView) findViewById(R.id.btn_show_old_pwd);
        mIvNewPwd = (ImageView) findViewById(R.id.btn_show_new_pwd);
        mIvConfirmNewPwd = (ImageView) findViewById(R.id.btn_show_confirm_new_pwd);

        mBtnReset.setOnClickListener(this);
        mIvOldPwd.setOnClickListener(this);
        mIvNewPwd.setOnClickListener(this);
        mIvConfirmNewPwd.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_old_pwd:
                mIsShowOldPwd = !mIsShowOldPwd;
                if (mIsShowOldPwd) {
                    mIvOldPwd.setBackgroundResource(R.drawable.btn_pwd_display);
                    mEtOldPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                    mEtOldPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mIvOldPwd.setBackgroundResource(R.drawable.btn_pwd_hide);
                    mEtOldPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtOldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.btn_show_new_pwd:
                mIsShowNewPwd = !mIsShowNewPwd;
                if (mIsShowNewPwd) {
                    mIvNewPwd.setBackgroundResource(R.drawable.btn_pwd_display);
                    mEtNewPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                    mEtNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mIvNewPwd.setBackgroundResource(R.drawable.btn_pwd_hide);
                    mEtNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.btn_show_confirm_new_pwd:
                mIsShowConfirmNewPwd = !mIsShowConfirmNewPwd;
                if (mIsShowConfirmNewPwd) {
                    mIvConfirmNewPwd.setBackgroundResource(R.drawable.btn_pwd_display);
                    mEtConfirmNewPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                    mEtConfirmNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mIvConfirmNewPwd.setBackgroundResource(R.drawable.btn_pwd_hide);
                    mEtConfirmNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtConfirmNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.btn_reset_pwd:
                resetPwd();
                break;
        }
    }
    private  void resetPwd(){
        String oldPwd = mEtOldPwd.getText().toString();
        String newPwd = mEtNewPwd.getText().toString();
        String confirmNewPwd = mEtConfirmNewPwd.getText().toString();
        if (TextUtils.isEmpty(oldPwd)) {
            AbToastUtil.showToast(mContext, R.string.error_pwd_old);
            mEtOldPwd.setFocusable(true);
            mEtOldPwd.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(oldPwd) < 6 ) {
            AbToastUtil.showToast(mContext, R.string.error_oldpwd_length1);
            return;
        }
        if (AbStrUtil.strLength(oldPwd) > 20) {
            AbToastUtil.showToast(mContext, R.string.error_oldpwd_length2);
            mEtOldPwd.setFocusable(true);
            mEtOldPwd.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newPwd)) {
            AbToastUtil.showToast(mContext, R.string.error_pwd_new);
            mEtNewPwd.setFocusable(true);
            mEtNewPwd.requestFocus();
            return;
        }
        if (AbStrUtil.strLength(newPwd) < 6 ) {
            AbToastUtil.showToast(mContext, R.string.error_newpwd_length1);
            mEtNewPwd.setFocusable(true);
            mEtNewPwd.requestFocus();
            return;
        }
        if (AbStrUtil.strLength(newPwd) > 20) {
            AbToastUtil.showToast(mContext, R.string.error_newpwd_length2);
            mEtNewPwd.setFocusable(true);
            mEtNewPwd.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmNewPwd)) {
            AbToastUtil.showToast(mContext, R.string.error_confirmpwd_new);
            mEtConfirmNewPwd.setFocusable(true);
            mEtConfirmNewPwd.requestFocus();
            return;
        }
        if (AbStrUtil.strLength(confirmNewPwd) < 6 ) {
            AbToastUtil.showToast(mContext, R.string.error_newpwd_length1);
            mEtConfirmNewPwd.setFocusable(true);
            mEtConfirmNewPwd.requestFocus();
            return;
        }
        if (AbStrUtil.strLength(confirmNewPwd) > 20) {
            AbToastUtil.showToast(mContext, R.string.error_newpwd_length2);
            mEtConfirmNewPwd.setFocusable(true);
            mEtConfirmNewPwd.requestFocus();
            return;
        }

        if (!confirmNewPwd.equals(newPwd)){
            AbToastUtil.showToast(mContext, R.string.error_pwd_match);
            mEtConfirmNewPwd.setFocusable(true);
            mEtConfirmNewPwd.requestFocus();
            return;
        }

        resetPwdTask(oldPwd, newPwd);
    }

    protected void onSuccessCallback(String url, String content, JSONObject param){
        AbToastUtil.showToast(mContext, R.string.reset_pwd_success);
        String newPwd = mEtNewPwd.getText().toString();
        AbSharedUtil.putString(mContext, AbConstant.KEY_SP_PASSWORD, newPwd);
        Intent msgIntent = new Intent(LoginRegisterActivity.MESSAGE_RECEIVED_ACTION);
        msgIntent.putExtra(LoginRegisterActivity.KEY_PWD_MODIFY, newPwd);
        mContext.sendBroadcast(msgIntent);
        finish();
    }
    private  void resetPwdTask(final  String oldPwd, final  String newPwd){
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("newpwd", newPwd);
            object.put("oldpwd", oldPwd);
        }
        catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
       post("/Member/ResetPwd", object);
    }
}
