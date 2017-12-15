package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.adapter.WalletListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.model.Wallet;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyWalletActivity extends BaseTActivity implements AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = MyWalletActivity.class.getSimpleName();

    private LinearLayout mlLayoutrechargeLog = null;
    private LinearLayout mlLayoutrepayLog = null;
    private TextView mTextView = null;
    private SwipeRefreshLayout mSwipeRefresh = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);

        initView();
        requestData();
    }

    public void onRefresh(){
        requestData();
        mSwipeRefresh.setRefreshing(false);
    }

    protected String getToolBarTitle(){
        return  getString(R.string.label_my_wallet);
    }

    private void initView() {
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefresh.setOnRefreshListener(this);

        mlLayoutrechargeLog  = (LinearLayout)findViewById(R.id.ll_my_rechargelog);
        mlLayoutrechargeLog.setOnClickListener(this);

        mlLayoutrepayLog  = (LinearLayout)findViewById(R.id.ll_my_paylog);
        mlLayoutrepayLog.setOnClickListener(this);

        mTextView = (TextView)findViewById(R.id.tv_money);

        Button button = (Button)findViewById(R.id.btn_pay);
        button.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_pay:
                intent = new Intent(MyWalletActivity.this,PayActivity.class);
                startActivity(intent);

                break;
            case R.id.ll_my_rechargelog:
                intent = new Intent(mContext, MyWalletLogActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.ll_my_paylog:
                intent = new Intent(mContext, MyWalletLogActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;

        }
   }
    private void requestData() {
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Wallet/WalletDetail", object);
    }

    protected void onSuccessCallback(String url, String content, JSONObject param) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        try {
            if (url.equals("/Wallet/WalletDetail")) {
                JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
                String incomeMoney = jsonObject.getString("Money");
                if (!TextUtils.isEmpty(incomeMoney)){
                    mTextView.setText("¥ "+incomeMoney);
                }else{
                    mTextView.setText("¥ 0");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
