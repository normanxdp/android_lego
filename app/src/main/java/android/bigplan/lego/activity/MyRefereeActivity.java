package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.adapter.MyRefereeListAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.model.Referee;
import android.bigplan.lego.model.SearchResult;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.Intent;
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

public class MyRefereeActivity extends BaseTActivity implements AdapterView.OnItemClickListener, View.OnClickListener , SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = MyRefereeActivity.class.getSimpleName();
    private SwipeRefreshLayout mSwipeRefresh = null;
    LayoutInflater mInflater = null;
    private String  mStrLogType = null;
    private AbImageLoader mAbImageLoader = null;
    private ListView mListView;
    private MyRefereeListAdapter mListViewAdapter;
    private List<Referee> mList;
    private LinearLayout  mllNothing = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreferee);
        mAbImageLoader = AbImageLoader.getInstance(this);
        mInflater = LayoutInflater.from(this);
        mStrLogType = getIntent().getStringExtra("type");
        initView();
        requestData();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.label_lazy_myreferee2);
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

        mListView.setOnItemClickListener(this);
        mListViewAdapter =  new MyRefereeListAdapter(mContext, mList);
        mListView.setAdapter(mListViewAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();

    }

    @Override
    public void onClick(View v) {

    }
    private void requestData() {
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Member/GetMyReferee", object);
    }

    protected void onShowErrorMsg(String url, String msg) {
        mllNothing.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        AbToastUtil.showToast(mContext, msg);
    }

    protected void onSuccessCallback(String url, String content, JSONObject param) {
        if (url.equals("/Member/GetMyReferee")) {
            try {
                JSONArray jsonArray = new JSONObject(content).getJSONArray("data");
                mList = (List<Referee>) AbJsonUtil.fromJson(jsonArray.toString(), new TypeToken<List<Referee>>() {});
                if (mList != null && mList.size() > 0){
                    mllNothing.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mListViewAdapter.updateListView(mList);
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
