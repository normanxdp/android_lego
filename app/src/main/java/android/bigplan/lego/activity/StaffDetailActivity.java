package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.adapter.IDcardPicGridViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.model.AroundStaff;
import android.bigplan.lego.model.ProvinceCity;
import android.bigplan.lego.model.Skill;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.Logger;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hedgehog.ratingbar.RatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class StaffDetailActivity extends BaseTActivity {
    private final static String TAG = StaffDetailActivity.class.getSimpleName();

    public final static String EXTRA_MEMBER_ID = "extra_member.id";
    private ImageView mIvAvatar;
    private TextView mTvName;
    private TextView mTvIDCard;
    private TextView mTvPhone;
    private TextView mTvSkill;
    private TextView mTvCity;

    private RatingBar mRbRate;
    private TextView mTvRate;

    private TextView mTvDistance;
    private GridView mGvIdCardPic;
    private IDcardPicGridViewAdapter mGvAdapter;
    private String[] mPicList;

    private String mCurMemberID;

    private AbImageLoader mAbImageLoader = null;
    private List<AroundStaff> mListAroundStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        mCurMemberID = intent.getStringExtra(EXTRA_MEMBER_ID);
    }

    protected String getToolBarTitle() {
        return getString(R.string.title_staff_detail);
    }

    private void initView() {
        setToolBarTitle("员工详情");
        if (mCurMemberID == null || mCurMemberID.equalsIgnoreCase(""))
            return;

        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvIDCard = (TextView) findViewById(R.id.tv_idcard);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvSkill = (TextView) findViewById(R.id.tv_skill);
        mTvCity = (TextView) findViewById(R.id.tv_city);

        mTvRate = (TextView) findViewById(R.id.tv_rating_score);
        mTvDistance = (TextView) findViewById(R.id.tv_distance);

        mRbRate = (RatingBar) findViewById(R.id.rb_score);

        mPicList = new String[4];
        mGvIdCardPic = (GridView) findViewById(R.id.gv_idcard_pic);
        mGvAdapter = new IDcardPicGridViewAdapter(mContext, mPicList);
        mGvIdCardPic.setAdapter(mGvAdapter);

        mAbImageLoader = AbImageLoader.getInstance(mContext);

        getStaffDetail(Integer.valueOf(mCurMemberID));
    }

    protected void onSuccessCallback(String url, String content, JSONObject param) {
        if (content != null) {
            AbResult abResult = (AbResult) AbJsonUtil.fromJson(content, AbResult.class);
            if (abResult.getCode() == AbResult.RESULT_OK) {
                String jsonData = AbJsonUtil.toJson(abResult.getData());

                mListAroundStaff = (List<AroundStaff>) AbJsonUtil.fromJson(jsonData, new TypeToken<List<AroundStaff>>() {  });
//                mTvDistance.setText(mListAroundStaff.get(0).getDistance());
                mTvIDCard.setText(mListAroundStaff.get(0).getIdCard());
                mTvRate.setText(mListAroundStaff.get(0).getStar());
                mRbRate.setStar(Float.valueOf(mListAroundStaff.get(0).getStar()));

                mTvName.setText(mListAroundStaff.get(0).getRealName());
                mTvPhone.setText(mListAroundStaff.get(0).getMobile());
                mTvSkill.setText(mListAroundStaff.get(0).getSkillList().get(0).getName());
                AbImageLoader.getInstance(mContext).display(mIvAvatar, mListAroundStaff.get(0).getAvatar());

            } else {

            }
        }
        try {
            JSONObject jsonObject = new JSONObject(content);
            JSONArray dataJsonArray = jsonObject.getJSONArray("data");
            Logger.d(TAG, "json array length:" + dataJsonArray.length());

            AroundStaff staffDetail = new AroundStaff();
            JSONObject staffJsonObj = dataJsonArray.getJSONObject(0);
            staffDetail.setStaffId(staffJsonObj.getString("MemberId"));
            staffDetail.setRealName(staffJsonObj.getString("RealName"));
            staffDetail.setStar(staffJsonObj.getString("Star"));
            staffDetail.setAvatar(staffJsonObj.getString("Avatar"));
            staffDetail.setMobile(staffJsonObj.getString("Mobile"));
            staffDetail.setIsPublic(staffJsonObj.getString("IsPublic"));
            staffDetail.setId(staffJsonObj.getString("Id"));
            staffDetail.setName(staffJsonObj.getString("Name"));
            staffDetail.setExplain(staffJsonObj.getString("Explain"));
            staffDetail.setDistance(staffJsonObj.getInt("Distance"));

            staffDetail.setCityCode(staffJsonObj.getString("CityCode"));
            staffDetail.setIdCard(staffJsonObj.getString("IdCard"));
            staffDetail.setUpperPic(staffJsonObj.getString("UpperPic"));
            staffDetail.setHandPic(staffJsonObj.getString("HandPic"));
            staffDetail.setIdCardFront(staffJsonObj.getString("IdCardFront"));
            staffDetail.setIdCardBack(staffJsonObj.getString("IdCardBack"));
            staffDetail.setScore(staffJsonObj.getString("Score"));
            staffDetail.setSex(staffJsonObj.getString("Sex"));
            staffDetail.setFirstLetter(staffJsonObj.getString("FirstLetter"));
            staffDetail.setWeChatId(staffJsonObj.getString("WeChatId"));
            staffDetail.setMScore(staffJsonObj.getString("MScore"));
            staffDetail.setMStar(staffJsonObj.getString("MStar"));

            List<Skill> skillList = (List<Skill>) AbJsonUtil.fromJson(staffJsonObj.getJSONArray("Skill").toString(), new TypeToken<List<Skill>>() {
            });
            staffDetail.setSkillList(skillList);

            mPicList[0] = staffDetail.getUpperPic();
            mPicList[1] = staffDetail.getHandPic();
            mPicList[2] = staffDetail.getIdCardFront();
            mPicList[3] = staffDetail.getIdCardBack();
            mGvAdapter.notifyDataSetChanged();

            mTvName.setText(staffDetail.getRealName());
            mTvPhone.setText(staffDetail.getMobile());
            mTvSkill.setText(staffDetail.getSkillList().get(0).getName());

            for (int i = 0; i < AppApplication.mCityList.size(); i++) {
                ProvinceCity city = AppApplication.mCityList.get(i);
                if (staffDetail.getCityCode().equals(city.getCode())) {
                    mTvCity.setText(city.getName());
                    break;
                }
            }

            mTvDistance.setText(String.format("%s %s%s",
                    mContext.getString(R.string.label_distance_you),
                    staffDetail.getDistance(),
                    mContext.getString(R.string.label_distance_meter)));
            mTvIDCard.setText(staffDetail.getIdCard());
            mTvRate.setText(staffDetail.getStar());
            mRbRate.setStar(Float.valueOf(staffDetail.getStar()));

            mAbImageLoader.display(mIvAvatar, R.drawable.default_head, staffDetail.getAvatar());
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }

    }

    private void getStaffDetail(int memberid) {
        JSONObject object = new JSONObject();
        try {
            object.put("memberids", memberid);
            object.put("isallinfo", 0);
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Member/GetStaff", object);
    }
}
