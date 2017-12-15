package android.bigplan.lego.activity;

import android.app.AlertDialog;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.msg.SecondMsgListActivity;
import android.bigplan.lego.adapter.BankListViewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.model.BankModule;
import android.bigplan.lego.model.CarouselPic;
import android.bigplan.lego.model.MyBankCard;
import android.bigplan.lego.model.MyOilCard;
import android.bigplan.lego.model.SecondMsg;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.DialogInterface;
import android.content.Intent;
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

public class MyOilCardListActivity extends BaseTActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private final static String TAG = MyOilCardListActivity.class.getSimpleName();

    LayoutInflater mInflater = null;
    private String  mStrSelectOilCard = "0";
    private AbImageLoader mAbImageLoader = null;
    private ListView lv_myBank;
    private BankListViewAdapter mWalletAdapter;
    private List<MyBankCard> mMyOilCardList;
    private LinearLayout  mllNothing = null;
    private  MyBankCard mDelModel = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybankcard);
        mAbImageLoader = AbImageLoader.getInstance(this);
        mInflater = LayoutInflater.from(this);
        String type = getIntent().getStringExtra("select_myoilcard");
        if (!TextUtils.isEmpty(type)){
            mStrSelectOilCard = type;
        }

        initView();

    }

    protected void onResume() {
        super.onResume();
        requestData();
    }

    protected String getToolBarTitle(){
        return  getString(R.string.label_my_oilcard);
    }

    protected String getToolBarRightTitle(){
        return  getString(R.string.label_add);
    }
    protected  void onRightTitleClick(){
        Intent intent = new Intent(mContext, AddOilCardActivity.class);
        startActivity(intent);
    }

    private void initView() {

        mMyOilCardList =  new ArrayList<>();
        lv_myBank = (ListView) findViewById(R.id.lv_mybank);
        mllNothing = (LinearLayout) findViewById(R.id.nothing);
        TextView tvNothing = (TextView)findViewById(R.id.text_noting);
        tvNothing.setText(getString(R.string.error_no_oilcard));

        mWalletAdapter =  new BankListViewAdapter(mContext, mMyOilCardList);
        lv_myBank.setAdapter(mWalletAdapter);
        if (mStrSelectOilCard.equals("1")){
            lv_myBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("model", mMyOilCardList.get(position));
                    setResult(0, intent);
                    finish();
                }
            });
        }

        lv_myBank.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示");
                builder.setMessage("确定删掉该油卡？");
                builder.setPositiveButton(" 确定 ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        JSONObject object = new JSONObject();
                        final MyBankCard model = mMyOilCardList.get(arg2);

                        delOilCard(model);
                    }
                });
                builder.setNegativeButton(" 取消 ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.show();
                return true;
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

    }

    private void delOilCard(final MyBankCard model){
        mDelModel = model;
        String card = model.getBankCard();
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object .put("card", card);

        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Wallet/DelOilCard", object);
    }
    private void requestData() {
        JSONObject object = new JSONObject();
        try {
            object .put("memberid", AppApplication.getInstance().getUser().getMemberId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        post("/Wallet/GetMyOilCardList", object);
    }
    protected void onSuccessCallback(String url, String content, JSONObject param) {
        try {
            if (url.equals("/Wallet/GetMyOilCardList")) {
                if (content != null) {
                    try {
                        AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
                        if (abResult.getCode() == AbResult.RESULT_OK) {
                            JSONArray jsonArray = new JSONObject(content).getJSONArray("data");
                            List<MyOilCard> tmpList = (List<MyOilCard>) AbJsonUtil.fromJson(jsonArray.toString(), new TypeToken<List<MyOilCard>>() {});

                            if (tmpList != null && tmpList.size() > 0){
                                mMyOilCardList.clear();
                                mllNothing.setVisibility(View.GONE);
                                lv_myBank.setVisibility(View.VISIBLE);
                                for (int i = 0; i < tmpList.size(); i++) {
                                    final MyOilCard tmpcard = tmpList.get(i);
                                    MyBankCard card = new MyBankCard();
									card.setIsOilMode("1");
                                    card.setId(tmpcard.getId());
                                    card.setBankCard(tmpcard.getOilCard());
                                    card.setCreateTime(tmpcard.getCreateTime());
                                    card.setBankType(tmpcard.getCardType());
                                    card.setName(tmpcard.getName());
                                    card.setRealName(tmpcard.getRealName());
                                    mMyOilCardList.add(card);
                                }
                                if (mMyOilCardList != null && mMyOilCardList.size() > 0){
                                    mWalletAdapter.updateView(mMyOilCardList);
                                }

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
            }else if (url.equals("/Wallet/DelOilCard")) {
                mMyOilCardList.remove(mDelModel);
                mWalletAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
