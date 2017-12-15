package android.bigplan.lego.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.model.AroundStaff;
import android.bigplan.lego.model.CarouselPic;
import android.bigplan.lego.model.StoreDetail;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.DialogUtil;
import android.bigplan.lego.util.Logger;
import android.bigplan.lego.view.AbViewPagerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.reflect.TypeToken;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import android.bigplan.lego.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
public class StoreDetailActivity extends BaseTActivity {
    private final static String TAG = StoreDetailActivity.class.getSimpleName();

    private ImageView mIvAvatar = null;
    private ImageView mBtnBack = null;
    private ImageView mBtnShare = null;
    private ImageView mBtnFavorite = null;
    private TextView mTvTitle = null;
    private TextView mTvDistance = null;

    private TextView mTvStoreName = null;
    private TextView mTvStoreAddress = null;
    private TextView mTvStorePhone = null;
    private TextView mTvIntroduction = null;
    private SmartTabLayout mBannerIndicator;
    private BannerViewPager mBannerViewPager;
    private TextView mBtnNavi = null;
    private TextView mBtnBQ = null;
    private Button mBtnPay = null;
    private String mStrCodeUrl = null;
    private StoreDetail mStoreDetail = null;
    private List<CarouselPic> mCarouselPicList;
    private ProgressDialog dialog;
    private AbImageLoader mAbImageLoader;

    private String mMemberId = "0";


    @Override
    protected String getToolBarTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        AppApplication.getInstance().addActivity(this);
        mAbImageLoader = AbImageLoader.getInstance(this);
        String id = getIntent().getStringExtra("store_memberid");

        if (!TextUtils.isEmpty(id)){
            mMemberId = id;
        }else{
            AbToastUtil.showToast(mContext, R.string.label_error_store_id);
        }
        initView();
        getCarouselPicture();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBannerViewPager != null) {
            mBannerViewPager.play();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBannerViewPager != null) {
            mBannerViewPager.stop();
        }
    }

    private void initView() {
        mBannerViewPager = (BannerViewPager) findViewById(R.id.shop_pic_banner);
        mBannerIndicator = (SmartTabLayout) findViewById(R.id.shop_indicator_smart_tab);
        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
        String avatar = getIntent().getStringExtra("lego_store_avatar");
        if (!TextUtils.isEmpty(avatar)){
            mAbImageLoader.display(mIvAvatar, avatar, AppApplication.mWinWidth, 165);
        }

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setVisibility(View.GONE);

        mTvDistance = (TextView) findViewById(R.id.tv_distance);
        final String distance = getIntent().getStringExtra("lego_store_distance");
        if (!TextUtils.isEmpty(distance)){
            mTvDistance.setText("距您"+distance);
        }


        mTvStoreName = (TextView) findViewById(R.id.tv_store_name);
        String storeName = getIntent().getStringExtra("lego_store_name");
        if (!TextUtils.isEmpty(storeName)){
            mTvStoreName.setText(storeName);
        }

        mTvStoreAddress = (TextView) findViewById(R.id.tv_store_address);
        String address = getIntent().getStringExtra("lego_store_address");
        if (!TextUtils.isEmpty(address)){
            mTvStoreAddress.setText(address);
        }

        mTvStorePhone = (TextView) findViewById(R.id.tv_store_phone);
        String mobile = getIntent().getStringExtra("lego_store_mobilereserve");
        if (!TextUtils.isEmpty(mobile)){
            mTvStorePhone.setText(mobile);
        }

        mTvIntroduction = (TextView) findViewById(R.id.tv_introduction);
        String introduction = getIntent().getStringExtra("lego_store_introduction");
        if (!TextUtils.isEmpty(introduction)){
            mTvIntroduction.setText(introduction);
        }else{
            mTvIntroduction.setText("商家介绍:暂无");
        }

        mBtnBack = (ImageView) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        mBtnShare = (ImageView) findViewById(R.id.btn_share);
        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mBtnFavorite = (ImageView) findViewById(R.id.btn_favorite);
        mBtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMemberId.equals("0")){
                    AbToastUtil.showToast(mContext, R.string.error_noexist_store);
                }else{
                    String isFavoraite = mStoreDetail.getIsFavoraite();
                    if (TextUtils.isEmpty(isFavoraite) || isFavoraite.equals("0")){
                        addFavorite();
                    }else{
                        delFavorite();
                    }

                }
            }
        });

        mBtnNavi = (TextView) findViewById(R.id.btn_navi);
        mBtnNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NaviToHereActivity.class);
                Log.d(TAG, "Latitude"+mStoreDetail.getLatitude()+"Longitud,="+mStoreDetail.getLongitude());
                String lat = mStoreDetail.getLatitude();
                String lng = mStoreDetail.getLongitude();
                if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng) || lat.equals("0") || lng.equals("0")){
                    AbToastUtil.showToast(mContext, getString(R.string.error_localtion_store));
                    return;
                }
                double sLat = AppApplication.getInstance().getLatitude();
                double sLng = AppApplication.getInstance().getLongitude();
                if (sLng < 73.0 || sLng > 136.0 || sLat < 3.0 || sLat > 74.0) { //中国的经纬度是73.2-135.5 纬度 3-54
                    AbToastUtil.showToast(mContext, getString(R.string.error_localtion_current));
                    return;
                }

                intent.putExtra(NaviToHereActivity.EXTRA_DEST_LAT, Double.valueOf(lat));
                intent.putExtra(NaviToHereActivity.EXTRA_DEST_LNG,  Double.valueOf(lng));
                intent.putExtra(NaviToHereActivity.EXTRA_START_LAT, sLat);
                intent.putExtra(NaviToHereActivity.EXTRA_START_LNG, sLng);

                startActivity(intent);
            }
        });

        mBtnBQ = (TextView) findViewById(R.id.btn_bq);
        mBtnBQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showErWeiMa();
            }
        });

        mBtnPay = (Button) findViewById(R.id.btn_pay);
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShopPayActivity.class);
                intent.putExtra("staffid", mMemberId);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initData() {
        mStoreDetail = new StoreDetail();
        getStoreDetail();
        getQRcode(0);
    }

    public void getQRcode(int isClick) {
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("objid", mMemberId);
            object.put("type", "2");
            object.put("isClick", isClick);
        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Qr/GetEncoded", object);
    }

    public void showErWeiMa() {
        AbLogUtil.d("mStrCodeUrl:" + mStrCodeUrl);
        if (TextUtils.isEmpty(mStrCodeUrl)){
            getQRcode(1);
        }else{
            final Dialog alertDialog = new Dialog(this, R.style.selectorDialog);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(R.layout.item_erweima);
            ImageView iv = (ImageView) alertDialog.findViewById(R.id.erweima_iv);
            mAbImageLoader.display(iv, mStrCodeUrl);
            View fl = alertDialog.findViewById(R.id.erweima_ly);
            fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }

    }
    private void addFavorite(){
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("staffid", mMemberId);
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Member/AddFavorite", object);
    }

    private void delFavorite(){
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("staffid", mMemberId);
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Member/DelFavorite", object);
    }

    private void getStoreDetail() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("staffid", mMemberId);
            object.put("longitude", Double.toString(AppApplication.getInstance().getLongitude()));
            object.put("latitude", Double.toString(AppApplication.getInstance().getLatitude()));
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Member/GetStoreDetail", object);
    }

    protected void onStartCallback(String url, JSONObject param) {
        if (url.equals("/Member/GetStoreDetail")) {
            if (dialog == null) {
                dialog = DialogUtil.loadingDialog(mContext, R.string.loading);
            }
            dialog.show();
        }
    }

    protected void onFinishCallback() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void initBannerPic(final List<CarouselPic> carouselPics) {
        if (carouselPics != null && carouselPics.size() > 0) {
            mIvAvatar.setVisibility(View.GONE);
            mBannerViewPager.setVisibility(View.VISIBLE);
            mBannerIndicator.setVisibility(View.VISIBLE);

            ArrayList<View> playViews = new ArrayList<View>();
            for (int i = 0; i < carouselPics.size(); i++) {
                final CarouselPic tmpCarousel = carouselPics.get(i);
                View playView = View.inflate(this, R.layout.item_viewpager_banner, null);
                final ImageView mPlayImage = (ImageView) playView.findViewById(R.id.iv_play_image);
                mPlayImage.setTag(tmpCarousel.getPic());
                mAbImageLoader.display(mPlayImage, tmpCarousel.getPic(), AppApplication.mWinWidth, 165);

                String title = tmpCarousel.getTitle();
                if (!TextUtils.isEmpty(title)){
                    final LinearLayout llImgage = (LinearLayout)playView.findViewById(R.id.ll_play_image);
                    final TextView playText = (TextView)playView.findViewById(R.id.mPlayText);
                    llImgage.setVisibility(View.VISIBLE);
                    playText.setText(title);
                }
                mPlayImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.d(TAG, "name" + tmpCarousel.getTitle());
                        String strLink = tmpCarousel.getLink();
                        if (!TextUtils.isEmpty(strLink)) {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra(WebActivity.WEB_KEY_URL, strLink);
                            mContext.startActivity(intent);
                        }
                    }
                });
                playViews.add(playView);
            }
            mBannerViewPager.setAdapter(new AbViewPagerAdapter(this, playViews));
            mBannerIndicator.setViewPager(mBannerViewPager);
        }
    }
    private void getCarouselPicture() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("staffid", mMemberId);
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Home/GetShopCarouselPicture", object);
    }
    protected void onSuccessCallback(String url, String content, JSONObject param) {
        try {

            if (url.equals("/Home/GetShopCarouselPicture")){
                JSONObject jsonObject = new JSONObject(content);
                JSONArray carouselListJson = jsonObject.getJSONArray("data");
                List<CarouselPic> tmpLeagueList = (List<CarouselPic>) AbJsonUtil.fromJson(carouselListJson.toString(), new TypeToken<List<CarouselPic>>() {});
                mCarouselPicList = tmpLeagueList == null ? new ArrayList<CarouselPic>(0) : tmpLeagueList;
                initBannerPic(mCarouselPicList);
            } else if (url.equals("/Qr/GetEncoded")){
                JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
                mStrCodeUrl = jsonObject.getString("link");
                int isClick = param.getInt("isClick");
                if (isClick == 1){
                    showErWeiMa();
                }
            }else if (url.equals("/Member/GetStoreDetail")) {
                JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
                mStoreDetail.setAvatar(jsonObject.getString("Avatar"));
                mStoreDetail.setRealName(jsonObject.getString("RealName"));
                mStoreDetail.setMemberId(Integer.parseInt(jsonObject.getString("MemberId")));
                mStoreDetail.setCityCode(jsonObject.getString("CityCode"));
                mStoreDetail.setHandPic(jsonObject.getString("HandPic"));
                mStoreDetail.setIdCard(jsonObject.getString("IdCard"));
                mStoreDetail.setIdCardBackPic(jsonObject.getString("IdCardBackPic"));
                mStoreDetail.setIdCardFrontPic(jsonObject.getString("IdCardFrontPic"));
                mStoreDetail.setLatitude(jsonObject.getString("Latitude"));
                mStoreDetail.setLongitude(jsonObject.getString("Longitude"));
                mStoreDetail.setMobile(jsonObject.getString("Mobile"));
                mStoreDetail.setStoreName(jsonObject.getString("StoreName"));
                mStoreDetail.setMScore(jsonObject.getString("MScore"));
                mStoreDetail.setMStar(jsonObject.getString("MStar"));
                mStoreDetail.setUpperPic(jsonObject.getString("UpperPic"));
                mStoreDetail.setState(Integer.parseInt(jsonObject.getString("State")));
                mStoreDetail.setStar(jsonObject.getString("Star"));
                mStoreDetail.setAddress(jsonObject.getString("Address"));
                mStoreDetail.setIntroduction(jsonObject.getString("Introduction"));
                mStoreDetail.setScore(Integer.parseInt(jsonObject.getString("Score")));
                mStoreDetail.setMobileReserve(jsonObject.getString("MobileReserve"));
                mStoreDetail.setDistance(jsonObject.getString("Distance"));
                mStoreDetail.setIsFavoraite(jsonObject.getString("IsFavoraite"));
                String avatar = mStoreDetail.getAvatar();
                if (!TextUtils.isEmpty(avatar)) {
                    mAbImageLoader.display(mIvAvatar, avatar, AppApplication.mWinWidth, 165);
                }

                if (mStoreDetail.getIsFavoraite().equals("1")){
                    mBtnFavorite.setImageResource(R.drawable.btn_favorite_select);
                }else{
                    mBtnFavorite.setImageResource(R.drawable.btn_favorite);
                }
                mTvTitle.setText(mStoreDetail.getStoreName());
                mTvStoreName.setText(mStoreDetail.getStoreName());
                String mobileReserve = mStoreDetail.getMobileReserve();
                if (TextUtils.isEmpty(mobileReserve)){
                    mTvStorePhone.setText(mStoreDetail.getMobile());
                }else{
                    mTvStorePhone.setText(mobileReserve);
                }

                String address = mStoreDetail.getAddress();
                if (!TextUtils.isEmpty(address)){
                    mTvStoreAddress.setText(address);
                }

                String introduction = mStoreDetail.getIntroduction();
                if (!TextUtils.isEmpty(introduction)){
                    mTvIntroduction.setText(introduction);
                }

                String distance = jsonObject.getString("Distance");
                if (!TextUtils.isEmpty(distance)){
                    mTvDistance.setText("距您"+distance);
                }

            }else if (url.equals("/Member/AddFavorite")) {
                mStoreDetail.setIsFavoraite("1");
                mBtnFavorite.setImageResource(R.drawable.btn_favorite_select);
                AbToastUtil.showToast(mContext, R.string.favorite_success);
            }else if (url.equals("/Member/DelFavorite")) {
                mStoreDetail.setIsFavoraite("0");
                mBtnFavorite.setImageResource(R.drawable.btn_favorite);
                AbToastUtil.showToast(mContext, R.string.del_favorite_success);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
