package android.bigplan.lego.adapter;

import android.app.Activity;
import android.bigplan.lego.R;
import android.bigplan.lego.model.BankModule;
import android.bigplan.lego.model.MyBankCard;
import android.bigplan.lego.model.Wallet;
import android.bigplan.lego.util.AbDateUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BankListAdapter extends BaseAdapter {

    private List<BankModule> mList;
    private Context mContext;

    public BankListAdapter(Context ctx, List<BankModule> list) {
        mList = list;
        mContext = ctx;
    }

    public void updateView(List<BankModule> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_bankcard, null);
            viewHolder.ly_name = (TextView) view.findViewById(R.id.ly_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final BankModule order = mList.get(position);
        viewHolder.ly_name.setText(order.getName());


        return view;
    }

    final static class ViewHolder {
        public TextView ly_name;
    }


}
