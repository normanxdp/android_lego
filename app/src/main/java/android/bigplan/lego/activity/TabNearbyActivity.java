package android.bigplan.lego.activity;

import android.app.ProgressDialog;
import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.fragment.NearByListFragment;
import android.bigplan.lego.fragment.NearByMapFragment;
import android.bigplan.lego.fragment.NearBySearchResultFragment;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.model.AroundStaff;
import android.bigplan.lego.model.SearchResult;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.DialogUtil;
import android.bigplan.lego.util.Logger;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TabNearbyActivity extends BaseTabActivity implements NearbySearch.NearbyListener {
    private final static String TAG = TabNearbyActivity.class.getSimpleName();

    private FragmentType fragmentType;

    private static NearByMapFragment mMapFragment = null;
    private static NearByListFragment mListFragment = null;
    private static NearBySearchResultFragment mResultFragment = null;

    private Menu mMenu = null;
    private int mSearchState = 0;

    static int index = 0;

    public ArrayList<SearchResult> mResultList = null;
    public ArrayList<SearchResult> mAroundStaff = null;
    private String mSearchKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        AppApplication.getInstance().addActivity(this);
        mResultList = new ArrayList<>();
        mAroundStaff = new ArrayList<>();
        initView();
        getAroundStaff();
    }

    @Override
    protected String getToolBarTitle() {
        return getString(R.string.title_nearby);
    }

    @Override
    protected void back() {
        //Empty
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nearby, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_switch_map:
                onSwitchFragment(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppApplication.getInstance().mNearbySearch.addNearbyListener(this);

        if (AppApplication.getInstance().isUserLogin
                || "1".equalsIgnoreCase(AppApplication.getInstance().getUser().getIsLazyStaff())) {
            AppApplication.getInstance().mNearbySearch.startUploadNearbyInfoAuto(new UploadInfoCallback() {
                //设置自动上传数据和上传的间隔时间
                @Override
                public UploadInfo OnUploadInfoCallback() {
                    Logger.d(TAG, "OnUploadInfoCallback");
                    UploadInfo loadInfo = new UploadInfo();
                    loadInfo.setCoordType(NearbySearch.AMAP);
                    //位置信息
                    LatLonPoint latLonPoint = new LatLonPoint(
                            (float) (AppApplication.getInstance().getLatitude()),
                            (float) (AppApplication.getInstance().getLongitude()));
                    loadInfo.setPoint(latLonPoint);
                    Logger.d(TAG, "lat: " + (float) (AppApplication.getInstance().getLatitude()) +
                            "  long: " + (float) (AppApplication.getInstance().getLongitude()));
                    //上传member_id信息
                    Logger.d(TAG, "upload userid: " + AppApplication.getInstance().getUser().getMemberId() + "_1");
                    loadInfo.setUserID(AppApplication.getInstance().getUser().getMemberId() + "_1");
                    index++;
                    return loadInfo;
                }
            }, 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppApplication.getInstance().mNearbySearch.stopUploadNearbyInfoAuto();
    }

    public void searchNearByInfo(int distance) {
        if (AppApplication.getInstance().isInitLocaltion()){
            //设置搜索条件
            NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
            //设置搜索的中心点
            query.setCenterPoint(new LatLonPoint(AppApplication.getInstance().getLatitude(),
                            AppApplication.getInstance().getLongitude()));
            Logger.d(TAG, "searchNearByInfo lat: " + AppApplication.getInstance().getLatitude() +
                    "  long: " + AppApplication.getInstance().getLongitude());
            //设置搜索的坐标体系
            query.setCoordType(NearbySearch.AMAP);
            //设置搜索半径
            query.setRadius(distance);
            //设置查询的时间
            query.setTimeRange(1000);
            //设置查询的方式驾车还是距离
            query.setType(NearbySearchFunctionType.DISTANCE_SEARCH);
            //调用异步查询接口
            AppApplication.getInstance().mNearbySearch.searchNearbyInfoAsyn(query);
        }
    }

    @Override
    public void onUserInfoCleared(int i) {
        Logger.d(TAG, "onUserInfoCleared " + i);
    }

    //周边检索的回调函数
    @Override
    public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) {
        Logger.d(TAG, "onNearbyInfoSearched");
        //搜索周边附近用户回调处理
        if (resultCode == 1000) {
            if (nearbySearchResult != null
                    && nearbySearchResult.getNearbyInfoList() != null
                    && nearbySearchResult.getNearbyInfoList().size() > 0) {
                Logger.d(TAG, "list size: " + nearbySearchResult.getNearbyInfoList().size());
                for (int i = 0; i < nearbySearchResult.getNearbyInfoList().size(); i++) {
                    NearbyInfo nearbyInfo = nearbySearchResult.getNearbyInfoList().get(i);
                    Log.d(TAG, "周边搜索结果为size " + nearbySearchResult.getNearbyInfoList().size() +
                            "  first: " + nearbyInfo.getUserID() + "  " + nearbyInfo.getDistance() + "  "
                            + nearbyInfo.getDrivingDistance() + "  " + nearbyInfo.getTimeStamp() + "  " +
                            nearbyInfo.getPoint().toString());
                }

            } else {
                Log.d(TAG, "周边搜索结果为空");
            }
        } else {
            Log.d(TAG, "周边搜索出现异常，异常码为：" + resultCode);
        }
    }

    @Override
    public void onNearbyInfoUploaded(int i) {
        Logger.d(TAG, "onNearbyInfoUploaded " + i);
    }

    private void initView() {
        hideToolBarBack();
        
        mMapFragment = new NearByMapFragment();
        mListFragment = new NearByListFragment();
        mResultFragment = new NearBySearchResultFragment();

        fragmentType = FragmentType.NearByMap;
        mSearchState = FragmentType.NearByList.getIndex();
        startFragment();

    }

    public void clearAllSearchText() {
        mMapFragment.setSearchText("");
        mListFragment.setSearchText("");
        mResultFragment.setSearchText("");
    }

    private void startFragment() {
        Fragment fragment = fragmentType.getFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.frame_layout, fragment).commit();
    }

    public void onSwitchFragment(MenuItem item) {
        searchNearByInfo(1000);

        FragmentType toType = null;
        if (fragmentType == FragmentType.NearByList || fragmentType == FragmentType.NearByResult) {
            Logger.d(TAG, "change to map");
            item.setIcon(R.drawable.btn_switch_map);

            toType = FragmentType.NearByMap;
        } else {
            Logger.d(TAG, "change to list " + mSearchState);
            item.setIcon(R.drawable.btn_switch_list);

            if (mSearchState == FragmentType.NearByList.getIndex()) {
                toType = FragmentType.NearByList;
            } else {
                toType = FragmentType.NearByResult;
            }
        }

        switchFragment(toType);
    }


    public void switchFragment(FragmentType toType) {
        Fragment from;
        Fragment to;

        from = fragmentType.getFragment();
        to = toType.getFragment();
        fragmentType = toType;

        if (toType != FragmentType.NearByMap) {
            mSearchState = toType.getIndex();
            mMenu.getItem(0).setIcon(R.drawable.btn_switch_list);
        }

        if (from != to) {
            if (to.isAdded()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.hide(from);
                ft.show(to).commit();
            } else {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.hide(from);
                ft.add(R.id.frame_layout, to).commit();
            }
        }
    }

    public void getSearchResult(String key) {
        mSearchKey = key;
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("latitude", Double.toString(AppApplication.getInstance().getLatitude()));
            object.put("longitude", Double.toString(AppApplication.getInstance().getLatitude()));
            object.put("skey", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Member/SearchStaff", object);
    }

    public void getAroundStaff(){
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("latitude", Double.toString(AppApplication.getInstance().getLatitude()));
            object.put("longitude", Double.toString(AppApplication.getInstance().getLatitude()));
            object.put("type", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Member/GetAroundStaff", object);
    }
    protected void onSuccessCallback(String url, String content, JSONObject param) {
        try {
            JSONObject jsonObject = new JSONObject(content);
            JSONArray arrayJson = jsonObject.getJSONArray("data");
            if (url.indexOf("/Member/SearchStaff") != -1) {

                mResultList = (ArrayList<SearchResult>) AbJsonUtil.fromJson(arrayJson.toString(), new TypeToken<List<SearchResult>>() {});

                mResultFragment.updateView(mResultList, mSearchKey);
                mListFragment.updateView(mSearchKey);
            } else if (url.indexOf("/Member/GetAroundStaff") != -1){
                mAroundStaff = (ArrayList<SearchResult>) AbJsonUtil.fromJson(arrayJson.toString(), new TypeToken<List<SearchResult>>() {});
                mMapFragment.updateView(mAroundStaff, "");
            }

       } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum FragmentType {

        NearByMap(0, "map", mMapFragment),
        NearByList(1, "list", mListFragment),
        NearByResult(2, "result", mResultFragment);

        private int index;
        private String tag;
        private Fragment fragment;

        private FragmentType(int index, String tag, Fragment fragment) {
            this.index = index;
            this.tag = tag;
            this.fragment = fragment;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

    }
}
