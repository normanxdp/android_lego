package android.bigplan.lego.fragment;
import android.app.Dialog;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.DialogFactory;
import android.support.v4.app.Fragment;
import android.bigplan.lego.R;

import org.json.JSONObject;

public class BaseFragment extends Fragment {
    public Dialog mDialog = null;
    public void showRequestDialog(String msg) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(getContext(), msg);
        mDialog.show();
    }

    public void showRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(getContext(), getString(R.string.loading));
        mDialog.show();
    }

    public void closeRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

    }


    protected  void onSuccessCallback(String url, String content, JSONObject param){}
    protected void onFailureCallback(String url, Throwable error){AbToastUtil.showToast(getContext(), error.getMessage());}
    protected void onStartCallback(){
        //showRequestDialog(getContext().getString(R.string.toast_submiting));
    }
    protected void onFinishCallback(JSONObject param){closeRequestDialog();}
    protected void onShowErrorMsg(String url, String msg) {
        AbToastUtil.showToast(getContext(), msg);}

    protected void get(String url, final JSONObject object){
        AbLogUtil.d("url:" + url);
        AbLogUtil.d("params:"+object);
        AbHttpUtil httpUtil = AbHttpUtil.getInstance(getContext());
        String tmpUrl = AbConstant.REQUEST_URL + url;
        httpUtil.get(tmpUrl, new AbStringHttpResponseListener(url, object) {
            public void onSuccess(int statusCode, String content) {
                if (content != null) {
                    AbLogUtil.d(object+"====="+content);
                    try {
                        AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
                        if (abResult.getCode() == AbResult.RESULT_OK) {
                            onSuccessCallback(strUrl, content, jsonParam);
                        } else {
                            onShowErrorMsg(strUrl, abResult.getMsg());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onStart() {
                onStartCallback();
            }

            public void onFailure(int statusCode, String content, Throwable error) {
                onFailureCallback(strUrl, error);
            }

            public void onFinish() {
                onFinishCallback(jsonParam);
            }
        });
    }
    protected void post(String url, final JSONObject object){
        AbLogUtil.d("url:" + url);
        AbLogUtil.d("params:"+object);
        AbRequest request = new AbRequest();
        AbRequestParams params = request.getRequestParams(object.toString());
        AbHttpUtil httpUtil = AbHttpUtil.getInstance(getContext());

        String tmpUrl = AbConstant.REQUEST_URL + url;
        httpUtil.post(tmpUrl, params, new AbStringHttpResponseListener(url, object) {
            public void onSuccess(int statusCode, String content) {
                if (content != null) {
                    AbLogUtil.d(object+"====="+content);
                    try {
                        AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
                        if (abResult.getCode() == AbResult.RESULT_OK) {
                            onSuccessCallback(strUrl, content, jsonParam);
                        } else {
                            onShowErrorMsg(strUrl, abResult.getMsg());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onStart() {
                onStartCallback();
            }

            public void onFailure(int statusCode, String content, Throwable error) {
                onFailureCallback(strUrl, error);
            }

            public void onFinish() {
                onFinishCallback(jsonParam);
            }
        });
    }

}
