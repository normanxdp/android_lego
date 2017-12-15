package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.view.RoundImageView;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class ShopTransToMoneyActivity extends BaseTActivity implements View.OnClickListener {
    private final static String TAG = ShopTransToMoneyActivity.class.getSimpleName();
    private TextView mTvMoney = null;
    private EditText mEtMoney = null;
    private EditText mEtPwd = null;

    private String maxMoney = "0";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomoney);
        initView();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.label_transmoney);
    }

    private void initView() {
        mTvMoney  = (TextView) findViewById(R.id.tv_money);
        mEtMoney = (EditText)findViewById(R.id.edit_pay_money);
        mEtPwd = (EditText)findViewById(R.id.et_pwd);

        maxMoney = getIntent().getStringExtra("max_cash_money");
        mTvMoney.setText(getString(R.string.label_transto_money) + maxMoney);


        Button button = (Button)findViewById(R.id.btn_pay);
        button.setOnClickListener(this);
    }

    private void payTask(String memberId, String money, String pwd){
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", memberId);
            object .put("money", money);
            object .put("password", pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Wallet/ToConsumeWallet", object);
    }

    private void pay(){
        String password = mEtPwd.getText().toString();
        String money = mEtMoney.getText().toString();

        if (TextUtils.isEmpty(money)) {
            AbToastUtil.showToast(mContext, R.string.hint_input_your_monety);
            mEtMoney.setFocusable(true);
            mEtMoney.requestFocus();
            return;
        }

        if (!AbStrUtil.isMoneyNumber(money)) {
            AbToastUtil.showToast(mContext, R.string.error_format_pay_input);
            mEtMoney.setFocusable(true);
            mEtMoney.requestFocus();
            return;
        }

        float nMax = Float.valueOf(maxMoney).floatValue();
        float nMoney = Float.valueOf(money).floatValue();
        if (nMoney > nMax){
            AbToastUtil.showToast(mContext, R.string.error_pay_input_max);
            mEtMoney.setFocusable(true);
            mEtMoney.requestFocus();
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
            payTask(memberId, money, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_pay:
                pay();
                break;
        }
   }

    protected void onSuccessCallback(String url, String content, JSONObject param) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        try {
           if (url.equals("/Wallet/ToConsumeWallet")){
               AbToastUtil.showToast(mContext, R.string.error_transto_success);
               finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
