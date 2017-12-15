package android.bigplan.lego.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.msg.ReceptionRoomActivity;
import android.bigplan.lego.adapter.HomeMainFragmentAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.apppermission.PermissionsActivity;
import android.bigplan.lego.apppermission.PermissionsArray;
import android.bigplan.lego.apppermission.PermissionsChecker;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.model.AroundStaff;
import android.bigplan.lego.model.CarouselPic;
import android.bigplan.lego.model.HomeGridViewButton;
import android.bigplan.lego.model.IndexModule;
import android.bigplan.lego.model.SearchTag;
import android.bigplan.lego.model.Skills;
import android.bigplan.lego.model.User;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.DialogUtil;
import android.bigplan.lego.util.Logger;
import android.bigplan.lego.view.AbViewPagerAdapter;
import android.bigplan.lego.view.BannerViewPager;
import android.bigplan.lego.view.ClearEditText;
import android.bigplan.lego.view.ExpandScrollView;
import android.bigplan.lego.view.VpSwipeRefreshLayout;
import android.bigplan.lego.view.XTextView;
import android.bigplan.lego.view.pullview.AbPullToRefreshView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.uuzuche.lib_zxing.activity.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import static android.bigplan.lego.global.AbConstant.ACTION_HOME_REFRESH;


public class TabHomeActivity extends BaseTabActivity implements AdapterView.OnItemClickListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,/* AMapLocationListener,*/ ExpandScrollView.OnScrollListener {
    private final static String TAG = TabHomeActivity.class.getSimpleName();
    public final static int REQUESTCODE_SELECT_CITY = 11;
    public final static String EXTRA_SELECTED_CITY = "extra.selected_city";

    private List<IndexModule> mModule;

    private GridView mGridView;

    private ProgressDialog dialog;
    private CheckBox mCbSanCode, mCbMessage;

    private List<AroundStaff> mAroundStaffList;
    private List<CarouselPic> mCarouselPicList;
    private List<HomeGridViewButton> mBtnList;

    private AbImageLoader mAbImageLoader;

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption;
    private int bannerHeight;
    private Banner banner = null;
    private View mRlSearch;
    private int mSearchHeight;
    private View search_line;
    private ViewPager mVpMain;
    private SmartTabLayout mMainTabIndicator;
    private HomeMainFragmentAdapter mMainFragmentAdapter;
    private EditText mTvSearchBar = null;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private VpSwipeRefreshLayout mSwipeRefresh;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppApplication.getInstance().addActivity(this);
        mAbImageLoader = AbImageLoader.getInstance(this);
        mAroundStaffList = new ArrayList<AroundStaff>();

        initView();
//        initLocation();
        mPermissionsChecker = new PermissionsChecker(mContext);
        GetHomeConfig();
        getCarouselPicture(0, null);
        getArroundStaff();
    }

    public void GetHomeConfig() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("memberid", AppApplication.getInstance().getUser().getMemberId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post("/Home/GetHomePage", jsonParams);
    }
    private static final int REQUEST_CODE = 0; // 请求码
    private static final int REQUEST_SCAN_CODE = 111; // 请求码
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PermissionsArray.PERMISSIONS);
    }

    protected void onResume() {
        super.onResume();
        mSearchHeight = mRlSearch.getHeight();
        banner.startAutoPlay();
        String release = Build.VERSION.RELEASE;

        String substring = release.substring(0, 1);
        if(substring!=null&&Integer.valueOf(substring)>5) {
            // 缺少权限时, 进入权限配置页面
            if (mPermissionsChecker.lacksPermissions(PermissionsArray.PERMISSIONS)) {
                startPermissionsActivity();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        banner.stopAutoPlay();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocationClient!=null)
            mLocationClient.onDestroy();//销毁定位客户端。
    }

    public void updateView(String searchKey) {
        mTvSearchBar.setText(searchKey);
    }

    public void setSearchText(String text) {
        if (mTvSearchBar != null)
            mTvSearchBar.setText(text);
    }


    private void initView() {
        mGridView = (GridView) findViewById(R.id.gv_service_item);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        banner = (Banner) findViewById(R.id.banner);
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        //可选样式如下:
        //1. Banner.CIRCLE_INDICATOR    显示圆形指示器
        //2. Banner.NUM_INDICATOR    显示数字指示器
        //3. Banner.NUM_INDICATOR_TITLE    显示数字指示器和标题
        //4. Banner.CIRCLE_INDICATOR_TITLE    显示圆形指示器和标题
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setBannerAnimation(Transformer.Default);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.isAutoPlay(true);

        //设置轮播图片间隔时间（不设置默认为2000）
        banner.setDelayTime(4000);

        mRlSearch = findViewById(R.id.ly_search);
        search_line = findViewById(R.id.search_line);

        //  mCbLocation = (CheckBox) findViewById(R.id.btn_select_location);


       // mTvSearchBar.setOnClickListener(this);
        mCbMessage = (CheckBox) findViewById(R.id.btn_msg_center);
        mCbSanCode = (CheckBox) findViewById(R.id.btn_scancode);
        mCbSanCode.setOnClickListener(new View.OnClickListener() {
            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }

            @Override
            public String toString() {
                return super.toString();
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(mContext, CaptureActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                Intent intent = new Intent(getApplication(), com.uuzuche.lib_zxing.activity.CaptureActivity.class);
                startActivityForResult(intent, REQUEST_SCAN_CODE);
            }
        });


        mVpMain = (ViewPager) findViewById(R.id.vp_main);
        mMainTabIndicator = (SmartTabLayout) findViewById(R.id.viewpagertab);
        mMainFragmentAdapter = new HomeMainFragmentAdapter(getSupportFragmentManager(), this);
        mVpMain.setAdapter(mMainFragmentAdapter);
        mVpMain.setOffscreenPageLimit(1);

        mMainTabIndicator.setViewPager(mVpMain);

        mSwipeRefresh = (VpSwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefresh.offsetTopAndBottom(600);
        mSwipeRefresh.setOnRefreshListener(this);

        ((ExpandScrollView) findViewById(R.id.scrollView)).setOnScrollListener(this);
        //    mCbLocation.setOnClickListener(this);

        mCbMessage.setOnClickListener(this);

        mTvSearchBar = (EditText) findViewById(R.id.tv_search_bar);
        mTvSearchBar.setFocusable(true);
        mTvSearchBar.setFocusableInTouchMode(true);
        mTvSearchBar.requestFocus();
        mTvSearchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mTvSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );
                    }

                    if (mTvSearchBar.getText() != null && !mTvSearchBar.getText().toString().equalsIgnoreCase("")) {
                        String text = mTvSearchBar.getText().toString();
                        Intent intent = new Intent(mContext, SearchFromModuleActivity.class);
                        intent.putExtra("key", text);
                        startActivity(intent);
                    }

                    return true;
                }

                return false;
            }
        });

    }

    public void onRefresh(){
        if (banner != null){
            banner.stopAutoPlay();
        }

        GetHomeConfig();
        getCarouselPicture(0, null);
        if (banner != null){
            banner.startAutoPlay();
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_HOME_REFRESH);
        intent.putExtra("lego_home_isrefresh", "1");
        sendBroadcast(intent);
        mSwipeRefresh.setRefreshing(false);
    }

    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.btn_msg_center:
                if (AppApplication.getInstance().isUserLogin) {
                    intent = new Intent(mContext, ReceptionRoomActivity.class);
                } else {
                    intent = new Intent(mContext, LoginRegisterActivity.class);
                }
                startActivity(intent);
                break;

            case R.id.tv_search_bar:
                Intent secondActivity = null;
                if (AppApplication.getInstance().isUserLogin) {
                    secondActivity = new Intent(mContext, MainTabActivity.class);
                    secondActivity.putExtra("action", "second");
                    AbSharedUtil.putString(mContext, AbConstant.KEY_FROM_HOME, "1");
                    startActivity(secondActivity);

                } else {
                    secondActivity = new Intent(mContext, LoginRegisterActivity.class);
                }
                startActivity(secondActivity);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Logger.d(TAG, "position: " + position);
        if (position == 0) {
            Intent intent;
            if (AppApplication.getInstance().isUserLogin) {

            } else {
                intent = new Intent(mContext, LoginRegisterActivity.class);
                startActivity(intent);
            }
        }
    }

    protected String getInitParam(String result){
        String keyWord = result;
        try {
            keyWord = URLDecoder.decode(result, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        result = AbRequest.getOriginalData(keyWord);
        return result;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        if (requestCode == REQUESTCODE_SELECT_CITY) {
        } else if (requestCode == REQUEST_SCAN_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != dataIntent) {
                Bundle bundle = dataIntent.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String resultString = bundle.getString(CodeUtils.RESULT_STRING);
                    //http://39.108.107.221:1317/member/register?params=
                    // V5ET5YVl0i3zLtjkJqbFG2bDzHVE8Wor8oAaTFzmaZ83oWZnZQQXEXPEXXMQDrsjd8fv5BKZv3FfgDQ2XpnTA4ya0Wjy2eyN
                    //V5ET5YVl0i3zLtjkJqbFG2bDzHVE8Wor8oAaTFzmaZ83oWZnZQQXEXPEXXMQDrsjd8fv5BKZv3FfgDQ2XpnTA4ya0Wjy2eyN
                    //解析params的字段了
                    String[] split = resultString.split("params=");
                    String params = split[1];
                    String data = getInitParam(params);
                    if (!TextUtils.isEmpty(data)){
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            String type = jsonObject.getString("type");
                            String objid = jsonObject.getString("objid");
                            int nType = Integer.parseInt(type);
                            Intent intent = null;
                            switch (nType){
                                case 1://个人的二维码
                                    AbToastUtil.showToast(mContext, getString(R.string.qrcode_invitecode));
                                    break;
                                case 2://转账二维码支付
                                    if (AppApplication.getInstance().isUserLogin){
                                        String pwd = AppApplication.getInstance().getUser().getPayPwd();
                                        if (TextUtils.isEmpty(pwd)){
                                            AbToastUtil.showToast(mContext, "您还没设置支付密码，请先设置支付密码！");
                                        }else{
                                            intent = new Intent(mContext, ShopPayActivity.class);
                                            intent.putExtra("staffid", objid);
                                            startActivity(intent);
                                        }
                                    }else{
                                        intent = new Intent(mContext, LoginRegisterActivity.class);
                                        startActivity(intent);
                                    }

                                    break;
                                case 3: //商家二维码，跳转到商店详情
                                    if (AppApplication.getInstance().isUserLogin){
                                        intent = new Intent(mContext, StoreDetailActivity.class);
                                        intent.putExtra("store_memberid",  objid);
                                        intent.setClass(mContext, StoreDetailActivity.class);
                                        startActivity(intent);
                                    }else{
                                        intent = new Intent(mContext, LoginRegisterActivity.class);
                                        startActivity(intent);
                                    }

                                    break;

                            }
                        }catch (Exception e){
                            AbToastUtil.showToast(mContext, getString(R.string.qrcode_invitecode));
                        }
                    }else{
                        AbToastUtil.showToast(mContext, getString(R.string.qrcode_invitecode));
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(mContext, "Scan failed!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(mContext, "Scan failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getArroundStaff() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    protected void onStartCallback() {
        if (dialog == null) {
            dialog = DialogUtil.loadingDialog(mContext, R.string.loading);
        }
        dialog.show();
    }

    protected void onFinishCallback() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected void onSuccessCallback(String url, String content, JSONObject param) {
        try {
            if (url.equals("/Home/GetCarouselPicture")) {
                JSONObject jsonObject = new JSONObject(content);
                JSONArray carouselListJson = jsonObject.getJSONArray("data");
                List<CarouselPic> tmpLeagueList = (List<CarouselPic>) AbJsonUtil.fromJson(carouselListJson.toString(), new TypeToken<List<CarouselPic>>() {
                });
                mCarouselPicList = tmpLeagueList == null ? new ArrayList<CarouselPic>(0) : tmpLeagueList;
                initBannerPic(mCarouselPicList);
            } else if (url.equals("/Home/GetHomePage")) {
                JSONObject jsonObject = new JSONObject(content);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray module = data.getJSONArray("module");
                mModule = (List<IndexModule>) AbJsonUtil.fromJson(module.toString(), new TypeToken<List<IndexModule>>() {
                });
                if (mModule != null) {
                    if(mModule.size()>6) {
                        mGridView.setNumColumns(4);
                    }
                    Collections.sort(mModule);

                    mGridView.setAdapter(new BaseAdapter() {
                        @SuppressLint({"ViewHolder", "NewApi"})
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final IndexModule module = mModule.get(position);
                            View gridItem = LayoutInflater.from(mContext).inflate(R.layout.gridview_home_service_btn, null);

                            if (!AbStrUtil.isEmpty(module.getTitle())) {//module标题
                                ((TextView) gridItem.findViewById(R.id.tv_name)).setText(module.getTitle());
                            }

                            if (!AbStrUtil.isEmpty(module.getFontColor())) { //设置字体的颜色
                                ((TextView) gridItem.findViewById(R.id.tv_name)).setTextColor(Color.parseColor(module.getFontColor().trim()));
                            }

                            if (!AbStrUtil.isEmpty(module.getIconLink())) {//module图标
                                mAbImageLoader.display(((ImageView) gridItem.findViewById(R.id.iv_avatar)), R.mipmap.ic_launcher, module.getIconLink());
                            } else {
                                ((ImageView) gridItem.findViewById(R.id.iv_avatar)).setBackgroundResource(module.getIcon());
                            }

                            gridItem.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = null;
                                    if (AppApplication.getInstance().isUserLogin) {
                                        String strLink = module.getLink();
                                        if (!TextUtils.isEmpty(strLink)) {
                                            intent = new Intent(mContext, WebActivity.class);
                                            intent.putExtra(WebActivity.WEB_KEY_URL, strLink);
                                        } else {
                                            intent = new Intent(mContext, SearchFromModuleActivity.class);
                                            intent.putExtra("title", module.getTitle());
                                            intent.putExtra("type",  String.valueOf(module.getModuleId()));
                                            startActivity(intent);
                                        }
                                    } else {
                                        intent = new Intent(mContext, LoginRegisterActivity.class);
                                    }
                                    startActivity(intent);

                                }
                            });

                            return gridItem;
                        }

                        public long getItemId(int position) {
                            return 0;
                        }

                        public Object getItem(int position) {
                            return null;
                        }

                        public int getCount() {
                            return mModule == null ? 0 : mModule.size();
                        }
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCarouselPicture(final int type, final AbPullToRefreshView view) {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Home/GetCarouselPicture", object);
    }

    private void initBannerPic(final List<CarouselPic> carouselPics) {
        if (carouselPics != null && carouselPics.size() > 0) {
            ArrayList<View> playViews = new ArrayList<View>();
            ArrayList<String> titles = new ArrayList<String>();
            ArrayList<String> images = new ArrayList<String>();

            for (int i = 0; i < carouselPics.size(); i++) {
                  final CarouselPic tmpCarousel = carouselPics.get(i);
//                View playView = View.inflate(this, R.layout.item_viewpager_banner, null);
//                final ImageView mPlayImage = (ImageView) playView.findViewById(R.id.iv_play_image);
//                mPlayImage.setTag(tmpCarousel.getPic());
//                mAbImageLoader.display(mPlayImage, tmpCarousel.getPic(), 654, 165);
//                mPlayImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Logger.d(TAG, "name" + tmpCarousel.getTitle());
//                        String strLink = tmpCarousel.getLink();
//                        if (!TextUtils.isEmpty(strLink)) {
//                            Intent intent = new Intent(mContext, WebActivity.class);
//                            intent.putExtra(WebActivity.WEB_KEY_URL, strLink);
//                            mContext.startActivity(intent);
//                        }
//                    }
//                });
//                playViews.add(playView);
                String pic =  tmpCarousel.getPic();
                images.add(pic);

                String title = tmpCarousel.getTitle();
                titles.add(title);
            }
            if (titles.size() > 0 && images.size() > 0){
                banner.setBannerTitles(titles);
                banner.setImages(images);
                banner.setImages(images)
                        .setImageLoader(new GlideImageLoader())
                        .setOnBannerListener(new OnBannerListener() {
                            public void OnBannerClick(int position) {
                                if (position <= mCarouselPicList.size()){
                                    final CarouselPic tmpCarousel = carouselPics.get(position);
                                    String strLink = tmpCarousel.getLink();
                                    if (!TextUtils.isEmpty(strLink)) {
                                        Intent intent = new Intent(mContext, WebActivity.class);
                                        intent.putExtra(WebActivity.WEB_KEY_URL, strLink);
                                        mContext.startActivity(intent);
                                    }
                                }
                            }
                        }).start();
            }
//            mBannerViewPager.setAdapter(new AbViewPagerAdapter(this, playViews));
//            mBannerIndicator.setViewPager(mBannerViewPager);
        }
    }

    protected String getToolBarTitle() {
        return null;
    }

    public void onScrollChanged(int x, int y, int oldX, int oldY) {
        boolean b = y >= bannerHeight - mSearchHeight;
        //   mCbLocation.setChecked(b);
        mCbMessage.setChecked(b);
        if (b) {
            search_line.setVisibility(View.VISIBLE);
        } else {
            search_line.setVisibility(View.INVISIBLE);
        }
    }

    public void onScrollStopped() {
    }

    public void onScrolling() {
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);

        }
    }

}
