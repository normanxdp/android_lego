package android.bigplan.lego.wxapi;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import android.app.Activity;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.BandMobileCodeActivity;
import android.bigplan.lego.activity.BaseTActivity;
import android.bigplan.lego.activity.MainTabActivity;
import android.bigplan.lego.activity.MyDetailActivity;
import android.bigplan.lego.activity.TabMyActivity;
import android.bigplan.lego.app.AppApplication;

import android.bigplan.lego.app.Constants;
import android.bigplan.lego.fragment.LoginFragment;

import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;

import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.activity.MainTabActivity;
import android.bigplan.lego.model.User;
import android.bigplan.lego.task.thread.AbThreadFactory;
import android.bigplan.lego.util.AbAppUtil;
import android.bigplan.lego.util.AbFileUtil;
import android.bigplan.lego.util.AbImageUtil;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class WXEntryActivity extends BaseTActivity implements IWXAPIEventHandler {
	private  String mNumber, mNickName, mAvatar;
	private  String reqState = null;
	private  boolean mbFirst = true;

	private final static String TAG = WXEntryActivity.class.getSimpleName();
//	private Bundle bundle;
	//这个实体类是我自定义的实体类，用来保存第三方的数据的实体类
//	private ThirdUserInfo info= null;

	protected String getToolBarTitle(){
	return  "";
}
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	super.setContentView(0);
//		AppManager. getAppManager().addActivity(WXEntryActivity. this);
	if(LoginFragment. mIwapi!=null) {
		LoginFragment.mIwapi.handleIntent(getIntent(), WXEntryActivity.this);//必须调用此句话
	}
	if(MyDetailActivity. mIwapi!=null) {
		MyDetailActivity. mIwapi.handleIntent(getIntent(), WXEntryActivity.this);  //必须调用此句话
	}

}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(LoginFragment. mIwapi!=null) {
			LoginFragment.mIwapi.handleIntent(intent, WXEntryActivity.this);//必须调用此句话
		}
		if(MyDetailActivity. mIwapi!=null) {
			MyDetailActivity. mIwapi.handleIntent(intent, WXEntryActivity.this);  //必须调用此句话
		}
	}

	@Override
	public void onReq(BaseReq req) {
		System. out.println();
	}
	/**
	 * Title: onResp
	 *
	 *  API：https://open.weixin.qq.com/ cgi- bin/showdocument ?action=dir_list&t=resource/res_list&verify=1&id=open1419317853 &lang=zh_CN
	 * Description:在此处得到Code之后调用https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
	 *  获取到token和openID。之后再调用https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID 获取用户个人信息
	 *
	 * @param
	 *
	 * @see com.tencent.mm.sdk.openapi.IWXAPIEventHandler# (com.tencent.mm.sdk.openapi. )
	 */
	@Override
	public void onResp(BaseResp resp ) {
//		bundle=getIntent().getExtras();
//		AbToastUtil.showToast(WXEntryActivity.this,"onResp id  "+arg0.openId);
//		SendAuth.Resp resp = new SendAuth.Resp( bundle);
		//获取到code之后，需要调用接口获取到access_token
//		AbToastUtil.showToast(WXEntryActivity.this,"onResp id  "+arg0.openId+   "    "+resp. errCode);

		AbLogUtil.d("resp.errCode:" + resp.errCode);
		if (resp.errCode == BaseResp.ErrCode. ERR_OK) {
//			String code = resp. token;
//			String code = resp. code;
//			if(LoginFragment. isWxLogin){
////				getToken(code);
//			} else{
//				WXEntryActivity. this.finish();
//			}
//			AbToastUtil.showToast(WXEntryActivity.this,"onResp  "+resp. errCode);//49w1
			if (resp instanceof SendAuth.Resp){

				SendAuth.Resp newResp = (SendAuth.Resp) resp;

				//获取微信传回的code
				String code = newResp.code;
				String openId = newResp.openId;
				reqState = newResp.state;
				AbLogUtil.d("newResp.state:" + newResp.state);
				getToken(code);
			}
		} else {
			WXEntryActivity. this.finish();
		}


	}

	public void getToken(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
				"appid=" + Constants.WXAPP_ID +
				"&secret=" + Constants.APPSECRET+
				"&code=" +code+
				"&grant_type=authorization_code";
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("key", "getToken");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		get(url, jsonParams);

	}

	//获取到token和openID之后，调用此接口得到身份信息
	private void getUserInfo(String token,String openID){
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" +token+"&openid=" +openID;
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("key", "getUserInfo");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		get(url, jsonParams);
	}

	private  void bandMobile(String type){
		Intent intent = new Intent(WXEntryActivity.this, BandMobileCodeActivity.class);
		intent.putExtra(BandMobileCodeActivity.APPLY_STAFF, type);
		startActivity(intent);
	}

	public void bandUser(String response){
		JSONObject object = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(response);
			String openid = jsonObject.getString("openid");
			object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
			object.put("type", 2);
			object.put("number",openid);//唯一值相当QQ号码
		} catch (Exception ex) {
			Logger.e(TAG, ex.getMessage());
		}
		post("/Member/BoundQuickUser", object);
	}

	public void loginSanFangTask(String response) {
		AbFileUtil.writeByteArrayToSD("",response.toString(),true);
		JSONObject object = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(response);
			String nickname = jsonObject.getString("nickname");
			String figureurl_qq_2 = jsonObject.getString("headimgurl");
			String openid = jsonObject.getString("openid");

			object.put("type", 2);
			object.put("number",openid);//唯一值相当QQ号码
			object.put("avatar", figureurl_qq_2);
			object.put("nickname", nickname);
			object.put("deviceno", AbAppUtil.getImeiNumber(this));
			object.put("devicetype", "2");
			mNumber = openid;
			mAvatar = figureurl_qq_2;
			mNickName = nickname;
		} catch (Exception ex) {
		}

		post("/Member/QuickLogin", object);

	}

	protected void onFailureCallback(String url, Throwable error){
		if (url.equals("/Member/BoundQuickUser")){
			AbToastUtil.showToast(mContext, error.getMessage());
			finish();
		}else {
			AbToastUtil.showToast(WXEntryActivity.this, "登陆失 败请重试"+error.getMessage());
			finish();
			AppApplication.getInstance().isUserLogin = false;
		}

	}
	protected void onSuccessCallback(String url, String content, JSONObject param){
		try {
			if (url.equals("/Member/QuickLogin")){
				AbSharedUtil.putString(WXEntryActivity.this, AbConstant.KEY_QUICK_USERNAME, mNumber);
				AbSharedUtil.putString(WXEntryActivity.this, AbConstant.KEY_QUICK_USER_TYPE, "2");
				AbSharedUtil.putString(WXEntryActivity.this, AbConstant.KEY_QUICK_USER_AVATAR, mAvatar);
				AbSharedUtil.putString(WXEntryActivity.this, AbConstant.KEY_QUICK_USER_NICKNAME, mNickName);
				JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
				User user = AppApplication.getInstance().getUser();
				user.setMemberId(jsonObject.getString("MemberId"));
				user.setPassId(jsonObject.getString("PassId"));
				user.setMobile(jsonObject.getString("Mobile"));
				user.setAvatar(jsonObject.getString("Avatar"));
				user.setSex(jsonObject.getString("Sex"));
				user.setRealName(jsonObject.getString("RealName"));
				user.setNickname(jsonObject.getString("Nickname"));
				user.setStoreName(jsonObject.getString("StoreName"));
				user.setFirstLetter(jsonObject.getString("FirstLetter"));
				user.setQQ(jsonObject.getString("QQ"));
				user.setWeChatId(jsonObject.getString("WeChatId"));
				user.setIntroduce(jsonObject.getString("Introduce"));
				user.setState(jsonObject.getString("State"));
				user.setCreateTime(jsonObject.getString("CreateTime"));
				user.setIsLazyStaff(jsonObject.getString("IsLazyStaff"));
				user.setSinopecAccountCount(jsonObject.getString("SinopecAccountCount"));
				user.setPetroChinaAccountCount(jsonObject.getString("PetroChinaAccountCount"));
				user.setOilMoney(jsonObject.getString("OilMoney"));
				user.setPayPwd(jsonObject.getString("PayPwd"));
				user.setIntroduction(jsonObject.getString("Introduction"));
				AppApplication.getInstance().setUser(user);
				AppApplication.getInstance().saveUser2Sqlite(user);
				AppApplication.getInstance().isUserLogin = true;
				String mobile = jsonObject.getString("Mobile");

				if (TextUtils.isEmpty(mobile)){
					bandMobile("2");
				}else{
					Intent intent = new Intent(WXEntryActivity.this, MainTabActivity.class);
					startActivity(intent);
				}
			}else if (url.equals("/Member/BoundQuickUser")) {
				String number = param.getString("number");
				AppApplication.getInstance().getUser().setWeChatId(number);
				Intent intent = new Intent(WXEntryActivity.this, MyDetailActivity.class);
				startActivity(intent);

			}else{

				String key = param.getString("key");
				if (key.equals("getToken")){
					JSONObject jsonObject = new JSONObject(content);
					String WXaccess_token = jsonObject.getString("access_token");
					String WXopenid =   jsonObject.getString("openid");
					getUserInfo(WXaccess_token,WXopenid);
				}else{
					if (reqState.equals("wechat_sdk_demo_band")){
						bandUser(content);
					}else{
						loginSanFangTask(content);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}