package android.bigplan.lego.activity;

import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.model.BankModule;
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

/**
 * 个人资料 文 件 名: PersonalDataActivity.java 描 述: <描述>
 *
 * @author: chenzy
 * @date: 2015-7-31
 */
public class AddBankActivity extends BaseTActivity{

    private TextView tv_bank;
    private LinearLayout ly_bank, ly_mobile;
    private EditText tv_bankcard, tv_name, tv_mobile;
    private Button mBtn = null;
    public static final int REQUESTCODE_SELECT_BANK = 1;
    private String mCode = "0";

    protected  String getToolBarTitle(){
        return getString(R.string.label_addbankcard);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_activity);
        initView();
    }

    private void initView() {
        tv_name = (EditText)findViewById(R.id.tv_name);
        ly_bank = (LinearLayout)findViewById(R.id.ly_select_bank);
        tv_bank = (TextView)findViewById(R.id.tv_bank);
        tv_bankcard = (EditText)findViewById(R.id.tv_bankcard);
        ly_mobile = (LinearLayout)findViewById(R.id.ly_mobile);
        tv_mobile = (EditText)findViewById(R.id.tv_mobile);
        mBtn = (Button)findViewById(R.id.btn_confirm);

        ly_bank.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBankActivity.this, BankListActivity.class);
                startActivityForResult(intent, REQUESTCODE_SELECT_BANK);
            }
        });

        mBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                addBank();
            }
        });
		String type  = getIntent().getStringExtra("lego_add_oilcard_type");
		if (TextUtils.isEmpty(type)) {
			mCode = "0";
		}else{
			mCode = type;
		}
		
    }

    protected void addBank(){
        String name = tv_name.getText().toString();
        String card = tv_bankcard.getText().toString();
        String mobile  = tv_mobile.getText().toString();

        if (mCode.equals("0")){
            AbToastUtil.showToast(mContext, R.string.error_bank);
            return;
        }
        if (TextUtils.isEmpty(name)){
            AbToastUtil.showToast(mContext, R.string.error_person);
            tv_name.setFocusable(true);
            tv_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(card)){
            AbToastUtil.showToast(mContext, R.string.error_card);
            tv_bankcard.setFocusable(true);
            tv_bankcard.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mobile)){
            AbToastUtil.showToast(mContext, R.string.error_phone);
            tv_mobile.setFocusable(true);
            tv_mobile.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(mobile) != 11 || !AbStrUtil.isNumberLetter(mobile)) {
            AbToastUtil.showToast(mContext, R.string.error_phone_input);
            tv_mobile.setFocusable(true);
            tv_mobile.requestFocus();
            return;
        }
        addBankTask(name, mobile, mCode, card);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE_SELECT_BANK) {
            if (data != null) {
                BankModule model = (BankModule)data.getSerializableExtra("model");
                if (model != null) {
                    mCode = model.getCode();
                    String name = model.getName();
                    if (!TextUtils.isEmpty(name)) {
                        tv_bank.setText(name);
                    }
                }
            }
        }
    }

    public void addBankTask(String name, String mobile, String bankType, String card) {
        JSONObject object = new JSONObject();
        String memberId = AppApplication.getInstance().getUser().getMemberId();
        try {
            object.put("memberid", memberId);
            object.put("name", name);
            object.put("mobile", mobile);
            object.put("banktype", bankType);
            object.put("card", card);
        }
        catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Wallet/AddBankCard", object);
    }


    protected  void onSuccessCallback(String url, String content, JSONObject param){
        AbToastUtil.showToast(mContext, "添加银行卡成功");
        finish();
    }
}
