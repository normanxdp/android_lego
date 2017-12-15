package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.adapter.SearchResultListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.model.ApplyCash;
import android.bigplan.lego.model.SearchResult;
import android.bigplan.lego.util.AbAppUtil;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
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

public class SearchFromModuleActivity extends BaseTActivity implements AdapterView.OnItemClickListener, View.OnClickListener , SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = SearchFromModuleActivity.class.getSimpleName();
    private SwipeRefreshLayout mSwipeRefresh = null;
    private String  mStrLogType = "10000";
    private String mKey = null;
    private ListView mListView;
    private SearchResultListViewAdapter mListViewAdapter;
    private List<SearchResult> mList;
    private LinearLayout  mllNothing = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywalletlog);
        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type)){
            mStrLogType = type;
            mKey = null;
        }else{
            mKey = getIntent().getStringExtra("key");
        }

        initView();
        requestData();
    }
    protected void back() {
        Intent intent = new Intent(mContext, MainTabActivity.class);
        startActivity(intent);
        finish();
    }
    public void onBackPressed() {
        back();
    }

    protected String getToolBarTitle(){
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)){
            return title;
        }
        mKey = getIntent().getStringExtra("key");
        if (!TextUtils.isEmpty(mKey)){
            return getString(R.string.label_title_result_search);
        }
        return  getString(R.string.label_around_staff);
    }
    public void onRefresh(){
        requestData();
        mSwipeRefresh.setRefreshing(false);
    }
    private void initView() {
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefresh.setOnRefreshListener(this);
        mList =  new ArrayList<>();
        mListView = (ListView) findViewById(R.id.lv_mywallet);
        mllNothing = (LinearLayout) findViewById(R.id.nothing);
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(getString(R.string.error_no_shop));

        mListView.setOnItemClickListener(this);
        mListViewAdapter =  new SearchResultListViewAdapter(mContext, mList);
        mListView.setAdapter(mListViewAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        if (mList != null && mList.size() > 0){
            if (AppApplication.getInstance().isUserLogin){
                SearchResult  result = mList.get(position);
                String memberId = result.getMemberId();
                String distance = result.getDistance();
                String avatar = result.getAvatar();

                Log.d(TAG, "memberId="+memberId+",lego_store_distance="+distance+",lego_store_avatar="+avatar);
                intent.putExtra("store_memberid", memberId);
                intent.putExtra("lego_store_distance", distance);
                intent.putExtra("lego_store_avatar", avatar);
                intent.putExtra("lego_store_name", result.getShowName());
                intent.putExtra("lego_store_address", result.getAddress());
                intent.putExtra("lego_store_mobilereserve", result.getMobileReserve());
                intent.putExtra("lego_store_introduction", result.getIntroduction());

                intent.setClass(mContext, StoreDetailActivity.class);
                startActivity(intent);
            }else{
                intent = new Intent(mContext, LoginRegisterActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
    private void requestData() {
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("longitude", Double.toString(AppApplication.getInstance().getLongitude()));
            object.put("latitude", Double.toString(AppApplication.getInstance().getLatitude()));
            if (!TextUtils.isEmpty(mKey)){
                object.put("skey", mKey);
                post("/Member/SearchStaff", object);
            }else{
                object.put("type", mStrLogType);
                post("/Member/GetStaffByType", object);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onShowErrorMsg(String url, String msg) {
        mllNothing.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        AbToastUtil.showToast(mContext, msg);
    }

    protected void onSuccessCallback(String url, String content, JSONObject param) {

        if (url.equals("/Member/GetStaffByType")|| url.equals("/Member/SearchStaff")) {
            try {
                JSONArray jsonArray = new JSONObject(content).getJSONArray("data");
                mList = (List<SearchResult>) AbJsonUtil.fromJson(jsonArray.toString(), new TypeToken<List<SearchResult>>() {});
                if (mList != null && mList.size() > 0){
                    mllNothing.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mListViewAdapter.updateView(mList);
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
