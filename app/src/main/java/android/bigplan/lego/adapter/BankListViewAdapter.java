package android.bigplan.lego.adapter;

import android.app.Activity;
import android.bigplan.lego.R;
import android.bigplan.lego.model.MyBankCard;
import android.bigplan.lego.model.Wallet;
import android.bigplan.lego.util.AbDateUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.text.TextUtils;
import java.util.List;

/**
 * Created by gg on 16-5-12.
 */
public class BankListViewAdapter extends BaseAdapter {

    private List<MyBankCard> mWalletList;
    private Context mContext;
    private Activity mActivity;

    public BankListViewAdapter(Context ctx, List<MyBankCard> list) {
        mWalletList = list;
        mContext = ctx;
    }

    public void updateView(List<MyBankCard> list) {
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
            viewHolder.tv_time_wallet.setVisibility(View.GONE);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final MyBankCard order = mWalletList.get(position);
        viewHolder.tv_tiltle_wallet.setText(order.getName());
		String isOllModel = order.getIsOilMode();
		if (!TextUtils.isEmpty(isOllModel) && "1".equals(isOllModel)){
			viewHolder.tv_forwath_wallet.setText(order.getBankCard()+" "+order.getRealName());
		}else{
			viewHolder.tv_forwath_wallet.setText(order.getBankCard());
		}
        
        viewHolder.tv_qiangan_wallet.setText(AbDateUtil.getStringBySecFormat(order.getCreateTime()));

        return view;
    }

    final static class ViewHolder {

        public TextView tv_tiltle_wallet;
        public TextView tv_qiangan_wallet;
        public TextView tv_forwath_wallet;
        public TextView tv_time_wallet;

    }


}
