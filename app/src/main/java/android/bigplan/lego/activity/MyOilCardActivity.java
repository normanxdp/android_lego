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

public class MyOilCardActivity extends BaseTActivity{
    private final static String TAG = MyOilCardActivity.class.getSimpleName();
    private AbImageLoader mAbImageLoader = null;

    TextView mTvId1 = null;
    TextView mTvId2 = null;
    TextView mTvId3 = null;
    protected void onResume() {
        super.onResume();
        getMyOilCardInfo();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_oilcard);
        mAbImageLoader = AbImageLoader.getInstance(this);
        initView();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.label_my_oilcard);
    }

    private void initView() {
        RoundImageView avatar = (RoundImageView)findViewById(R.id.grid_avatar);
        String avatarUrl = AppApplication.getInstance().getUser().getAvatar();
        if (!TextUtils.isEmpty(avatarUrl)){
            mAbImageLoader.display(avatar, R.drawable.default_head, avatarUrl);
        }

        mTvId1 = (TextView)findViewById(R.id.tv_1);
        mTvId2 = (TextView)findViewById(R.id.tv_2);
        mTvId3 = (TextView)findViewById(R.id.tv_3);
        String petroChinaAccountCount = AppApplication.getInstance().getUser().getPetroChinaAccountCount();
        String sinopecAccountCount = AppApplication.getInstance().getUser().getSinopecAccountCount();
        String oilMoney = AppApplication.getInstance().getUser().getOilMoney();

        if (mTvId1 != null && !TextUtils.isEmpty(petroChinaAccountCount)){
            mTvId1.setText("中石油账号："+petroChinaAccountCount+"个");
        }else{
            mTvId1.setText("中石油账号：没绑定");
        }
        if (mTvId2 != null && !TextUtils.isEmpty(sinopecAccountCount)){
            mTvId2.setText("中石化账号："+sinopecAccountCount+"个");
        }else{
            mTvId2.setText("中石化账号：没绑定");
        }

        if (mTvId3 != null && !TextUtils.isEmpty(oilMoney)){
            mTvId3.setText("可充卡额度：¥"+oilMoney);
        }
        Button btn  = (Button) findViewById(R.id.btn_modify_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPayWnd();
            }
        });

        LinearLayout petrochina = (LinearLayout)findViewById(R.id.ll_yl_petrochina);
        petrochina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				Intent intent = new Intent(mContext, AddOilCardActivity.class);
				intent.putExtra("lego_add_oilcard_type", "0");
                startActivity(intent);
				
            }
        });

        LinearLayout sinopec = (LinearLayout)findViewById(R.id.ll_yl_sinopec);
        sinopec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddOilCardActivity.class);
				intent.putExtra("lego_add_oilcard_type", "1");
				startActivity(intent);
            }
        });

        LinearLayout  petrochinalog = (LinearLayout) findViewById(R.id.ll_my_petrochinalog);
        LinearLayout  sinopeclog = (LinearLayout) findViewById(R.id.ll_my_sinopeclog);
        LinearLayout  oilcardList = (LinearLayout) findViewById(R.id.ll_my_oldcard);
        petrochinalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyWalletLogActivity.class);
                intent.putExtra("type", "10");
                startActivity(intent);
            }
        });
        sinopeclog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyWalletLogActivity.class);
                intent.putExtra("type", "11");
                startActivity(intent);
            }
        });

        oilcardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyOilCardListActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void gotoPayWnd(){
        Intent intent = new Intent(mContext, PayOilActivity.class);
        startActivity(intent);

    }
    protected void getMyOilCardInfo(){
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }

        post("/Wallet/GetMyOilCardInfo", object);
    }
    protected void onSuccessCallback(String url, String content, JSONObject param) {

        try {
            JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
            String petroChinaAccount = jsonObject.getString("PetroChinaAccountCount");
            String sinopecAccount = jsonObject.getString("SinopecAccountCount");
            String oilMoney = jsonObject.getString("OilMoney");

            if (mTvId1 != null && !TextUtils.isEmpty(petroChinaAccount)){
                mTvId1.setText("中石油账号："+petroChinaAccount+"个");
                AppApplication.getInstance().getUser().setPetroChinaAccountCount(petroChinaAccount);
            }

            if (mTvId2 != null && !TextUtils.isEmpty(sinopecAccount)){
                mTvId2.setText("中石化账号："+sinopecAccount+"个");
                AppApplication.getInstance().getUser().setSinopecAccountCount(sinopecAccount);
            }

            if (mTvId3 != null && !TextUtils.isEmpty(oilMoney)){
                mTvId3.setText("可充卡额度：¥"+oilMoney);
                AppApplication.getInstance().getUser().setOilMoney(oilMoney);
            }

        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
    }
}
