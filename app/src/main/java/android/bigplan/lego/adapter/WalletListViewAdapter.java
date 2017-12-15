package android.bigplan.lego.adapter;

import android.app.Activity;
import android.bigplan.lego.R;
import android.bigplan.lego.model.Wallet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gg on 16-5-12.
 */
public class WalletListViewAdapter extends BaseAdapter {

    private List<Wallet> mWalletList;
    private Context mContext;
    private Activity mActivity;

    public WalletListViewAdapter(Context ctx, List<Wallet> list) {
        mWalletList = list;
        mContext = ctx;
    }

    public void updateView(List<Wallet> list) {
        mWalletList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mWalletList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWalletList.get(position);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_wallet, null);
            viewHolder.tv_tiltle_wallet = (TextView) view.findViewById(R.id.tv_tiltle_wallet);
            viewHolder.tv_qiangan_wallet = (TextView) view.findViewById(R.id.tv_qiangan_wallet);
            viewHolder.tv_forwath_wallet = (TextView) view.findViewById(R.id.tv_forwath_wallet);
            viewHolder.tv_time_wallet = (TextView) view.findViewById(R.id.tv_time_wallet);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Wallet order = mWalletList.get(position);
//        public TextView tv_tiltle_wallet;
//        public TextView tv_qiangan_wallet;
//        public TextView tv_forwath_wallet;
//        public TextView tv_time_wallet;
        viewHolder.tv_tiltle_wallet.setText(order.getTitle());
        viewHolder.tv_qiangan_wallet.setText(order.getMoney());
        viewHolder.tv_forwath_wallet.setText(order.getSubTitle());
        viewHolder.tv_time_wallet.setText(order.getCreateTime());

        return view;
    }

    final static class ViewHolder {

        public TextView tv_tiltle_wallet;
        public TextView tv_qiangan_wallet;
        public TextView tv_forwath_wallet;
        public TextView tv_time_wallet;

    }


}
