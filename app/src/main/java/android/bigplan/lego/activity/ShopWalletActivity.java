package android.bigplan.lego.activity;

import android.app.Dialog;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.msg.ReceptionRoomActivity;
import android.bigplan.lego.adapter.WalletListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.fragment.AbAlertDialogFragment;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.model.Wallet;
import android.bigplan.lego.util.AbDialogFragmentUtil;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

public class ShopWalletActivity extends BaseTActivity implements AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    private final static String TAG = ShopWalletActivity.class.getSimpleName();

    LayoutInflater mInflater = null;

    private AbImageLoader mAbImageLoader = null;
    private ListView lv_mywallet;
    private WalletListViewAdapter mWalletAdapter;
    private List<Wallet> mwalletList;
    private SwipeRefreshLayout mSwipeRefresh = null;
    private TextView mIncomeMoney = null;
    private TextView mCangetCash = null;
    private String mStrCodeUrl = null;
    private int bankCount = 0;
    private String canGetCashMoney = "0";

    protected void onResume() {
        super.onResume();
        bankCount = Integer.parseInt(AppApplication.getInstance().getUser().getBankCount());
        requestData();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopwallet);
        mAbImageLoader = AbImageLoader.getInstance(this);
        mInflater = LayoutInflater.from(this);
        initView();
        getQRcode();
    }

    public void getQRcode() {
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("objid",  AppApplication.getInstance().getUser().getMemberId());
            object.put("type", "2");
        } catch (Exception e) {
            e.printStackTrace();

        }
        post("/Qr/GetEncoded", object);
    }

    public void onRefresh(){
        requestData();
        mSwipeRefresh.setRefreshing(false);
    }

    protected String getToolBarTitle(){
        return  getString(R.string.label_shop_wallet);
    }

    private void initView() {
        ImageView erCode = (ImageView)findViewById(R.id.iv_erweima_shop);
        erCode.setOnClickListener(this);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefresh.setOnRefreshListener(this);

        LinearLayout llGetcash = (LinearLayout)findViewById(R.id.ll_tixian_getcash);
        LinearLayout lltomoney = (LinearLayout)findViewById(R.id.ll_tomoney);

        LinearLayout llincomelog = (LinearLayout)findViewById(R.id.ll_my_incomelog);
        LinearLayout llgetcashlog = (LinearLayout)findViewById(R.id.ll_my_getcash_log);
        LinearLayout lltranscode = (LinearLayout)findViewById(R.id.ll_my_trancode);

        LinearLayout ll_mybankcardlist = (LinearLayout)findViewById(R.id.ll_my_bankcardlist);

        llGetcash.setOnClickListener(this);
        lltomoney.setOnClickListener(this);
        llincomelog.setOnClickListener(this);
        llgetcashlog.setOnClickListener(this);
        lltranscode.setOnClickListener(this);
        ll_mybankcardlist.setOnClickListener(this);

        mIncomeMoney = (TextView)findViewById(R.id.tv_income);
        mCangetCash = (TextView)findViewById(R.id.tv_cangetcash);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void showErWeiMa() {
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


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_tixian_getcash:
                if (bankCount == 0){
                    AbDialogFragmentUtil.showAlertDialog(mContext,
                            R.drawable.ic_alert, "提示", "提现请先绑定银行卡!",
                            new AbAlertDialogFragment.AbDialogOnClickListener() {
                                @Override
                                public void onPositiveClick() {
                                    Intent intent2 = new Intent(mContext, AddBankActivity.class);
                                    startActivity(intent2);
                                }

                                @Override
                                public void onNegativeClick() {

                                }
                            }, true);
                }else{
                    Intent intent3 = new Intent(mContext, GetCashActivity.class);
                    intent3.putExtra("max_cash_money",canGetCashMoney);
                    startActivity(intent3);
                }

                break;
            case R.id.ll_tomoney:
                Intent intent4 = new Intent(mContext, ShopTransToMoneyActivity.class);
                intent4.putExtra("max_cash_money",canGetCashMoney);
                startActivity(intent4);
                break;
            case R.id.ll_my_bankcardlist:
                intent = new Intent(mContext, MyBankCardActivity.class);
                intent.putExtra("select_mybankcard","0");
                startActivity(intent);
                break;
            case R.id.ll_my_incomelog:
                intent = new Intent(mContext, MyWalletLogActivity.class);
                intent.putExtra("type", "0");
                startActivity(intent);
                break;
            case R.id.ll_my_getcash_log:
                intent = new Intent(mContext, MyGetCashLogActivity.class);
                intent.putExtra("type", "3");
                startActivity(intent);
                break;
            case R.id.ll_my_trancode:
                intent = new Intent(mContext, MyWalletLogActivity.class);
                intent.putExtra("type", "4");
                startActivity(intent);
                break;
            case R.id.iv_erweima_shop:
                showErWeiMa();
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
            JSONObject jsonObject = new JSONObject(content).getJSONObject("data");
            if (url.equals("/Wallet/WalletDetail")) {

                String incomeMoney = jsonObject.getString("IncomeMoney");
                canGetCashMoney = jsonObject.getString("CanGetCashMoney");
                String count = jsonObject.getString("BankCount");
                AppApplication.getInstance().getUser().setBankCount(count);
                bankCount = Integer.parseInt(count);
                if (!TextUtils.isEmpty(incomeMoney)){
                    mIncomeMoney.setText("余额：¥ "+incomeMoney);
                }else{
                    mIncomeMoney.setText("余额：¥ 0");
                }

                if (!TextUtils.isEmpty(canGetCashMoney)){
                    mCangetCash.setText("可提现：¥ "+canGetCashMoney);
                }else{
                    mCangetCash.setText("可提现：¥ 0");
                }
            }else if (url.equals("/Qr/GetEncoded")){
                mStrCodeUrl = jsonObject.getString("link");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
