package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.adapter.WalletListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.model.Wallet;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.bigplan.lego.global.AbConstant.ACTION_HOME_REFRESH;

public class MyWalletLogActivity extends BaseTActivity implements AdapterView.OnItemClickListener, View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = MyWalletLogActivity.class.getSimpleName();

    LayoutInflater mInflater = null;
    private String  mStrLogType = null;
    private AbImageLoader mAbImageLoader = null;
    private ListView lv_mywallet;
    private WalletListViewAdapter mWalletAdapter;
    private List<Wallet> mwalletList;
    private LinearLayout  mllNothing = null;
    private SwipeRefreshLayout mSwipeRefresh;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywalletlog);
        mAbImageLoader = AbImageLoader.getInstance(this);
        mInflater = LayoutInflater.from(this);
        mStrLogType = getIntent().getStringExtra("type");
        initView();
        requestData();
    }

    public void onRefresh(){
        requestData();
        mSwipeRefresh.setRefreshing(false);
    }

    protected String getToolBarTitle(){
        return  getString(R.string.label_my_wallet_log);
    }

    private void initView() {
       if (!TextUtils.isEmpty(mStrLogType)){
           String reason = "";
           switch (mStrLogType){
               case "-1":
                   reason = getString(R.string.label_my_wallet_log_1);
                   break;
               case "0":
                   reason = getString(R.string.label_my_wallet_log0);
                   break;
               case "1":
                   reason = getString(R.string.label_my_wallet_log1);
                   break;
               case "2":
                   reason = getString(R.string.label_my_wallet_log2);
                   break;
               case "3":
                   reason = getString(R.string.label_my_wallet_log3);
                   break;
               case "4":
                   reason = getString(R.string.label_my_wallet_log4);
                   break;
               case "10":
                   reason = getString(R.string.label_my_wallet_log5);
                   break;
               case "11":
                   reason = getString(R.string.label_my_wallet_log6);
                   break;
           }
           if (!TextUtils.isEmpty(reason)){
               setToolBarTitle(reason);
           }
       }
        mwalletList =  new ArrayList<>();
        lv_mywallet = (ListView) findViewById(R.id.lv_mywallet);
        mllNothing = (LinearLayout) findViewById(R.id.nothing);

        lv_mywallet.setOnItemClickListener(this);
        mWalletAdapter =  new WalletListViewAdapter(mContext, mwalletList);
        lv_mywallet.setAdapter(mWalletAdapter);


        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefresh.setOnRefreshListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

    }
    private void requestData() {
        JSONObject object = new JSONObject();
        String url = "/Wallet/GetPayLog";
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());

            if (!TextUtils.isEmpty(mStrLogType) && mStrLogType.compareToIgnoreCase("-1") != 0){
                object.put("reason", mStrLogType);
            }

            if (!TextUtils.isEmpty(mStrLogType) && mStrLogType.equals("3")){
                url = "/Wallet/GetCashApplyLog";
                object.put("pageindex", 0);
                object.put("pagesize", 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        post(url, object);
    }

    protected void onShowErrorMsg(String url, String msg) {
        mllNothing.setVisibility(View.VISIBLE);
        lv_mywallet.setVisibility(View.GONE);
        AbToastUtil.showToast(mContext, msg);
    }

    protected void onSuccessCallback(String url, String content, JSONObject param) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (url.equals("/Wallet/GetPayLog")) {
            try {
                JSONArray jsonArray = new JSONObject(content).getJSONArray("data");
                mwalletList = (List<Wallet>) AbJsonUtil.fromJson(jsonArray.toString(), new TypeToken<List<Wallet>>() {});
                if (mwalletList != null && mwalletList.size() > 0){
                    mllNothing.setVisibility(View.GONE);
                    lv_mywallet.setVisibility(View.VISIBLE);
                    mWalletAdapter.updateView(mwalletList);
                }else{
                    mllNothing.setVisibility(View.VISIBLE);
                    lv_mywallet.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
