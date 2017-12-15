package android.bigplan.lego.fragment;

import android.bigplan.lego.R;
import android.bigplan.lego.activity.TabNearbyActivity;
import android.bigplan.lego.adapter.SearchHistoryListViewAdapter;
import android.bigplan.lego.db.dao.SearchTagDao;
import android.bigplan.lego.model.SearchTag;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.view.FlowLayout;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gg on 16-4-24.
 */
public class NearByListFragment extends BaseFragment {
    private static final String TAG = NearByListFragment.class.getSimpleName();

    private ListView mListView = null;
    private SearchHistoryListViewAdapter mAdapter = null;
    private ArrayList<SearchTag> mSearchTagList = null;

    private View mRootView = null;
    private SwipeRefreshLayout mSwipeRefresh = null;
    private FlowLayout mFlHotSearch = null;
    private EditText mEtSearch = null;
    private ImageView mBtnDeleteHistory = null;
    private Button mBtnCancel = null;

    private SearchTagDao mSearchTagDao = null;

    private Context mContext = null;
    private TabNearbyActivity mParentActivity = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_nearby_list, container,
                false);
        mContext = getActivity();

        mSearchTagDao = new SearchTagDao(mContext);
        mSearchTagList = new ArrayList<>();

        initView();
        initData();

        return mRootView;
    }

    private void initView() {
        mParentActivity = (TabNearbyActivity) getActivity();

        mSwipeRefresh = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe);
        mSwipeRefresh.offsetTopAndBottom(600);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHotKeys();
                mSwipeRefresh.setRefreshing(false);
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

                    if (mEtSearch.getText() != null && !mEtSearch.getText().toString().equalsIgnoreCase("")) {
                        SearchTag tag = new SearchTag();
                        tag.setKeyWords(mEtSearch.getText().toString());
                        mSearchTagDao.addSearchTag(tag);
                        getHistoryTag();
                        mAdapter.updateView(mSearchTagList);

                        mParentActivity.getSearchResult(mEtSearch.getText().toString());
                        mParentActivity.switchFragment(TabNearbyActivity.FragmentType.NearByResult);
                    }

                    return true;
                }

                return false;
            }
        });

        mListView = (ListView) mRootView.findViewById(R.id.lv_search_history);
        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new SearchHistoryListViewAdapter(mContext, mSearchTagList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mParentActivity.getSearchResult(mSearchTagList.get(position).getKeyWords());
                mParentActivity.switchFragment(TabNearbyActivity.FragmentType.NearByResult);
            }
        });

        mFlHotSearch = (FlowLayout) mRootView.findViewById(R.id.fl_labelflow);
        mFlHotSearch.removeAllViews();

        mBtnDeleteHistory = (ImageView) mRootView.findViewById(R.id.btn_delete);
        mBtnDeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchTagDao.deleteAll();
                mSearchTagList = new ArrayList<SearchTag>();
                mAdapter.updateView(mSearchTagList);
            }
        });

        mBtnCancel = (Button) mRootView.findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParentActivity.clearAllSearchText();
                mParentActivity.switchFragment(TabNearbyActivity.FragmentType.NearByMap);
            }
        });
    }

    private  void initData(){
        getHistoryTag();
        getHotKeys();
    }

    public void updateView(String searchKey) {
        mEtSearch.setText(searchKey);
    }

    public void setSearchText(String text) {
        if (mEtSearch != null)
            mEtSearch.setText(text);
    }

    protected void getHotKeys() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", "0");
            object.put("pagesize", "10");
            object.put("pageindex", "0");

        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Contacts/HotKeys", object);
    }

    protected void getHistoryTag() {
        mSearchTagList = mSearchTagDao.getAllSearchTag();
        mAdapter.updateView(mSearchTagList);
    }

    protected void onSuccessCallback(String url, String content, JSONObject param) {
        try {
            if (url.indexOf("/Contacts/HotKeys") != -1) {
                mFlHotSearch.removeAllViews();
                JSONObject jsonObject = new JSONObject(content);
                JSONArray arrayJson = jsonObject.getJSONArray("data");
                List<SearchTag> tagDatas = (List<SearchTag>) AbJsonUtil.fromJson(arrayJson.toString(), new TypeToken<List<SearchTag>>() {});
                int count = 0;
                for (int i = 0; tagDatas != null && i < tagDatas.size(); i++) {
                    String name = tagDatas.get(i).getKeyWords();
                    if (!TextUtils.isEmpty(name)){
                        count++;
                        final TextView tagView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_profile_hcf, mFlHotSearch, false);
                        tagView.setTag(i);
                        tagView.setText(tagDatas.get(i).getKeyWords());
                        tagView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String text = tagView.getText().toString();
                                //跳转到搜索activity
                                mParentActivity.getSearchResult(text);
                                mParentActivity.switchFragment(TabNearbyActivity.FragmentType.NearByResult);

                            }
                        });

                        mFlHotSearch.addView(tagView);
                    }

                }

                if (count == 0){
                    mFlHotSearch.setVisibility(View.GONE);
                }else {
                    mFlHotSearch.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
