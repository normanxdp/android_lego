package android.bigplan.lego.activity;

import android.app.AlertDialog;
import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * TODO 请将初始化操作放到该页面中进行
 */
public class WelcomeActivity extends BaseTActivity {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private Handler mHandler = new Handler();
    private String mDownloadUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
    }

    private void gotoNext(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacksAndMessages(null);
                boolean isFrist = AbSharedUtil.getBoolean(WelcomeActivity.this, "isFrist", true);
                if (isFrist) {
                    startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
                } else {
                    startActivity(new Intent(WelcomeActivity.this, MainTabActivity.class));
                }
                finish();
            }
        }, 2000);
    }
    @Override
    protected void onStart() {
        super.onStart();
        boolean isFrist = AbSharedUtil.getBoolean(WelcomeActivity.this, "isFrist", true);
        if (isFrist == false){
            checkNewVersion();
        }else{
            gotoNext();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }

    }

    @Override
    protected String getToolBarTitle() {
        return null;
    }



    private void checkNewVersion() {

        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }

        post("/Update/Check", object);
    }

    protected void onSuccessCallback(String url, String content, JSONObject param){
        try {
            if (url.equals("/Update/Check")) {
                JSONObject jsonObject = new JSONObject(content).getJSONObject("data");

                String downloadUrl = jsonObject.getString("url");
                String upgrade = jsonObject.getString("upgrade");
                String upgradeContent = jsonObject.getString("content");
                if (!TextUtils.isEmpty(upgrade) && "1".equals(upgrade) && !TextUtils.isEmpty(downloadUrl)){
                    mDownloadUrl = downloadUrl;
                    AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                    builder.setTitle("升级提示");
                    builder.setMessage(upgradeContent);
                    builder.setPositiveButton(" 升级 ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(mDownloadUrl);
                            intent.setData(content_url);
                            startActivity(intent);
                            gotoNext();
                        }
                    });
                    builder.setNegativeButton(" 取消 ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            gotoNext();
                        }
                    });
                    builder.show();
                }else{
                    gotoNext();
                }
            }
        } catch (Exception ex) {
            gotoNext();
        }
    }

    protected void onFailureCallback(String url, Throwable error){
        gotoNext();
    }

    protected void onShowErrorMsg(String url, String msg) {
        gotoNext();
    }
}