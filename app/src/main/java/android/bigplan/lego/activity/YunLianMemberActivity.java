package android.bigplan.lego.activity;

import android.app.Dialog;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.msg.ReceptionRoomActivity;
import android.bigplan.lego.adapter.WalletListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.fragment.AbAlertDialogFragment;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.model.Wallet;
import android.bigplan.lego.util.AbAppUtil;
import android.bigplan.lego.util.AbDialogFragmentUtil;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.bigplan.lego.view.RoundImageView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

public class YunLianMemberActivity extends BaseTActivity{
    private final static String TAG = YunLianMemberActivity.class.getSimpleName();
    private AbImageLoader mAbImageLoader = null;
    EditText mEtYMobile = null;
    EditText mEtYId = null;
    TextView mTvId = null;
    TextView mTvMobile = null;
    protected void onResume() {
        super.onResume();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yunlianaccount);
        mAbImageLoader = AbImageLoader.getInstance(this);
        initView();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.lable_yl_member);
    }

    private void initView() {
        RoundImageView avatar = (RoundImageView)findViewById(R.id.grid_avatar);
        String avatarUrl = getIntent().getStringExtra("lego_member_avatar");
        if (!TextUtils.isEmpty(avatarUrl)){
            mAbImageLoader.display(avatar, R.drawable.default_head, avatarUrl);
        }

        mTvId = (TextView)findViewById(R.id.tv_yunmemberid);
        mTvMobile = (TextView)findViewById(R.id.tv_telephone);
        String yId = getIntent().getStringExtra("lego_member_yid");
        String yMobile = getIntent().getStringExtra("lego_member_ymobile");
        if (mTvId != null && !TextUtils.isEmpty(yId)){
            mTvId.setText("云联惠账号："+yId);
        }
        if (mTvMobile != null && !TextUtils.isEmpty(yMobile)){
            mTvMobile.setText("云联惠手机："+yMobile);
        }

        mEtYId = (EditText)findViewById(R.id.edit_yunlian_memberid);
        mEtYMobile = (EditText)findViewById(R.id.edit_yunlian_mobile);
        Button btn  = (Button) findViewById(R.id.btn_modify_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });

        LinearLayout login = (LinearLayout)findViewById(R.id.ll_yl_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(WebActivity.WEB_KEY_URL, "http://uc.yunlianhui.cn/index.php/Login/login.html");
                startActivity(intent);
            }
        });

        LinearLayout register = (LinearLayout)findViewById(R.id.ll_yl_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(WebActivity.WEB_KEY_URL, "http://uc.yunlianhui.cn/index.php/Register/registerOne/rcm_id/%E6%B8%85%E8%BF%9C%E4%BA%91%E8%81%94%E4%B9%90%E8%B4%AD.html");
                startActivity(intent);
            }
        });
    }

    private void modify(){
        String id = mEtYId.getText().toString();
        String mobile = mEtYMobile.getText().toString();
        if (TextUtils.isEmpty(id)){
            AbToastUtil.showToast(mContext, getString(R.string.hint_input_your_ylid));
            mEtYId.setFocusable(true);
            mEtYId.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mobile)){
            AbToastUtil.showToast(mContext, getString(R.string.hint_input_your_ylmobile));
            mEtYMobile.setFocusable(true);
            mEtYMobile.requestFocus();
            return;
        }

        JSONObject object = new JSONObject();
        try {
            object.put("mobile", mobile);
            object.put("yid", id);
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }

        post("/Member/BoundYLMember", object);
    }
    protected void onSuccessCallback(String url, String content, JSONObject param) {
        AbToastUtil.showToast(mContext, getString(R.string.reset_yl_success));
        try {
            String yid = param.getString("yid");
            String mobile = param.getString("mobile");
            mTvId.setText("云联惠账号："+yid);
            mTvMobile.setText("云联惠手机："+mobile);
            AppApplication.getInstance().getUser().setYMobile(mobile);
            AppApplication.getInstance().getUser().setYunAccount(yid);

        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
    }
}
