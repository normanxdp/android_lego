package android.bigplan.lego.activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.model.UpdateVersion;
import android.bigplan.lego.model.User;
import android.bigplan.lego.util.AbAppUtil;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.DialogInterface.OnClickListener;
import com.iflytek.cloud.Setting;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class SettingActivity extends BaseTActivity implements View.OnClickListener {
    private final static String TAG = SettingActivity.class.getSimpleName();

    private LinearLayout mLlCheckVersion;
    private LinearLayout mLlLogout;
    private String mDownloadUrl = null;

    private TextView mTvHints;
    private TextView mTvVersion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        AppApplication.getInstance().addActivity(this);
        initView();
    }

    @Override
    protected String getToolBarTitle() {
        return getString(R.string.label_about);
    }

    private void initView() {
        mLlCheckVersion = (LinearLayout) findViewById(R.id.ll_check_version);
        mLlLogout = (LinearLayout) findViewById(R.id.ll_logout);

        mTvVersion = (TextView) findViewById(R.id.tv_version);


        mLlCheckVersion.setOnClickListener(this);
        mLlLogout.setOnClickListener(this);
        mTvVersion.setText(String.format("%s: V%s", getString(R.string.label_version_num), AbAppUtil.getPackageInfo(this).versionName));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_check_version:
                checkNewVersion();

              // AbToastUtil.showToast(mContext, getString(R.string.label_version_already_update));
                break;
            case R.id.ll_logout:
                if (AppApplication.getInstance().isUserLogin)
                {
                    exitUser();
                }else{
                    AbToastUtil.showToast(mContext, getString(R.string.label_logouted));
                }
                break;
        }
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
            if (url.equals("/Member/Logout")) {
                AbToastUtil.showToast(mContext, getString(R.string.label_logouted));
                JPushInterface.stopPush(getApplicationContext());
                AppApplication.getInstance().clearLoginParams();
            } else if (url.equals("/Update/Check")) {
                JSONObject jsonObject = new JSONObject(content).getJSONObject("data");

                String downloadUrl = jsonObject.getString("url");
                String upgrade = jsonObject.getString("upgrade");
                String upgradeContent = jsonObject.getString("content");


                if (!TextUtils.isEmpty(upgrade) && "1".equals(upgrade) && !TextUtils.isEmpty(downloadUrl)){
                    mDownloadUrl = downloadUrl;
                    AlertDialog.Builder builder = new Builder(SettingActivity.this);
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
                        }
                    });
                    builder.setNegativeButton(" 取消 ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    builder.show();
                }else{
                    AbToastUtil.showToast(mContext, getString(R.string.label_version_already_update));
                }
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
    }

    protected void onFailureCallback(String url, Throwable error){
        if (url.equals("/Update/Check")) {
            AbToastUtil.showToast(mContext, getString(R.string.label_version_already_update));
        }else{
            super.onFailureCallback(url, error);
        }
    }

    protected void onShowErrorMsg(String url, String msg) {
        if (url.equals("/Update/Check")) {
            AbToastUtil.showToast(mContext, getString(R.string.label_version_already_update));
        }else{
            super.onShowErrorMsg(url, msg);
        }
    }
    //退出账户
    private void exitUser() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }

        String strUrl = AbConstant.REQUEST_URL + "/Member/Logout";
        post("/Member/Logout", object);

    }
}
