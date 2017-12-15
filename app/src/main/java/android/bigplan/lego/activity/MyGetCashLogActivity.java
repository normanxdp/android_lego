package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.adapter.GetCashListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.model.ApplyCash;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbToastUtil;
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

public class MyGetCashLogActivity extends BaseTActivity implements AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = MyGetCashLogActivity.class.getSimpleName();

    LayoutInflater mInflater = null;
    private String  mStrLogType = null;
    private AbImageLoader mAbImageLoader = null;
    private ListView mListView;
    private GetCashListViewAdapter mCashLogAdapter;
    private List<ApplyCash> mCashList;
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
        return  getString(R.string.label_my_wallet_log3);
    }

    private void initView() {

        mCashList =  new ArrayList<>();
        mListView = (ListView) findViewById(R.id.lv_mywallet);
        mllNothing = (LinearLayout) findViewById(R.id.nothing);

        mListView.setOnItemClickListener(this);
        mCashLogAdapter =  new GetCashListViewAdapter(mContext, mCashList);
        mListView.setAdapter(mCashLogAdapter);

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
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("pageindex", 0);
            object.put("pagesize", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Wallet/GetCashApplyLog", object);
    }

    protected void onShowErrorMsg(String url, String msg) {
        mllNothing.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        AbToastUtil.showToast(mContext, msg);
    }

    protected void onSuccessCallback(String url, String content, JSONObject param) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (url.equals("/Wallet/GetCashApplyLog")) {
            try {
                JSONArray jsonArray = new JSONObject(content).getJSONArray("data");
                mCashList = (List<ApplyCash>) AbJsonUtil.fromJson(jsonArray.toString(), new TypeToken<List<ApplyCash>>() {});
                if (mCashList != null && mCashList.size() > 0){
                    mllNothing.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mCashLogAdapter.updateView(mCashList);
                }else{
                    mllNothing.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
