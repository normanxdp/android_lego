package android.bigplan.lego.activity;

import android.annotation.SuppressLint;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.msg.ChatActivity;
import android.bigplan.lego.adapter.SearchHistoryListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.db.dao.SearchTagDao;
import android.bigplan.lego.model.SearchTag;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.view.FlowLayout;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索页
 * Created by FingerArt.me on 2016/6/3.
 */
public class SearchActivity extends BaseTActivity {
    private final static String TAG = "SearchActivity";

    private ListView mListView = null;
    private SearchHistoryListViewAdapter mAdapter = null;
    private ArrayList<SearchTag> mSearchTagList = null;

    private FlowLayout mFlHotSearch = null;
    private EditText mEtSearch = null;
    private ImageView mBtnDeleteHistory = null;
    private Button mBtnCancel = null;

    private SearchTagDao mSearchTagDao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.requestFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchTagDao = new SearchTagDao(this);
        mSearchTagList = new ArrayList<>();

        initView();
        initData();
    }

    private void initView() {
        mEtSearch = (EditText) findViewById(R.id.et_search_bar);
        mEtSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){

                    InputMethodManager imm = (InputMethodManager)v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );
                    }

                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        SearchTag tag = new SearchTag();
                        tag.setKeyWords(mEtSearch.getText().toString());
                        mSearchTagDao.addSearchTag(tag);
                    }

                    return true;

                }
                return false;
            }
        });

        mListView = (ListView) findViewById(R.id.lv_search_history);
        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new SearchHistoryListViewAdapter(mContext, mSearchTagList);
        mListView.setAdapter(mAdapter);

        mFlHotSearch = (FlowLayout)findViewById(R.id.fl_labelflow);
        mFlHotSearch.removeAllViews();

        mBtnDeleteHistory = (ImageView) findViewById(R.id.btn_delete);
        mBtnDeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchTagDao.deleteAll();
                mSearchTagList = new ArrayList<SearchTag>();
                mAdapter.updateView(mSearchTagList);
            }
        });

        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private  void initData(){
        getHistoryTag();
        getHotKeys();
    }

    @Override
    protected String getToolBarTitle() {
        return null;
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
                                //goToSearchActivity(text);

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
