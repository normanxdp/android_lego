package android.bigplan.lego.activity;

import android.app.Activity;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.third.BaseUiListener;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.app.Constants;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.model.User;
import android.bigplan.lego.util.AbDialogFragmentUtil;
import android.bigplan.lego.util.AbFileUtil;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbImageUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyDetailActivity extends BaseTActivity implements View.OnClickListener {
    private final static String TAG = MyDetailActivity.class.getSimpleName();

    public static final int REQUESTCODE_SELECT_PICTURE = 2;
    public static final int REQUESTCODE_TAKE_PHOTO = 1;

    private LinearLayout mLlMyNickName;

    private LinearLayout mLlModifyPwd;
    private LinearLayout mLlPayPwd;
    private FrameLayout mFlModifyAvatar;
    private String mStrCurImagePath  = null;
    LayoutInflater mInflater = null;
    private ImageView mIvAvatar;
    private AbImageLoader mAbImageLoader = null;

    private View mChoosePhotoView = null;
    private Uri mTakePhotoUri;
    private Button mLogoutBtn = null;
    private Uri mImgUri = Uri.fromFile(new File(AbConstant.DIR_COMP_IMAGES+File.separator+"My.png"));
    private static final int CAMERA_CROP_DATA = 3;
    public static final String USER_NAME = "my_show_name";

    private TextView  mTvMobile = null;
    private TextView  mTvWeChat = null;
    private TextView  mTvQQ = null;

    private String openid = null;
    private IUiListener listener;
    private Tencent mTencent;
    public QQAuth mQQAuth;
   // 申请的id

    public static IWXAPI mIwapi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detail);
        mAbImageLoader = AbImageLoader.getInstance(this);
        mInflater = LayoutInflater.from(this);
        initView();
        File file = new File(AbConstant.DIR_COMP_IMAGES);

        if(!file.exists()){
            boolean success = file.mkdirs();
            Logger.d(TAG, "make dile dir: " + success);
        }
    }

    protected String getToolBarTitle(){
        return  getString(R.string.title_my_detail_info);
    }

    private void initView() {
        mLlMyNickName = (LinearLayout) findViewById(R.id.ll_my_nickname);

        mLlModifyPwd = (LinearLayout) findViewById(R.id.ll_modify_pwd);
        mLlPayPwd = (LinearLayout) findViewById(R.id.ll_pay_pwd);

        mLogoutBtn = (Button)this.findViewById(R.id.btn_logout);
        mFlModifyAvatar = (FrameLayout) findViewById(R.id.fl_avatar);
        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
        TextView setTip = (TextView)findViewById(R.id.tx_my_paysetting);
        String payPwd = AppApplication.getInstance().getUser().getPayPwd();
        if (TextUtils.isEmpty(payPwd)){
            setTip.setText(getString(R.string.no_settings));
        }else{
            setTip.setText(getString(R.string.modify_pay));
        }

        TextView TvUserName = (TextView) findViewById(R.id.tx_my_nickname);
        String showName = getIntent().getStringExtra(MyDetailActivity.USER_NAME);
        if (TextUtils.isEmpty(showName)){
            showName =  AppApplication.getInstance().getUser().getShowName();
        }
        TvUserName.setText(showName);

        mAbImageLoader.display(mIvAvatar, R.drawable.default_head, AppApplication.getInstance().getUser().getAvatar());

        mLlMyNickName.setOnClickListener(this);
        mLogoutBtn.setOnClickListener(this);

        mLlModifyPwd.setOnClickListener(this);
        mLlPayPwd.setOnClickListener(this);
        mFlModifyAvatar.setOnClickListener(this);

        mTvMobile = (TextView)findViewById(R.id.tv_my_mobile);
        mTvWeChat = (TextView)findViewById(R.id.tv_my_wechat);
        mTvQQ = (TextView)findViewById(R.id.tv_my_qq);
        mTvMobile.setOnClickListener(this);
        mTvWeChat.setOnClickListener(this);
        mTvQQ.setOnClickListener(this);

        String qq = AppApplication.getInstance().getUser().getQQ();
        String weChat = AppApplication.getInstance().getUser().getWeChatId();
        String mobile = AppApplication.getInstance().getUser().getMobile();

        if (!TextUtils.isEmpty(qq)) {
            mTvQQ.setText("已绑定");
            mTvQQ.setClickable(false);
            mTvQQ.setTextColor(getResources().getColor(R.color.hint_gray));
        }
        if (!TextUtils.isEmpty(weChat)) {
            mTvWeChat.setText("已绑定");
            mTvWeChat.setClickable(false);
            mTvWeChat.setTextColor(getResources().getColor(R.color.hint_gray));
        }
        if (!TextUtils.isEmpty(mobile)) {
            String strPre = mobile.substring(0, 4);
            String strBack = mobile.substring(8);
            mTvMobile.setText(strPre+"****"+strBack);
            mTvMobile.setClickable(false);
            mTvMobile.setTextColor(getResources().getColor(R.color.hint_gray));
        }
    }

    private void bandWechat(){

        mIwapi = WXAPIFactory.createWXAPI(mContext,  Constants.WXAPP_ID, true );
        mIwapi.registerApp(Constants.WXAPP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_band";
        mIwapi.sendReq(req);//执行完毕这句话之后，会在WXEntryActivity回调
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


    public void bandThirdTask(Object response, int type) {
        AbFileUtil. writeByteArrayToSD("",response.toString(),true);
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("type", type);
            object.put("number",openid);//唯一值相当QQ号码
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Member/BoundQuickUser", object);
    }

    private void updateUserInfo( ) {

        QQToken qqToken = mTencent.getQQToken();
        UserInfo info =new UserInfo(mContext, qqToken);
        info.getUserInfo(new IUiListener() {
            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onComplete(final Object response) {
                bandThirdTask(response, 1);
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
//                    AbToastUtil.showToast(mContext,values.toString());

                    openid = values.optString("openid");
                    final String access_token = values.optString("access_token");
                    final String expires_in = values.optString("expires_in");
                    Logger.v("syso",values.toString());
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
            mTencent.loginWithOEM(mContext, "all", listener, "10000144",  "10000144", "xxxx");
        } else {
            // 注销登录
            mQQAuth.logout(mContext);


        }
    }

    private  void bandQQ(){
        mQQAuth = QQAuth.createInstance(Constants.QQAppid, mContext);
        // 实例化
        mTencent = Tencent.createInstance(Constants.QQAppid, mContext);
        onClickLogin();
    }

    private  void bandMobile(){
        Intent intent = new Intent(mContext, BandMobileCodeActivity.class);
        intent.putExtra(BandMobileCodeActivity.APPLY_STAFF, "0");
        mContext.startActivity(intent);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_my_nickname:
                String isLazy =  AppApplication.getInstance().getUser().getIsLazyStaff();
                if (isLazy.equals("1")){
                    AbToastUtil.showToast(mContext, getString(R.string.forbid_shop_modify_name));
                }else{
                    intent = new Intent(mContext, EditNiChengActivity.class);
                    mContext.startActivity(intent);
                }

                break;

            case R.id.ll_modify_pwd:
                intent = new Intent(mContext, ResetPwdActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.ll_pay_pwd:
                String pwd = AppApplication.getInstance().getUser().getPayPwd();
                if (!TextUtils.isEmpty(pwd)){
                    intent = new Intent(mContext, PayPwdValidateActivity.class);
                }else{
                    intent = new Intent(mContext, PayPwdActivity.class);
                }
                mContext.startActivity(intent);
                break;
            case R.id.fl_avatar:
                showPhotoSelectPop();
                break;
            case R.id.btn_logout:
                exitUser();
                break;
            case R.id.tv_my_qq:
                bandQQ();
                break;
            case R.id.tv_my_wechat:
                bandWechat();
                break;
            case R.id.tv_my_mobile:
                bandMobile();
                break;
        }
    }

    private void setCropImg(String path) {
        uploadAvatar(path);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        } else if (requestCode == REQUESTCODE_TAKE_PHOTO) {
            Logger.d(TAG, "file path: " + mTakePhotoUri.getPath());
            /*
            uploadAvatar(mTakePhotoUri.getPath());
            */
            doCrop(mTakePhotoUri);
        } else if (requestCode == REQUESTCODE_SELECT_PICTURE) {
            if (data != null) {
                Uri uri = data.getData();
                cropImageUriAfterKikat(uri);
            }
            /*
            if (data != null) {
                Uri uri = data.getData();
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor imageCursor = managedQuery(uri,proj,null,null,null);

                int columnIndex = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                imageCursor.moveToFirst();
                String img_path = imageCursor.getString(columnIndex);
                Logger.d(TAG, "file path: " + img_path);
                uploadAvatar(img_path);
            }*/
        }else if (requestCode == CAMERA_CROP_DATA){
            if (null != mImgUri) {
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImgUri));
                    mIvAvatar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                setCropImg(mImgUri.getEncodedPath());
            }
        }else if(requestCode==0x999){
            String path =data.getStringExtra("PATH");
            if (!TextUtils.isEmpty(path)) {
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeFile(path);
                mIvAvatar.setImageBitmap(bitmap);
                setCropImg(path);
            }
        }
        if(listener!=null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        }
    }

    private void doCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "can't find crop app", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 600);
            intent.putExtra("outputY", 600);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImgUri);
            intent.putExtra("noFaceDetection", true);

            // only one
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,res.activityInfo.name));
                startActivityForResult(i, CAMERA_CROP_DATA);
            }
        }
    }

    private void cropImageUriAfterKikat(Uri uri) {
        Intent ins = new Intent("com.android.camera.action.CROP");
        ins.setDataAndType(uri, "image/*");
        ins.putExtra("crop", "true");
        ins.putExtra("aspectX", 1);
        ins.putExtra("aspectY", 1);
        ins.putExtra("outputX", 640);
        ins.putExtra("outputY", 640);
        ins.putExtra("scale", true);
        ins.putExtra("return-data", false);
        ins.putExtra(MediaStore.EXTRA_OUTPUT, mImgUri);
        ins.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        ins.putExtra("noFaceDetection", true);
        startActivityForResult(ins, CAMERA_CROP_DATA);
    }

    private void showPhotoSelectPop() {
        mChoosePhotoView = mInflater.inflate(R.layout.dialog_choose_photo, null);
        Button btnTakePhoto = (Button)mChoosePhotoView.findViewById(R.id.item_popupwindows_camera);
        Button btnChooseGallery = (Button)mChoosePhotoView.findViewById(R.id.item_popupwindows_Photo);
        Button btnCancel = (Button)mChoosePhotoView.findViewById(R.id.item_popupwindows_cancel);
        btnTakePhoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                AbDialogFragmentUtil.removeDialog(mContext);
                takePhoto();

            }
        });
        btnChooseGallery.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                AbDialogFragmentUtil.removeDialog(mContext);
                selectImageFromLocal();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                AbDialogFragmentUtil.removeDialog(mContext);
            }
        });
        AbDialogFragmentUtil.showDialog(mChoosePhotoView, Gravity.BOTTOM);
    }

    public void selectImageFromLocal() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            }
            else {
                intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            startActivityForResult(intent, REQUESTCODE_SELECT_PICTURE);
        }
        else {
            AbToastUtil.showToast(this, R.string.toast_no_external_storage);
        }
    }

    private void takePhoto() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String dir = AbConstant.DIR_COMP_IMAGES;
            File destDir = new File(dir);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            // File file = new File(dir, JMessageClient.getMyInfo().getUserName() + ".jpg");
            File file = new File(dir, "take_photo.jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mTakePhotoUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePhotoUri);
            try {
                startActivityForResult(intent, REQUESTCODE_TAKE_PHOTO);
            }
            catch (ActivityNotFoundException anf) {
                AbToastUtil.showToast(this, R.string.toast_camera_is_not_ready);
            }
        }
        else {
            AbToastUtil.showToast(this, R.string.toast_no_external_storage);
        }
    }

    protected void back() {
        Intent intent = new Intent(mContext, MainTabActivity.class);
        String showName = getIntent().getStringExtra(MyDetailActivity.USER_NAME);

        if (!TextUtils.isEmpty(showName)){
            AppApplication.getInstance().getUser().setShowName(showName);
            intent.putExtra("action", "five");
        }

        startActivity(intent);
        finish();
    }

    protected void onSuccessCallback(String url, String content, JSONObject param){
        try {
            if (url.equals("/Member/Logout")){
                logout();
            }else if (url.equals("/Member/UpdateAvatar")){
                JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
                String avatarURL = jsonObject.getString("AvatarUrl");
                User user = AppApplication.getInstance().getUser();
                user.setAvatar(avatarURL);

                Intent msgIntent = new Intent(TabMyActivity.MESSAGE_CHANGEAVR_ACTION);
                msgIntent.putExtra(TabMyActivity.KEY_CHANGE_AVATAR, avatarURL);
                mContext.sendBroadcast(msgIntent);
                Logger.d(TAG, "avatar url: " + avatarURL);
                Bitmap thumbBitmap = AbImageUtil.getScaleBitmap(new File(mStrCurImagePath), 200, 200);
                mIvAvatar.setImageBitmap(thumbBitmap);

            }else if (url.equals("/Member/BoundQuickUser")) {
                int type = param.getInt("type");
                if (type == 1){
                    mTvQQ.setText("已绑定");
                    mTvQQ.setClickable(false);
                    mTvQQ.setTextColor(getResources().getColor(R.color.hint_gray));
                }else if (type == 2){
                    mTvWeChat.setText("已绑定");
                    mTvWeChat.setClickable(false);
                    mTvWeChat.setTextColor(getResources().getColor(R.color.hint_gray));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void logout(){
        JPushInterface.stopPush(getApplicationContext());
        AppApplication.getInstance().clearLoginParams();
        Intent intent = new Intent(mContext, LoginRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    //退出账户
    private void exitUser() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Member/Logout", object);
    }

    private void uploadAvatar(final String filePath) {
        JSONObject object = new JSONObject();
        String picBase64 = AbImageUtil.Bitmap2StrByBase64(new File(filePath));
        mStrCurImagePath = filePath;
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("avatar", picBase64);
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Member/UpdateAvatar", object);
    }

}
