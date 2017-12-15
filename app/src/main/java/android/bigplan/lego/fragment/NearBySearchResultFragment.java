package android.bigplan.lego.fragment;

import android.bigplan.lego.R;
import android.bigplan.lego.activity.LoginRegisterActivity;
import android.bigplan.lego.activity.StoreDetailActivity;
import android.bigplan.lego.activity.TabNearbyActivity;
import android.bigplan.lego.adapter.SearchResultListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.model.SearchResult;
import android.bigplan.lego.model.SearchTag;
import android.bigplan.lego.util.AbJsonUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gg on 17-9-16.
 */

public class NearBySearchResultFragment extends BaseFragment {
    private final static String TAG = "NearBySearchResultFragment";

    private View mRootView = null;
    private Context mContext = null;

    private EditText mEtSearch = null;
    private ListView mLvResult = null;
    private SearchResultListViewAdapter mAdapter = null;
    private ArrayList<SearchResult> mResultList = null;
    private Button mBtnCancel = null;
    private NearBySearchResultFragment mCurrentFragment = null;
    private TabNearbyActivity mParentActivity = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_nearby_search_result, container,
                false);
        mContext = getActivity();
        mParentActivity = (TabNearbyActivity)getActivity();
        mCurrentFragment = this;

        initView();

        return mRootView;
    }

    private void initView() {
        mBtnCancel = (Button) mRootView.findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResultList = new ArrayList<SearchResult>();
                mAdapter.updateView(mResultList);
                mParentActivity.clearAllSearchText();
                mParentActivity.switchFragment(TabNearbyActivity.FragmentType.NearByList);
            }
        });

        mEtSearch = (EditText) mRootView.findViewById(R.id.et_search_bar);
        mEtSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager)v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );
                    }

                    mParentActivity.getSearchResult(mEtSearch.getText().toString());

                    return true;
                }

                return false;
            }
        });

        mLvResult = (ListView) mRootView.findViewById(R.id.lv_search_result);
        mResultList = new ArrayList<>();
        mAdapter = new SearchResultListViewAdapter(mContext, mResultList);
        mLvResult.setAdapter(mAdapter);
        mLvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (AppApplication.getInstance().isUserLogin){
                    intent = new Intent();
                    SearchResult  result = mResultList.get(position);
                    String memberId = result.getMemberId();
                    intent.putExtra("store_memberid", memberId);
                    String distance = result.getDistance();
                    String avatar = result.getAvatar();
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
        });
    }

    public void updateView(ArrayList<SearchResult> resultList, String searchKey) {
        mResultList = resultList;
        mAdapter.updateView(resultList);
        mEtSearch.setText(searchKey);
    }

    public void setSearchText(String text) {
        if (mEtSearch != null)
            mEtSearch.setText(text);
    }
}
