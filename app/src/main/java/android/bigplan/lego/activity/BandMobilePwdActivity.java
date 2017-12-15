package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.model.User;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import org.json.JSONObject;

public class BandMobilePwdActivity extends BaseTActivity implements View.OnClickListener {
    private final static String TAG = BandMobilePwdActivity.class.getSimpleName();
    private EditText mEtNewPwd;
    private EditText mEtNewConformPwd;
    private Button mBtnConfirm;
    private CheckBox mIvShowPwd;
    private CheckBox mIvShowPwd2;
    public static final String TELEPHONE = "telephone";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandmobile_pwd);
        AppApplication.getInstance().addActivity(this);
        initView();
    }

    protected String getToolBarTitle() {
        return getString(R.string.title_set_pwd);
    }

    private void initView() {
        mEtNewPwd = (EditText) findViewById(R.id.et_pwd);
        mEtNewConformPwd = (EditText) findViewById(R.id.et_pwd_conform);

        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mIvShowPwd = (CheckBox) findViewById(R.id.btn_show_pwd);
        mIvShowPwd2 = (CheckBox) findViewById(R.id.btn_show_pwd2);

        mBtnConfirm.setOnClickListener(this);

        mIvShowPwd2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEtNewConformPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                    mEtNewConformPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEtNewConformPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtNewConformPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

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
            case R.id.btn_confirm:
                setPwdTask();
                break;
        }
    }

    private void setPwdTask() {
        String strPwd = mEtNewPwd.getText().toString();
        String strConformPwd = mEtNewConformPwd.getText().toString();

        if (AbStrUtil.strLength(strPwd) < 6 || AbStrUtil.strLength(strPwd) < 6) {
            AbToastUtil.showToast(mContext, R.string.error_pwd_length1);
            return;
        }
        if (AbStrUtil.strLength(strConformPwd) < 6 || AbStrUtil.strLength(strConformPwd) < 6) {
            AbToastUtil.showToast(mContext, R.string.error_pwd_length1);
            return;
        }

        if (!strConformPwd.equals(strPwd)){
            AbToastUtil.showToast(mContext, R.string.error_pwd_match);
            return;
        }
        String strTele = getIntent().getStringExtra(BandMobilePwdActivity.TELEPHONE);
        setPwdTask(strTele, strPwd);
    }

    protected void onSuccessCallback(String url, String content, JSONObject param){
        try {
            if (url.equals("/Member/UpdateBoundMobilePassword")) {
                String strPwd = mEtNewPwd.getText().toString();
                String strTele = getIntent().getStringExtra(BandMobilePwdActivity.TELEPHONE);
                AbSharedUtil.putString(mContext, AbConstant.KEY_SP_TELPHONE, strTele);
                AbSharedUtil.putString(mContext, AbConstant.KEY_SP_PASSWORD, strPwd);

                AppApplication.getInstance().getUser().setMobile(strTele);
                Intent intent = new Intent(mContext, MyDetailActivity.class);
                mContext.startActivity(intent);
                finish();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
    }

    public void setPwdTask(final String strTele, final String strPwd) {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("mobile", strTele);
            object.put("password", strPwd);
        } catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Member/UpdateBoundMobilePassword", object);
    }
}
