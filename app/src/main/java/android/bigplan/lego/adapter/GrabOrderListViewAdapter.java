package android.bigplan.lego.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.bigplan.lego.R;
import android.bigplan.lego.activity.msg.ChatActivity;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.model.Order;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.DialogFactory;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by gg on 16-5-12.
 */
public class GrabOrderListViewAdapter extends BaseAdapter {

    private List<Order> mOrderList;
    private Context mContext;
    private Activity mActivity;

    public GrabOrderListViewAdapter(Context ctx, List<Order> list) {
        mOrderList = list;
        mContext = ctx;
    }

    public void updateView(List<Order> list) {
        mOrderList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_qiangdan, null);

            viewHolder.ll_address1_qiangan = (LinearLayout) view.findViewById(R.id.ll_address1_qiangan);
            viewHolder.tv_forwath1 = (TextView) view.findViewById(R.id.tv_forwath1_qiangan);
            viewHolder.tv_forwath2 = (TextView) view.findViewById(R.id.tv_forwath2_qiangan);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_for_qiangan);
            viewHolder.tv_startAddr = (TextView) view.findViewById(R.id.tv_address1_qiangan);
            viewHolder.bt_qiandan = (Button) view.findViewById(R.id.bt_qiandan);

            viewHolder.tv_endAddr = (TextView) view.findViewById(R.id.tv_address2_qiangan);
            viewHolder.tv_money = (TextView) view.findViewById(R.id.tv_qian_qiangan);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Order order = mOrderList.get(position);

        viewHolder.tv_title.setText(order.getDemTitle());
        viewHolder.tv_forwath1.setText(order.getExplain());
        viewHolder.tv_forwath2.setText(order.getExplain());
        viewHolder.tv_startAddr.setText(order.getEndAddr());
        viewHolder.tv_endAddr.setText(order.getStartAddr());
        if(order.getType()==1){
            viewHolder. ll_address1_qiangan.setVisibility(View.VISIBLE);
            viewHolder.  tv_forwath2 .setVisibility(View.VISIBLE);
//            mOrderDetail.getStyle().equals("1")帮我买
//            mOrderDetail.getStyle().equals("2")帮我送
            if(order.getStyle().equals("1")){
                viewHolder. ll_address1_qiangan.setVisibility(View.GONE);
                viewHolder.  tv_forwath2 .setVisibility(View.GONE);
            }
        }else{
            viewHolder. ll_address1_qiangan.setVisibility(View.GONE);
            viewHolder.  tv_forwath2 .setVisibility(View.GONE);
        }
//        String strMoney = String.format("%s %s", mContext.getString(R.string.label_order_money), order.getValuation());
        viewHolder.tv_money.setText("¥ "+order.getValuation());
        viewHolder.bt_qiandan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabOrder(order.getDemId(),order.getMemberId());
            }
        });
        return view;
    }

    final static class ViewHolder {
//        public ImageView iv_icon;
        public TextView tv_forwath1;
        public TextView tv_forwath2;
        public TextView tv_title;
        public LinearLayout ll_address1_qiangan;
        public TextView tv_startAddr;
        public TextView tv_endAddr;
        public TextView tv_money;
        public Button   bt_qiandan ;
    }
    private void grabOrder(String  mOrderID,String   MemberId) {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("longitude", Double.toString(AppApplication.getInstance().getLongitude()));
            object.put("latitude", Double.toString(AppApplication.getInstance().getLatitude()));
            object.put("citycode", "4403");
            object.put("longitude", Double.toString(AppApplication.getInstance().getLongitude()));
            object.put("latitude", Double.toString(AppApplication.getInstance().getLatitude()));
            object.put("demid", mOrderID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post("/Demand/GrabDemand", MemberId,object);
    }
    protected void post(String url, final String MemberId, final JSONObject object){
        AbLogUtil.d("url:" + url);
        AbLogUtil.d("params:" + object);
        AbRequest request = new AbRequest();
        AbRequestParams params = request.getRequestParams(object.toString());
        AbHttpUtil httpUtil = AbHttpUtil.getInstance(mContext);

        String tmpUrl = AbConstant.REQUEST_URL + url;
        httpUtil.post(tmpUrl, params, new AbStringHttpResponseListener(url, object) {
            public void onSuccess(int statusCode, String content) {
                if (content != null) {
                    AbLogUtil.d(object + "=====" + content);
                    try {
                        AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
                        if (abResult.getCode() == AbResult.RESULT_OK) {

                        } else {
//                            mContext.onShowErrorMsg(strUrl, abResult.getMsg());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 开始执行前
            @Override
            public void onStart() {
                showRequestDialog("正在申请");

            }

            @Override
            public void onFailure(int statusCode, String content,
                                  Throwable error) {

            }

            // 完成后调用，失败，成功，都要调用
            public void onFinish() {
                 closeRequestDialog();
            }
        });
    }
    public void showRequestDialog(String msg) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(mContext, msg);
        mDialog.show();
    }


    public Dialog mDialog = null;
    public void closeRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

    }
}
