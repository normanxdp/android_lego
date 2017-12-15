package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.view.RoundImageView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class BecomeStaffActivity extends BaseTActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private final static String TAG = BecomeStaffActivity.class.getSimpleName();

    private EditText mEtStoreName = null;
    private EditText mEtAddress = null;
    private EditText mEtTelphone = null;
    private EditText mEtPwd = null;
    private EditText mEtCardNum = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_staff);
        initView();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.title_become_staff);
    }

    private void initView() {
        mEtStoreName = (EditText)findViewById(R.id.edit_storenaem);
        mEtPwd = (EditText)findViewById(R.id.et_pwd);
        mEtAddress = (EditText)findViewById(R.id.edit_address);
        mEtCardNum = (EditText)findViewById(R.id.edit_idcard_num);
        mEtTelphone = (EditText)findViewById(R.id.et_telphone);
        Button button = (Button)findViewById(R.id.btn_pay);
        button.setOnClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    private void payTask(final String mobile, final String pwd, final String address, final  String storeName, final  String cardId){
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object .put("mobile", mobile);
            object .put("pwd", pwd);
            if (!TextUtils.isEmpty(address)){
                object .put("address", address);
            }
            object .put("storename", storeName);
            object .put("cardid", cardId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        post("/Member/UpgradeToStaff", object);
    }

    private void pay(){
        String password = mEtPwd.getText().toString();
        String storeName = mEtStoreName.getText().toString();
        String mobile = mEtTelphone.getText().toString();
        String address = mEtAddress.getText().toString();
        String cardId = mEtCardNum.getText().toString();

        if (TextUtils.isEmpty(storeName)) {
            AbToastUtil.showToast(mContext, R.string.error_store);
            mEtStoreName.setFocusable(true);
            mEtStoreName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(cardId)) {
            AbToastUtil.showToast(mContext, R.string.hint_input_your_idcard_num);
            mEtCardNum.setFocusable(true);
            mEtCardNum.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mobile)) {
            AbToastUtil.showToast(mContext, R.string.error_reservationnumber);
            mEtTelphone.setFocusable(true);
            mEtTelphone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            AbToastUtil.showToast(mContext, R.string.error_pwd);
            mEtPwd.setFocusable(true);
            mEtPwd.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            AbToastUtil.showToast(mContext, R.string.error_pwd);
            mEtPwd.setFocusable(true);
            mEtPwd.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(password) < 6) {
            AbToastUtil.showToast(mContext, R.string.error_pwd_length1);
            mEtPwd.setFocusable(true);
            mEtPwd.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(password) > 20) {
            AbToastUtil.showToast(mContext, R.string.error_pwd_length2);
            mEtPwd.setFocusable(true);
            mEtPwd.requestFocus();
            return;
        }
        String memberId = AppApplication.getInstance().getUser().getMemberId();
        if (TextUtils.isEmpty(memberId)){
            AbToastUtil.showToast(mContext, R.string.label_login_invalid);
            return;
        }

        try {
            payTask(mobile, password, address, storeName, cardId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                pay();
                break;
        }
   }


    protected void onSuccessCallback(String url, String content, JSONObject param) {
        try {
            if (url.equals("/Member/UpgradeToStaff")) {
                AbToastUtil.showToast(mContext, R.string.label_upgrade_success);
                AppApplication.getInstance().getUser().setIsLazyStaff("1");
                AppApplication.getInstance().getUser().setStoreName(mEtStoreName.getText().toString());
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
