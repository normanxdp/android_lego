package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.adapter.BankListAdapter;
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
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

public class BankListActivity extends BaseTActivity{
    private final static String TAG = BankListActivity.class.getSimpleName();
    private ListView lv_myBank;
    private BankListAdapter mBankListAdapter;
    private List<BankModule> mBankList;
    private LinearLayout  mllNothing = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybankcard);
        initView();
        requestData();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.label_banklist);
    }


    private void initView() {
        mBankList =  new ArrayList<>();
        lv_myBank = (ListView) findViewById(R.id.lv_mybank);
        mllNothing = (LinearLayout) findViewById(R.id.nothing);
        mllNothing.setVisibility(View.GONE);
        mBankListAdapter =  new BankListAdapter(mContext, mBankList);
        lv_myBank.setAdapter(mBankListAdapter);
        lv_myBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("model", mBankList.get(position));
                setResult(0, intent);
                finish();
            }
        });
    }


    private void requestData() {
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Wallet/GetBankList", object);
    }
    protected void onSuccessCallback(String url, String content, JSONObject param) {
        try {
            if (url.equals("/Wallet/GetBankList")) {
                if (content != null) {
                    try {
                        AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
                        if (abResult.getCode() == AbResult.RESULT_OK) {
                            JSONArray jsonArray = new JSONObject(content).getJSONArray("data");
                            mBankList = (List<BankModule>) AbJsonUtil.fromJson(jsonArray.toString(), new TypeToken<List<BankModule>>() {});
                            if (mBankList != null && mBankList.size() > 0){
                                lv_myBank.setVisibility(View.VISIBLE);
                                mBankListAdapter.updateView(mBankList);
                            }else{
                                AbToastUtil.showToast(mContext, "获取不到银行列表");
                            }
                        } else {

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
