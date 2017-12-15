package android.bigplan.lego.fragment;

import android.bigplan.lego.R;
import android.bigplan.lego.activity.LoginRegisterActivity;
import android.bigplan.lego.activity.StoreDetailActivity;
import android.bigplan.lego.adapter.ArroundStaffListViewAdapter;
import android.bigplan.lego.adapter.SearchResultListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.model.AroundStaff;
import android.bigplan.lego.model.SearchResult;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import static android.bigplan.lego.global.AbConstant.ACTION_HOME_REFRESH;
import static android.bigplan.lego.global.AbConstant.ACTION_LOCALTION;

public class HomeMainStaffFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = HomeMainStaffFragment.class.getSimpleName();
    private ArroundStaffListViewAdapter.AroundAdapterType type;
    private boolean initialized;
    private int resId;
    private ListView mListView;
    private List<SearchResult> mObjects;
    private SearchResultListViewAdapter mListViewAdapter;
    private View rootView;
    private LocaltionReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new LocaltionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(ACTION_HOME_REFRESH);
        filter.addAction(ACTION_LOCALTION);

        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    public HomeMainStaffFragment( ) {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerMessageReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        this.resId  = arguments.getInt("home_main");
        String values = arguments.getString("values");
        String type = arguments.getString("type");
        this.type= ArroundStaffListViewAdapter.AroundAdapterType.getExamType(values, type);

        if (rootView == null) {
            rootView = (ViewGroup) inflater.inflate(resId, container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mObjects = new ArrayList<SearchResult>();
        if (mListView == null) {
            mListView = (ListView) view.findViewById(R.id.listview);
            mListViewAdapter = new SearchResultListViewAdapter(getContext(), mObjects);
            mListView.setAdapter(mListViewAdapter);
            mListView.setOnItemClickListener(this);
            mListView.setOnScrollListener(mScrollListener);
        }

    }

    public class LocaltionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive"+intent.getAction());
            if (ACTION_LOCALTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra("lego_localtion_get");
                if (!TextUtils.isEmpty(messge) && messge.equals("1")){
                    requestRemoteData();
                }
            }else if (ACTION_HOME_REFRESH.equals(intent.getAction())) {
                String messge = intent.getStringExtra("lego_home_isrefresh");
                if (!TextUtils.isEmpty(messge) && messge.equals("1")) {
                    requestRemoteData();
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!initialized) {
            requestRemoteData();
            initialized = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        getActivity().unregisterReceiver(mMessageReceiver);
    }

    private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        private boolean isIdle;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case SCROLL_STATE_IDLE:
                    isIdle = true;
                    break;
                default:
                    isIdle = false;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount == totalItemCount && isIdle) {
                //TODO 在这里添加加载下一页数据逻辑
            }
        }
    };

    protected  void onSuccessCallback(String url, String content, JSONObject param){
        AbResult abResult = (AbResult) AbJsonUtil.fromJson(content, AbResult.class);
        String jsonData = AbJsonUtil.toJson(abResult.getData());
        mObjects = (List<SearchResult>) AbJsonUtil.fromJson(jsonData, new TypeToken<List<SearchResult>>() {});
        mListViewAdapter.updateView(mObjects);
    }
    private void requestRemoteData() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
			object.put("longitude", Double.toString(AppApplication.getInstance().getLongitude()));
            object.put("latitude", Double.toString(AppApplication.getInstance().getLatitude()));
            object.put("type", "0");
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post(type.getUri(), object);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean isLogined = AppApplication.getInstance().isUserLogin;
        //跳转到详情
        Intent intent = new Intent();
        if (mObjects != null && mObjects.size() > 0){
            if (AppApplication.getInstance().isUserLogin){
                SearchResult  result = mObjects.get(position);
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

                intent.setClass(getActivity(), StoreDetailActivity.class);
                getActivity().startActivity(intent);
            }else{
                intent = new Intent(getActivity(), LoginRegisterActivity.class);
                startActivity(intent);
            }
        }
    }

}
