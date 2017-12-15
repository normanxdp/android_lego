package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.model.BankModule;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;

public class AddOilCardActivity extends BaseTActivity{

    private TextView tv_bank;
    private LinearLayout ly_bank, ly_mobile;
    private EditText tv_bankcard, tv_mobile, tv_name;
    private Button mBtn = null;
    public static final int REQUESTCODE_SELECT_BANK = 1;
    private String mCode = "0";


    protected  String getToolBarTitle(){
        return getString(R.string.label_addoilcard);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_oil_activity);
        initView();
    }

    private void initView() {

        ly_bank = (LinearLayout)findViewById(R.id.ly_select_bank);
        tv_bank = (TextView)findViewById(R.id.tv_bank);
        tv_bankcard = (EditText)findViewById(R.id.tv_bankcard);
        ly_mobile = (LinearLayout)findViewById(R.id.ly_mobile);
        tv_mobile = (EditText)findViewById(R.id.tv_mobile);
        tv_name = (EditText)findViewById(R.id.tv_oilcard_name);
        LinearLayout ll_oilCardtype = (LinearLayout)findViewById(R.id.ll_oiltype);
        mBtn = (Button)findViewById(R.id.btn_confirm);

        RadioGroup group = (RadioGroup)this.findViewById(R.id.radiogroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

           public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int radioButtonId = arg0.getCheckedRadioButtonId();
                if (radioButtonId == R.id.radio1){
                    mCode = "0";
                }else{
                    mCode = "1";
                }
           }
        });
        String type = getIntent().getStringExtra("lego_add_oilcard_type");

        if (TextUtils.isEmpty(type)){
            group.setVisibility(View.VISIBLE);
            ll_oilCardtype.setVisibility(View.VISIBLE);
        }else{
            mCode = type;
            group.setVisibility(View.GONE);
            ll_oilCardtype.setVisibility(View.GONE);
            if ("1".equals(mCode)){
                setToolBarTitle("增加中石化油卡");
            }else{
                setToolBarTitle("增加中石油油卡");
            }

        }

        ly_bank.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddOilCardActivity.this, BankListActivity.class);
                startActivityForResult(intent, REQUESTCODE_SELECT_BANK);
            }
        });

        mBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                addOilCard();
            }
        });
    }

    protected void addOilCard(){

        String card = tv_bankcard.getText().toString();
        String mobile  = tv_mobile.getText().toString();
        String name = tv_name.getText().toString();

        if (TextUtils.isEmpty(card)){
            AbToastUtil.showToast(mContext, R.string.error_oilcard);
            tv_bankcard.setFocusable(true);
            tv_bankcard.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)){
            AbToastUtil.showToast(mContext, R.string.error_oil_person);
            tv_name.setFocusable(true);
            tv_name.requestFocus();
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
        addOilCardTask(mobile, mCode, card, name);
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

    public void addOilCardTask(String mobile, String cardType, String card, String name) {
        JSONObject object = new JSONObject();
        String memberId = AppApplication.getInstance().getUser().getMemberId();
        try {
            object.put("memberid", memberId);
            object.put("mobile", mobile);
            object.put("name", name);
            object.put("cardtype", cardType);
            object.put("card", card);
        }

        catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Wallet/AddOilCard", object);
    }


    protected  void onSuccessCallback(String url, String content, JSONObject param){
        AbToastUtil.showToast(mContext, "添加油卡成功");
        finish();
    }
}
