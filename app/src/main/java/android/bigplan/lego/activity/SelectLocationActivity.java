package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.adapter.SelectCityListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.model.ProvinceCity;
import android.bigplan.lego.util.Logger;
import android.bigplan.lego.view.sort.CharacterParser;
import android.bigplan.lego.view.sort.PinyinComparator;
import android.bigplan.lego.view.sort.SideBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gg on 16-4-13.
 */
public class SelectLocationActivity extends BaseTActivity {
    private final static String TAG = SelectLocationActivity.class.getSimpleName();

    private Button mBtnCurCity;
    private String mStrCurCity;

    private List<ProvinceCity> mCityList;
    private ListView mCityListView;
    private SelectCityListViewAdapter mCityListAdapter;

    private SideBar mSideBar;
    private TextView mDialog;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<ProvinceCity> SourceDateList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        AppApplication.getInstance().addActivity(this);
        mStrCurCity = "清远";
        setToolBarTitle(getString(R.string.title_select_location) + mStrCurCity);
        initData();
        initView();
    }

    @Override
    protected String getToolBarTitle() {
        return null;
    }

    @Override
    protected void back() {
        onBackPressed();
    }

    private void initView() {
        mBtnCurCity = (Button) findViewById(R.id.btn_cur_location);
        mBtnCurCity.setText(mStrCurCity);

        mCityListAdapter = new SelectCityListViewAdapter(mContext, mCityList);
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        mSideBar = (SideBar) findViewById(R.id.sidrbar);
        mDialog = (TextView) findViewById(R.id.dialog);
        mSideBar.setTextView(mDialog);

        // 设置右侧触摸监听
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mCityListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mCityListView.setSelection(position);
                }
            }
        });
        mCityListView = (ListView) findViewById(R.id.lv_city);
        mCityListView.setAdapter(mCityListAdapter);
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Logger.d(TAG, "on item click: " + mCityList.get(position).getName());
                Intent intent = new Intent();
                intent.putExtra(TabHomeActivity.EXTRA_SELECTED_CITY, mCityList.get(position).getName());
                setResult(TabHomeActivity.REQUESTCODE_SELECT_CITY, intent);
                finish();
            }
        });
    }

    private void initData() {
        mCityList = new ArrayList<>();
        mCityList.addAll(AppApplication.mCityList);
    }

}
