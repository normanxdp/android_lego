package android.bigplan.lego.fragment;

import android.app.Activity;
import android.app.Application;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.LoginRegisterActivity;
import android.bigplan.lego.activity.NaviToHereActivity;
import android.bigplan.lego.activity.SearchActivity;
import android.bigplan.lego.activity.StoreDetailActivity;
import android.bigplan.lego.activity.TabNearbyActivity;
import android.bigplan.lego.activity.StaffDetailActivity;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.model.AroundStaff;
import android.bigplan.lego.model.SearchResult;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.bigplan.lego.view.XTextView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.hedgehog.ratingbar.RatingBar;

import java.util.ArrayList;
import java.util.List;
import android.bigplan.lego.global.AbConstant;


/**
 * Created by gg on 16-4-24.
 */
public class NearByMapFragment extends BaseFragment implements LocationSource,
        AMapLocationListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter,
        AMap.OnMapLoadedListener {
    private static final String TAG = NearByMapFragment.class.getSimpleName();

    private View rootView;
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private List<SearchResult> mSearchResultList;
    private List<LatLng> mStaffLatLngList;
    private AbImageLoader mAbImageLoader = null;
    private XTextView mTVSearch = null;

    private TabNearbyActivity mParentActivity = null;

    public NearByMapFragment() {
        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_nearby_map, container,
                false);
        AMap.OnInfoWindowClickListener onInfoWindowClickListener = new AMap.OnInfoWindowClickListener(){

            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker.isInfoWindowShown()){
                    marker.hideInfoWindow();//这个是隐藏infowindow窗口的方法
                }
            }
        };
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapView = (MapView) getView().findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();

    }

    public void updateView(ArrayList<SearchResult> resultList, String searchKey) {
        mSearchResultList = resultList;
        addStaffMarkersToMap();
        if (!TextUtils.isEmpty(searchKey)){
            mTVSearch.setText(searchKey);
        }

    }

    public void setSearchText(String text) {
        if (mTVSearch != null)
            mTVSearch.setText(text);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = (TabNearbyActivity) getActivity();
        mSearchResultList = new ArrayList<>();
        mStaffLatLngList = new ArrayList<>();
    }

    private void initView() {
        mParentActivity = (TabNearbyActivity) getActivity();

        if (aMap == null) {
            aMap = mapView.getMap();
        }
        setUpMap();
        mAbImageLoader = AbImageLoader.getInstance(mParentActivity);
        mTVSearch = (XTextView) getView().findViewById(R.id.tv_search_bar);
        mTVSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mParentActivity.switchFragment(TabNearbyActivity.FragmentType.NearByList);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.icon_location));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细

        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false


        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件

        addStaffMarkersToMap();
//        AbToastUtil.showToast(getActivity().getApplicationContext(), "设置一些amap的属性");
    }

    /**
     * 在地图上添加marker
     */
    private void addStaffMarkersToMap() {
        if (mSearchResultList != null && mSearchResultList.size() > 0) {

            for (int i = 0; i < mSearchResultList.size(); i++) {
                SearchResult result = mSearchResultList.get(i);

                LatLng position = new LatLng(Double.valueOf(result.getLatitude()), Double.valueOf(result.getLongitude()));
                mStaffLatLngList.add(position);
                Marker marker  = aMap.addMarker(new MarkerOptions()
                        .position(position)
                        .draggable(false)
                        .title(result.getShowName())
                        .snippet(String.valueOf(result.getMemberId()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_shop_marker)));
//                arker.showInfoWindow();

            }


        }
//        for (int i = 0; i < 5; i++) {
//            LatLng position = new LatLng(Float.valueOf("22.54"+i),
//                    Float.valueOf("114.0"+i));
//            mStaffLatLngList.add(position);
//            Marker marker =   aMap.addMarker(new MarkerOptions()
//                    .position(position)
//                    .icon(BitmapDescriptorFactory
//                            .fromResource((R.drawable.icon_staff_marker)))
//                    .title("在这里"+i));
//        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
//        AbToastUtil.showToast(getActivity().getApplicationContext(),"定位成功后回调函数");
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                AppApplication.getInstance().setLocation(amapLocation);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
              //  Logger.d(TAG, "city: " + amapLocation.getCity() + "  bearing: " + amapLocation.getBearing());
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);

            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mParentActivity.getApplicationContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(false);
            mLocationOption.setInterval(1000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
//            AbToastUtil.showToast(getActivity().getApplicationContext(),"激活定位");
//            if (mAMapLocationManager == null) {
//                mAMapLocationManager = LocationManagerProxy.getInstance(this);
//			/*
//			 * mAMapLocManager.setGpsEnable(false);
//			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
//			 * API定位采用GPS和网络混合定位方式
//			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
//			 */
//                mAMapLocationManager.requestLocationData(
//                        LocationProviderProxy.AMapNetwork, 2000, 10, this);
//            }
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
//        AbToastUtil.showToast(getActivity().getApplicationContext(),"停止定位");
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();


            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        Logger.d(TAG, "getInfoWindow");
        View view = LayoutInflater.from(mParentActivity).inflate(R.layout.marker_popup_window, null);

        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        ImageView ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        TextView tvIntroduction = (TextView) view.findViewById(R.id.tv_introduction);
        TextView btnCheckDetail = (TextView) view.findViewById(R.id.btn_check_detail);
        TextView btnNavi =(TextView) view.findViewById(R.id.btn_navi);

        SearchResult result = null;
        for (int i = 0; i < mSearchResultList.size(); i++) {
            result = mSearchResultList.get(i);
            if (result.getMemberId().equalsIgnoreCase(marker.getSnippet())) {
                break;
            }
        }

        if (result != null) {
            tvName.setText(result.getShowName());
            tvIntroduction.setText(result.getIntroduction());
            mAbImageLoader.display(ivAvatar, R.drawable.default_head, result.getAvatar());

            final SearchResult tmpResult = result;
            btnCheckDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (AppApplication.getInstance().isUserLogin){
                        intent = new Intent(mParentActivity, StoreDetailActivity.class);
                        String memberId = tmpResult.getMemberId();
                        String distance = tmpResult.getDistance();
                        String avatar = tmpResult.getAvatar();

                        Log.d(TAG, "memberId="+memberId+",lego_store_distance="+distance+",lego_store_avatar="+avatar);
                        intent.putExtra("store_memberid", memberId);
                        intent.putExtra("lego_store_distance", distance);
                        intent.putExtra("lego_store_avatar", avatar);
                        intent.putExtra("lego_store_name", tmpResult.getShowName());
                        intent.putExtra("lego_store_address", tmpResult.getAddress());
                        intent.putExtra("lego_store_mobilereserve", tmpResult.getMobileReserve());
                        intent.putExtra("lego_store_introduction", tmpResult.getIntroduction());

                        mParentActivity.startActivity(intent);
                        getActivity().startActivity(intent);
                    }else{
                        intent = new Intent(getActivity(), LoginRegisterActivity.class);
                        startActivity(intent);
                    }

                    marker.hideInfoWindow();
                }
            });

            btnNavi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mParentActivity, NaviToHereActivity.class);

                    String lat = tmpResult.getLatitude();
                    String lng = tmpResult.getLongitude();
                    if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng) || lat.equals("0") || lng.equals("0")){
                        AbToastUtil.showToast(mParentActivity, getString(R.string.error_localtion_store));
                        return;
                    }
                    double sLat = AppApplication.getInstance().getLatitude();
                    double sLng = AppApplication.getInstance().getLongitude();
                    if (sLng < 73.0 || sLng > 136.0 || sLat < 3.0 || sLat > 74.0) { //中国的经纬度是73.2-135.5 纬度 3-54
                        AbToastUtil.showToast(mParentActivity, getString(R.string.error_localtion_current));
                        return;
                    }

                    intent.putExtra(NaviToHereActivity.EXTRA_DEST_LAT, Double.valueOf(lat));
                    intent.putExtra(NaviToHereActivity.EXTRA_DEST_LNG,  Double.valueOf(lng));
                    intent.putExtra(NaviToHereActivity.EXTRA_START_LAT, sLat);
                    intent.putExtra(NaviToHereActivity.EXTRA_START_LNG, sLng);

                    startActivity(intent);
                }
            });
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.hideInfoWindow();

            }
        });
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
        LatLngBounds bounds = new LatLngBounds.Builder().build();
        for (int i = 0; i < mStaffLatLngList.size(); i++) {
            bounds.including(mStaffLatLngList.get(i));
        }
        LatLng position = new LatLng(AppApplication.getInstance().getLatitude(),
                AppApplication.getInstance().getLongitude());
        bounds.including(position);

        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 18));

        LatLng localLatLng = new LatLng(AbConstant.DEFAULT_DOUBLE_LATITUDE, AbConstant.DEFAULT_DOUBLE_LONGITUDE);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localLatLng,18));

    }


}
