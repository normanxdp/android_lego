package android.bigplan.lego.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.bigplan.lego.R;
import android.bigplan.lego.app.Constants;
import android.bigplan.lego.util.AbToastUtil;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this,  Constants.WXAPP_ID );

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		AbToastUtil.showToast(this, "在onresp里面谈土司==" + resp.errCode);
//		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			int code = resp.errCode;
			String msg = "";
			switch (code) {
			case 0:
				msg = "支付成功！";
				AbToastUtil.showToast(this, "支付成功==" + resp.errCode);
				break;
			case -1:
				msg = "支付失败！";
				AbToastUtil.showToast(this, "支付失败==" + resp.errCode);
				break;
			case -2:
				msg = "您取消了支付！";
				break;

			default:
				msg = "支付失败！";
				break;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage(msg);

			builder.setNegativeButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					WXPayEntryActivity.this.finish();
				}
			});
			builder.show();
			
			
//			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
//			builder2.setTitle("提示");
//			builder2.setMessage("支付结果" + resp.errStr + ";code="
//					+ String.valueOf(resp.errCode));
//			builder2.show();
		}
	}
}