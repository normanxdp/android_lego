package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.adapter.BankListViewAdapter;
import android.bigplan.lego.adapter.WalletListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.model.BankModule;
import android.bigplan.lego.model.MyBankCard;
import android.bigplan.lego.model.Wallet;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyBankCardActivity extends BaseTActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private final static String TAG = MyBankCardActivity.class.getSimpleName();

    LayoutInflater mInflater = null;
    private String  mStrSelectBank = "0";
    private AbImageLoader mAbImageLoader = null;
    private ListView lv_myBank;
    private BankListViewAdapter mWalletAdapter;
    private List<MyBankCard> mMyBankCardList;
    private LinearLayout  mllNothing = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybankcard);
        mAbImageLoader = AbImageLoader.getInstance(this);
        mInflater = LayoutInflater.from(this);
        String type = getIntent().getStringExtra("select_mybankcard");
        if (!TextUtils.isEmpty(type)){
            mStrSelectBank = type;
        }

        initView();

    }

    protected void onResume() {
        super.onResume();
        requestData();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.label_mybankcardlist);
    }

    protected String getToolBarRightTitle(){
        return  getString(R.string.label_add);
    }
    protected  void onRightTitleClick(){
        Intent intent = new Intent(mContext, AddBankActivity.class);
        startActivity(intent);
    }

    private void initView() {

        mMyBankCardList =  new ArrayList<>();
        lv_myBank = (ListView) findViewById(R.id.lv_mybank);
        mllNothing = (LinearLayout) findViewById(R.id.nothing);

        lv_myBank.setOnItemClickListener(this);
        mWalletAdapter =  new BankListViewAdapter(mContext, mMyBankCardList);
        lv_myBank.setAdapter(mWalletAdapter);
        if (mStrSelectBank.equals("1")){
            lv_myBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("model", mMyBankCardList.get(position));
                    setResult(0, intent);
                    finish();
                }
            });
        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

    }
    private void requestData() {
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Wallet/GetMyBankCardList", object);
    }
    protected void onSuccessCallback(String url, String content, JSONObject param) {
        try {
            if (url.equals("/Wallet/GetMyBankCardList")) {
                if (content != null) {
                    try {
                        AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
                        if (abResult.getCode() == AbResult.RESULT_OK) {
                            JSONArray jsonArray = new JSONObject(content).getJSONArray("data");
                            mMyBankCardList = (List<MyBankCard>) AbJsonUtil.fromJson(jsonArray.toString(), new TypeToken<List<MyBankCard>>() {});
                            if (mMyBankCardList != null && mMyBankCardList.size() > 0){
                                mllNothing.setVisibility(View.GONE);
                                lv_myBank.setVisibility(View.VISIBLE);
                                mWalletAdapter.updateView(mMyBankCardList);
                            }else{
                                mllNothing.setVisibility(View.VISIBLE);
                                lv_myBank.setVisibility(View.GONE);
                            }
                        } else {
                            mllNothing.setVisibility(View.VISIBLE);
                            lv_myBank.setVisibility(View.GONE);
                            String msg = abResult.getMsg();
                            AbToastUtil.showToast(mContext, msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
