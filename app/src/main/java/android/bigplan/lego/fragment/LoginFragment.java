package android.bigplan.lego.fragment;

import android.app.Activity;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.BandMobileCodeActivity;
import android.bigplan.lego.activity.ForgetPwdActivity;
import android.bigplan.lego.activity.LoginRegisterActivity;
import android.bigplan.lego.activity.MainTabActivity;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.app.Constants;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.model.User;
import android.bigplan.lego.task.thread.AbThreadFactory;
import android.bigplan.lego.util.AbAppUtil;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
//import com.tencent.connect.UserInfo;
//import com.tencent.connect.auth.QQAuth;
//import com.tencent.connect.auth.QQToken;
//import com.tencent.mm.sdk.modelmsg.SendAuth;
//import com.tencent.mm.sdk.openapi.IWXAPI;
////import com.tencent.mm.sdk.openapi.SendAuth;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.bigplan.lego.activity.third.BaseUiListener;

public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private LoginRegisterActivity loginRegisterActivity;

    private Button mBtnLogin;
    private CheckBox mBtnShowPwd;
    private Button mBtnForgetPwd  ;

    private EditText mEtUserTele;
    private EditText mEtPwd;
    public static IWXAPI mIwapi;
    public static boolean isWxLogin=false;
    private IUiListener listener;
    private String openid;
    private ImageView btn_login_qq;
    private ImageView btn_login_weixin;
    private  String mNumber = null, mNickName = null, mAvatar = null, mType = "0";
    private  boolean mbFirst = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container,
                false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.loginRegisterActivity = (LoginRegisterActivity) activity;

    }


    public void setUserInfo() {
        String telphone = AbSharedUtil.getString(getContext(), AbConstant.KEY_SP_TELPHONE);
        String password = AbSharedUtil.getString(getContext(), AbConstant.KEY_SP_PASSWORD);
        if (!TextUtils.isEmpty(telphone)) {
            mEtUserTele.setText(telphone);
        }
        if (!TextUtils.isEmpty(password)) {
            mEtPwd.setText(password);
        } else {
            mEtPwd.setText("");
        }
    }

    private void initView() {
        mEtUserTele = (EditText) getView().findViewById(R.id.et_telphone);
        mEtPwd = (EditText) getView().findViewById(R.id.et_pwd);


        btn_login_qq = (ImageView) getView().findViewById(R.id.btn_login_qq);
        btn_login_weixin = (ImageView) getView().findViewById(R.id.btn_login_weixin);
        mBtnLogin = (Button) getView().findViewById(R.id.btn_login);
        mBtnShowPwd = (CheckBox) getView().findViewById(R.id.btn_show_pwd);
        mBtnForgetPwd = (Button) getView().findViewById(R.id.btn_forget_pwd);

        btn_login_weixin.setOnClickListener(this);
        btn_login_qq.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mBtnForgetPwd.setOnClickListener(this);
        mBtnShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        setUserInfo();
    }

    @Override
    public void onClick(View v) {
        mNickName = AbSharedUtil.getString(getContext(), AbConstant.KEY_QUICK_USER_NICKNAME);
        mNumber = AbSharedUtil.getString(getContext(), AbConstant.KEY_QUICK_USERNAME);
        mAvatar = AbSharedUtil.getString(getContext(), AbConstant.KEY_QUICK_USER_AVATAR);
        mType = AbSharedUtil.getString(getContext(), AbConstant.KEY_QUICK_USER_TYPE);

        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_forget_pwd:
                Intent intent = new Intent(getContext(), ForgetPwdActivity.class);
                getContext().startActivity(intent);
                break;

            case R.id.btn_login_weixin:
                if (mType.equals("2") && !TextUtils.isEmpty(mNumber) && !TextUtils.isEmpty(mNickName)){
                    loginSanFangTask();
                }else{
                    mIwapi = WXAPIFactory.createWXAPI(getActivity(),  Constants.WXAPP_ID, true);
                    mIwapi.registerApp(Constants.WXAPP_ID);
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo";
                    mIwapi.sendReq(req);//执行完毕这句话之后，会在WXEntryActivity回调
                }

                break;
            case R.id.btn_login_qq:
                if (mType.equals("1") && !TextUtils.isEmpty(mNumber) && !TextUtils.isEmpty(mNickName)){
                    loginSanFangTask();
                }else{
                    mQQAuth = QQAuth.createInstance(Constants.QQAppid, getActivity());
                    // 实例化
                    mTencent = Tencent.createInstance(Constants.QQAppid, getActivity());
                    onClickLogin();
                }


                break;

        }
    }
    private Tencent mTencent;
    public QQAuth mQQAuth;
    // 申请的id


    private void login() {
        String password = mEtPwd.getText().toString();
        String telphone = mEtUserTele.getText().toString();

        if (TextUtils.isEmpty(telphone)) {
            AbToastUtil.showToast(getContext(), R.string.error_phone);
            mEtUserTele.setFocusable(true);
            mEtUserTele.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(telphone) != 11 || !AbStrUtil.isNumberLetter(telphone)) {
            AbToastUtil.showToast(getContext(), R.string.error_phone_input);
            mEtUserTele.setFocusable(true);
            mEtUserTele.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            AbToastUtil.showToast(getContext(), R.string.error_pwd);
            mEtPwd.setFocusable(true);
            mEtPwd.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(password) < 6) {
            AbToastUtil.showToast(getContext(), R.string.error_pwd_length1);
            mEtPwd.setFocusable(true);
            mEtPwd.requestFocus();
            return;
        }

        if (AbStrUtil.strLength(password) > 20) {
            AbToastUtil.showToast(getContext(), R.string.error_pwd_length2);
            mEtPwd.setFocusable(true);
            mEtPwd.requestFocus();
            return;
        }

        try {
            loginTask(telphone, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void onFailureCallback(String url, Throwable error){
        AbToastUtil.showToast(getContext(), error.getMessage());
        AppApplication.getInstance().isUserLogin = false;
    }

    protected  void onSuccessCallback(String url, String content, JSONObject param){
        try {
            if (url.equals("/Member/Login")) {
                String telphone = param.getString("mobile");
                String password = param.getString("pwd");
                AbSharedUtil.putString(getContext(), AbConstant.KEY_SP_TELPHONE, telphone);
                AbSharedUtil.putString(getContext(), AbConstant.KEY_SP_PASSWORD, password);
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
                user.setIntroduction(jsonObject.getString("Introduction"));
                user.setState(jsonObject.getString("State"));
                user.setCreateTime(jsonObject.getString("CreateTime"));
                user.setSinopecAccountCount(jsonObject.getString("SinopecAccountCount"));
                user.setPetroChinaAccountCount(jsonObject.getString("PetroChinaAccountCount"));
                user.setOilMoney(jsonObject.getString("OilMoney"));
                user.setIsLazyStaff(jsonObject.getString("IsLazyStaff"));;
                user.setPayPwd(jsonObject.getString("PayPwd"));
                user.setYMobile(jsonObject.getString("YMobile"));
                user.setYunAccount(jsonObject.getString("YunAccount"));
                AppApplication.getInstance().setUser(user);
                AppApplication.getInstance().saveUser2Sqlite(user);
                AppApplication.getInstance().isUserLogin = true;
                Intent intent = new Intent(getContext(), MainTabActivity.class);
                getContext().startActivity(intent);
            }else if (url.equals("/Member/QuickLogin")){
                AbSharedUtil.putString(getContext(), AbConstant.KEY_QUICK_USERNAME, mNumber);
                AbSharedUtil.putString(getContext(), AbConstant.KEY_QUICK_USER_TYPE, mType);
                AbSharedUtil.putString(getContext(), AbConstant.KEY_QUICK_USER_AVATAR, mAvatar);
                AbSharedUtil.putString(getContext(), AbConstant.KEY_QUICK_USER_NICKNAME, mNickName);

                JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
                User user = AppApplication.getInstance().getUser();
                user.setMemberId(jsonObject.getString("MemberId"));
                user.setPassId(jsonObject.getString("PassId"));
                user.setQQ(jsonObject.getString("QQ"));
                user.setMobile(jsonObject.getString("Mobile"));
                user.setAvatar(jsonObject.getString("Avatar"));
                user.setSex(jsonObject.getString("Sex"));
                user.setRealName(jsonObject.getString("RealName"));
                user.setNickname(jsonObject.getString("Nickname"));
                user.setStoreName(jsonObject.getString("StoreName"));
                user.setFirstLetter(jsonObject.getString("FirstLetter"));
                user.setWeChatId(jsonObject.getString("WeChatId"));
                user.setIntroduce(jsonObject.getString("Introduce"));
                user.setIntroduction(jsonObject.getString("Introduction"));
                user.setState(jsonObject.getString("State"));
                user.setCreateTime(jsonObject.getString("CreateTime"));
                user.setIsLazyStaff(jsonObject.getString("IsLazyStaff"));
                user.setSinopecAccountCount(jsonObject.getString("SinopecAccountCount"));
                user.setPetroChinaAccountCount(jsonObject.getString("PetroChinaAccountCount"));
                user.setOilMoney(jsonObject.getString("OilMoney"));
                user.setPayPwd(jsonObject.getString("PayPwd"));
                AppApplication.getInstance().setUser(user);
                AppApplication.getInstance().saveUser2Sqlite(user);
                AppApplication.getInstance().isUserLogin = true;
				String mobile = jsonObject.getString("Mobile");
				if (TextUtils.isEmpty(mobile)){
					bandMobile(mType);
				}else{
                    Intent intent = new Intent(getContext(), MainTabActivity.class);
                    getContext().startActivity(intent);
				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	private  void bandMobile(String type){
        Intent intent = new Intent(getContext(), BandMobileCodeActivity.class);
        intent.putExtra(BandMobileCodeActivity.APPLY_STAFF, type);
        getContext().startActivity(intent);
    }
	
    public void loginTask(final String telphone, final String password) {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", telphone);
            object.put("pwd", password);
            object.put("deviceno", AbAppUtil.getImeiNumber(getContext()));
            object.put("devicetype", "2");

        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }

        post("/Member/Login", object);
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                String rmsg=response.toString().replace(",", "\n");
                if (response.has("nickname")) {
                    try {
//                        mUserInfo.setVisibility(android.view.View.VISIBLE);
//                        mUserInfo.setText(response.getString("nickname"));
//                        tx.setText(response.getString("nickname"));
//                        tx.append(rmsg);

                    } catch ( Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what == 1){
                Bitmap bitmap = (Bitmap)msg.obj;
//                mUserLogo.setImageBitmap(bitmap);
//                mUserLogo.setVisibility(android.view.View.VISIBLE);
            }
        }

    };

    private void updateUserInfo( ) {

        QQToken qqToken = mTencent.getQQToken();
        UserInfo info =new UserInfo(getActivity(), qqToken);
        info.getUserInfo(new IUiListener() {
            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onComplete(final Object response) {
                try{
                    JSONObject json = (JSONObject)response;
                    mNickName = json.getString("nickname");
                    mAvatar = json.getString("figureurl_qq_2");
                    mType = "1";
                }catch (Exception ex) {
                    Logger.e(TAG, ex.getMessage());
                }

                loginSanFangTask();

                Message msg = new Message();
                msg.obj = response;
                msg.what = 0;
                mHandler.sendMessage(msg);
                new Thread(){
                    @Override
                    public void run() {
                        JSONObject json = (JSONObject)response;
                        if(json.has("figureurl")){
                            Bitmap bitmap = null;
                            try {
                                bitmap = getbitmap(json.getString("figureurl_qq_2"));
                            } catch (JSONException e) {

                            }
                            Message msg = new Message();
                            msg.obj = bitmap;
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }
                    }

                }.start();
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
            }
        });



    }



    public static Bitmap getbitmap(String imageUri) {
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
        return bitmap;
    }

    public void onClickLogin() {
        // 登录
        if (!mQQAuth.isSessionValid()) {
            // 实例化回调接口
            // updateLoginButton();
//                        mNewLoginButton.setTextColor(Color.BLUE);
//                        mNewLoginButton.setText("登录");
            listener = new BaseUiListener() {
                @Override
                protected void doComplete(JSONObject values) {
//                    AbToastUtil.showToast(getActivity(),values.toString());

                    openid = values.optString("openid");
                    mNumber = openid;
                    final String access_token = values.optString("access_token");
                    final String expires_in = values.optString("expires_in");
//                    Logger.v("syso",values.toString());
                    mTencent.setOpenId(openid);
                    mTencent.setAccessToken(access_token,expires_in);

                    updateUserInfo( );

                    if (mQQAuth != null) {

                    }
                }


            };
            // "all": 所有权限，listener: 回调的实例
            // mQQAuth.login(this, "all", listener);

            // 这版本登录是使用的这种方式，后面的几个参数是啥意思 我也没查到
            mTencent.loginWithOEM(getActivity(), "all", listener, "10000144",  "10000144", "xxxx");
        } else {
            // 注销登录
            mQQAuth.logout(getActivity());


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode,resultCode,data,listener );
    }

    public void loginSanFangTask() {
        JSONObject object = new JSONObject();
        try {
            object.put("type", mType);
            object.put("number",mNumber);//唯一值相当QQ号码
            object.put("avatar", mAvatar);
            object.put("nickname", mNickName);
            object.put("deviceno", AbAppUtil.getImeiNumber(getActivity()));
            object.put("devicetype", "2");
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Member/QuickLogin", object);
    }
}
