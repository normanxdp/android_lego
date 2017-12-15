package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.view.RoundImageView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ShopPayActivity extends BaseTActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private final static String TAG = ShopPayActivity.class.getSimpleName();

    private RoundImageView mAvatar = null;
    private TextView mTvShop = null;
    private Button mBtnPay = null;
    private EditText mEtMoney = null;
    private EditText mEtPwd = null;
    private AbImageLoader mAbImageLoader = null;
    private String mStaffId = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_pay);
        mAbImageLoader = AbImageLoader.getInstance(this);
        initView();
        requestData();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.label_my_pay);
    }

    private void initView() {
        mAvatar  = (RoundImageView)findViewById(R.id.iv_avatar);
        mTvShop = (TextView)findViewById(R.id.tx_shop);

        mBtnPay  = (Button)findViewById(R.id.btn_pay);
        mBtnPay.setOnClickListener(this);

        mEtMoney = (EditText)findViewById(R.id.edit_pay_money);
        mEtPwd = (EditText)findViewById(R.id.et_pwd);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    private void payTask(String memberId, String money, String pwd){
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", memberId);
            object .put("tomemberid", mStaffId);
            object .put("money", money);
            object .put("password", pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Wallet/Pay", object);
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
            AbToastUtil.showToast(mContext, R.string.error_pay_input);
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
        if (memberId.equals(mStaffId)){
            AbToastUtil.showToast(mContext, R.string.label_pay_error_self);
            return;
        }
        if (mBtnPay != null){
            mBtnPay.setClickable(false);
            mBtnPay.setText(getString(R.string.label_btn_paying));
        }
        payTask(memberId, money, password);
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
    private void requestData() {
        mStaffId = getIntent().getStringExtra("staffid");
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", mStaffId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Member/GetStoreDetail", object);
    }
    protected void onFailureCallback(String url, Throwable error){
        if (url.equals("/Wallet/Pay")) {
            if (mBtnPay != null){
                mBtnPay.setClickable(true);
                mBtnPay.setText(getString(R.string.label_btn_pay));
            }
        } else {
            super.onFailureCallback(url, error);
        }
    }
    protected void onSuccessCallback(String url, String content, JSONObject param) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        try {
            if (url.equals("/Member/GetStoreDetail")) {
                JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
                String realName = jsonObject.getString("RealName");
                String storeName = jsonObject.getString("StoreName");
                String avatar = jsonObject.getString("Avatar");
                if (TextUtils.isEmpty(storeName)){
                    storeName = realName;
                }
                mAbImageLoader.display(mAvatar, R.drawable.default_head, avatar);
                mTvShop.setText(storeName);
            }else if (url.equals("/Wallet/Pay")){
                Intent intent = new Intent(mContext, MyPaySuccessActivity.class);
                intent.putExtra("title", "支付结果");
                intent.putExtra("pay_success", "支付成功");
                String name = mTvShop.getText().toString();
                intent.putExtra("lego_pay_member_name", name);
                intent.putExtra("lego_pay_money", param.getString("money"));
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
