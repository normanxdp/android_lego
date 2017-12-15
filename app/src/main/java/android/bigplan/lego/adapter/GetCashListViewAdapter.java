package android.bigplan.lego.adapter;

import android.app.Activity;
import android.bigplan.lego.R;
import android.bigplan.lego.model.ApplyCash;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GetCashListViewAdapter extends BaseAdapter {

    private List<ApplyCash> mApplyCashList;
    private Context mContext;
    private Activity mActivity;

    public GetCashListViewAdapter(Context ctx, List<ApplyCash> list) {
        mApplyCashList = list;
        mContext = ctx;
    }

    public void updateView(List<ApplyCash> list) {
        mApplyCashList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mApplyCashList.size();
    }

    @Override
    public Object getItem(int position) {
        return mApplyCashList.get(position);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_getcash_log, null);
            viewHolder.tv_tiltle_ApplyCash = (TextView) view.findViewById(R.id.tv_tiltle_wallet);
            viewHolder.tv_qiangan_ApplyCash = (TextView) view.findViewById(R.id.tv_qiangan_wallet);

            viewHolder.tv_time_ApplyCash = (TextView) view.findViewById(R.id.tv_time_wallet);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final ApplyCash order = mApplyCashList.get(position);
        viewHolder.tv_tiltle_ApplyCash.setText("¥"+order.getMoney());
        viewHolder.tv_qiangan_ApplyCash.setText(order.getStateName());
        viewHolder.tv_time_ApplyCash.setText(order.getCreateTime());

        return view;
    }

    final static class ViewHolder {

        public TextView tv_tiltle_ApplyCash;
        public TextView tv_qiangan_ApplyCash;
        public TextView tv_time_ApplyCash;

    }


}
