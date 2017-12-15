package android.bigplan.lego.activity;
import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.passwork.DialogWidget;

import android.bigplan.lego.util.passwork.PaySetPasswordView;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

public class PayPwdValidateActivity extends BaseTActivity implements View.OnClickListener {
    private static final String TAG = PayPwdValidateActivity.class.getSimpleName();
    private View decorViewDialog;
    private ImageView viewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorViewDialog = getDecorViewDialog();
        setContentView(R.layout.contentview);
        setContentView(decorViewDialog);
        AppApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.pay_pwd_validates);
    }
    private DialogWidget mDialogWidget;
    private void initView() {
        viewById = (ImageView) findViewById(R.id.toolbar_title_setpayword);
        viewById.setOnClickListener(this);
    }
    protected View getDecorViewDialog() {
        // TODO Auto-generated method stub
        return PaySetPasswordView.getInstance("validate_password", this, new PaySetPasswordView.OnPayListener() {
            public void onSurePay(String password) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(password)){
                    AbToastUtil.showToast(mContext,getString(R.string.error_pwd));
                    return;
                }
                if (password.length() != 6){
                    AbToastUtil.showToast(mContext,getString(R.string.error_pwd_length));
                    return;
                }
                validatePayData(password);
            }

            @Override
            public void onCancelPay() {
                // TODO Auto-generated method stub
                mDialogWidget.dismiss();
                mDialogWidget = null;
                Toast.makeText(getApplicationContext(), "取消了", Toast.LENGTH_SHORT).show();

            }
        }).getView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_title_setpayword:
                finish();
                break;
        }
    }
    protected void onFailureCallback(String url, Throwable error){AbToastUtil.showToast(mContext, error.getMessage());}
    protected void onFinishCallback(){closeRequestDialog();}
    protected void onStartCallback(){showRequestDialog(mContext.getString(R.string.toast_submiting));}
    protected void onSuccessCallback(String url, String content, JSONObject param){
        try {
            if (url.equals("/Wallet/ValidatePassword")){
                AbToastUtil.showToast(mContext,"密码验证通过");
                Intent intent = new Intent(mContext, PayPwdActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void validatePayData(String pass) {
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("password", pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Wallet/ValidatePassword", object);
    }
}
