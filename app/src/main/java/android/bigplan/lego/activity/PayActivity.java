package android.bigplan.lego.activity;

import java.util.Map;

import android.annotation.SuppressLint;
import android.bigplan.lego.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.app.Constants;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.pay.AuthResult;
import android.bigplan.lego.pay.PayDemoActivity;
import android.bigplan.lego.pay.PayResult;
import android.bigplan.lego.pay.util.OrderInfoUtil2_0;
import android.bigplan.lego.util.AbFileUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class PayActivity extends BaseTabActivity implements OnClickListener {
	public static PayActivity instance = null;

	private RelativeLayout rl_pay_wx_img;
	private RelativeLayout rl_pay_zfb_img;
	private RelativeLayout rl_pay_unpay_img;
	private RelativeLayout rl_pay_jb_img;
	private TextView pay_wx_img;
	private TextView pay_zfb_img;
	private TextView pay_unpay_img;
	private TextView pay_jb_img;
	private int ZFselect = 1;
	private TextView buy_itnow;

	private  String mSign = null;
	private String ordernub;
	private String order;


	private String orderdanhao;
	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	Map<String, String> resultunifiedorder;
	StringBuffer sb;
	private String productName;
	private AlertDialog dialog;
	private TextView tv_warn_message;
	private TextView but_warn_ok;
	private TextView but_warn_no;

	String mappId ;
	String mNoncestr ;
	String mOrderCode ;
	String mPackage ;
	String mPartnerId ;
	String[] money ={"1000.00元", "2000.00元", "5000.00元", "8000.00元", "10000.00元", "20000.00元"};
	String mTimeStamp ;
	String mMsg ;
	private TextView tv_pay_price;

	/** 支付宝支付业务：入参app_id */
	public static final String APPID = "2017092608935213";

	/** ：入参pid值支付宝账户登录授权业务 */
	public static final String PID = "";
	/** 支付宝账户登录授权业务：入参target_id值 */
	public static final String TARGET_ID = "";

	/** 商户私钥，pkcs8格式 */
	/** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
	/** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
	/** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
	/** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
	/** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
	public static final String RSA_PRIVATE = "";

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;
	private static final int SDK_CHECK_FLAG = 2;
	private Context context;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					@SuppressWarnings("unchecked")
					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
					/**
					 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为9000则代表支付成功
					if (TextUtils.equals(resultStatus, "9000")) {
						// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
						Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					} else {
						// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
						Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
					}
					break;
				}
				case SDK_AUTH_FLAG: {
					@SuppressWarnings("unchecked")
					AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
					String resultStatus = authResult.getResultStatus();

					// 判断resultStatus 为“9000”且result_code
					// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
					if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
						// 获取alipay_open_id，调支付时作为参数extern_token 的value
						// 传入，则支付账户为该授权账户
						Toast.makeText(PayActivity.this, "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
					} else {
						// 其他状态值则为授权失败
						Toast.makeText(PayActivity.this, "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
					}
					break;
				}
				default:
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		context = this;
		instance=this;

		initView();
		initData();
		init();

		req = new PayReq();
		sb = new StringBuffer();
//		msgApi.registerApp(Constants.APP_ID);
	}

	@Override
	protected String getToolBarTitle() {
		return  "充值";
	}

	private void initView() {
		// TODO Auto-generated method stub


		RelativeLayout	rl_chongzhi_pay = (RelativeLayout) findViewById(R.id.rl_chongzhi_pay);
		rl_pay_wx_img = (RelativeLayout) findViewById(R.id.rl_pay_wx_img);
		rl_pay_zfb_img = (RelativeLayout) findViewById(R.id.rl_pay_zfb_img);
		rl_pay_unpay_img = (RelativeLayout) findViewById(R.id.rl_pay_unpay_img);
		rl_pay_jb_img = (RelativeLayout) findViewById(R.id.rl_pay_jb_img);
		
		pay_wx_img = (TextView) findViewById(R.id.pay_wx_img);
		pay_zfb_img = (TextView) findViewById(R.id.pay_zfb_img);
		pay_unpay_img = (TextView) findViewById(R.id.pay_unpay_img);
		pay_jb_img = (TextView) findViewById(R.id.pay_jb_img);
		tv_pay_price = (TextView) findViewById(R.id.tv_pay_price);

		buy_itnow = (TextView) findViewById(R.id.buy_itnow);
		pb_pay = (ProgressBar) findViewById(R.id.pb_pay);
		pay_wx_img.setBackgroundResource(R.drawable.choosan);

		buy_itnow.setOnClickListener(this);
		rl_pay_wx_img.setOnClickListener(this);
		rl_pay_zfb_img.setOnClickListener(this);
		rl_pay_unpay_img.setOnClickListener(this);
		rl_pay_jb_img.setOnClickListener(this);
		rl_chongzhi_pay.setOnClickListener(this);

		Builder isExit = new Builder(this);
	}

	private void initData() {
		if (tv_pay_price != null){
			tv_pay_price.setText("1000.00元");
		}
	}

	private void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_pay_wx_img://微信
			// 点击选择类别
			clear();
			pay_wx_img.setBackgroundResource(R.drawable.choosan);
			ZFselect = 1;
			break;
		case R.id.rl_pay_zfb_img://支付宝
			clear();
			pay_zfb_img.setBackgroundResource(R.drawable.choosan);
			ZFselect = 2;
			break;

		case R.id.rl_pay_jb_img:
			clear();
			pay_jb_img.setBackgroundResource(R.drawable.choosan);
			ZFselect = 4;

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
		case R.id.buy_itnow:// 立即支付
			aliPay();
			break;
		default:
			break;
		}

	}

	protected void aliPay(){
		if (AbConstant.environment == 0){
			AbToastUtil.showToast(mContext, "测试环境不允许做支付测试");
			return;
		}
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
		try {
			object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
			object.put("money", result);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		post("/Wallet/GetPaySign", object);
	}

	private void clear() {
		// TODO Auto-generated method stub
		pay_wx_img.setBackgroundResource(R.drawable.choosa);
		pay_zfb_img.setBackgroundResource(R.drawable.choosa);
		pay_unpay_img.setBackgroundResource(R.drawable.choosa);
		pay_jb_img.setBackgroundResource(R.drawable.choosa);
	}

	private ProgressBar pb_pay;


	protected void onSuccessCallback(String url, String content, JSONObject param){
		super.onSuccessCallback(url, content, param);
		try {
			if (url.equals("/Wallet/GetPaySign")) {
				try {
					JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
					mSign = jsonObject.getString("Result");
					Log.d("AbLogUtil sign=", mSign);
					if (TextUtils.isEmpty(mSign)){
						AbToastUtil.showToast(mContext, R.string.error_sign_empty);
					}else{
						Runnable payRunnable = new Runnable() {
							@Override
							public void run() {
								PayTask alipay = new PayTask(PayActivity.this);
								Map<String, String> result = alipay.payV2(mSign, true);
								Log.d("AbLogUtil msp", result.toString());
								Message msg = new Message();
								msg.what = SDK_PAY_FLAG;
								msg.obj = result;
								mHandler.sendMessage(msg);
							}
						};

						Thread payThread = new Thread(payRunnable);
						payThread.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
		}
	}
	@Override
	public void onBackPressed() {
		finish();
	}
}
