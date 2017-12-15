package android.bigplan.lego.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.model.MyBankCard;
import android.bigplan.lego.pay.AuthResult;
import android.bigplan.lego.pay.PayResult;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class PayOilActivity extends BaseTabActivity implements OnClickListener {
	public static PayOilActivity instance = null;

	private String mOilType = "1";
	private Button buy_itnow;

	String[] money ={"1000.00元", "2000.00元", "3000.00元", "4000.00元", "5000.00元"};
	private TextView tv_pay_price;
	private TextView tv_card_id;

	private EditText mEditPayPwd = null;
	private String mOilCard = null;
	public static final int REQUESTCODE_SELECT_OIL_CARD = 1000;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_oil);

		initView();
		initData();
		init();
	}

	@Override
	protected String getToolBarTitle() {
		return  getString(R.string.label_lazy_oil_chongzhi);
	}

	private void initView() {
		// TODO Auto-generated method stub


		RelativeLayout	rl_chongzhi_pay = (RelativeLayout) findViewById(R.id.rl_chongzhi_pay);
		tv_pay_price = (TextView) findViewById(R.id.tv_pay_price);

		RelativeLayout	rl_select_oiltype = (RelativeLayout) findViewById(R.id.rl_select_oiltype);
		tv_card_id = (TextView) findViewById(R.id.rl_select_oilcardid);

		buy_itnow = (Button) findViewById(R.id.buy_itnow);
		buy_itnow.setOnClickListener(this);

		mEditPayPwd = (EditText) findViewById(R.id.et_pwd);
		rl_chongzhi_pay.setOnClickListener(this);
		rl_select_oiltype.setOnClickListener(this);
	}

	private void initData() {
		if (tv_pay_price != null){
			tv_pay_price.setText("1000.00元");
		}
	}

	private void init() {
		// TODO Auto-generated method stub

	}

	private void gotoMyOilCardList(){
		Intent intent = new Intent(mContext, MyOilCardListActivity.class);

		intent.putExtra("select_myoilcard", "1");
		startActivityForResult(intent, REQUESTCODE_SELECT_OIL_CARD);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUESTCODE_SELECT_OIL_CARD) {
			if (data != null) {
				MyBankCard model = (MyBankCard)data.getSerializableExtra("model");
				if (model != null) {

					String card = model.getBankCard();
					mOilType = model.getBankType();
					String name = model.getName();
					String realName = model.getRealName();
					if (!TextUtils.isEmpty(card)) {
						mOilCard = card;
						String showText = card;
						if (!TextUtils.isEmpty(realName)){
							showText = showText + "("+realName+")";
						}
						showText = showText+" "+name;
						tv_card_id.setText(showText);
					}
				}
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_select_oiltype://支付宝
			gotoMyOilCardList();
			break;
		case R.id.rl_chongzhi_pay:
			Builder builder = new Builder(this);
			builder.setSingleChoiceItems(
					money, 0,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
								default:
									tv_pay_price.setText(money[which]);
									break;
							}
							dialog.dismiss();
						}
					});
			builder.show();
			break;
		case R.id.buy_itnow:
			aliPay();
			break;
		default:
			break;
		}
	}

	protected void aliPay(){

		JSONObject object = new JSONObject();
		String money = tv_pay_price.getText().toString();
		if (TextUtils.isEmpty(money)) {
			AbToastUtil.showToast(mContext, getString(R.string.error_pay_input));
			return;
		}
		String result = money.replace("元","");
		if (!AbStrUtil.isMoneyNumber(result)) {
			AbToastUtil.showToast(mContext, R.string.error_pay_input);
			return;
		}
		if (TextUtils.isEmpty(mOilCard)){
			AbToastUtil.showToast(mContext, R.string.label_select_your_oilcard);
			return;
		}
		String password = mEditPayPwd.getText().toString();
		if (TextUtils.isEmpty(password)) {
			AbToastUtil.showToast(mContext, R.string.error_pwd);
			mEditPayPwd.setFocusable(true);
			mEditPayPwd.requestFocus();
			return;
		}

		if (AbStrUtil.strLength(password) < 6) {
			AbToastUtil.showToast(mContext, R.string.error_pwd_length1);
			mEditPayPwd.setFocusable(true);
			mEditPayPwd.requestFocus();
			return;
		}

		if (AbStrUtil.strLength(password) > 20) {
			AbToastUtil.showToast(mContext, R.string.error_pwd_length2);
			mEditPayPwd.setFocusable(true);
			mEditPayPwd.requestFocus();
			return;
		}

		if (buy_itnow != null){
			buy_itnow.setClickable(false);
			buy_itnow.setText(getString(R.string.label_btn_paying));
		}
		try {
			object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
			object.put("money", result);
			object.put("type", mOilType);
			object.put("card", mOilCard);
			object.put("password", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post("/Wallet/RechargeForOilCard", object);
	}

	protected void onFailureCallback(String url, Throwable error){
		if (url.equals("/Wallet/RechargeForOilCard")) {
			if (buy_itnow != null){
				buy_itnow.setClickable(true);
				buy_itnow.setText(getString(R.string.order_pay_queding));
			}
		} else {
			super.onFailureCallback(url, error);
		}
	}

	protected void onShowErrorMsg(String url, String msg) {
		buy_itnow.setClickable(true);
		buy_itnow.setText(getString(R.string.order_pay_queding));
		super.onShowErrorMsg(url, msg);
	}

	protected void onSuccessCallback(String url, String content, JSONObject param){
		super.onSuccessCallback(url, content, param);
		try {
			if (url.equals("/Wallet/RechargeForOilCard")) {
				AbToastUtil.showToast(mContext, getString(R.string.label_pay_oilcard_charge));
				buy_itnow.setClickable(true);
				buy_itnow.setText(getString(R.string.order_pay_queding));
				Intent intent = new Intent(mContext, MyPaySuccessActivity.class);
				intent.putExtra("title", "充卡结果");
				intent.putExtra("pay_success", "充卡成功");

				intent.putExtra("lego_pay_member_name", "");
				intent.putExtra("lego_pay_money", param.getString("money"));
				startActivity(intent);
				finish();
			}
		} catch (Exception ex) {
		}
	}
	@Override
	public void onBackPressed() {
		finish();
	}
}
