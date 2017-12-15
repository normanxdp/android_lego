package android.bigplan.lego.activity;

import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.model.BankModule;
import android.bigplan.lego.model.MyBankCard;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import android.bigplan.lego.R;


import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.view.AbTitleBar;


public class GetCashActivity extends BaseTActivity{

    private TextView tv_bank, tv_money;
    private LinearLayout ly_bank;
    private EditText tv_pwd, ev_money, tv_mobile;
    private Button mBtn = null;
    public static final int REQUESTCODE_SELECT_BANK_CARD = 2;
    private String maxMoney = "0";

    protected  String getToolBarTitle(){
        return getString(R.string.label_cash_apply);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getcash_activity);
        initView();
    }

    private void initView() {
        ev_money = (EditText)findViewById(R.id.ev_money);
        tv_money = (TextView) findViewById(R.id.tv_money);
        maxMoney = getIntent().getStringExtra("max_cash_money");
        tv_money.setText(getString(R.string.label_cash_money) + maxMoney);

        ly_bank = (LinearLayout)findViewById(R.id.ly_select_bank);
        tv_bank = (TextView)findViewById(R.id.tv_bank);
        tv_pwd = (EditText)findViewById(R.id.tv_pwd);

        tv_mobile = (EditText)findViewById(R.id.tv_mobile);
        mBtn = (Button)findViewById(R.id.btn_confirm);

        ly_bank.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetCashActivity.this, MyBankCardActivity.class);
                intent.putExtra("select_mybankcard", "1");
                startActivityForResult(intent, REQUESTCODE_SELECT_BANK_CARD);
            }
        });

        mBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                apply();
            }
        });
    }

    protected void apply(){
        String  money = ev_money.getText().toString();
        String password = tv_pwd.getText().toString();
        String card = tv_bank.getText().toString();

        if (TextUtils.isEmpty(money)){
            AbToastUtil.showToast(mContext, R.string.error_pay_input);
            ev_money.setFocusable(true);
            ev_money.requestFocus();
            return;
        }


        if (!AbStrUtil.isMoneyNumber(money)) {
            AbToastUtil.showToast(mContext, R.string.error_format_pay_input);
            ev_money.setFocusable(true);
            ev_money.requestFocus();
            return;
        }

        Float nMax = Float.parseFloat(maxMoney);
        Float nMoney = Float.parseFloat(money);
        if (nMoney > nMax){
            AbToastUtil.showToast(mContext, R.string.error_pay_input_max);
            ev_money.setFocusable(true);
            ev_money.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(card)){
            AbToastUtil.showToast(mContext, R.string.error_card);
            tv_bank.setFocusable(true);
            tv_bank.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            AbToastUtil.showToast(mContext, R.string.error_pwd);
            tv_pwd.setFocusable(true);
            tv_pwd.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(password) < 6) {
            AbToastUtil.showToast(mContext, R.string.error_pwd_length1);
            tv_pwd.setFocusable(true);
            tv_pwd.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(password) > 20) {
            AbToastUtil.showToast(mContext, R.string.error_pwd_length2);
            tv_pwd.setFocusable(true);
            tv_pwd.requestFocus();
            return;
        }

        applyTask(money, password, card);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE_SELECT_BANK_CARD) {
            if (data != null) {
                MyBankCard model = (MyBankCard)data.getSerializableExtra("model");
                if (model != null) {

                    String card = model.getBankCard();
                    if (!TextUtils.isEmpty(card)) {
                        tv_bank.setText(card);
                    }
                }
            }
        }
    }

    public void applyTask(String money, String password, String card) {
        JSONObject object = new JSONObject();
        String memberId = AppApplication.getInstance().getUser().getMemberId();
        try {
            object.put("memberid", memberId);
            object.put("money", money);
            object.put("password", password);
            object.put("card", card);
        }
        catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Wallet/GetCashApply", object);
    }


    protected  void onSuccessCallback(String url, String content, JSONObject param){
        AbToastUtil.showToast(mContext, "申请提交成功，请等待客服审核");
        finish();
    }
}
