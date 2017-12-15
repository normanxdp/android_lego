package android.bigplan.lego.activity;

import android.app.Dialog;
import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.model.User;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.DialogFactory;
import android.bigplan.lego.util.Logger;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public abstract class BaseTActivity extends AppCompatActivity{
    private TextView mTvTitle;
    private TextView mTvRightTitle;
    protected BaseTActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (layoutResID > 0){
            super.setContentView(layoutResID);
        }

        mContext = this;
        initTitleBar();
    }

    public Dialog mDialog = null;
    public void showRequestDialog(String msg) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(this, msg);
        mDialog.show();
    }

    public void showRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(this, getString(R.string.loading));
        mDialog.show();
    }

    public void closeRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

    }

    private void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            Logger.e("FingerArt", "XML中不存在toolbar布局");
            return;
        }

        mTvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        if (mTvTitle != null) {
            mTvTitle.setText(getToolBarTitle() + "");
        }

        mTvRightTitle = (TextView) toolbar.findViewById(R.id.toolbar_title_right);
        if (mTvRightTitle != null) {

            String strRightTitle = getToolBarRightTitle();
            if (TextUtils.isEmpty(strRightTitle)){
                mTvRightTitle.setVisibility(View.GONE);

            }else{
                mTvRightTitle.setVisibility(View.VISIBLE);
                mTvRightTitle.setText(strRightTitle);
                mTvRightTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRightTitleClick();
                    }
                });
            }
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    protected void hideToolBarBack() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    protected void setToolBarTitle(String title) {
        mTvTitle.setText(title);
    }
    protected void setToolBarTitleImage(int image) {
        mTvRightTitle.setBackgroundResource(image);
        mTvRightTitle.setVisibility(View.VISIBLE);
    }
    protected abstract String getToolBarTitle();

    protected String getToolBarRightTitle(){ return  " "; }
    protected void back() {finish();}
    protected  void onRightTitleClick(){}
    protected  void onSuccessCallback(String url, String content, JSONObject param){}
    protected void onFailureCallback(String url, Throwable error){AbToastUtil.showToast(mContext, error.getMessage());}
    protected void onStartCallback(String url, JSONObject param){
        //showRequestDialog(mContext.getString(R.string.toast_submiting));
    }
    protected void onFinishCallback(){closeRequestDialog();}
    protected void onShowErrorMsg(String url, String msg) {AbToastUtil.showToast(mContext, msg);}

    protected void get(String url){
        AbLogUtil.d("url:" + url);
        AbHttpUtil httpUtil = AbHttpUtil.getInstance(mContext);
        httpUtil.get(url, new AbStringHttpResponseListener(url, null) {
            public void onSuccess(int statusCode, String content) {
                mContext.onSuccessCallback(strUrl, content, jsonParam);
            }
            public void onStart() {
                mContext.onStartCallback(strUrl, jsonParam);
            }
            public void onFailure(int statusCode, String content, Throwable error) {
                mContext.onFailureCallback(strUrl, error);
            }
            public void onFinish() {
                mContext.onFinishCallback();
            }
        });
    }

    protected void get(String url, final JSONObject object){
        AbLogUtil.d("url:" + url);
        AbLogUtil.d("params:"+object);
        AbHttpUtil httpUtil = AbHttpUtil.getInstance(mContext);
        httpUtil.get(url, new AbStringHttpResponseListener(url, object) {
            public void onSuccess(int statusCode, String content) {
                if (content != null) {
                    mContext.onSuccessCallback(strUrl, content, jsonParam);
                }
            }

            public void onStart() {
                mContext.onStartCallback(strUrl, jsonParam);
            }
            public void onFailure(int statusCode, String content, Throwable error) {
                mContext.onFailureCallback(strUrl, error);
            }
            public void onFinish() {
                mContext.onFinishCallback();
            }
        });
    }

    protected void post(String url, final JSONObject object){
        AbLogUtil.d("url:" + url);
        AbLogUtil.d("params:"+object);
        AbRequest request = new AbRequest();
        AbRequestParams params = request.getRequestParams(object.toString());
        AbHttpUtil httpUtil = AbHttpUtil.getInstance(mContext);

        String tmpUrl = AbConstant.REQUEST_URL + url;
        httpUtil.post(tmpUrl, params, new AbStringHttpResponseListener(url, object) {
            public void onSuccess(int statusCode, String content) {
                if (content != null) {
                    AbLogUtil.d(object+"====="+content);
                    try {
                        AbObjResult abResult = (AbObjResult)AbJsonUtil.fromJson(content, AbObjResult.class);
                        if (abResult.getCode() == AbResult.RESULT_OK) {
                            mContext.onSuccessCallback(strUrl, content, jsonParam);
                        } else {
                            mContext.onShowErrorMsg(strUrl, abResult.getMsg());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onStart() {
                mContext.onStartCallback(strUrl, jsonParam);
            }

            public void onFailure(int statusCode, String content, Throwable error) {
                mContext.onFailureCallback(strUrl, error);
            }

            public void onFinish() {
                mContext.onFinishCallback();
            }
        });
    }
}
